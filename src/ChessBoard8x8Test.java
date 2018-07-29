import junit.framework.TestCase;

import java.util.Arrays;

/**
 * ChessBoard8x8Test -- Tests for the ChessBoard8x8 class.
 * Tests most of the game logic such as index to position conversion,
 * testing if a spot is occupied, if a spot is occupied by an opponent,
 * testing the movement mechanism for the piece, checking if 'checks' are detected,
 * testing if checkmate and stalemate is detected, testing if the board is initialized correctly, etc.
 * @author sahil1105
 */
public class ChessBoard8x8Test extends TestCase {

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
     * Check if the function boardIdxToPosition is working correctly.
     */
    public void testBoardIdxToPosition() {

        //make sure the invalid ones are caught
        assertEquals(gameBoard.boardIdxToPosition(-1), null);
        assertEquals(gameBoard.boardIdxToPosition(-10), null);
        assertEquals(gameBoard.boardIdxToPosition(64), null);
        assertEquals(gameBoard.boardIdxToPosition(100), null);

        //edge cases
        assertEquals(gameBoard.boardIdxToPosition(0)[0], 0);
        assertEquals(gameBoard.boardIdxToPosition(0)[1], 0);
        assertEquals(gameBoard.boardIdxToPosition(1)[0], 1);
        assertEquals(gameBoard.boardIdxToPosition(1)[1], 0);
        assertEquals(gameBoard.boardIdxToPosition(63)[0], 7);
        assertEquals(gameBoard.boardIdxToPosition(63)[1], 7);
        assertEquals(gameBoard.boardIdxToPosition(31)[0], 7);
        assertEquals(gameBoard.boardIdxToPosition(31)[1], 3);
        assertEquals(gameBoard.boardIdxToPosition(32)[0], 0);
        assertEquals(gameBoard.boardIdxToPosition(32)[1], 4);

        //other random tests
        assertEquals(gameBoard.boardIdxToPosition(26)[0], 2);
        assertEquals(gameBoard.boardIdxToPosition(26)[1], 3);
        assertEquals(gameBoard.boardIdxToPosition(33)[0], 1);
        assertEquals(gameBoard.boardIdxToPosition(33)[1], 4);

    }


    /**
     * Check if the isOccupied function is working correctly through exhaustive testing after
     * board initialization.
     */
    public void testIsOccupied() {

        //test invalid positions
        assertEquals(gameBoard.isOccupied(gameBoard.boardIdxToPosition(Integer.MIN_VALUE)), -1);
        assertEquals(gameBoard.isOccupied(gameBoard.boardIdxToPosition(Integer.MAX_VALUE)), -1);
        for (int idx = -10; idx < 0; idx++) {
            assertEquals(gameBoard.isOccupied(gameBoard.boardIdxToPosition(idx)), -1);
        }
        //test occupied positions
        for (int idx = 0; idx < 16; idx++) {
            assertEquals(gameBoard.isOccupied(gameBoard.boardIdxToPosition(idx)), 1);
        }
        //test unoccupied positions
        for (int idx = 16; idx < 47; idx++) {
            assertEquals(gameBoard.isOccupied(gameBoard.boardIdxToPosition(idx)), 0);
        }
        //test occupied positions
        for (int idx = 48; idx < 64; idx++) {
            assertEquals(gameBoard.isOccupied(gameBoard.boardIdxToPosition(idx)), 1);
        }
        //test invalid positions
        for (int idx = 64; idx < 70; idx++) {
            assertEquals(gameBoard.isOccupied(gameBoard.boardIdxToPosition(idx)), -1);
        }

    }

    /**
     * Check if the isOccupiedByOpponent function works correctly through exhaustive testing.
     */
    public void testIsOccupiedByOpponent() {

        //test invalid positions
        for (int idx = -10; idx < 0; idx++) {
            assertEquals(gameBoard.isOccupiedByOpponent(gameBoard.boardIdxToPosition(idx), player1), -1);
        }
        //test occupied positions (by friendly pieces)
        for (int idx = 0; idx < 16; idx++) {
            assertEquals(gameBoard.isOccupiedByOpponent(gameBoard.boardIdxToPosition(idx), player1), 0);
        }
        //test unoccupied positions
        for (int idx = 16; idx < 47; idx++) {
            assertEquals(gameBoard.isOccupiedByOpponent(gameBoard.boardIdxToPosition(idx), player1), 0);
        }
        //test occupied positions (by opponent pieces)
        for (int idx = 48; idx < 64; idx++) {
            assertEquals(gameBoard.isOccupiedByOpponent(gameBoard.boardIdxToPosition(idx), player1), 1);
        }
        //test invalid positions
        for (int idx = 64; idx < 70; idx++) {
            assertEquals(gameBoard.isOccupiedByOpponent(gameBoard.boardIdxToPosition(idx), player1), -1);
        }

    }

