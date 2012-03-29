import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import parkingAsyn.Parking;

public class Server implements Server_itf {

	private static final String nameServerUrl = "";
	private int currentId;

	public Server() {

		this.currentId = 0;
	}

	@Override
	public int lookup(String name) throws RemoteException {

		try {
			Integer unParking = (Integer) Naming.lookup("rmi://" + nameServerUrl + "/" + name);
		} 
		catch(NotBoundException e) { 
			
		}
		

		
		return 0;
	}

	@Override
	public void register(String name, int id) throws RemoteException {

		// TODO Auto-generated method stub

	}

	@Override
	public int create(Object o) throws RemoteException {

		
		this.currentId++;
		return this.currentId;
	}

	@Override
	public Object lock_read(int id, Client_itf client) throws RemoteException {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object lock_write(int id, Client_itf client) throws RemoteException {

		// TODO Auto-generated method stub
		return null;
	}

}
