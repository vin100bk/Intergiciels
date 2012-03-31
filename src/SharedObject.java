import java.io.*;

enum SharedObjectState
{
	NL, RLC, WLC, RLT, WLT, RLT_WLC
};

public class SharedObject implements Serializable, SharedObject_itf {

	private static final long serialVersionUID = 1L;

	public Object obj;
	private int id;
	private SharedObjectState state;

	public SharedObject(int id, Object obj) {

		this.id = id;
		this.obj = obj;
		this.state = SharedObjectState.NL;
	}

	// invoked by the user program on the client node
	public void lock_read() {

		if (this.state == SharedObjectState.NL)
		{
			this.obj = Client.lock_read(this.id);
		}

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

	// invoked by the user program on the client node
	public void lock_write() {

		if (this.state == SharedObjectState.NL || this.state == SharedObjectState.RLC)
		{
			this.obj = Client.lock_write(this.id);
		}

		// State update
		switch (this.state) {
		case NL:
			this.state = SharedObjectState.RLT;
			break;
		case RLC:
			this.state = SharedObjectState.RLT;
			break;
		case WLC:
			this.state = SharedObjectState.RLT;
			break;
		}

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

		notifyAll();
	}

	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {

		if (this.state == SharedObjectState.WLT)
		{
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

		return this.obj;
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {

		if (this.state == SharedObjectState.RLT)
		{
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
	}

	public synchronized Object invalidate_writer() {

		if (this.state == SharedObjectState.WLT || this.state == SharedObjectState.RLT_WLC)
		{
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

		return this.obj;
	}

	// return the object id
	public int getId() {

		return this.id;
	}
}
