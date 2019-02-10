package lighthouse.model;

import java.awt.Color;

/**
 * The (entire) abstract lighthouse grid in all its glory.
 */
public class Grid {
	private final int width;
	private final int height;
	/** The 4x6 game board. */
	private final GameBoard board = new GameBoard(4, 6);
	
	// TODO: Listeners
	
	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Color getCell(int x, int y) {
		// TODO: Currently a sample color is outputted instead of the
		//       actual grid content. Check whether coordinate lies
		//       in the board's bounds and return the appropriate board
		//       cells if so.
		return new Color((x * 255) / width, (y * 255) / height, (x * y * 255) / (width * height));
	}
}
