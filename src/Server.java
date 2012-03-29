import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Server extends UnicastRemoteObject implements Server_itf, Serializable {

	private static final long serialVersionUID = 2L;

	private int incrementId;
	private Map<Integer, ServerObject> objs;
	private Map<String, Integer> objsNames;

	public static void main(String[] args) throws RemoteException, MalformedURLException,
			AlreadyBoundException {

		// RMI
		LocateRegistry.createRegistry(3535);
		Naming.bind("rmi://localhost:3535/ReplicateServer", new Server());
	}

	public Server() throws RemoteException, MalformedURLException, AlreadyBoundException {

		this.incrementId = 0;
		this.objs = new HashMap<Integer, ServerObject>();
		this.objsNames = new HashMap<String, Integer>();
	}

	@Override
	public int lookup(String name) throws RemoteException {
		
		int objId = 0;
		
		if(this.objsNames.get(name) != null)
		{
			objId = this.objsNames.get(name);
		}

		return objId;
	}

	@Override
	public void register(String name, int id) throws RemoteException {

		this.objsNames.put(name, id);
	}

	@Override
	public int create(Object o) throws RemoteException {

		this.incrementId++;
		this.objs.put(this.incrementId, new ServerObject(this.incrementId, o));

		return this.incrementId;
	}

	@Override
	public Object lock_read(int id, Client_itf client) throws RemoteException {

		return this.objs.get(new Integer(id)).lock_read(client);
	}

	@Override
	public Object lock_write(int id, Client_itf client) throws RemoteException {

		return this.objs.get(new Integer(id)).lock_write(client);
	}

}
