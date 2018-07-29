import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * RookTest -- Tests the functionality of the Rook class, mainly that it lists its
 * possibleMoves correctly.
 * @author sahil1105
 */

public class RookTest extends TestCase {

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

        List<int[]> possibleMoves = gameBoard.board[0].getPossibleMoves(gameBoard);
        //should have no possible moves at the start, also confirms that it can't attack its
        // own pieces and can't skip over pieces
        assertEquals(possibleMoves.size(), 0);
        possibleMoves = gameBoard.board[7].getPossibleMoves(gameBoard);
        //should have no possible moves at the start, also confirms that it can't attack its
        // own pieces and can't skip over pieces
        assertEquals(possibleMoves.size(), 0);
        possibleMoves = gameBoard.board[56].getPossibleMoves(gameBoard);
        //should have no possible moves at the start, also confirms that it can't attack its
        // own pieces and can't skip over pieces
        assertEquals(possibleMoves.size(), 0);
        possibleMoves = gameBoard.board[63].getPossibleMoves(gameBoard);
        //should have no possible moves at the start, also confirms that it can't attack its
        // own pieces and can't skip over pieces
        assertEquals(possibleMoves.size(), 0);

        gameBoard.board[15] = null; //get rid of the pawn in front of one of player 1's rooks
        possibleMoves = gameBoard.board[7].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 6); //should have 6 possible moves now
        gameBoard.move(7, 31, true); //move the rook a bit
        gameBoard.move(31, 28, true); //move the rook to the middle
        possibleMoves = gameBoard.board[28].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 11); //should have 11 possible moves now

        gameBoard.move(28, 52, true); //put the king in check
        possibleMoves = gameBoard.board[52].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 7); //should have 7 possible moves now
        //should be able to attack the king
        assertEquals(Arrays.equals(possibleMoves.get(2), new int[] {4,7}), true);
        //check the other options including being able to capture other opponent pieces,
        // but not its own, and have full range of motion
        assertEquals(Arrays.equals(possibleMoves.get(2), new int[] {4,7}), true);
        assertEquals(Arrays.equals(possibleMoves.get(0), new int[] {5,6}), true);
        assertEquals(Arrays.equals(possibleMoves.get(1), new int[] {3,6}), true);
        assertEquals(Arrays.equals(possibleMoves.get(3), new int[] {4,5}), true);
        assertEquals(Arrays.equals(possibleMoves.get(4), new int[] {4,4}), true);
        assertEquals(Arrays.equals(possibleMoves.get(5), new int[] {4,3}), true);
        assertEquals(Arrays.equals(possibleMoves.get(6), new int[] {4,2}), true);

    }
}