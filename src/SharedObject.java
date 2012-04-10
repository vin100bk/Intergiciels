import java.io.*;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

enum SharedObjectState
{
	NL, RLC, WLC, RLT, WLT, RLT_WLC
};

public class SharedObject implements Serializable, SharedObject_itf {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger("client");
	
	public Object obj;
	private int id;
	private SharedObjectState state;
	private Client cli;	// Allows to stub Client in tests

	public SharedObject() {

		this.state = SharedObjectState.NL;
		try
		{
			this.cli = new Client();
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

	// invoked by the user program on the client node
	public void lock_read() {

		if (this.state == SharedObjectState.NL)
		{
			this.obj = cli.lock_read(this.id);
		}

		synchronized (this)
		{
			// State update
			switch (this.state) {
			case NL:
				this.state = SharedObjectState.RLT;
				break;
			case RLT:
				this.state = SharedObjectState.RLT;
				break;
			case WLC:
				this.state = SharedObjectState.RLT_WLC;
				break;
			}
		}
		
		log.log(Level.INFO, "New state : " + this.state);
	}

	// invoked by the user program on the client node
	public void lock_write() {

		if (this.state == SharedObjectState.NL || this.state == SharedObjectState.RLC)
		{
			this.obj = cli.lock_write(this.id);
		}

		synchronized (this)
		{
			// State update
			switch (this.state) {
			case NL:
				this.state = SharedObjectState.WLT;
				break;
			case RLC:
				this.state = SharedObjectState.WLT;
				break;
			case WLC:
				this.state = SharedObjectState.WLT;
				break;
			}
		}
		
		log.log(Level.INFO, "New state : " + this.state);
	}

	// invoked by the user program on the client node
	public synchronized void unlock() {

		switch (this.state) {
		case RLT:
			this.state = SharedObjectState.RLC;
			break;
		case WLT:
			this.state = SharedObjectState.WLC;
			break;
		case RLT_WLC:
			this.state = SharedObjectState.WLC;
			break;
		}
		
		log.log(Level.INFO, "New state : " + this.state);

		notifyAll();
	}

	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {
		
		if (this.state == SharedObjectState.WLT)
		{
			log.log(Level.INFO, "wait");
			
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		switch (this.state) {
		case WLC:
			this.state = SharedObjectState.RLC;
			break;
		case WLT:
			this.state = SharedObjectState.RLC;
			break;
		case RLT_WLC:
			this.state = SharedObjectState.RLT;
			break;
		}
		
		log.log(Level.INFO, "New state : " + this.state);

		return this.obj;
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {
		
		if (this.state == SharedObjectState.RLT)
		{
			log.log(Level.INFO, "wait");
			
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		switch (this.state) {
		case RLT:
			this.state = SharedObjectState.NL;
			break;
		case RLC:
			this.state = SharedObjectState.NL;
			break;
		}
		
		log.log(Level.INFO, "New state : " + this.state);
	}

	public synchronized Object invalidate_writer() {
		
		if (this.state == SharedObjectState.WLT || this.state == SharedObjectState.RLT_WLC)
		{
			log.log(Level.INFO, "wait");
			
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		switch (this.state) {
		case WLT:
			this.state = SharedObjectState.NL;
			break;
		case WLC:
			this.state = SharedObjectState.NL;
			break;
		case RLT_WLC:
			this.state = SharedObjectState.NL;
			break;
		}
		
		log.log(Level.INFO, "New state : " + this.state);

		return this.obj;
	}

	// return the object id
	public int getId() {

		return this.id;
	}

	// Set the object id
	public void setId(int id) {

		this.id = id;
	}
	
	//Methods for tests
	public void setClient(Client client) {
		
		this.cli = client;
	}
	
	public SharedObjectState getState() {
		
		return this.state;
	}
}
