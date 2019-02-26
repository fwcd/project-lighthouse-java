package lighthouse.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lighthouse.util.ListenerList;

/**
 * Holds the current game state which includes the actively manipulated board,
 * the current level and more.
 */
public class GameState {
    private static final Logger LOG = LoggerFactory.getLogger(GameState.class);
	private static final Gson GSON = new Gson();
	
    private Board board = new Board();
    private Level level = new Level();
	private Board backupBoard;
	
    private final ListenerList<Level> levelListeners = new ListenerList<>();
	private final ListenerList<Board> boardListeners = new ListenerList<>();
    
    public void backupBoard() { backupBoard = board.copy(); }
    
    public void revertToBackupBoardOr(Supplier<Board> otherwise) {
        if (backupBoard == null) {
            setBoard(otherwise.get());
        } else {
            setBoard(backupBoard.copy());
        }
    }
    
    public void setBoard(Board board) {
        this.board = board;
        boardListeners.fire(board);
    }
	
	/** Saves a level as JSON to a file. */
	public void saveLevelTo(Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            LOG.info("Saving level to {}...", path);
            GSON.toJson(level, writer);
		}
	}
	
	/** Loads a level from a JSON file. */
	public void loadLevelFrom(Path path) throws IOException {
		try (BufferedReader reader = Files.newBufferedReader(path)) {
            LOG.info("Loading level from {}...", path);
            level = GSON.fromJson(reader, Level.class);
			levelListeners.fire(level);
		}
    }
	
	public Board getBoard() { return board; }
	
	public Level getLevel() { return level; }
	
	public ListenerList<Level> getLevelListeners() { return levelListeners; }
	
	public ListenerList<Board> getBoardListeners() { return boardListeners; }
}