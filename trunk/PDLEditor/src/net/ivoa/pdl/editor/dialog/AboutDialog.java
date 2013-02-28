package net.ivoa.pdl.editor.dialog;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import net.ivoa.pdl.editor.PDLEditorApp;


public class AboutDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AboutDialog dialog = new AboutDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AboutDialog() {
		setBounds(100, 100, 300, 241);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblPdlEditor = new JLabel(PDLEditorApp.getAppName());
		lblPdlEditor.setBounds(16, 19, 113, 16);
		contentPanel.add(lblPdlEditor);
		
		JLabel lblVersion = new JLabel("Version: "+PDLEditorApp.getAppVersion());
		lblVersion.setBounds(16, 47, 90, 16);
		contentPanel.add(lblVersion);
		
		String[] authors = PDLEditorApp.getAppAuthors();
		for(int na=0;na<authors.length;na++) {
		    JLabel lblAuthor = new JLabel(authors[na]);
			lblAuthor.setBounds(16, 75+na*16, 271, 16);
			contentPanel.add(lblAuthor);
		}
		
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose(); // close the window
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}
