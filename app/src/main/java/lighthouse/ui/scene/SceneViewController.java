package lighthouse.ui.scene;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lighthouse.ui.SwingViewController;
import lighthouse.ui.scene.controller.DelegateResponder;
import lighthouse.ui.scene.controller.NoResponder;
import lighthouse.ui.scene.controller.SceneResponder;
import lighthouse.ui.scene.view.LighthouseView;
import lighthouse.ui.scene.view.SceneView;
import lighthouse.ui.scene.viewmodel.LighthouseViewModel;
import lighthouse.ui.scene.viewmodel.graphics.SceneViewModel;
import lighthouse.util.transform.DoubleVecBijection;

/**
 * Manages a scene together with its views.
 */
public class SceneViewController implements SwingViewController {
	private static final Logger LOG = LoggerFactory.getLogger(SceneViewController.class);
	private final JComponent component;
	private final AnimationRunner animationRunner = new SceneAnimationRunner();
	
	private final SceneViewModel viewModel;
	private LighthouseViewModel lighthouseViewModel;
	
	private final List<SceneView> sceneViews = new ArrayList<>();
	private final List<LighthouseView> lighthouseViews = new ArrayList<>();
	private final DelegateResponder responder = new DelegateResponder(NoResponder.INSTANCE);
	
	public SceneViewController() {
		viewModel = new SceneViewModel();
		component = new JPanel();
	}
	
	public void render() {
		for (SceneView view : sceneViews) {
			view.draw(viewModel);
		}
		if (lighthouseViewModel == null) {
			LOG.warn("Could not render scene to lighthouse views without transformation functions");
		} else {
			for (LighthouseView view : lighthouseViews) {
				view.draw(lighthouseViewModel);
			}
		}
	}
	
	public void setLighthouseTransforms(DoubleVecBijection lighthouseSizeToGridSize, DoubleVecBijection lighthousePosToGridPos) {
		lighthouseViewModel = new LighthouseViewModel(viewModel, lighthouseSizeToGridSize, lighthousePosToGridPos);
	}
	
	public void addSceneView(SceneView view) {
		sceneViews.add(view);
	}
	
	public void addLighthouseView(LighthouseView view) {
		lighthouseViews.add(view);
	}
	
	public void setResponder(SceneResponder responder) { this.responder.setDelegate(responder); }
	
	public DelegateResponder getResponder() { return responder; }
	
	public AnimationRunner getAnimationRunner() { return animationRunner; }
	
	@Override
	public JComponent getComponent() { return component; }
}