package lighthouse.snake.model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import lighthouse.model.BaseGameState;
import lighthouse.util.IntVec;
import lighthouse.util.LighthouseConstants;

public class SnakeGameState extends BaseGameState {
	private Snake snake;
	private final Set<IntVec> foods = new HashSet<>();
	private final int boardWidth = LighthouseConstants.COLS;
	private final int boardHeight = LighthouseConstants.ROWS;
	private int score = 0;
	
	public SnakeGameState() {
		snake = new Snake(boardWidth, boardHeight);
		spawnFood();
	}
	
	public void spawnFood() {
		IntVec pos = randomPos();
		while (snake.contains(pos)) {
			pos = randomPos();
		}
		foods.add(pos);
	}

	public boolean hasLost() {
		return snake.isDead();
	}
	
	public void advance() {
		if (hasLost()) return;

		boolean ate = foods.contains(snake.nextHead());
		if (ate) {
			foods.remove(snake.nextHead());
			spawnFood();
			score++;
		}
		snake.move(!ate);
	}

	public void reset() {
		snake = new Snake(boardWidth, boardHeight);
		score = 0;
	}
	
	public int getScore() {
		return score;
	}
	
	private IntVec randomPos() {
		Random r = ThreadLocalRandom.current();
		return new IntVec(r.nextInt(boardWidth), r.nextInt(boardHeight));
	}
	
	public Set<IntVec> getFoods() {
		return foods;
	}
	
	public Snake getSnake() {
		return snake;
	}

	/** Returns the grid size in the game's own coordinate system. */
	@Override 
	public IntVec getGridSize() {
		return new IntVec(boardWidth, boardHeight);
	}
}
