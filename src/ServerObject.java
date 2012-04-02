import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

enum ServerObjectState
{
	NL, RL, WL
};

public class ServerObject {

	private int id;
	private Object obj;
	private List<Client_itf> readers;
	private Client_itf writer;
	private ServerObjectState state;

	public ServerObject(int id, Object obj) {

		this.id = id;
		this.obj = obj;
		this.readers = new ArrayList<Client_itf>();
		this.writer = null;
		this.state = ServerObjectState.NL;
	}

	public synchronized Object lock_read(Client_itf client) throws RemoteException {

		if (this.state == ServerObjectState.WL && this.writer != null)
		{
			this.obj = this.writer.reduce_lock(this.id);
		}

		this.readers.add(client);

		// State update
		switch (this.state) {
		case NL:
			this.state = ServerObjectState.RL;
			break;
		case RL:
			this.state = ServerObjectState.RL;
			break;
		case WL:
			this.state = ServerObjectState.RL;
			break;
		}

		return this.obj;
	}

	public synchronized Object lock_write(Client_itf client) throws RemoteException {

		if (this.state == ServerObjectState.RL && !this.readers.isEmpty())
		{
			for (Client_itf cli : this.readers)
			{
				cli.invalidate_reader(this.id);
			}
		}
		else if (this.state == ServerObjectState.WL && this.writer != null)
		{
			this.obj = this.writer.invalidate_writer(this.id);
		}

		this.writer = client;

		// State update
		switch (this.state) {
		case NL:
			this.state = ServerObjectState.WL;
			break;
		case RL:
			this.state = ServerObjectState.WL;
			break;
		case WL:
			this.state = ServerObjectState.WL;
			break;
		}

		return this.obj;
	}

}
