package form_windows;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class WelcomeForm {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WelcomeForm window = new WelcomeForm();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WelcomeForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(350, 250, 382, 469);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Welcome!");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblNewLabel.setBounds(106, 21, 169, 48);
		frame.getContentPane().add(lblNewLabel);

		JButton btnNewButton = new JButton("Admin Login");
		btnNewButton.setIcon(new ImageIcon(WelcomeForm.class.getResource("/img/admin.png")));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 25));
		btnNewButton.addActionListener(new ActionListener() {
			// add event listner to the button
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				// open Admin login form
				AdminLoginForm adminLoginForm = new AdminLoginForm();
				adminLoginForm.setVisible(true);
			}
		});
		btnNewButton.setBounds(45, 248, 273, 55);
		frame.getContentPane().add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Guest Login");
		btnNewButton_1.setIcon(new ImageIcon(WelcomeForm.class.getResource("/img/user.png")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 25));
		btnNewButton_1.addActionListener(new ActionListener() {
			// add event listner to the button
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				// open Dashboard to guest user
				DashBoardFrm dashboardForm = new DashBoardFrm(false);
				dashboardForm.main(null);
			}
		});
		btnNewButton_1.setBounds(45, 321, 273, 55);
		frame.getContentPane().add(btnNewButton_1);

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setIcon(new ImageIcon(WelcomeForm.class.getResource("/img/logo.jpg")));
		lblNewLabel_1.setBounds(21, 80, 324, 113);
		frame.getContentPane().add(lblNewLabel_1);
	}
}
