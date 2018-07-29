import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * BishopTest -- Tests the functionality of the Bishop class, mainly that it lists its
 * possibleMoves correctly.
 * @author sahil1105
 */
public class BishopTest extends TestCase {

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

        List<int[]> possibleMoves = gameBoard.board[2].getPossibleMoves(gameBoard);
        //no possible moves at the start
        assertEquals(possibleMoves.size(), 0);

        //check that 2 possible moves open up when a pawn in front of it is moved
        gameBoard.move(9, 17, player1);
        possibleMoves = gameBoard.board[2].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 2);
        assertEquals(Arrays.equals(possibleMoves.get(0), new int[] {1,1}), true);
        assertEquals(Arrays.equals(possibleMoves.get(1), new int[] {0,2}), true);

        //make sure moving a piece next to it doesn't expand its moving possibilities
        gameBoard.move(57,42, !player1);
        possibleMoves = gameBoard.board[58].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 0);

        //make sure the correct possible moves are enumerated when a pawn diagonal of it moved out of the way
        gameBoard.move(12, 28, player1);
        possibleMoves = gameBoard.board[5].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 5);
        assertEquals(Arrays.equals(possibleMoves.get(0), new int[] {4,1}), true);
        assertEquals(Arrays.equals(possibleMoves.get(1), new int[] {3,2}), true);
        assertEquals(Arrays.equals(possibleMoves.get(2), new int[] {2,3}), true);
        assertEquals(Arrays.equals(possibleMoves.get(3), new int[] {1,4}), true);
        assertEquals(Arrays.equals(possibleMoves.get(4), new int[] {0,5}), true);

        //when a pawn in front of it moved, it must now have 5 possible moves
        gameBoard.move(52, 36, !player1);
        possibleMoves = gameBoard.board[61].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 5);

        //make sure it can capture opposition pieces
        gameBoard.move(8, 16, player1);
        possibleMoves = gameBoard.board[61].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 5);
        assertEquals(Arrays.equals(possibleMoves.get(4), new int[] {0,2}), true);

    }
}