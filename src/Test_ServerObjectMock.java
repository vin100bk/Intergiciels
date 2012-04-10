import java.rmi.RemoteException;

public class Test_ServerObjectMock extends ServerObject {

	public int lock_readCount;
	public int lock_writeCount;

	public Test_ServerObjectMock() {

		super(0, null);
		lock_readCount = 0;
		lock_writeCount = 0;
	}

	public synchronized Object lock_read(Client_itf client) throws RemoteException {

		lock_readCount++;
		return null;
	}

	public synchronized Object lock_write(Client_itf client) throws RemoteException {

		lock_writeCount++;
		return null;
	}
}