    /**
     * Test the initBoard function to ensure that the gameBoard is properly initiated.
     */
    public void testInitBoard() {

        for (int idx = 8; idx < 16; idx++) {

            //check that they are initialized as pawns
            assertEquals(gameBoard.board[idx] instanceof Pawn, true);
            //check that they are assigned to the right player
            assertEquals(gameBoard.board[idx].isPlayer1(), true);
            //check that their internal position is right
            assertEquals(Arrays.equals(gameBoard.board[idx].getPos(), gameBoard.boardIdxToPosition(idx)), true );
        }
        for (int idx = 48; idx < 56; idx++) {
            assertEquals(gameBoard.board[idx] instanceof Pawn, true);
            assertEquals(gameBoard.board[idx].isPlayer1(), false);
            assertEquals(Arrays.equals(gameBoard.board[idx].getPos(), gameBoard.boardIdxToPosition(idx)), true );
        }

        for (int idx = 16; idx < 48; idx++) {
            //check that these spaces are initially empty
            assertEquals(gameBoard.board[idx] == null, true);
        }

        for (int idx = 0; idx < 8; idx++) {
            //check that they are ChessPiece objects
            assertEquals(gameBoard.board[idx] instanceof ChessPiece, true);
            assertEquals(gameBoard.board[idx].isPlayer1(), true); //check they are assigned to the right player
            //check that their internal position is right
            assertEquals(Arrays.equals(gameBoard.board[idx].getPos(), gameBoard.boardIdxToPosition(idx)), true );
        }

        for (int idx = 56; idx < 64; idx++) {
            assertEquals(gameBoard.board[idx] instanceof ChessPiece, true);
            assertEquals(gameBoard.board[idx].isPlayer1(), false);
            assertEquals(Arrays.equals(gameBoard.board[idx].getPos(), gameBoard.boardIdxToPosition(idx)), true );
        }

    }

    /**
     * Check the movement functionality of the board. Includes testing what happens when a player tries to move a piece
     * to an empty slot, to an invalid space, tries to capture an opponent's piece, try to control an opponent's piece,
     * tries to capture one of his/her own pieces, tries to put the king in check, etc.
     */
    public void testMove() {

        assertEquals(gameBoard.move(0, 24, true), false); // rook can't move past the pawn
        assertEquals(gameBoard.move(48, 40, true), false); //can't control opponent's pieces
        assertEquals(gameBoard.move(11, 19, true), true); //should be able to move the pawn
        assertEquals(gameBoard.board[19] instanceof Pawn, true);
        assertEquals(gameBoard.board[19].getPos()[0], 3); //check that internal position values are updated
        assertEquals(gameBoard.board[19].getPos()[1], 2);
        assertEquals(gameBoard.board[11] == null, true);
        //knight should be able to leap over other pieces
        assertEquals(gameBoard.move(57, 42, false), true);
        //should not be able to move from where there is no piece
        assertEquals(gameBoard.move(11, 19, true), false);
        //should not be able to move off the board
        assertEquals(gameBoard.move(15, 64, true), false);
        //should not be able to move off the board
        assertEquals(gameBoard.move(new int[] {6,0}, new int[] {9,8}, true), false);
        // should not be able to attack its own player
        assertEquals(gameBoard.move(6, 12, true), false);
        //knight moves
        assertEquals(gameBoard.move(1, 18, true), true);
        //pawn shouldn't move diagonally without an opponent being there
        assertEquals(gameBoard.move(52, 45, false), false);
        //pawn moves two steps ahead
        assertEquals(gameBoard.move(52, 36, false), true);
        //standard move
        assertEquals(gameBoard.move(18, 35, true), true);
        //shouldn't be allowed as it would put the king in check
        assertEquals(gameBoard.move(60, 52, false), false);
        //pawns cannot kill opponents straight ahead
        assertEquals(gameBoard.move(51, 35, false), false);
        //bishop moves
        assertEquals(gameBoard.move(61, 43, false), true);
        //System.out.println(gameBoard);

    }

