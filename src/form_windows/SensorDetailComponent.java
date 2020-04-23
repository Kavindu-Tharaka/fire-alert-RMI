package form_windows;

import javax.swing.JPanel;

import rmi_server_codes.RMIService;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class SensorDetailComponent extends JPanel {

	private String sensorId;
	private int floorNumber;
	private String roomNumber;
	private boolean status;
	private int co2Level;
	private int smokeLevel;
	private JLabel lblsensorid;
	private boolean isAdminn;
	private JFrame frame;

	public SensorDetailComponent(String sensorId, int floorNumber, String roomNumber, boolean status, int co2level,
			int smokelevel, boolean isAdminn, JFrame frame) {
		setLayout(null);
		this.sensorId = sensorId;
		this.floorNumber = floorNumber;
		this.roomNumber = roomNumber;
		this.status = status;
		this.co2Level = co2level;
		this.smokeLevel = smokelevel;
		this.isAdminn = isAdminn;
		this.frame = frame;

		JPanel panel = new JPanel();
		// set Red color if the smoke or CO2 level goes above level 5
		panel.setBackground(this.co2Level > 5 || this.smokeLevel > 5 ? new Color(210, 0, 0) : new Color(0, 204, 0));
		panel.setBounds(0, 0, 175, 174);
		add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel_4 = new JLabel("Floor");
		lblNewLabel_4.setForeground(new Color(255, 255, 255));
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_4.setBounds(57, 59, 73, 27);
		panel.add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("Room");
		lblNewLabel_5.setForeground(new Color(255, 255, 255));
		lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_5.setBounds(57, 125, 81, 38);
		panel.add(lblNewLabel_5);

		JLabel lblfloornumber = new JLabel("" + this.floorNumber);
		lblfloornumber.setForeground(new Color(255, 255, 255));
		lblfloornumber.setFont(new Font("Tahoma", Font.BOLD, 35));
		lblfloornumber.setBounds(63, 24, 38, 34);
		panel.add(lblfloornumber);

		JLabel lblroomnumber = new JLabel(this.roomNumber);
		lblroomnumber.setForeground(new Color(255, 255, 255));
		lblroomnumber.setFont(new Font("Tahoma", Font.BOLD, 35));
		lblroomnumber.setBounds(39, 97, 126, 35);
		panel.add(lblroomnumber);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		panel_1.setBounds(176, 0, 542, 174);
		add(panel_1);
		panel_1.setLayout(null);

		JLabel lblsensorstatus = new JLabel(this.status ? "Activated" : "Deactivated");
		lblsensorstatus.setForeground(this.status ? Color.black : Color.red);
		lblsensorstatus.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblsensorstatus.setBounds(10, 10, 163, 25);
		panel_1.add(lblsensorstatus);

		JLabel lblNewLabel_2 = new JLabel("Smoke");
		lblNewLabel_2.setForeground(Color.GRAY);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 40));
		lblNewLabel_2.setBounds(121, 121, 144, 35);
		panel_1.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("CO2");
		lblNewLabel_3.setForeground(Color.GRAY);
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 40));
		lblNewLabel_3.setBounds(318, 114, 119, 49);
		panel_1.add(lblNewLabel_3);

		JLabel lblco2level = new JLabel("" + this.smokeLevel);
		lblco2level.setForeground(new Color(0, 102, 204));
		lblco2level.setFont(new Font("Tahoma", Font.PLAIN, 65));
		lblco2level.setBounds(166, 46, 73, 62);
		panel_1.add(lblco2level);

		JLabel lblsmokelevele = new JLabel("" + this.co2Level);
		lblsmokelevele.setForeground(new Color(0, 102, 204));
		lblsmokelevele.setFont(new Font("Tahoma", Font.PLAIN, 65));
		lblsmokelevele.setBounds(336, 46, 73, 62);
		panel_1.add(lblsmokelevele);

		JButton btnNewButton = new JButton("");
		btnNewButton.setVisible(isAdminn);
		btnNewButton.addActionListener(new ActionListener() {
			// add event listner to the button
			public void actionPerformed(ActionEvent e) {
				EditSensorForm editSensorForm = new EditSensorForm(lblfloornumber.getText(), lblroomnumber.getText(),
						lblsensorid.getText(), frame);
				editSensorForm.setVisible(true);
			}
		});
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setIcon(new ImageIcon(SensorDetailComponent.class.getResource("/img/edit_.png")));
		btnNewButton.setBounds(446, 10, 25, 25);
		btnNewButton.setBorderPainted(false);
		panel_1.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.setVisible(isAdminn);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				RMIService service;
				boolean res = false;
				try {
					// find the remote service
					service = (RMIService) Naming.lookup("rmi://localhost:5099/AirSensorService");

					try {
						// call the reomte method and get response to a variable
						res = service.deleteSensor(lblsensorid.getText());

					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				} catch (MalformedURLException | RemoteException | NotBoundException ex) {
					ex.printStackTrace();
				}

				if (res) {
					frame.dispose();
					DashBoardFrm dashboardForm = new DashBoardFrm(true);
					dashboardForm.main(null);
				} else {
					System.out.println("Error");
				}
			}
		});
		btnNewButton_1.setBackground(Color.WHITE);
		btnNewButton_1.setIcon(new ImageIcon(SensorDetailComponent.class.getResource("/img/delete.png")));
		btnNewButton_1.setBounds(481, 10, 29, 25);
		btnNewButton_1.setBorderPainted(false);
		panel_1.add(btnNewButton_1);

		lblsensorid = new JLabel(this.sensorId);
		lblsensorid.setBounds(156, 144, 45, 13);
		lblsensorid.setVisible(false);
		add(lblsensorid);
	}

	public int getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(int floorNumber) {
		this.floorNumber = floorNumber;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getCo2level() {
		return co2Level;
	}

	public void setCo2level(int co2level) {
		this.co2Level = co2level;
	}

	public int getSmokelevel() {
		return smokeLevel;
	}

	public void setSmokelevel(int smokelevel) {
		this.smokeLevel = smokelevel;
	}
}
