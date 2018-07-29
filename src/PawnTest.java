import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * PawnTest -- Tests the functionality of the Pawn class, mainly that it lists its
 * possibleMoves correctly.
 * @author sahil1105
 */
public class PawnTest extends TestCase {

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

        List<int[]> possibleMoves = gameBoard.board[10].getPossibleMoves(gameBoard);
        //should have 2 possible moves at the start, also confirms that it can't attack its own pieces
        assertEquals(possibleMoves.size(), 2);
        //make sure that the right moves are enumerated
        assertEquals(Arrays.equals(possibleMoves.get(0), new int[] {2,2}), true);
        assertEquals(Arrays.equals(possibleMoves.get(1), new int[] {2,3}), true);

        //check moves of player 2's pawn since it must only move one way
        possibleMoves = gameBoard.board[52].getPossibleMoves(gameBoard);
        //should have 2 possible moves at the start, also confirms that it can't attack its own pieces
        assertEquals(possibleMoves.size(), 2);
        //make sure that the right moves are enumerated
        assertEquals(Arrays.equals(possibleMoves.get(0), new int[] {4,5}), true);
        assertEquals(Arrays.equals(possibleMoves.get(1), new int[] {4,4}), true);

        //move some pawns around to enable a capture opportunity
        gameBoard.move(11, 27, true);
        possibleMoves = gameBoard.board[27].getPossibleMoves(gameBoard);
        //should have just 1 possible moves, also checks that 2 step moves are only allowed at start
        assertEquals(possibleMoves.size(), 1);
        gameBoard.move(52, 36, false);
        possibleMoves = gameBoard.board[27].getPossibleMoves(gameBoard);
        //should have 2 possible moves now, including taking out the opponent's pawn
        assertEquals(possibleMoves.size(), 2);
        assertEquals(Arrays.equals(possibleMoves.get(1), new int[] {4,4}), true);

        //player 2's pawn should also be able to take out the other pawn
        possibleMoves = gameBoard.board[36].getPossibleMoves(gameBoard);
        //should have 2 possible moves now, including taking out the opponent's pawn
        assertEquals(possibleMoves.size(), 2);
        assertEquals(Arrays.equals(possibleMoves.get(1), new int[] {3,3}), true);

        gameBoard.move(13, 29, true); //another target for player 2's pawn
        //player 2's pawn should also be able to take out the other pawns
        possibleMoves = gameBoard.board[36].getPossibleMoves(gameBoard);
        //should have 3 possible moves now, including taking out the opponent's pawns
        assertEquals(possibleMoves.size(), 3);
        //make sure opponent pawns can be captured by this pawn
        assertEquals(Arrays.equals(possibleMoves.get(1), new int[] {3,3}), true);
        assertEquals(Arrays.equals(possibleMoves.get(2), new int[] {5,3}), true);


    }
}