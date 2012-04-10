import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class Test_ServerTest {

	// ------
	// create
	// ------
	@Test
	public void test1__create_a_server_object_with_an_object_and_an_id() throws RemoteException,
			MalformedURLException, AlreadyBoundException {

		Server s = new Server();
		String o = new String("Hello");
		Integer id = s.create(o);
		assertNotNull(s.getObjs().get(id)); // TODO method getObjs on class Server
		assertTrue(s.getObjs().get(id) instanceof ServerObject);
		assertSame(s.getObjs().get(id).getObj(), o); // TODO method getObj on class ServerObject
	}

	// --------
	// register
	// --------
	@Test
	public void test2__bind_a_name_to_an_id() throws RemoteException, MalformedURLException,
			AlreadyBoundException {

		Server s = new Server();
		String sharedObjectName = new String("MySharedObject");
		Integer sharedObjectId = 77;
		s.register(sharedObjectName, sharedObjectId);
		assertNotNull(s.getObjsNames().get(sharedObjectName)); // TODO method getObjsNames on class Server
		assertEquals(s.getObjsNames().get(sharedObjectName), sharedObjectId);
	}

	// ------
	// lookup
	// ------
	@Test
	public void test3__return_the_id_matching_the_name() throws RemoteException, MalformedURLException,
			AlreadyBoundException {

		Server s = new Server();
		String sharedObjectName = new String("MySharedObject");
		Integer sharedObjectId = 77;
		s.register(sharedObjectName, sharedObjectId);
		Integer idReturned = s.lookup(sharedObjectName);
		assertEquals(idReturned, sharedObjectId);
	}

	// ---------
	// lock_read
	// ---------

	@Test
	public void test4__forward_the_method_to_the_ServerObject_matching_the_id() throws RemoteException,
			MalformedURLException, AlreadyBoundException {

		Server s = new Server();
		Test_ServerObjectMock som = new Test_ServerObjectMock();
		Map<Integer, ServerObject> somap = new HashMap<Integer, ServerObject>();
		somap.put(7, som);
		s.setObjs(somap);
		s.lock_read(7, new Test_ClientMock());
		assertEquals(som.lock_readCount, 1);
	}

	// ----------
	// lock_write
	// ----------

	@Test
	public void test5__forward_the_method_to_the_ServerObject_matching_the_id() throws RemoteException,
			MalformedURLException, AlreadyBoundException {

		Server s = new Server();
		Test_ServerObjectMock som = new Test_ServerObjectMock();
		Map<Integer, ServerObject> somap = new HashMap<Integer, ServerObject>();
		somap.put(7, som);
		s.setObjs(somap);
		s.lock_write(7, new Test_ClientMock());
		assertEquals(som.lock_writeCount, 1);
	}
}