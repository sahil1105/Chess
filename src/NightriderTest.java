import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * NightriderTest -- Tests the functionality of the Nightrider class, mainly that it lists its
 * possibleMoves correctly.
 * @author sahil1105
 */
public class NightriderTest extends TestCase {

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

        gameBoard.board[1] = new Nightrider(new int[] {1,0}, true);// replace a knight with a nightrider
        gameBoard.board[62] = new Nightrider(new int[] {6,7}, false);
        String temp = gameBoard.toString();
        List<int[]> possibleMoves = gameBoard.board[1].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 4); //ensure there are 4 possible moves
        //check that they are the correct 4 possible moves
        assertEquals(Arrays.equals(possibleMoves.get(0), new int[] {2,2}), true);
        assertEquals(Arrays.equals(possibleMoves.get(1), new int[] {3,4}), true);
        assertEquals(Arrays.equals(possibleMoves.get(2), new int[] {4,6}), true);
        assertEquals(Arrays.equals(possibleMoves.get(3), new int[] {0,2}), true);
        possibleMoves = gameBoard.board[62].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 4); //ensure there are 4 possible moves
        gameBoard.move(1,35, player1); //move the Nightrider
        possibleMoves = gameBoard.board[35].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 11); //ensure there are 11 possible moves after the move
        //empty board
        gameBoard = new ChessBoard8x8();
        gameBoard.board[27] = new Nightrider(new int[] {3,3}, player1);
        possibleMoves = gameBoard.board[27].getPossibleMoves(gameBoard);
        assertEquals(possibleMoves.size(), 12); //ensure there are 12 possible moves



    }
}