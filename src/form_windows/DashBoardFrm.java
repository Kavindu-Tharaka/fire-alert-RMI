package form_windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;
import org.json.JSONObject;

import rmi_server_codes.RMIService;

import java.awt.GridLayout;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.Timer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DashBoardFrm extends JFrame {

	private static JPanel contentPane;
	private static String responseBody;
	private static boolean isAdmin = false;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DashBoardFrm frame = new DashBoardFrm(isAdmin);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				RMIService service;
				
				try {
					
					service = (RMIService) Naming.lookup("rmi://localhost:5099/AirSensorService");
					
					Timer t = new Timer(0, null);

					t.addActionListener(new ActionListener() {

					    @Override
					    public void actionPerformed(ActionEvent e) {
							//System.out.println  ("Add : " + service.add(2,2));
							
							try {
								responseBody = service.getAllSensorDetails();
							} catch (RemoteException e1) {
								e1.printStackTrace();
							}
							populateSensorComponents(responseBody);
					    }
					});

					t.setRepeats(true);
					t.setDelay(30000); //repeat every 30 sec
					t.start(); 

					
				} catch (MalformedURLException | RemoteException | NotBoundException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DashBoardFrm(boolean isAdminn) {
		isAdmin = isAdminn;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 542, 548);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Add New Sensor");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Menu Clicked");
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		mntmNewMenuItem.setVisible(isAdminn);
		
		JSeparator separator = new JSeparator();
		mnNewMenu.add(separator);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Exit");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(EXIT_ON_CLOSE);
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);
		
	
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		GridLayout gl_contentPane = new GridLayout();
		gl_contentPane.setColumns(1);
		gl_contentPane.setRows(0);
		contentPane.setLayout(gl_contentPane);
		
		//contentPane.add(new JButton("A"));
		
		JScrollPane scrollPane = new JScrollPane(contentPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setContentPane(scrollPane);
		
		//main(null);
	}
	
	public static void populateSensorComponents(String responseBody) {
		
		contentPane.removeAll();
		
		JSONObject res = new JSONObject(responseBody);
		JSONObject data = res.getJSONObject("data");
		JSONArray sensors = data.getJSONArray("sensors");
		
		for (int i = 0; i < sensors.length(); i++) {
			
			JSONObject obj  = sensors.getJSONObject(i);
			
			JSONObject lastReading = obj.getJSONObject("lastReading");
			
			int co2Level = lastReading.getInt("co2Level");
			int smokeLevel = lastReading.getInt("smokeLevel");
			String time = lastReading.getString("time");

			boolean activated = obj.getBoolean("activated");
			String _id = obj.getString("_id");
			String floor = obj.getString("floor");
			String room = obj.getString("room");
			
			System.out.println("activated : "+activated + "\n_id : "+_id + "\nfloor : "+floor + "\nroom : " + room +"\nco2Level : "+ co2Level +"\nsmokeLevel : " +smokeLevel+  "\ntime : " + time + "\n\n");
					
			SensorDetailComponent sensorDetailComponent = new SensorDetailComponent(_id, floor, room, activated, co2Level, smokeLevel);
			sensorDetailComponent.setVisible(true);
			contentPane.add(sensorDetailComponent);	
		}
		
		contentPane.validate();
		contentPane.repaint();
		

//			for (int i = 1; i <= 1; i++) {
//					SensorDetailComponent sensorDetailComponent = new SensorDetailComponent();
//					sensorDetailComponent.setVisible(true);
//					contentPane.add(sensorDetailComponent);			
//			}
		
		
	}

}
