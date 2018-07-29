import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * KingTest -- Tests the functionality of the King class, mainly that it lists its
 * possibleMoves correctly.
 * @author sahil1105
 */
public class KingTest extends TestCase {

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

        List<int[]> possibleMoves = gameBoard.board[4].getPossibleMoves(gameBoard);
        //should have no possible moves at the start, also confirms that it can't attack its own pieces
        assertEquals(possibleMoves.size(), 0);

        //System.out.println(gameBoard);
        gameBoard = new ChessBoard8x8();
        gameBoard.board[30] = new King(new int[] {6,3}, true); //a board with just a king
        possibleMoves = gameBoard.board[30].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 8); //should have 8 possible moves now
        //make sure that the right moves are enumerated, checking one at random
        assertEquals(Arrays.equals(possibleMoves.get(0), new int[] {7,3}), true);

        gameBoard.board[38] = new Pawn(new int[] {6,4}, true); //lets put a pawn to restrict the kings movement
        possibleMoves = gameBoard.board[30].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 7); //only 7 possible moves now

        gameBoard.board[39] = new Pawn(new int[] {7,4}, false); //lets put a pawn for the king to capture
        possibleMoves = gameBoard.board[30].getPossibleMoves(gameBoard);
        //make sure the king can capture the pawn
        assertEquals(Arrays.equals(possibleMoves.get(1), new int[] {7,4}), true);

    }
}