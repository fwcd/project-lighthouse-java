package lighthouse.ui.board.viewmodel.overlay;

public abstract class LoopableAnimation implements Animation {
	private int loopCount = 1;
	
	public void setLoopCount(int loopCount) { this.loopCount = loopCount; }
	
	@Override
	public int getLoopCount() { return loopCount; }
}