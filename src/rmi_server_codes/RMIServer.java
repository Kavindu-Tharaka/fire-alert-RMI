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

import org.apache.http.client.methods.HttpDelete;
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

		System.out.println("Server starts.....!");
		
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
		t.setDelay(15000); // repeat interval
		t.start();

	}

	protected RMIServer() throws RemoteException {
		super();
	}

	/*
	 * used to retrieve sensor readings and other details of all sensors
	 */
	@Override
	public String getAllSensorDetails() throws RemoteException {
		HttpClient client = HttpClient.newHttpClient();
		// prepare a HTTP request to send to API
		HttpRequest request = HttpRequest
				.newBuilder(URI.create("https://fire-alert-solution.herokuapp.com/api/v1/sensors/")).build();
		return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body)
				.thenApply((responseBody) -> parse(responseBody)).join();
	}

	public static String parse(String responseBody) {
		return responseBody;
	}

	/*
	 * used to retrieve authenticate Admin login credentials
	 */
	@Override
	public String loginValidator(String email, String password) throws RemoteException {

		JSONObject json = new JSONObject();
		json.put("email", email);
		json.put("password", password);

		String res = null;

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
			// prepare a HTTP request to send to API
			HttpPost request = new HttpPost("https://fire-alert-solution.herokuapp.com/api/v1/admin/login");
			StringEntity params = new StringEntity(json.toString());
			// add headers to the request
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			org.apache.http.HttpResponse response = httpClient.execute(request);

			// check the response
			if (response.getStatusLine().toString().equalsIgnoreCase("HTTP/1.1 200 OK")) {
				res = "success";
			} else {
				res = "failed";
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

	/*
	 * used to check co2 and smoke levels repeatedly
	 * 
	 */
	public static void checkStateRepeatedly() {
		HttpClient client = HttpClient.newHttpClient();
		// prepare a HTTP request to send to API
		HttpRequest request = HttpRequest
				.newBuilder(URI.create("https://fire-alert-solution.herokuapp.com/api/v1/sensors/")).build();
		client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body)
				.thenApply((responseBody) -> checkCo2andSmokeLevel(responseBody)).join();
	}

	private static String checkCo2andSmokeLevel(String responseBody) {

		JSONObject res = new JSONObject(responseBody);
		JSONObject data = res.getJSONObject("data");
		JSONArray sensors = data.getJSONArray("sensors");

		for (int i = 0; i < sensors.length(); i++) {

			JSONObject obj = sensors.getJSONObject(i);

			JSONObject lastReading = obj.getJSONObject("lastReading");

			int co2Level = lastReading.getInt("co2Level");
			int smokeLevel = lastReading.getInt("smokeLevel");
			String _id = obj.getString("_id");

			if (co2Level > 5 || smokeLevel > 5) {

				// create JSON object to send with Email API call
				JSONObject jsonReadingEmail = new JSONObject();
				jsonReadingEmail.put("smokeLevel", smokeLevel);
				jsonReadingEmail.put("co2Level", co2Level);

				JSONObject jsonEmail = new JSONObject();
				jsonEmail.put("to", "kavindu.ktm@gmail.com");
				jsonEmail.put("sensor", _id);
				jsonEmail.put("reading", jsonReadingEmail);

				// create JSON object to send with SMS API call
				JSONObject jsonReadingSms = new JSONObject();
				jsonReadingSms.put("smokeLevel", smokeLevel);
				jsonReadingSms.put("co2Level", co2Level);

				JSONObject jsonSms = new JSONObject();
				jsonSms.put("to", "+94762210487");
				jsonSms.put("sensor", _id);
				jsonSms.put("reading", jsonReadingSms);

				CloseableHttpClient httpClient = HttpClientBuilder.create().build();

				try {
					// prepare a HTTP request to send to API to send email
					HttpPost requestEmail = new HttpPost("https://fire-alert-solution.herokuapp.com/api/v1/email");
					StringEntity paramsEmail = new StringEntity(jsonEmail.toString());
					// add headers to the request
					requestEmail.addHeader("content-type", "application/json");
					requestEmail.addHeader("Authorization", "agfYjhdioJK5ghiH46dHr8gfg857yfrJYuit57vf");
					requestEmail.setEntity(paramsEmail);
					org.apache.http.HttpResponse responseEmail = httpClient.execute(requestEmail);

					// prepare a HTTP request to send to API to send SMS
					HttpPost requestSms = new HttpPost("https://fire-alert-solution.herokuapp.com/api/v1/sms");
					StringEntity paramsSms = new StringEntity(jsonSms.toString());
					// add headers to the request
					requestSms.addHeader("content-type", "application/json");
					requestSms.addHeader("Authorization", "agfYjhdioJK5ghiH46dHr8gfg857yfrJYuit57vf");
					requestSms.setEntity(paramsSms);
					org.apache.http.HttpResponse responseSms = httpClient.execute(requestSms);

					// check the responses
					System.out.println(responseEmail.getStatusLine().toString().equalsIgnoreCase("HTTP/1.1 201 Created")
							? "Email has Sent"
							: "Email has not Sent");
					System.out.println(responseSms.getStatusLine().toString().equalsIgnoreCase("HTTP/1.1 201 Created")
							? "Sms has Sent"
							: "Sms has not Sent");

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

	/*
	 * used to register a new sensor
	 */
	@Override
	public boolean addSensor(String id, int floor, String room) throws RemoteException {

		boolean res = false;

		JSONObject json = new JSONObject();
		json.put("_id", id);
		json.put("floor", floor);
		json.put("room", room);

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
			// prepare a HTTP request to send to API
			HttpPost request = new HttpPost("https://fire-alert-solution.herokuapp.com/api/v1/sensors");
			StringEntity params = new StringEntity(json.toString());
			// add headers to the request
			request.addHeader("content-type", "application/json");
			request.addHeader("Authorization",
					"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVlOWRiYzlmZTE3NTBiMDAxN2Q1OGRiOSIsImlhdCI6MTU4NzM5NTc0NCwiZXhwIjoxNTg5OTg3NzQ0fQ.4MZXhOVMVkiMboNhoGyiCDeuY6yfysrgH70PB1nAKok");
			request.setEntity(params);
			org.apache.http.HttpResponse response = httpClient.execute(request);

			System.out.println(response.getStatusLine().toString().equalsIgnoreCase("HTTP/1.1 201 Created"));

			// check the response
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

	/*
	 * used to edit existing sensor details
	 */
	@Override
	public boolean editSensor(String id, int floor, String room) throws RemoteException {

		boolean res = false;

		JSONObject json = new JSONObject();
		json.put("_id", id);
		// json.put("activated", activated);
		json.put("floor", floor);
		json.put("room", room);

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
			// prepare a HTTP request to send to API
			HttpPatch request = new HttpPatch("https://fire-alert-solution.herokuapp.com/api/v1/sensors/" + id);
			StringEntity params = new StringEntity(json.toString());
			// add headers to the request
			request.addHeader("content-type", "application/json");
			request.addHeader("Authorization",
					"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVlOWRiYzlmZTE3NTBiMDAxN2Q1OGRiOSIsImlhdCI6MTU4NzM5NTc0NCwiZXhwIjoxNTg5OTg3NzQ0fQ.4MZXhOVMVkiMboNhoGyiCDeuY6yfysrgH70PB1nAKok");
			request.setEntity(params);
			org.apache.http.HttpResponse response = httpClient.execute(request);

			System.out.println(response.getStatusLine().toString().equalsIgnoreCase("HTTP/1.1 200 OK"));

			// check the response
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

	/*
	 * used to delete existing sensor
	 */
	@Override
	public boolean deleteSensor(String id) throws RemoteException {

		boolean res = false;

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
			// prepare a HTTP request to send to API
			HttpDelete request = new HttpDelete("https://fire-alert-solution.herokuapp.com/api/v1/sensors/" + id);
			// add headers to the request
			request.addHeader("content-type", "application/json");
			request.addHeader("Authorization",
					"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVlOWRiYzlmZTE3NTBiMDAxN2Q1OGRiOSIsImlhdCI6MTU4NzM5NTc0NCwiZXhwIjoxNTg5OTg3NzQ0fQ.4MZXhOVMVkiMboNhoGyiCDeuY6yfysrgH70PB1nAKok");
			org.apache.http.HttpResponse response = httpClient.execute(request);

			// check the response
			res = response.getStatusLine().toString().equalsIgnoreCase("HTTP/1.1 204 No Content");

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
