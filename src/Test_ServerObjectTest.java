import java.rmi.RemoteException;

import org.junit.Test;
import static org.junit.Assert.*;

public class Test_ServerObjectTest {

	// CONTEXT : no write lock is taken on the object held by the ServerObject

	// ---------
	// lock_read
	// ---------

	@Test
	public void test1__lock_the_object_on_state_RL() throws RemoteException {

		String o = new String("Object held by the ServerObject");
		ServerObject so = new ServerObject(7, o);
		Test_ClientMock futureReader = new Test_ClientMock();
		so.lock_read(futureReader);
		assertEquals(so.getState(), ServerObjectState.RL);
	}

	@Test
	public void test2__save_the_reader_to_be_able_to_call_it_later() throws RemoteException {

		String o = new String("Object held by the ServerObject");
		ServerObject so = new ServerObject(7, o);
		Client futureReader = new Test_ClientMock();
		so.lock_read(futureReader);
		assertTrue(so.getReaders().contains(futureReader)); // TODO method getReaders on class
	}

	@Test
	public void test3__return_the_object_to_the_reader() throws RemoteException {

		String o = new String("Object held by the ServerObject");
		ServerObject so = new ServerObject(7, o);
		Test_ClientMock futureReader = new Test_ClientMock();
		Object objectReturned = so.lock_read(futureReader);
		assertEquals(o, objectReturned);
	}

	// ----------
	// lock_write
	// ----------

	@Test
	public void test4__lock_the_object_on_state_WL() throws RemoteException {

		String o = new String("Object held by the ServerObject");
		ServerObject so = new ServerObject(7, o);
		Test_ClientMock futureWriter = new Test_ClientMock();
		so.lock_write(futureWriter);
		assertEquals(so.getState(), ServerObjectState.WL);
	}

	@Test
	public void test5__save_the_writer_to_be_able_to_call_it_later() throws RemoteException {

		String o = new String("Object held by the ServerObject");
		ServerObject so = new ServerObject(7, o);
		Test_ClientMock futureWriter = new Test_ClientMock();
		so.lock_write(futureWriter);
		assertSame(so.getWriter(), futureWriter);
	}

	@Test
	public void test6__return_the_object_to_the_writer() throws RemoteException {

		String o = new String("Object held by the ServerObject");
		ServerObject so = new ServerObject(7, o);
		Test_ClientMock futureWriter = new Test_ClientMock();
		Object objectReturned = so.lock_write(futureWriter);
		assertEquals(o, objectReturned);
	}

	// CONTEXT : if a write lock is taken on the object

	// ---------
	// lock_read
	// ---------

	@Test
	public void test7__call_the_writer_reduce_lock_method() throws RemoteException {

		String o = new String("Object held by the ServerObject");
		ServerObject so = new ServerObject(7, o);
		Test_ClientMock currentWriter = new Test_ClientMock();
		Test_ClientMock futureReader = new Test_ClientMock();
		so.lock_write(currentWriter); // take the write lock
		so.lock_read(futureReader);
		assertEquals(currentWriter.reduce_lockCount, 1);
	}

	@Test
	public void test8__save_the_reader_to_be_able_to_call_it_later() throws RemoteException {

		String o = new String("Object held by the ServerObject");
		ServerObject so = new ServerObject(7, o);
		Test_ClientMock currentWriter = new Test_ClientMock();
		Test_ClientMock futureReader = new Test_ClientMock();
		so.lock_write(currentWriter); // take the write lock
		so.lock_read(futureReader);
		assertTrue(so.getReaders().contains(futureReader)); // TODO method getReaders on class
	}

	@Test
	public void test10__set_the_ServerObject_state_to_RL() throws RemoteException {

		String o = new String("Object held by the ServerObject");
		ServerObject so = new ServerObject(7, o);
		Test_ClientMock currentWriter = new Test_ClientMock();
		Test_ClientMock futureReader = new Test_ClientMock();
		so.lock_write(currentWriter); // take the write lock
		so.lock_read(futureReader);
		assertEquals(so.getState(), ServerObjectState.RL);
	}

	// ----------
	// lock_write
	// ----------

	@Test
	public void test11__call_the_writer_invalidate_writer_mehtod() throws RemoteException {

		String o = new String("Object held by the ServerObject");
		ServerObject so = new ServerObject(7, o);
		Test_ClientMock currentWriter = new Test_ClientMock();
		Test_ClientMock futureWriter = new Test_ClientMock();
		so.lock_write(currentWriter); // take the write lock
		so.lock_write(futureWriter);
		assertEquals(currentWriter.invalidate_writerCount, 1);
	}

