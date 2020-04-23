package form_windows;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import rmi_server_codes.RMIService;

import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.Timer;
import java.awt.Color;

public class AddSensorForm extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtsensorid;
	private JTextField txtfloorno;
	private JTextField txtroomno;
	private boolean res;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AddSensorForm dialog = new AddSensorForm();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AddSensorForm() {
		setBounds(250, 300, 483, 258);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("ID (Serial No. on Sensor)");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(23, 33, 190, 28);
		contentPanel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Floor No.");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(23, 75, 127, 28);
		contentPanel.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Room No.");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(23, 124, 149, 20);
		contentPanel.add(lblNewLabel_2);

		txtsensorid = new JTextField();
		txtsensorid.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtsensorid.setBounds(230, 35, 216, 26);
		contentPanel.add(txtsensorid);
		txtsensorid.setColumns(10);

		txtfloorno = new JTextField();
		txtfloorno.setBounds(230, 78, 216, 28);
		contentPanel.add(txtfloorno);
		txtfloorno.setColumns(10);

		txtroomno = new JTextField();
		txtroomno.setBounds(230, 123, 216, 28);
		contentPanel.add(txtroomno);
		txtroomno.setColumns(10);

		JLabel lblerromsg = new JLabel("Same ID " + txtsensorid.getText() + " is already exist!");
		lblerromsg.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblerromsg.setForeground(Color.RED);
		lblerromsg.setBounds(153, 161, 204, 19);
		lblerromsg.setVisible(false);
		contentPanel.add(lblerromsg);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						String id = txtsensorid.getText();
						int floor = Integer.parseInt(txtfloorno.getText());
						String room = txtroomno.getText();

						RMIService service;

						try {
							//find the service
							service = (RMIService) Naming.lookup("rmi://localhost:5099/AirSensorService");

							try {
								//invoke remote method and assign to a variable
								res = service.addSensor(id, floor, room);

							} catch (RemoteException e1) {
								e1.printStackTrace();
							}
						} catch (MalformedURLException | RemoteException | NotBoundException ex) {
							ex.printStackTrace();
						}

						if (res) {

							DashBoardFrm dashboardForm = new DashBoardFrm(true);
							dashboardForm.main(null);
						} else {
							lblerromsg.setVisible(true);
						}

					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(EXIT_ON_CLOSE);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
