package form_windows;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import rmi_server_codes.RMIService;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.Color;

public class AdminLoginForm extends JFrame {

	private JPanel contentPane;
	private JTextField txtemail;
	private JPasswordField txtpassword;
	private static AdminLoginForm frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new AdminLoginForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AdminLoginForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(350, 250, 382, 469);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Email");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel.setBounds(34, 159, 77, 16);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(34, 217, 85, 22);
		contentPane.add(lblNewLabel_1);

		JLabel lblerror = new JLabel("Invalid Credentials. Try Again.");
		lblerror.setForeground(Color.RED);
		lblerror.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblerror.setBounds(89, 382, 185, 22);
		contentPane.add(lblerror);
		lblerror.setVisible(false); // error msg will not be visible at the beginning

		txtemail = new JTextField();
		txtemail.setFont(new Font("Tahoma", Font.ITALIC, 17));
		txtemail.setBounds(121, 154, 205, 27);
		contentPane.add(txtemail);
		txtemail.setColumns(10);

		txtpassword = new JPasswordField();
		txtpassword.setFont(new Font("Tahoma", Font.ITALIC, 17));
		txtpassword.setBounds(121, 215, 205, 27);
		contentPane.add(txtpassword);
		txtpassword.setColumns(10);

		JButton btnsignin = new JButton("Sign In");
		btnsignin.setIcon(new ImageIcon(AdminLoginForm.class.getResource("/img/signin_.png")));
		btnsignin.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnsignin.addActionListener(new ActionListener() {
			// add event listner to the button
			public void actionPerformed(ActionEvent e) {

				String email = txtemail.getText();
				String password = txtpassword.getText();

				RMIService service;
				String result = null;
				try {
					// find the remote service
					service = (RMIService) Naming.lookup("rmi://localhost:5099/AirSensorService");
					// invoke the remote method
					result = service.loginValidator(email, password);

				} catch (MalformedURLException | RemoteException | NotBoundException ex) {
					ex.printStackTrace();
				}

				System.out.println(result); // write the result on the console

				if (result.equalsIgnoreCase("success")) {
					// if valid Admin credential, open the dashboard
					frame.dispose();
					DashBoardFrm dashboardForm = new DashBoardFrm(true);
					dashboardForm.main(null);
				} else {
					// if invalid Admin credential, display error msg
					lblerror.setVisible(true);
				}

			}
		});
		btnsignin.setBounds(100, 296, 154, 43);
		contentPane.add(btnsignin);

		JLabel lblNewLabel_2 = new JLabel("Admin Login");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblNewLabel_2.setIcon(new ImageIcon(AdminLoginForm.class.getResource("/img/admin.png")));
		lblNewLabel_2.setBounds(75, 55, 218, 48);
		contentPane.add(lblNewLabel_2);
	}
}