	@Test
	public void test12__let_the_object_on_state_WL() throws RemoteException {

		String o = new String("Object held by the ServerObject");
		ServerObject so = new ServerObject(7, o);
		Test_ClientMock currentWriter = new Test_ClientMock();
		Test_ClientMock futureWriter = new Test_ClientMock();
		so.lock_write(currentWriter); // take the write lock
		so.lock_write(futureWriter);
		assertEquals(so.getState(), ServerObjectState.WL);
	}

	@Test
	public void test13__save_the_writer_to_be_able_to_call_it_later() throws RemoteException {

		String o = new String("Object held by the ServerObject");
		ServerObject so = new ServerObject(7, o);
		Test_ClientMock currentWriter = new Test_ClientMock();
		Test_ClientMock futureWriter = new Test_ClientMock();
		so.lock_write(currentWriter); // take the write lock
		so.lock_write(futureWriter);
		assertSame(so.getWriter(), futureWriter);
	}

	// CONTEXT : if a read lock is taken on the object

	// ---------
	// lock_read
	// ---------

	@Test
	public void test15__let_the_object_on_state_RL() throws RemoteException {

		String o = new String("Object held by the ServerObject");
		ServerObject so = new ServerObject(7, o);
		Test_ClientMock currentReader = new Test_ClientMock();
		Test_ClientMock futureReader = new Test_ClientMock();
		so.lock_read(currentReader);
		so.lock_read(futureReader);
		assertEquals(so.getState(), ServerObjectState.RL);
	}

	@Test
	public void test16__save_all_readers_to_be_able_to_call_them_later() throws RemoteException {

		String o = new String("Object held by the ServerObject");
		ServerObject so = new ServerObject(7, o);
		Test_ClientMock currentReader = new Test_ClientMock();
		Test_ClientMock futureReader = new Test_ClientMock();
		so.lock_read(currentReader);
		so.lock_read(futureReader);
		assertTrue(so.getReaders().contains(currentReader)); // TODO method getReaders on class
																// ServerObject
		assertTrue(so.getReaders().contains(futureReader));
	}

	@Test
	public void test17__return_the_object_to_the_reader() throws RemoteException {

		String o = new String("Object held by the ServerObject");
		ServerObject so = new ServerObject(7, o);
		Test_ClientMock currentReader = new Test_ClientMock();
		Test_ClientMock futureReader = new Test_ClientMock();
		so.lock_read(currentReader);
		Object objectReturned = so.lock_read(futureReader);
		assertEquals(o, objectReturned);
	}

	// -----------
	// lock_writer
	// -----------

	@Test
	public void test18__call_readers_invalidate_reader_method() throws RemoteException {

		String o = new String("Object held by the ServerObject");
		ServerObject so = new ServerObject(7, o);
		Test_ClientMock currentReader = new Test_ClientMock();
		Test_ClientMock futureWriter = new Test_ClientMock();
		so.lock_read(currentReader);
		so.lock_write(futureWriter);
		assertEquals(currentReader.invalidate_readerCount, 1);
	}

	@Test
	public void test19__save_the_writer_to_be_able_to_call_it_later() throws RemoteException {

		String o = new String("Object held by the ServerObject");
		ServerObject so = new ServerObject(7, o);
		Test_ClientMock currentReader = new Test_ClientMock();
		Test_ClientMock futureWriter = new Test_ClientMock();
		so.lock_read(currentReader);
		so.lock_write(futureWriter);
		assertSame(so.getWriter(), futureWriter);
	}

	@Test
	public void test20__set_the_ServerObject_state_to_WL() throws RemoteException {

		String o = new String("Object held by the ServerObject");
		ServerObject so = new ServerObject(7, o);
		Test_ClientMock currentReader = new Test_ClientMock();
		Test_ClientMock futureWriter = new Test_ClientMock();
		so.lock_read(currentReader);
		so.lock_write(futureWriter);
		assertEquals(so.getState(), ServerObjectState.WL);
	}

	@Test
	public void test21__return_the_object_to_the_writer() throws RemoteException {

		String o = new String("Object held by the ServerObject");
		ServerObject so = new ServerObject(7, o);
		Test_ClientMock currentReader = new Test_ClientMock();
		Test_ClientMock futureWriter = new Test_ClientMock();
		so.lock_read(currentReader);
		Object objectReturned = so.lock_write(futureWriter);
		assertSame(o, objectReturned);
	}
}