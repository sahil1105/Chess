import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * QueenTest -- Tests the functionality of the Queen class, mainly that it lists its
 * possibleMoves correctly.
 * @author sahil1105
 */
public class QueenTest extends TestCase {

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

        List<int[]> possibleMoves = gameBoard.board[3].getPossibleMoves(gameBoard);
        //should have no possible moves at the start, also confirms that it can't attack its own pieces
        // and can't skip over pieces
        assertEquals(possibleMoves.size(), 0);
        gameBoard.move(51, 35, false); //clear some space in front of the queen
        possibleMoves = gameBoard.board[59].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 2); //should have 2 possible moves
        gameBoard.move(52, 44, false);
        possibleMoves = gameBoard.board[59].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 6); //should have 6 possible moves
        gameBoard.move(59, 45, false);
        possibleMoves = gameBoard.board[45].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 14); //should have 14 possible moves
        // check at random that the right moves are enumerated
        assertEquals(Arrays.equals(possibleMoves.get(3), new int[] {3,7}), true);
        gameBoard.move(13, 29, true); //restrict the queen's movement
        possibleMoves = gameBoard.board[45].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 12); //should have 12 possible moves now
        gameBoard.move(45, 31, false); //put the opposition king in check
        possibleMoves = gameBoard.board[31].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 13); //should have 13 possible moves now
        // check that the king can in fact be attacked
        assertEquals(Arrays.equals(possibleMoves.get(6), new int[] {4,0}), true);
        //check that all other possible moves are right
        assertEquals(Arrays.equals(possibleMoves.get(0), new int[] {6,4}), true);
        assertEquals(Arrays.equals(possibleMoves.get(1), new int[] {5,5}), true);
        assertEquals(Arrays.equals(possibleMoves.get(2), new int[] {4,6}), true);
        assertEquals(Arrays.equals(possibleMoves.get(3), new int[] {3,7}), true);
        assertEquals(Arrays.equals(possibleMoves.get(4), new int[] {6,2}), true);
        assertEquals(Arrays.equals(possibleMoves.get(5), new int[] {5,1}), true);
        assertEquals(Arrays.equals(possibleMoves.get(7), new int[] {6,3}), true);
        assertEquals(Arrays.equals(possibleMoves.get(8), new int[] {5,3}), true);
        assertEquals(Arrays.equals(possibleMoves.get(9), new int[] {7,4}), true);
        assertEquals(Arrays.equals(possibleMoves.get(10), new int[] {7,5}), true);
        assertEquals(Arrays.equals(possibleMoves.get(11), new int[] {7,2}), true);
        assertEquals(Arrays.equals(possibleMoves.get(12), new int[] {7,1}), true);


    }
}