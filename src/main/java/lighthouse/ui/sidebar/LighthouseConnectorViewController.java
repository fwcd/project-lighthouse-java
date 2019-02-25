package lighthouse.ui.sidebar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lighthouse.ui.board.BoardViewController;
import lighthouse.ui.board.view.RemoteLighthouseGridView;
import lighthouse.util.ConfigFile;
import lighthouse.util.ResourceConfigFile;

/**
 * Manages a view containing the Lighthouse
 * connection controls.
 */
public class LighthouseConnectorViewController {
	private static final Logger LOG = LoggerFactory.getLogger(LighthouseConnectorViewController.class);
	private final JComponent component;
	private final BoardViewController grid; // The aliased grid
	private boolean connected = false;
	
	public LighthouseConnectorViewController(BoardViewController grid) {
		this.grid = grid;
		component = new JPanel();
		component.setLayout(new BoxLayout(component, BoxLayout.Y_AXIS));
		
		JTextField usernameField = new JTextField();
		component.add(labelled("Username", usernameField));
		
		JTextField tokenField = new JTextField();
		component.add(labelled("Token", tokenField));
		
		JButton connectButton = new JButton("Connect");
		connectButton.addActionListener(l -> connect(usernameField.getText(), tokenField.getText()));
		component.add(connectButton);
		
		// Creates a remote Lighthouse view if the required login information is present
		ConfigFile auth = new ResourceConfigFile("/authentication.txt");
		if (auth.has("username") && auth.has("token")) {
			usernameField.setText(auth.get("username"));
			tokenField.setText(auth.get("token"));
		} else {
			LOG.warn("Warning: Authentication did not contain 'username' and/or 'token'");
		}
	}
	
	private void connect(String username, String token) {
		if (connected) {
			JOptionPane.showMessageDialog(component, "Already connected!");
		} else {
			RemoteLighthouseGridView remoteView = new RemoteLighthouseGridView(username, token);
			remoteView.connect();
			grid.addView(remoteView);
			connected = true;
		}
	}
	
	private JPanel labelled(String label, JComponent component) {
		JPanel box = new JPanel();
		box.setLayout(new BoxLayout(box, BoxLayout.X_AXIS));
		box.add(new JLabel(label));
		box.add(component);
		return box;
	}
	
	public JComponent getComponent() {
		return component;
	}
}