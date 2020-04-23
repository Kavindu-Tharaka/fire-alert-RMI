package form_windows;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
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

public class EditSensorForm extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private boolean res;
	private JTextField txtFloorNo;
	private JTextField txtRoomNo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EditSensorForm dialog = new EditSensorForm(null, null, null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EditSensorForm(String lblfloornumber, String lblroomnumber, String lblsensorid, JFrame frame) {
		setBounds(350, 300, 377, 233);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Floor No.");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
			lblNewLabel.setBounds(41, 36, 100, 32);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Room No.");
			lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
			lblNewLabel_1.setBounds(41, 98, 100, 32);
			contentPanel.add(lblNewLabel_1);
		}
		{
			txtFloorNo = new JTextField();
			txtFloorNo.setBounds(158, 43, 176, 27);
			contentPanel.add(txtFloorNo);
			txtFloorNo.setColumns(10);
		}
		{
			txtRoomNo = new JTextField();
			txtRoomNo.setBounds(158, 101, 176, 27);
			contentPanel.add(txtRoomNo);
			txtRoomNo.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				txtFloorNo.setText(lblfloornumber);
				txtRoomNo.setText(lblroomnumber);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						RMIService service;

						try {
							//find the service
							service = (RMIService) Naming.lookup("rmi://localhost:5099/AirSensorService");

							try {
								//invoke remote method and assign to a variable
								res = service.editSensor(lblsensorid, Integer.parseInt(txtFloorNo.getText()),
										txtRoomNo.getText());

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
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
