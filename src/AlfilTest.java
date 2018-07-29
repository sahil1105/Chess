import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * AlfilTest -- Tests the functionality of the Alfil class, mainly that it lists its
 * possibleMoves correctly.
 * @author sahil1105
 */
public class AlfilTest extends TestCase {

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

        //replace some pawns with some alfils
        gameBoard.board[11] = new Alfil(new int[] {3,1}, true);
        gameBoard.board[13] = new Alfil(new int[] {5,1}, true);
        gameBoard.board[49] = new Alfil(new int[] {1,6}, false);
        gameBoard.board[54] = new Alfil(new int[] {6,6}, false);
        String temp = gameBoard.toString();
        List<int[]> possibleMoves = gameBoard.board[11].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 2); //ensure there are 2 possible moves
        assertEquals(Arrays.equals(possibleMoves.get(0), new int[] {5,3}), true);
        assertEquals(Arrays.equals(possibleMoves.get(1), new int[] {1,3}), true);
        possibleMoves = gameBoard.board[54].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 1); //ensure there is just 1 possible move
        assertEquals(Arrays.equals(possibleMoves.get(0), new int[] {4,4}), true);
        gameBoard.move(54, 36, !player1);
        possibleMoves = gameBoard.board[36].getPossibleMoves(gameBoard);
        //ensure there are 3 possible move, makes sure it doesn't attack friendlies
        assertEquals(possibleMoves.size(), 3);

        //empty board
        gameBoard = new ChessBoard8x8();
        gameBoard.board[35] = new Alfil(new int[] {3,4}, true);
        possibleMoves = gameBoard.board[35].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 4); //ensure there are 4 possible move
        gameBoard.board[29] = new Alfil(new int[] {5,3}, false);
        possibleMoves = gameBoard.board[29].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 4); //ensure there are 4 possible move
        //check the correctness of the 4 listed possible moves
        assertEquals(Arrays.equals(possibleMoves.get(0), new int[] {7,5}), true);
        assertEquals(Arrays.equals(possibleMoves.get(1), new int[] {7,1}), true);
        assertEquals(Arrays.equals(possibleMoves.get(2), new int[] {3,5}), true);
        assertEquals(Arrays.equals(possibleMoves.get(3), new int[] {3,1}), true);

    }
}