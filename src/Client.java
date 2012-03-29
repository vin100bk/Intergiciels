import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.net.*;

public class Client extends UnicastRemoteObject implements Client_itf {

	private static final long serialVersionUID = 1L;
	
	private static Server_itf server;
	private static Map<Integer, SharedObject> objs;
	private static Client client;

	public Client() throws RemoteException {

		super();
	}

	// /////////////////////////////////////////////////
	// Interface to be used by applications
	// /////////////////////////////////////////////////

	// initialization of the client layer
	public static void init() {

		try
		{
			server = (Server_itf) Naming.lookup("rmi://localhost:3535/ReplicateServer");
			objs = new HashMap<Integer, SharedObject>();
			client = new Client();
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		catch (NotBoundException e)
		{
			e.printStackTrace();
		}
	}

	// lookup in the name server
	public static SharedObject lookup(String name) {

		SharedObject ret = null;

		try
		{
			ret = objs.get(server.lookup(name));
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}

		return ret;
	}

	// binding in the name server
	public static void register(String name, SharedObject so) {

		try
		{
			server.register(name, so.getId());
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

	// creation of a shared object
	public static SharedObject create(Object o) {

		SharedObject ret = null;

		try
		{
			int objId = server.create(o);
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

		Object ret = null;
		
		try
		{
			ret = server.lock_read(id, client);
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		
		return ret;
	}

	// request a write lock from the server
	public static Object lock_write(int id) {

		Object ret = null;
		
		try
		{
			ret = server.lock_write(id, client);
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		
		return ret;
	}

	// receive a lock reduction request from the server
	public Object reduce_lock(int id) throws java.rmi.RemoteException {

		return objs.get(id).reduce_lock();
	}

	// receive a reader invalidation request from the server
	public void invalidate_reader(int id) throws java.rmi.RemoteException {

		objs.get(id).invalidate_reader();
	}

	// receive a writer invalidation request from the server
	public Object invalidate_writer(int id) throws java.rmi.RemoteException {
		
		return objs.get(id).invalidate_writer();
	}
}
