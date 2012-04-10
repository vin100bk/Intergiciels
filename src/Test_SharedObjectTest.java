import java.rmi.RemoteException;

import org.junit.Test;
import static org.junit.Assert.*;

public class Test_SharedObjectTest {

	// CONTEXT : state NL

	// lock_read
	@Test
	public void test1__set_the_state_to_RLT() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_read();
		assertEquals(so.getState(), SharedObjectState.RLT);
	}

	@Test
	public void test2__forward_read_lock_to_the_Client() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_read();
		assertEquals(cm.lock_readCount, 1);
	}

	// lock_write
	@Test
	public void test3__set_the_state_to_WLT() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_write();
		assertEquals(so.getState(), SharedObjectState.WLT);
	}

	@Test
	public void test4__forward_write_lock_to_the_Client() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_write();
		assertEquals(cm.lock_writeCount, 1);
	}

	// CONTEST : state RLT

	// unlock
	@Test
	public void test5__set_the_state_to_RLC() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_read();
		so.unlock();
		assertEquals(so.getState(), SharedObjectState.RLC);
	}

	@Test
	public void test7__set_the_state_to_NL() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_read();
		so.invalidate_reader();
		assertEquals(so.getState(), SharedObjectState.NL);
	}

	// CONTEXT : state RLC

	// lock_read
	@Test
	public void test8__set_the_state_to_RLT() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_read();
		so.unlock();
		so.lock_read();
		assertEquals(so.getState(), SharedObjectState.RLT);
	}

	// lock_write
	@Test
	public void test9__set_the_state_to_WLT() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_read();
		so.unlock();
		so.lock_write();
		assertEquals(so.getState(), SharedObjectState.WLT);
	}

	@Test
	public void test10__forward_write_lock_to_the_Client() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_read();
		so.unlock();
		so.lock_write();
		assertEquals(cm.lock_writeCount, 1);
	}

	// invalidate_reader
	@Test
	public void test11__set_the_state_to_NL() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_read();
		so.unlock();
		so.invalidate_reader();
		assertEquals(so.getState(), SharedObjectState.NL);
	}

	// CONTEST : state WLT

	// unlock
	@Test
	public void test12__set_the_state_to_WLC() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_write();
		so.unlock();
		assertEquals(so.getState(), SharedObjectState.WLC);
	}

	@Test
	public void test14__set_the_state_to_RLC() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_write();
		assertEquals(so.getState(), SharedObjectState.RLC);
	}

	@Test
	public void test15__set_the_state_to_NL() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_write();
		so.invalidate_writer();
		assertEquals(so.getState(), SharedObjectState.NL);
	}

	// CONTEST : state WLC

	// lock_read
	@Test
	public void test16__set_the_state_to_RLT_WLC() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_write();
		so.unlock();
		so.lock_read();
		assertEquals(so.getState(), SharedObjectState.RLT_WLC);
	}

	// lock_write
	@Test
	public void test17__set_the_state_to_WLT() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_write();
		so.unlock();
		so.lock_write();
		assertEquals(so.getState(), SharedObjectState.WLT);
	}

	// reduce_lock
	@Test
	public void test18__set_the_state_to_RLC() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_write();
		so.unlock();
		so.reduce_lock();
		assertEquals(so.getState(), SharedObjectState.RLC);
	}

	// invalidate_writer
	@Test
	public void test19__set_the_state_to_NL() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_write();
		so.unlock();
		so.invalidate_writer();
		assertEquals(so.getState(), SharedObjectState.NL);
	}

	// CONTEST : state RLT_WLC

	// unlock
	@Test
	public void test20__set_the_state_to_WLC() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_write();
		so.unlock();
		so.lock_read();
		so.unlock();
		assertEquals(so.getState(), SharedObjectState.WLC);
	}

	// reduce_lock
	@Test
	public void test21__set_the_state_to_RLT() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_write();
		so.unlock();
		so.lock_read();
		so.reduce_lock();
		assertEquals(so.getState(), SharedObjectState.RLT);
	}

	// invalidate_reader
	@Test
	public void test22__set_the_state_to_WLC() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_write();
		so.unlock();
		so.lock_read();
		so.invalidate_reader();
		assertEquals(so.getState(), SharedObjectState.WLC);
	}

	// invalidate_writer
	@Test
	public void test24__set_the_state_to_RLT() throws RemoteException {

		SharedObject so = new SharedObject();
		Test_ClientMock cm = new Test_ClientMock();
		so.setClient(cm);
		so.lock_write();
		so.unlock();
		so.lock_read();
		so.invalidate_writer();
		assertEquals(so.getState(), SharedObjectState.RLT);
	}
}
