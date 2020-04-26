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
	private static DashBoardFrm frame;
	private static boolean isAdmin = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new DashBoardFrm(isAdmin);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

				RMIService service;

				try {

					// find the service
					service = (RMIService) Naming.lookup("rmi://localhost:5099/AirSensorService");

					Timer t = new Timer(0, null);

					t.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							try {
								// call remote method and get the response to a variable
								responseBody = service.getAllSensorDetails();
							} catch (RemoteException e1) {
								e1.printStackTrace();
							}
							populateSensorComponents(responseBody);
						}
					});

					t.setRepeats(true);
					t.setDelay(5000); // repeat interval should be ==> t.setDelay(30000); 
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
		setBounds(210, 0, 720, 1045);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem = new JMenuItem("Register New Sensor");
		// add event listner to menu item
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				AddSensorForm addSensorForm = new AddSensorForm();
				addSensorForm.setVisible(true);
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		mntmNewMenuItem.setVisible(isAdminn);

		JSeparator separator = new JSeparator();
		mnNewMenu.add(separator);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Exit");
		// add event listner to menu item
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

		// add scroll bar
		JScrollPane scrollPane = new JScrollPane(contentPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setContentPane(scrollPane);

	}

	/**
	 * used to populate sensor details
	 */
	public static void populateSensorComponents(String responseBody) {

		contentPane.removeAll();

		JSONObject res = new JSONObject(responseBody);
		JSONObject data = res.getJSONObject("data");
		JSONArray sensors = data.getJSONArray("sensors");

		// loop through the response body and add each sensor's details to the user interface
		for (int i = 0; i < sensors.length(); i++) {

			JSONObject obj = sensors.getJSONObject(i);

			JSONObject lastReading = obj.getJSONObject("lastReading");

			int co2Level = lastReading.getInt("co2Level");
			int smokeLevel = lastReading.getInt("smokeLevel");
			String time = lastReading.getString("time");

			boolean activated = obj.getBoolean("activated");
			String _id = obj.getString("_id");
			int floor = obj.getInt("floor");
			String room = obj.getString("room");

			SensorDetailComponent sensorDetailComponent = new SensorDetailComponent(_id, floor, room, activated,
					co2Level, smokeLevel, isAdmin, frame);
			sensorDetailComponent.setVisible(true);
			contentPane.add(sensorDetailComponent);
		}

		contentPane.validate();
		contentPane.repaint();
	}

}
