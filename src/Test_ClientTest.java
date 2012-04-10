import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class Test_ClientTest {

	// static create
	@Test
	public void test1__call_the_server_to_create_the_corresponding_ServerObject() throws RemoteException,
			MalformedURLException, AlreadyBoundException {

		Test_ServerMock sm = new Test_ServerMock();
		Client.setServer(sm);
		Client.create(new String("object"));
		assertEquals(sm.createCount, 1);
	}

	// static lookup
	@Test
	public void test2__forward_the_lookup_to_the_server() throws RemoteException, MalformedURLException,
			AlreadyBoundException {

		Test_ServerMock sm = new Test_ServerMock();
		Client.setServer(sm);
		Client.lookup("object name");
		assertEquals(sm.lookupCount, 1);
	}

	// static register
	@Test
	public void test2__forward_the_registration_to_the_server() throws RemoteException,
			MalformedURLException, AlreadyBoundException {

		Test_ServerMock sm = new Test_ServerMock();
		Client.setServer(sm);
		Client.register("object name", new Test_SharedObjectMock());
		assertEquals(sm.registerCount, 1);
	}

	// static lock_read
	@Test
	public void test3__forward_the_lock_read_to_the_server() throws RemoteException, MalformedURLException,
			AlreadyBoundException {

		Test_ServerMock sm = new Test_ServerMock();
		Client.setServer(sm);
		Client.lock_read(0);
		assertEquals(sm.lock_readCount, 1);
	}

	// static lock_write
	@Test
	public void test4__forward_the_lock_write_to_the_server() throws RemoteException, MalformedURLException,
			AlreadyBoundException {

		Test_ServerMock sm = new Test_ServerMock();
		Client.setServer(sm);
		Client.lock_write(0);
		assertEquals(sm.lock_writeCount, 1);
	}

	// reduce_lock
	@Test
	public void test5__forward_the_reduce_lock_request_to_the_corresponding_SharedObject()
			throws RemoteException {

		Map<Integer, SharedObject> somap = new HashMap<Integer, SharedObject>();
		Test_SharedObjectMock som = new Test_SharedObjectMock();
		somap.put(7, som);
		Client.setObjs(somap);
		Client c = new Client();
		c.reduce_lock(7);
		assertEquals(som.reduce_lockCount, 1);
	}

	@Test
	public void test6__forward_the_invalidate_reader_method() throws RemoteException {

		Map<Integer, SharedObject> somap = new HashMap<Integer, SharedObject>();
		Test_SharedObjectMock som = new Test_SharedObjectMock();
		somap.put(7, som);
		Client.setObjs(somap);
		Client c = new Client();
		c.invalidate_reader(7);
		assertEquals(som.invalidate_readerCount, 1);
	}

	@Test
	public void test7__forward_the_invalidate_writer_method() throws RemoteException {

		Map<Integer, SharedObject> somap = new HashMap<Integer, SharedObject>();
		Test_SharedObjectMock som = new Test_SharedObjectMock();
		somap.put(7, som);
		Client.setObjs(somap);
		Client c = new Client();
		c.invalidate_writer(7);
		assertEquals(som.invalidate_writerCount, 1);
	}

}