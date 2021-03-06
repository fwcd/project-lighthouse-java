package lighthouse.ui.scene.controller;

import lighthouse.util.IntVec;

/**
 * An interface for responding to user events.
 * 
 * <p>Inputs are already processed to a more domain-
 * specific version since raw input events may look
 * differently across inputs (a mouse might use
 * exact mouse coordinates while an Xbox controller
 * would only receive offsets).</p>
 */
public interface SceneResponder {
	/** Presses at the specified position on the scene. Returns whether this was successful. */
	default boolean press(IntVec gridPos) { return false; }
	
	/** Right-presses at the specified position on the scene. Returns whether this was successful. */
	default boolean rightPress(IntVec gridPos) { return false; }
	
	/** Drags to the specified position on the scene. Returns whether this was successful. */
	default boolean dragTo(IntVec gridPos) { return false; }
	
	/** Releases at the specified position on the scene. Returns whether this was successful. */
	default boolean release(IntVec gridPos) { return false; }
	
	/** Creates a new selection on the scene. Returns the associated position if successful, otherwise returns null. */
	default IntVec selectAny() { return null; }

	/** Creates a selection at the specified position. Returns the position again if successful, otherwise returns null. */
	default IntVec select(IntVec gridPos) { return null; }
	
	/** Moves the selection up. Returns the next position if successful, otherwise null. */
	default IntVec up(IntVec gridPos) { return null; }
	
	/** Moves the selection to the left. Returns the next position if successful, otherwise null. */
	default IntVec left(IntVec gridPos) { return null; }
	
	/** Moves the selection down. Returns the next position if successful, otherwise null. */
	default IntVec down(IntVec gridPos) { return null; }
	
	/** Moves the selection to the right. Returns the next position if successful, otherwise null. */
	default IntVec right(IntVec gridPos) { return null; }
	
	/** Removes the selection. Returns whether this was successful. */
	default boolean deselect() { return false; }
	
	/** "Resets" the scene in some sense. Returns whether this was successful. */
	default boolean reset() { return false; }
	
	/** "Fires" a projectile etc. Returns whether this was successful. */
	default boolean fire() { return false; }
}
