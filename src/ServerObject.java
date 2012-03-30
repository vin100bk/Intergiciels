import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

enum ServerObjectState
{
	NL, RLT, WLT
};

public class ServerObject {

	private int id;
	private Object obj;
	private List<Client_itf> readers;
	private Client_itf writer;
	private ServerObjectState state;

	private final Lock mutex = new ReentrantLock();

	public ServerObject(int id, Object obj) {

		this.id = id;
		this.obj = obj;
		this.readers = new ArrayList<Client_itf>();
		this.writer = null;
		this.state = ServerObjectState.NL;
	}

	public Object lock_read(Client_itf client) throws RemoteException {

		mutex.lock();

		if (this.state == ServerObjectState.WLT && this.writer != null)
		{
			this.writer.reduce_lock(this.id);
		}

		this.readers.add(client);

		// State update
		switch (this.state) {
		case NL:
			this.state = ServerObjectState.RLT;
			break;
		case RLT:
			this.state = ServerObjectState.RLT;
			break;
		case WLT:
			this.state = ServerObjectState.RLT;
			break;
		}

		mutex.unlock();

		return this.obj;
	}

	public Object lock_write(Client_itf client) throws RemoteException {

		mutex.lock();

		if (this.state == ServerObjectState.RLT && !this.readers.isEmpty())
		{
			for (Client_itf cli : this.readers)
			{
				cli.invalidate_reader(this.id);
			}
		}
		else if (this.state == ServerObjectState.WLT && this.writer != null)
		{
			this.writer.invalidate_writer(this.id);
		}

		this.writer = client;

		// State update
		switch (this.state) {
		case NL:
			this.state = ServerObjectState.WLT;
			break;
		case RLT:
			this.state = ServerObjectState.WLT;
			break;
		case WLT:
			this.state = ServerObjectState.WLT;
			break;
		}

		mutex.unlock();

		return this.obj;
	}

}
