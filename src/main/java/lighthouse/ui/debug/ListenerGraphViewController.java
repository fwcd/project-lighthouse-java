package lighthouse.ui.debug;

import java.awt.BorderLayout;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lighthouse.model.AppModel;
import lighthouse.ui.GameViewController;
import lighthouse.ui.ViewController;
import lighthouse.ui.util.LayoutUtils;
import lighthouse.util.ListenerList;

/**
 * Manages a visual representation of the listener graph.
 */
public class ListenerGraphViewController implements ViewController {
	private static final Logger LOG = LoggerFactory.getLogger(ListenerGraphViewController.class);
	private final AppModel appModel;
	private final GameViewController gameVC;
	
	private final JComponent component;
	private final ListenerGraphView view;
	private List<ListenerGraph> models;
	
	public ListenerGraphViewController(AppModel appModel, GameViewController gameVC) {
		this.appModel = appModel;
		this.gameVC = gameVC;
		
		component = new JPanel(new BorderLayout());
		component.add(LayoutUtils.buttonOf("Update", this::update), BorderLayout.NORTH);
		
		view = new ListenerGraphView();
		component.add(view.getComponent(), BorderLayout.CENTER);
	}
	
	private void update() {
		models = buildGraphs();
		view.draw(models);
	}
	
	public List<ListenerGraph> buildGraphs() {
		Set<ListenerList<?>> roots = new HashSet<>();
		Set<Field> visited = new HashSet<>();
		findListenerListsIn(appModel.getGameState(), visited, roots);
		findListenerListsIn(gameVC, visited, roots);
		return roots.stream()
			.map(ListenerGraph::new)
			.collect(Collectors.toList());
	}
	
	private void findListenerListsIn(Object obj, Set<Field> visited, Set<ListenerList<?>> set) {
		if (obj != null) {
			Class<? extends Object> clazz = obj.getClass();
			if (clazz.getName().startsWith("lighthouse")) {
				findFieldsIn(clazz)
					.forEach(field -> {
						if (!visited.contains(field)) {
							visited.add(field);
						
							if (!field.isAccessible()) {
								field.setAccessible(true);
							}
							try {
								Object fieldObj = field.get(obj);
								if (ListenerList.class.isAssignableFrom(field.getType())) {
									set.add((ListenerList<?>) fieldObj);
								}
								findListenerListsIn(fieldObj, visited, set);
							} catch (IllegalArgumentException | IllegalAccessException e) {
								LOG.error("Error while accessing listener list field:", e);
							}
						}
					});
			}
		}
	}
	
	private Stream<Field> findFieldsIn(Class<?> objClass) {
		Stream.Builder<Field> stream = Stream.builder();
		findFieldsIn(objClass, stream);
		return stream.build();
	}
	
	private void findFieldsIn(Class<?> objClass, Stream.Builder<Field> stream) {
		for (Field field : objClass.getDeclaredFields()) {
			stream.accept(field);
		}
		Class<?> superClass = objClass.getSuperclass();
		if (superClass != null) {
			findFieldsIn(superClass);
		}
	}
	
	@Override
	public JComponent getComponent() { return component; }
}
