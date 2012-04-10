import java.rmi.*;

public class Test_ClientMock extends Client {

	public static int initCount = 0;
	public static int lookupCount = 0;
	public static int registerCount = 0;
	public static int createCount = 0;
	public static int lock_readCount = 0;
	public static int lock_writeCount = 0;
	public int reduce_lockCount;
	public int invalidate_readerCount;
	public int invalidate_writerCount;

	public Test_ClientMock() throws RemoteException {

		reduce_lockCount = 0;
		invalidate_readerCount = 0;
		invalidate_writerCount = 0;
	}

	public static void init() {

		initCount++;
	}

	public static SharedObject lookup(String name) {

		lookupCount++;
		return null;
	}

	public static void register(String name, SharedObject so) {

		registerCount++;
	}

	public static SharedObject create(Object o) {

		createCount++;
		return null;
	}

	public static Object lock_read(int id) {

		lock_readCount++;
		return null;
	}

	public static Object lock_write(int id) {

		lock_writeCount++;
		return null;
	}

	public Object reduce_lock(int id) throws java.rmi.RemoteException {

		reduce_lockCount++;
		return null;
	}

	public void invalidate_reader(int id) throws java.rmi.RemoteException {

		invalidate_readerCount++;
	}

	public Object invalidate_writer(int id) throws java.rmi.RemoteException {

		invalidate_writerCount++;
		return null;
	}
}
