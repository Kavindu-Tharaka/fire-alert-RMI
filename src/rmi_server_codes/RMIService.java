package rmi_server_codes;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIService extends Remote {

	/*
	 * methods that are exposed to desktop client
	 * */
	
	public String getAllSensorDetails() throws RemoteException;

	public String loginValidator(String email, String password) throws RemoteException;

	public boolean addSensor(String id, int floor, String room) throws RemoteException;

	public boolean editSensor(String id, int floor, String room) throws RemoteException;

	public boolean deleteSensor(String id) throws RemoteException;

}
