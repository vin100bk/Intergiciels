import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.HashMap;
import java.util.Map;
import java.net.*;

public class Client extends UnicastRemoteObject implements Client_itf {

	private static Server server;
	private static Map<Integer, SharedObject> objs;

	public Client() throws RemoteException {

		super();
	}

	// /////////////////////////////////////////////////
	// Interface to be used by applications
	// /////////////////////////////////////////////////

	// initialization of the client layer
	public static void init() {

		if (Client.server == null)
		{
			Client.server = new Server();
		}
		if (Client.objs == null)
		{
			Client.objs = new BidiMap<Integer, SharedObject_itf>();
		}
	}

	// lookup in the name server
	public static SharedObject lookup(String name) {

		SharedObject ret = null;
		
		try
		{
			ret = (SharedObject) objs.get(Client.server.lookup(name));
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		
		return ret;
	}

	// binding in the name server
	public static void register(String name, SharedObject so) {

		Client.server.register(name, so.getId());
	}

	// creation of a shared object
	public static SharedObject create(Object o) {

		SharedObject ret = null;
		
		try
		{
			int objId = Client.server.create(o);
			ret = new SharedObject(objId, o);
			objs.put(objId, ret);
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		
		return ret;
	}

	// ///////////////////////////////////////////////////////////
	// Interface to be used by the consistency protocol
	// //////////////////////////////////////////////////////////

	// request a read lock from the server
	public static Object lock_read(int id) {

	}

	// request a write lock from the server
	public static Object lock_write(int id) {

	}

	// receive a lock reduction request from the server
	public Object reduce_lock(int id) throws java.rmi.RemoteException {

	}

	// receive a reader invalidation request from the server
	public void invalidate_reader(int id) throws java.rmi.RemoteException {

	}

	// receive a writer invalidation request from the server
	public Object invalidate_writer(int id) throws java.rmi.RemoteException {

	}
}
