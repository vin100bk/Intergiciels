import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.net.MalformedURLException;

public class Test_ServerMock extends Server {

	public int lookupCount;
	public int registerCount;
	public int createCount;
	public int lock_readCount;
	public int lock_writeCount;

	public Test_ServerMock() throws RemoteException, MalformedURLException, AlreadyBoundException {

		lookupCount = 0;
		registerCount = 0;
		createCount = 0;
		lock_readCount = 0;
		lock_writeCount = 0;
	}

	public int lookup(String name) throws RemoteException {

		lookupCount++;
		return 0;
	}

	public void register(String name, int id) throws RemoteException {

		registerCount++;
	}

	public int create(Object o) throws RemoteException {

		createCount++;
		return 0;
	}

	public Object lock_read(int id, Client_itf client) throws RemoteException {

		lock_readCount++;
		return null;
	}

	public Object lock_write(int id, Client_itf client) throws RemoteException {

		lock_writeCount++;
		return null;
	}
}
