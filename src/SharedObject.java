import java.io.*;

enum SharedObjectLock
{
	NL, RLC, WLC, RLT, WLT, RLT_WLC
};

public class SharedObject implements Serializable, SharedObject_itf {

	private static final long serialVersionUID = 1L;

	public Object obj;
	private int id;
	private SharedObjectLock lock;

	public SharedObject(int id, Object obj) {

		this.id = id;
		this.obj = obj;
		this.lock = SharedObjectLock.NL;
	}

	// invoked by the user program on the client node
	public void lock_read() {

		Client.lock_read(this.id);
	}

	// invoked by the user program on the client node
	public void lock_write() {

		Client.lock_write(this.id);
	}

	// invoked by the user program on the client node
	public synchronized void unlock() {

		if (this.lock == SharedObjectLock.RLT)
		{
			this.lock = SharedObjectLock.RLC;
		}
		else if (this.lock == SharedObjectLock.WLT || this.lock == SharedObjectLock.RLT_WLC)
		{
			this.lock = SharedObjectLock.WLC;
		}
	}

	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {

		if (this.lock == SharedObjectLock.WLT || this.lock == SharedObjectLock.WLC)
		{
			this.lock = SharedObjectLock.RLC;
		}
		else if (this.lock == SharedObjectLock.RLT_WLC)
		{
			this.lock = SharedObjectLock.RLT;
		}

		return this.obj;
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {

		if (this.lock == SharedObjectLock.RLT || this.lock == SharedObjectLock.RLC)
		{
			this.lock = SharedObjectLock.NL;
		}
	}

	public synchronized Object invalidate_writer() {

		if (this.lock == SharedObjectLock.WLT || this.lock == SharedObjectLock.WLC || this.lock == SharedObjectLock.RLT_WLC)
		{
			this.lock = SharedObjectLock.NL;
		}

		return this.obj;
	}

	// return the object id
	public int getId() {

		return this.id;
	}
}
