package lighthouse.ui.scene.viewmodel.graphics;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import lighthouse.util.ColorUtils;
import lighthouse.util.DoubleVec;

public class DemoAnimation implements Animation {
	@Override
	public String getName() { return "Demo animation"; }
	
	@Override
	public int getTotalFrames() { return 300; }
	
	@Override
	public List<SceneShape> getShape(int frame) {
		return Arrays.asList(
			new SceneRect(1, frame / 40.0, 1, 1, Color.CYAN, Shading.FILLED),
			new SceneOval(new DoubleVec(2, 3), frame / 40.0, frame / 80.0, ColorUtils.withAlpha(((300 - frame) * 255) / 300, ColorUtils.LIGHT_VIOLET), Shading.FILLED),
			new SceneOval(new DoubleVec(2, 2), frame / 20.0, frame / 25.0, Color.YELLOW, Shading.OUTLINED)
		);
	}
}