    /**
     * Checks the functionality of the moveLeavesKingInCheck function. Tested using several situations
     * the board can be in and testing
     * whether the function can check if a move would leave its king in check and preventing it.
     * Also makes sure there are no false positives
     * through exhaustive testing.
     * Assumes 'move' is functioning correctly.
     */
    public void testMoveLeavesKingInCheck() {

        testMove();
        // moves king in the path of the opponent's knight
        assertEquals(gameBoard.moveLeavesKingInCheck(60, 52, false), true);
        gameBoard.move(59,31, false); //move the queen
        //moving the pawn leaves the king exposed to the opponent's queen
        assertEquals(gameBoard.moveLeavesKingInCheck(13, 29, true), true);
        //moving the pawn leaves the king exposed to the opponent's queen
        assertEquals(gameBoard.moveLeavesKingInCheck(13, 28, true), true);
        gameBoard.move(14, 22, true); //moves the pawn in the path of the queen
        //now this move shouldn't lead to a check
        assertEquals(gameBoard.moveLeavesKingInCheck(13, 28, true), false);
        gameBoard.move(48, 32, false); // move the pawn, so rook can move a bit
        //knight moves to get in position to take out opposition queen
        gameBoard.move(6, 21, true);
        gameBoard.move(31, 24, false); //queen moves out of danger
        gameBoard.move(10, 26, true);
        gameBoard.move(24, 25, false); //put the king in check
        //should still lead to check
        assertEquals(gameBoard.moveLeavesKingInCheck(4, 11, true), true);
        //should block the check
        assertEquals(gameBoard.moveLeavesKingInCheck(3, 11, true), false);
        //should block the check
        assertEquals(gameBoard.moveLeavesKingInCheck(2, 11, true), false);
        //System.out.println(gameBoard);
    }

    /**
     * Checks that the findKing function works correctly, i.e. it is able to find the specified player's king
     * on the board. Does this by moving the king and the pieces around it, to ensure positions are
     * correctly updated.
     * Assumes 'move' is functioning correctly.
     */
    public void testFindKing() {

        testMoveLeavesKingInCheck();
        gameBoard.move(2, 11, true); //block the check with the bishop
        gameBoard.move(25, 41, false); //queen retracts
        assertEquals(gameBoard.findKing(true)[0], 4);
        assertEquals(gameBoard.findKing(true)[1], 0);
        assertEquals(gameBoard.findKing(false)[0], 4);
        assertEquals(gameBoard.findKing(false)[1], 7);
        gameBoard.move(5, 14, true); //move the bishop
        gameBoard.move(60, 59, false); //move the king
        assertEquals(gameBoard.findKing(false)[0], 3);
        assertEquals(gameBoard.findKing(false)[1], 7);
        gameBoard.move(4,5, true); //move the king
        assertEquals(gameBoard.findKing(true)[0], 5);
        assertEquals(gameBoard.findKing(true)[1], 0);
        //System.out.println(gameBoard);

    }

    /**
     * Tests that check detection is working by putting the kings of both players in various situation where they
     * are and some aren't in check and testing that the function correctly recognizes these situations.
     * Assumes 'move' is working.
     */
    public void testIsInCheck() {

        testMoveLeavesKingInCheck();
        assertEquals(gameBoard.isInCheck(player1), true); //can be attacked by the queen
        gameBoard.move(25, 17, !player1); //move the queen away
        assertEquals(gameBoard.isInCheck(player1), false); //can't be attacked by the queen anymore
        gameBoard.move(35, 45, player1); //move the knight to put the king in check
        assertEquals(gameBoard.isInCheck(!player1), true);
        gameBoard.move(54, 45, !player1); // capture the knight with a pawn
        assertEquals(gameBoard.isInCheck(!player1), false);
        //System.out.println(gameBoard);

    }

