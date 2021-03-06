package lighthouse.snake.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import lighthouse.snake.model.SnakeGameState;
import lighthouse.ui.scene.viewmodel.graphics.SceneLayer;
import lighthouse.ui.scene.viewmodel.graphics.SceneRect;
import lighthouse.ui.scene.viewmodel.graphics.SceneShape;
import lighthouse.ui.scene.viewmodel.graphics.Shading;
import lighthouse.util.DoubleVec;
import lighthouse.util.IntVec;

public class SnakeSceneLayer implements SceneLayer {
    private static final Color LOSE_COLOR = new Color(0x700013); // dark red
	private final SnakeGameState gameState;
	private final Color snakeColor = Color.GREEN;
	private final Color foodColor = Color.RED;
	
	public SnakeSceneLayer(SnakeGameState gameState) {
		this.gameState = gameState;
	}

	@Override
	public boolean hasBackground() {
		return gameState.hasLost();
	}

	@Override
	public Color getBackground() {
		return gameState.hasLost() ? LOSE_COLOR : Color.BLACK;
	}
	
	@Override
	public List<SceneShape> getShapes() {
		List<SceneShape> shapes = new ArrayList<>();
	
		for (IntVec snakePos : gameState.getSnake().getBody()) {
			shapes.add(new SceneRect(snakePos.toDouble(), DoubleVec.ONE_ONE, snakeColor, Shading.FILLED));
		}
		
		for (IntVec foodPos : gameState.getFoods()) {
			shapes.add(new SceneRect(foodPos.toDouble(), DoubleVec.ONE_ONE, foodColor, Shading.FILLED));
		}
		
		return shapes;
	}
}
