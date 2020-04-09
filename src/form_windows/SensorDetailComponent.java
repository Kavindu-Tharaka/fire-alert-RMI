package form_windows;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class SensorDetailComponent extends JPanel {

	private String sensorId;
	private String floorNumber;
	private String roomNumber;
	private boolean status;
	private int co2Level;
	private int smokeLevel;
	
//	public SensorDetailComponent(String floorNumber, String roomNumber, String status, int co2level, int smokelevel) {
//		super();
//		this.floorNumber = floorNumber;
//		this.roomNumber = roomNumber;
//		this.status = status;
//		this.co2level = co2level;
//		this.smokelevel = smokelevel;
//	}
	
	public SensorDetailComponent(String sensorId, String floorNumber, String roomNumber, boolean status, int co2level, int smokelevel) {
		setLayout(null);
		//setBounds(10, 10, 520, 107);
		
		this.sensorId = sensorId;
		this.floorNumber = floorNumber;
		this.roomNumber = roomNumber;
		this.status = status;
		this.co2Level = co2level;
		this.smokeLevel = smokelevel;
		
		JPanel panel = new JPanel();
		panel.setBackground(this.co2Level > 5 ? new Color(210,0,0) : new Color(0, 204, 0));
		panel.setBounds(0, 0, 88, 100);
		add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_4 = new JLabel("Floor");
		lblNewLabel_4.setForeground(new Color(255, 255, 255));
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_4.setBounds(29, 31, 45, 13);
		panel.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("Room");
		lblNewLabel_5.setForeground(new Color(255, 255, 255));
		lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_5.setBounds(29, 76, 45, 13);
		panel.add(lblNewLabel_5);
		
		JLabel lblfloornumber = new JLabel(this.floorNumber);
		lblfloornumber.setForeground(new Color(255, 255, 255));
		lblfloornumber.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblfloornumber.setBounds(19, 0, 72, 34);
		panel.add(lblfloornumber);
		
		JLabel lblroomnumber = new JLabel(this.roomNumber);
		lblroomnumber.setForeground(new Color(255, 255, 255));
		lblroomnumber.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblroomnumber.setBounds(19, 44, 81, 35);
		panel.add(lblroomnumber);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		panel_1.setBounds(87, 0, 433, 100);
		add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblsensorstatus = new JLabel(this.status ? "Activated" : "Deactivated");
		lblsensorstatus.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblsensorstatus.setBounds(10, 10, 93, 13);
		panel_1.add(lblsensorstatus);
		
		JLabel lblNewLabel_2 = new JLabel("CO2");
		lblNewLabel_2.setForeground(Color.GRAY);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(101, 77, 45, 13);
		panel_1.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Smoke");
		lblNewLabel_3.setForeground(Color.GRAY);
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(263, 77, 45, 13);
		panel_1.add(lblNewLabel_3);
		
		JLabel lblco2level = new JLabel(""+this.co2Level);
		lblco2level.setForeground(new Color(0, 102, 204));
		lblco2level.setFont(new Font("Tahoma", Font.PLAIN, 45));
		lblco2level.setBounds(101, 19, 73, 62);
		panel_1.add(lblco2level);
		
		JLabel lblsmokelevele = new JLabel(""+this.smokeLevel);
		lblsmokelevele.setForeground(new Color(0, 102, 204));
		lblsmokelevele.setFont(new Font("Tahoma", Font.PLAIN, 45));
		lblsmokelevele.setBounds(263, 19, 73, 62);
		panel_1.add(lblsmokelevele);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setIcon(new ImageIcon(SensorDetailComponent.class.getResource("/img/edit.png")));
		btnNewButton.setBounds(398, 10, 25, 25);
		btnNewButton.setBorderPainted(false);
		panel_1.add(btnNewButton);
		
		JLabel lblsensorid = new JLabel(this.sensorId);
		lblsensorid.setBounds(156, 144, 45, 13);
		lblsensorid.setVisible(false);
		add(lblsensorid);
	}
	

	public String getFloorNumber() {
		return floorNumber;
	}


	public void setFloorNumber(String floorNumber) {
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