    /**
     * Tests that the game can recognize checkmate and stalemate situations.
     * It does this by creating a few situations of both and checking that the board
     * can correctly recognize these situations.
     * Also makes sure there are no false positives, such as positions where there is only a king left
     * but the game is not actually over. This is mainly to guard against code
     * that labels an isolated king as a defeat.
     * Assumes 'move' and 'isInCheck' are working correctly.
     */
    public void testIsGameOver() {

        testIsInCheck();
        assertEquals(gameBoard.isGameOver(player1), 0);
        assertEquals(gameBoard.isGameOver(!player1), 0);

        //referenced https://en.wikipedia.org/wiki/Checkmate to come up with these test cases

        //classic checkmate with a rook
        gameBoard = new ChessBoard8x8();
        gameBoard.board[31] = new King(new int[] {7,3}, true);
        gameBoard.board[29] = new King(new int[] {5,3}, false);
        gameBoard.board[55] = new Rook(new int[] {7,6}, false);
        assertEquals(gameBoard.isGameOver(true), 1);
        assertEquals(gameBoard.isGameOver(false), 0);

        //similar to D. Bryne vs fischer
        gameBoard = new ChessBoard8x8();
        gameBoard.board[1] = new Queen(new int[] {1,0}, false);
        gameBoard.board[14] = new King(new int[] {6,1}, true);
        gameBoard.board[33] = new Pawn(new int[] {1,4}, true);
        gameBoard.board[41] = new Bishop(new int[] {1,5}, true);
        gameBoard.board[42] = new Knight(new int[] {2,5}, true);
        gameBoard.board[50] = new Rook(new int[] {2,6}, true);
        gameBoard.board[54] = new Pawn(new int[] {6,6}, false);
        gameBoard.board[58] = new King(new int[] {2,7}, false);
        assertEquals(gameBoard.isGameOver(true), 0);
        assertEquals(gameBoard.isGameOver(false), 1);

        //testing fool's mate
        gameBoard = new ChessBoard8x8();
        gameBoard.initBoard();

        //make sure starting position is not game over state
        assertEquals(gameBoard.isGameOver(true), 0);
        assertEquals(gameBoard.isGameOver(false), 0);
        gameBoard.move(53, 45, false);
        gameBoard.move(12,28, true);
        gameBoard.move(54, 38, false);
        gameBoard.move(3, 39, true);
        assertEquals(gameBoard.isGameOver(true), 0);
        assertEquals(gameBoard.isGameOver(false), 1);

        //stalemate example referenced from https://en.wikipedia.org/wiki/Stalemate
        gameBoard = new ChessBoard8x8();
        gameBoard.board[7] = new King(new int[] {7,0}, true);
        gameBoard.board[13] = new King(new int[] {5,1}, false);
        gameBoard.board[22] = new Queen(new int[] {6,2}, false);
        assertEquals(gameBoard.isGameOver(true), -1);
        assertEquals(gameBoard.isGameOver(false), 0);

        //another stalemate check
        gameBoard = new ChessBoard8x8();
        gameBoard.board[30] = new King(new int[] {6,3}, false);
        gameBoard.board[41] = new Queen(new int[] {1,5}, false);
        gameBoard.board[48] = new Pawn(new int[] {0,6}, true);
        gameBoard.board[56] = new King(new int[] {0,7}, true);
        assertEquals(gameBoard.isGameOver(true), -1);
        assertEquals(gameBoard.isGameOver(false), 0);


    }

    /**
     * Tests that the undo functionality works correctly.
     * Makes sure it works for moves that do capture another piece and those that don't.
     * Ensure that the internal position state of the pieces involved is also reversed.
     */
    public void testUndoTheMove() {

        gameBoard.move(9, 25, player1); //move rook
        gameBoard.move(57, 42, !player1); //move knight
        Move testMove = new Move(14, 30, gameBoard.board[30]); //record move
        gameBoard.move(14, 30, player1); //make the move
        //undo the move
        gameBoard.undoTheMove(testMove.getStartIdx(), testMove.getDestIdx(), testMove.getOriginallyAtDest());
        //make sure move is properly undone
        assertEquals(gameBoard.board[14] instanceof Pawn, true);
        assertEquals(Arrays.equals(gameBoard.board[14].getPos(), new int[] {6, 1}), true);
        assertEquals(gameBoard.board[30], testMove.getOriginallyAtDest());

        //make sure captures are undone properly
        testMove = new Move(42, 25, gameBoard.board[25]); //save the move
        gameBoard.move(42, 25, !player1);
        gameBoard.undoTheMove(testMove.getStartIdx(), testMove.getDestIdx(), testMove.getOriginallyAtDest());
        assertEquals(gameBoard.board[42] instanceof Knight, true);
        assertEquals(Arrays.equals(gameBoard.board[42].getPos(), new int[] {2, 5}), true);
        assertEquals(gameBoard.board[25], testMove.getOriginallyAtDest());
        assertEquals(Arrays.equals(gameBoard.board[25].getPos(), new int[] {1, 3}), true);

    }

}