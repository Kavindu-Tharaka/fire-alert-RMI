package rmi_server_codes;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.json.JSONArray;
import org.json.JSONObject;

public class RMIServer extends UnicastRemoteObject implements RMIService {

	public static void main(String[] args) throws RemoteException, AlreadyBoundException, IOException {

		Registry registry = LocateRegistry.createRegistry(5099);
		registry.bind("AirSensorService", new RMIServer());
	}
	
	protected RMIServer() throws RemoteException {
		super();
	}
	
    public int add(int a, int b) throws RemoteException {
        System.out.println("Adding " + a + " and " + b + " in the Server");
        return a+b;
    }

	@Override
	public String getAllSensorDetails() throws RemoteException {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder(URI.create("https://aq-visualizer.herokuapp.com/api/v1/sensors/")).build();
		return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::body)
		.thenApply((responseBody) -> parse(responseBody))
		.join();
	}
	
	public static String parse(String responseBody) {
		
//		JSONObject res = new JSONObject(responseBody);
//		JSONObject data = res.getJSONObject("data");
//		JSONArray sensors = data.getJSONArray("sensors");
//		
//		for (int i = 0; i < sensors.length(); i++) {
//			
//			JSONObject obj  = sensors.getJSONObject(i);
//					
//			boolean activated = obj.getBoolean("activated");
//			String _id = obj.getString("_id");
//			String floor = obj.getString("floor");
//			String room = obj.getString("room");
//			
//			System.out.println("activated : "+activated + "\n_id : "+_id + "\nfloor : "+floor + "\nroom : " + room +"\n\n");
//					
//		}
		return responseBody;
	}

}
