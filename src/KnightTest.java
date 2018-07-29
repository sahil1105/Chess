import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * KnightTest -- Tests the functionality of the Knight class, mainly that it lists its
 * possibleMoves correctly.
 * @author sahil1105
 */
public class KnightTest extends TestCase {

    ChessBoard8x8 gameBoard; //Board to perform the tests on
    boolean player1; //boolean to keep track of turn

    /**
     * Initial setup for each of the tests. Initializes the game board and sets the turn to player1.
     * @throws Exception
     */
    public void setUp() throws Exception {
        super.setUp();
        gameBoard = new ChessBoard8x8();
        gameBoard.initBoard();
        player1 = true;
    }

    /**
     * Tests the getPossibleMoves function. Makes sure that the right possible moves are enumerated
     * and that they match the expectations. Does this by creating several situations and checking
     * that the correct possible moves are listed by the function.
     * Assumes that the move function works correctly.
     */
    public void testGetPossibleMoves() {

        List<int[]> possibleMoves = gameBoard.board[1].getPossibleMoves(gameBoard);
        //should have 2 possible moves at the start, also confirms that it can't attack its own pieces
        assertEquals(possibleMoves.size(), 2);
        assertEquals(Arrays.equals(possibleMoves.get(0), new int[] {2,2}), true);
        assertEquals(Arrays.equals(possibleMoves.get(1), new int[] {0,2}), true);
        gameBoard.move(1, 18, true);
        possibleMoves = gameBoard.board[18].getPossibleMoves(gameBoard);
        //should have 5 possible moves after this move, also confirms that it can't attack its own pieces
        assertEquals(possibleMoves.size(), 5);
        gameBoard.move(49, 33, false); //move a pawn which the knight can capture
        possibleMoves = gameBoard.board[18].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 5); //should still have 5 possible moves
        //should be able to capture the pawn, also confirms that it can't attack its own pieces
        assertEquals(Arrays.equals(possibleMoves.get(3), new int[] {1,4}), true);
        gameBoard.move(18, 35, true);
        gameBoard.move(35, 45, true);
        possibleMoves = gameBoard.board[45].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 8); //should have 8 possible moves
        //should be able to kill the opponent's king
        assertEquals(Arrays.equals(possibleMoves.get(5), new int[] {4,7}), true);

    }
}