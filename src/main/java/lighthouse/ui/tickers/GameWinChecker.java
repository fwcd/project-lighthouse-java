package lighthouse.ui.tickers;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import lighthouse.model.GameState;
import lighthouse.ui.loop.Ticker;

public class GameWinChecker implements Ticker {
	private final JComponent parent;
	private final GameState gameState;
	
	public GameWinChecker(JComponent parent, GameState gameState) {
		this.parent = parent;
		this.gameState = gameState;
	}
	
	@Override
	public void tick() {
		if (gameState.isWon()) {
			JOptionPane.showMessageDialog(parent, "GAME WON! Hooray!");
		}
	}
}