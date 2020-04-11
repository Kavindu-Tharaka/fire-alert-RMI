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

import org.apache.http.client.methods.HttpPatch;
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
		
		
		Timer t = new Timer(0, null);
		
		t.addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent e) {				
				try {
					//checkStateRepeatedly();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
		    }
		});

		t.setRepeats(true);
		t.setDelay(15000); //repeat every 15 sec
		t.start(); 
		
	}
	
	protected RMIServer() throws RemoteException {
		super();
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
	
	public static void checkStateRepeatedly() {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder(URI.create("https://aq-visualizer.herokuapp.com/api/v1/sensors/")).build();
		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::body)
		.thenApply((responseBody) -> checkCo2Level(responseBody))
		.join();
	}

	private static String checkCo2Level(String responseBody) {
		
		JSONObject res = new JSONObject(responseBody);
		JSONObject data = res.getJSONObject("data");
		JSONArray sensors = data.getJSONArray("sensors");
		
		for (int i = 0; i < sensors.length(); i++) {
			
			JSONObject obj  = sensors.getJSONObject(i);
			
			JSONObject lastReading = obj.getJSONObject("lastReading");
			
			int co2Level = lastReading.getInt("co2Level");
			int smokeLevel = lastReading.getInt("smokeLevel");
			String _id = obj.getString("_id");
			
			if (co2Level > 5) {
				
				JSONObject jsonReading = new JSONObject();
				jsonReading.put("smokeLevel", smokeLevel); 
				jsonReading.put("co2Level", co2Level); 
				
				JSONObject json = new JSONObject();
				json.put("to", "kavindu.ktm@gmail.com");    
				json.put("sensor", _id);    
				json.put("reading", jsonReading);    


				CloseableHttpClient httpClient = HttpClientBuilder.create().build();
						
				try {
				    HttpPost request = new HttpPost("https://aq-visualizer.herokuapp.com/api/v1/email");
				    StringEntity params = new StringEntity(json.toString());
				    request.addHeader("content-type", "application/json");
				    request.addHeader("Authorization", "agfYjhdioJK5ghiH46dHr8gfg857jfrJYuit57uo");
				    request.setEntity(params);
				    org.apache.http.HttpResponse response = httpClient.execute(request);

				    
				    System.out.println(response.getStatusLine().toString().equalsIgnoreCase("HTTP/1.1 201 Created") ? "Email has Sent" : "Email has not Sent");
				    

				} catch (Exception ex) {
				    ex.printStackTrace();
				} finally {
				    try {
						httpClient.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return null;
	}

	@Override
	public boolean addSensor(String id, int floor, String room) throws RemoteException {
		
		boolean res = false;
		
		JSONObject json = new JSONObject();
		json.put("_id", id);    
		json.put("floor", floor);    
		json.put("room", room);    


		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
				
		try {
		    HttpPost request = new HttpPost("https://aq-visualizer.herokuapp.com/api/v1/sensors");
		    StringEntity params = new StringEntity(json.toString());
		    request.addHeader("content-type", "application/json");
		    request.addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVlOTE2ZTA5YjM1MTU1MDAxN2NlYzJhOCIsImlhdCI6MTU4NjU4OTE5NCwiZXhwIjoxNTg5MTgxMTk0fQ.WjrtcFOBBL8dPVIGKZlCobAvaFuYrfB_J5cMWsEwcCE");
		    request.setEntity(params);
		    org.apache.http.HttpResponse response = httpClient.execute(request);

		    System.out.println(response.getStatusLine().toString().equalsIgnoreCase("HTTP/1.1 201 Created"));
		    
		    res = response.getStatusLine().toString().equalsIgnoreCase("HTTP/1.1 201 Created");
		    
		} catch (Exception ex) {
		    ex.printStackTrace();
		} finally {
		    try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return res;
	}

	@Override
	public boolean editSensor(String id, int floor, String room) throws RemoteException {

		boolean res = false;
		
		JSONObject json = new JSONObject();
		json.put("_id", id);    
		//json.put("activated", activated);    
		json.put("floor", floor);    
		json.put("room", room);     


		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
				
		try {
		    HttpPatch request = new HttpPatch("https://aq-visualizer.herokuapp.com/api/v1/sensors/"+id);
		    StringEntity params = new StringEntity(json.toString());
		    request.addHeader("content-type", "application/json");
		    request.addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVlOTE2ZTA5YjM1MTU1MDAxN2NlYzJhOCIsImlhdCI6MTU4NjU4OTE5NCwiZXhwIjoxNTg5MTgxMTk0fQ.WjrtcFOBBL8dPVIGKZlCobAvaFuYrfB_J5cMWsEwcCE");
		    request.setEntity(params);
		    org.apache.http.HttpResponse response = httpClient.execute(request);

		    System.out.println(response.getStatusLine().toString().equalsIgnoreCase("HTTP/1.1 200 OK"));
		    
		    res = response.getStatusLine().toString().equalsIgnoreCase("HTTP/1.1 200 OK");
		    
		} catch (Exception ex) {
		    System.out.println(ex);
		} finally {
		    try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	

}
