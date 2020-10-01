package rmi_server_codes;

import java.rmi.Remote;
import java.rmi.RemoteException;


/*
* this interface contains the methods that are exposed to desktop client
* */
public interface RMIService extends Remote {
	
	
	//used to retrieve sensor readings and other details of all sensors
	public String getAllSensorDetails() throws RemoteException;

	//used to retrieve authenticate Admin login credentials
	public String loginValidator(String email, String password) throws RemoteException;

	//used to register a new sensor
	public boolean addSensor(String id, int floor, String room) throws RemoteException;

	//used to edit existing sensor details
	public boolean editSensor(String id, int floor, String room) throws RemoteException;

	//used to delete existing sensor
	public boolean deleteSensor(String id) throws RemoteException;

}
