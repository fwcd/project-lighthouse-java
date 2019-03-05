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

import lighthouse.ui.ViewController;
import lighthouse.ui.board.BoardViewController;
import lighthouse.ui.board.input.BoardLighthouseInput;
import lighthouse.ui.board.view.RemoteLighthouseView;
import lighthouse.util.ConfigFile;
import lighthouse.util.ResourceConfigFile;

/**
 * Manages a view containing the Lighthouse
 * connection controls.
 */
public class LighthouseConnectorViewController implements ViewController {
	private static final Logger LOG = LoggerFactory.getLogger(LighthouseConnectorViewController.class);
	private final JComponent component;
	private final BoardViewController board; // The aliased grid
	private boolean connected = false;
	
	public LighthouseConnectorViewController(BoardViewController board) {
		this.board = board;
		component = new JPanel();
		component.setLayout(new BoxLayout(component, BoxLayout.Y_AXIS));
		
		JTextField usernameField = new JTextField();
		usernameField.setColumns(15);
		component.add(labelled("Username", usernameField));
		
		JTextField tokenField = new JTextField();
		tokenField.setColumns(15);
		component.add(labelled("Token", tokenField));
		
		JButton connectButton = new JButton("Connect");
		connectButton.addActionListener(l -> connect(usernameField.getText(), tokenField.getText()));
		component.add(connectButton);
		
		// Initialize login fields with config file data
		ConfigFile auth = new ResourceConfigFile("/authentication.txt");
		if (auth.has("username") && auth.has("token")) {
			usernameField.setText(auth.get("username"));
			tokenField.setText(auth.get("token"));
		} else {
			LOG.warn("Authentication did not contain 'username' and/or 'token'");
		}
	}
	
	private void connect(String username, String token) {
		if (connected) {
			JOptionPane.showMessageDialog(component, "Already connected!");
		} else {
			RemoteLighthouseView remoteView = new RemoteLighthouseView(username, token);
			BoardLighthouseInput lhInput = new BoardLighthouseInput();
			
			lhInput.addResponder(board.getResponder());
			board.addLighthouseView(remoteView);
			
			remoteView.addButtonInput(lhInput);
			remoteView.addConnectListener(v -> board.render());
			remoteView.connect();
			
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
	
	@Override
	public JComponent getComponent() {
		return component;
	}
}