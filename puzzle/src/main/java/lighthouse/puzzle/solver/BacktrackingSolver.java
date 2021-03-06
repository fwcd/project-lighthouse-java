package lighthouse.puzzle.solver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lighthouse.puzzle.model.Board;
import lighthouse.puzzle.model.Level;

/**
 * A simple solver that backtracks once it encounters
 * an already visited state.
 */
public class BacktrackingSolver implements Solver {
    private static final Logger LOG = LoggerFactory.getLogger(BacktrackingSolver.class);
    
    @Override
    public List<Board> solve(Level toSolve) {
        LOG.info("Starting solver");
        
        Deque<Board> moves = new ArrayDeque<>();
        Board goal = toSolve.getGoal();
        Set<Board> visited = new HashSet<>();
        
        moves.addLast(toSolve.getStart());
        while (!moves.isEmpty() && !moves.peekLast().equals(goal)) {
            // Stop the solver if the thread was interrupted
            if (Thread.interrupted()) {
                LOG.info("Stopped solver");
                return Collections.emptyList();
            }
            
            visited.add(moves.peekLast());
            Board next = moves.peekLast()
                .streamChildBoards()
                .filter(c -> !visited.contains(c) && toSolve.isAllowed(c))
                .findAny()
                .orElse(null);
            
            if (next == null) {
                moves.removeLast();
            } else {
                moves.addLast(next);
            }
        }
        
        List<Board> finalMoves = new ArrayList<>();
        LOG.info("Starting optimization with: {} moves", moves.size());

        while (!moves.isEmpty()) {
            // Stop the solver if the thread was interrupted
            if (Thread.interrupted()) {
                LOG.info("Stopped solver while optimizing");
                return Collections.emptyList();
            }
            
            Board board = moves.removeFirst();
            if (!moves.isEmpty()) {
                boolean reachable = board.hasChildBoard(moves.peekFirst());
                if (!reachable) {
                    LOG.warn("Illegal move sequence generated by solver in the first step");
                }
            }
            finalMoves.add(board);

            Iterator<Board> iter = moves.descendingIterator();
            boolean canSkip = false;

            while (iter.hasNext()) {
                Board futureBoard = iter.next();
                if (canSkip) {
                    iter.remove();
                } else {
                    if (board.hasChildBoard(futureBoard)) {
                        LOG.trace("Skipping");
                        canSkip = true;
                    }
                }
            }
        }
        
        LOG.info("Finished optimization with {} moves", finalMoves.size());
        LOG.info("Done");
        return finalMoves;
    }
}
