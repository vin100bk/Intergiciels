import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

enum ServerObjectLock
{
	NL, RLT, WLT
};

public class ServerObject {

	private int id;
	private Object obj;
	private List<Client_itf> readers;
	private Client_itf writer;
	private ServerObjectLock lock;

	public ServerObject(int id, Object obj) {

		this.id = id;
		this.obj = obj;
		this.readers = new ArrayList<Client_itf>();
		this.writer = null;
		this.lock = ServerObjectLock.NL;
	}

	public Object lock_read(Client_itf client) {

		if (this.writer != null)
		{
			try
			{
				this.writer.reduce_lock(this.id);
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
			}
		}

		this.readers.add(client);

		return this.obj;
	}

	public Object lock_write(Client_itf client) {

		if(!this.readers.isEmpty())
		{
			for(Client_itf cli : this.readers)
			{
				try
				{
					cli.invalidate_reader(this.id);
				}
				catch (RemoteException e)
				{
					e.printStackTrace();
				}
			}
		}
		else if(this.writer != null)
		{
			try
			{
				this.writer.invalidate_writer(this.id);
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
			}
		}
		
		this.writer = client;

		return this.obj;
	}

}
