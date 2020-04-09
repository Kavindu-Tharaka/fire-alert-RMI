package rmi_server_codes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import javax.swing.Timer;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import form_windows.SensorDetailComponent;


public class RMIServer extends UnicastRemoteObject implements RMIService {

	public static void main(String[] args) throws RemoteException, AlreadyBoundException, IOException {

		Registry registry = LocateRegistry.createRegistry(5099);
		registry.bind("AirSensorService", new RMIServer());
		
		
//		Timer t = new Timer(0, null);
//		String responseBody = "";
//		
//		t.addActionListener(new ActionListener() {
//
//		    @Override
//		    public void actionPerformed(ActionEvent e) {
//				//System.out.println  ("Add : " + service.add(2,2));
//				
//				try {
//					responseBody = getAllSensorDetails();
//				} catch (RemoteException e1) {
//					e1.printStackTrace();
//				}
//		    }
//		});
//
//		t.setRepeats(true);
//		t.setDelay(5000); //repeat every 30 sec
//		t.start(); 
		
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

	@Override
	public String loginValidator(String email, String password) throws RemoteException {
				
		JSONObject json = new JSONObject();
		json.put("email", email);    
		json.put("password", password); 
		
		String res = null;


		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
				
		try {
		    HttpPost request = new HttpPost("http://aq-visualizer.herokuapp.com/api/v1/admin/login");
		    StringEntity params = new StringEntity(json.toString());
		    request.addHeader("content-type", "application/json");
		    request.setEntity(params);
		    org.apache.http.HttpResponse response = httpClient.execute(request);

		    System.out.println(response.getStatusLine().toString().equalsIgnoreCase("HTTP/1.1 200 OK"));
		    System.out.println(response.getStatusLine().toString().equalsIgnoreCase("HTTP/1.1 400 Bad Request"));

		    if (response.getStatusLine().toString().equalsIgnoreCase("HTTP/1.1 200 OK")) {
				res =  "success";
			}
		    else{
		    	res =  "failed";
			}
		    
		    
		} catch (Exception ex) {
		    ex.printStackTrace();
		} finally {
		    try {
				httpClient.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return res;
	}
	
	public String checkStateRepeatedly() {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder(URI.create("https://aq-visualizer.herokuapp.com/api/v1/sensors/")).build();
		return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::body)
		.thenApply((responseBody) -> checkCo2Level(responseBody))
		.join();
	}

	private String checkCo2Level(String responseBody) {
		
		JSONObject res = new JSONObject(responseBody);
		JSONObject data = res.getJSONObject("data");
		JSONArray sensors = data.getJSONArray("sensors");
		
		for (int i = 0; i < sensors.length(); i++) {
			
			JSONObject obj  = sensors.getJSONObject(i);
			
			JSONObject lastReading = obj.getJSONObject("lastReading");
			
			int co2Level = lastReading.getInt("co2Level");
			int smokeLevel = lastReading.getInt("smokeLevel");
			String _id = obj.getString("_id");
			String floor = obj.getString("floor");
			String room = obj.getString("room");

			if (co2Level > 5) {
				
			}
			
		}
		
		return null;
	}
	

}
