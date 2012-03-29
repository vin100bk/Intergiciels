import java.io.*;

enum Lock
{
	NL, RLC, WLC, RLT, WLT, RLT_WLC
};

public class SharedObject implements Serializable, SharedObject_itf {

	private static final long serialVersionUID = 1L;

	public Object obj;
	private int id;
	private Lock lock;

	public SharedObject(int id, Object obj) {

		this.id = id;
		this.obj = obj;
		this.lock = Lock.NL;
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

		if (this.lock == Lock.RLT)
		{
			this.lock = Lock.RLC;
		}
		else if (this.lock == Lock.WLT || this.lock == Lock.RLT_WLC)
		{
			this.lock = Lock.WLC;
		}
	}

	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {

		if (this.lock == Lock.WLT || this.lock == Lock.WLC)
		{
			this.lock = Lock.RLC;
		}
		else if (this.lock == Lock.RLT_WLC)
		{
			this.lock = Lock.RLT;
		}

		return this.obj;
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {

		if (this.lock == Lock.RLT || this.lock == Lock.RLC)
		{
			this.lock = Lock.NL;
		}
	}

	public synchronized Object invalidate_writer() {

		if (this.lock == Lock.WLT || this.lock == Lock.WLC || this.lock == Lock.RLT_WLC)
		{
			this.lock = Lock.NL;
		}

		return this.obj;
	}

	// return the object id
	public int getId() {

		return this.id;
	}
}
