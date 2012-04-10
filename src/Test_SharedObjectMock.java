public class Test_SharedObjectMock extends SharedObject {

	public int lock_readCount;
	public int lock_writeCount;
	public int unlockCount;
	public int invalidate_readerCount;
	public int invalidate_writerCount;
	public int reduce_lockCount;
	public int getIdCount;

	public Test_SharedObjectMock() {

		super();
		lock_readCount = 0;
		lock_writeCount = 0;
		unlockCount = 0;
		invalidate_readerCount = 0;
		invalidate_writerCount = 0;
		reduce_lockCount = 0;
		getIdCount = 0;
	}

	public void lock_read() {

		lock_readCount++;
	}

	public void lock_write() {

		lock_writeCount++;
	}

	public synchronized void unlock() {

		unlockCount++;
	}

	public synchronized Object reduce_lock() {

		reduce_lockCount++;
		return null;
	}

	public synchronized void invalidate_reader() {

		invalidate_readerCount++;
	}

	public synchronized Object invalidate_writer() {

		invalidate_writerCount++;
		return null;
	}

	public int getId() {

		getIdCount++;
		return 0;
	}
}
