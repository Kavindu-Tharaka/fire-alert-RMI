package rmi_server_codes;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIService extends Remote{

	public int add(int a, int b) throws RemoteException;

	public String getAllSensorDetails() throws RemoteException;
	
	
}
