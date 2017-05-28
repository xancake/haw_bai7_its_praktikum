package org.haw.its.praktikum4.vorgabe;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Zeige ein Fenster mit einem Passworteingabe-Feld als modalen Dialog
 */
public class PasswordDialog extends JDialog {
	private static final long serialVersionUID = 873795915516658285L;

	private JPasswordField passwortField;
	private boolean status;

	/**
	 * Liefere den Status
	 * @return OK = true, cancel = false
	 */
	public boolean statusOK() {
		return status;
	}

	/**
	 * Liefere das Passwort (Array-Referenz)
	 */
	public char[] getPassword() {
		return passwortField.getPassword();
	}

	public PasswordDialog(String userName) {
		super((Frame)null, "HAW Department Informatik WP IT-Sicherheit", true);
		
		JLabel labelMessage1 = new JLabel("<html><body><font size=\"+1\">"
				+ "<em>Bitte Passwort für " + userName + " eingeben: </em>"
				+ "</font><br></body></html>",
				SwingConstants.CENTER);

		passwortField = new JPasswordField(15);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status = true;
				closeDialog();
			}
		});

		JButton cancelButton = new JButton("Abbruch");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status = false;
				closeDialog();
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(new BorderLayout(100, 20));
		contentPanel.add(labelMessage1, BorderLayout.NORTH);
		contentPanel.add(passwortField, BorderLayout.CENTER);
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);
		setContentPane(contentPanel);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void closeDialog() {
		setVisible(false);
		dispose();
	}

	public static void main(String argv[]) throws Exception {
		// Testmethode!
		PasswordDialog myClient = new PasswordDialog("Testuser");
		System.out.println("Status: " + myClient.statusOK());
		System.out.println("Passwort: " + new String(myClient.getPassword()));
	}
}
