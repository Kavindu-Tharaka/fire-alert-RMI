package form_windows;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JSeparator;

import org.json.JSONArray;
import org.json.JSONObject;
import rmi_server_codes.RMIService;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;

public class DashboardForm extends JFrame {

	private static JPanel contentPane;
	private static String responseBody;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DashboardForm frame = new DashboardForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				RMIService service;
				
				try {
					
					service = (RMIService) Naming.lookup("rmi://localhost:5099/AirSensorService");
					
					//System.out.println  ("Add : " + service.add(2,2));
					
					responseBody = service.getAllSensorDetails();
					populateSensorComponents(responseBody);
					
				} catch (MalformedURLException | RemoteException | NotBoundException e) {
					e.printStackTrace();
				}		
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DashboardForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1082, 577);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Add New Sensor");
		mnNewMenu.add(mntmNewMenuItem);
		
		JSeparator separator = new JSeparator();
		mnNewMenu.add(separator);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Exit");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
//		contentPane.setLayout();
		
//		populateSensorComponents(responseBody);
	}
	
	public static void populateSensorComponents(String responseBody) {
		
		
		JSONObject res = new JSONObject(responseBody);
		JSONObject data = res.getJSONObject("data");
		JSONArray sensors = data.getJSONArray("sensors");
		
		for (int i = 0; i < sensors.length(); i++) {
			
			JSONObject obj  = sensors.getJSONObject(i);
					
			boolean activated = obj.getBoolean("activated");
			String _id = obj.getString("_id");
			String floor = obj.getString("floor");
			String room = obj.getString("room");
			
			System.out.println("))))activated : "+activated + "\n_id : "+_id + "\nfloor : "+floor + "\nroom : " + room +"\n\n");
					
		}
		
		

		int boundX = 0;
		int boundY = 0;
		int len = 10;
		
		if (len % 5 == 0) {
			
			int rows = len / 5;
			int cols = 5;
			
			for (int i = 1; i <= rows; i++) {
				
				for(int j = 1; j <= cols; j++) {
				
					SensorDetailComponent sensorDetailComponent = new SensorDetailComponent();
					sensorDetailComponent.setBackground(Color.red);
					sensorDetailComponent.setBounds(0+boundX, 0+boundY, 200, 120);
					sensorDetailComponent.setVisible(true);
					contentPane.add(sensorDetailComponent);		
					
					boundX += 220;		
				}
				boundX = 0;
				boundY += 150;
				System.out.println();
			}
		}
		else {
			
			int rows1 = len / 5;
			int cols1 = 5;
			
			int rows2 = 1;
			int cols2 = len % 5;
			
			for (int i = 1; i <= rows1; i++) {
				
				for(int j = 1; j <= cols1; j++) {
				
					SensorDetailComponent sensorDetailComponent = new SensorDetailComponent();
					sensorDetailComponent.setBackground(Color.red);
					sensorDetailComponent.setBounds(0+boundX, 0+boundY, 200, 120);
					sensorDetailComponent.setVisible(true);
					contentPane.add(sensorDetailComponent);		
					
					boundX += 220;		
				}
				boundX = 0;
				boundY += 150;
				System.out.println();
			}
			
			for (int i = 1; i <= rows2; i++) {
				
				for(int j = 1; j <= cols2; j++) {
				
					SensorDetailComponent sensorDetailComponent = new SensorDetailComponent();
					sensorDetailComponent.setBackground(Color.red);
					sensorDetailComponent.setBounds(0+boundX, 0+boundY, 200, 120);
					sensorDetailComponent.setVisible(true);
					contentPane.add(sensorDetailComponent);		
					
					boundX += 220;		
				}
				boundX = 0;
				boundY += 150;
				System.out.println();
			}
		}
	}
}
