/**
 * Board -- Abstract class to represent a Game Board on which ChessPieces would be placed.
 * Most of the core functionality of the game must be implemented here, such as checking if a move is valid,
 * if the game is over (checkmate or stalemate), if there are legal moves left, etc.
 * @author sahil1105
 */
public abstract class Board {

    /**
     * An array of ChessPiece objects. Represents the actual game board.
     */
    protected ChessPiece[] board;

    /**
     * @param position
     * @return A pointer to the ChessPiece object at the specified position or 'null' if the position is empty.
     */
    public ChessPiece getPieceAtPosition(int[] position) {
        return null;
    }

    /**
     * Utility function that converts an array index to a ChessPiece location in n-dimensional space.
     * Needs to be implemented by children classes based on their dimensionality and board structure.
     * @param pos
     * @return An int[] describing the location corresponding to the given index in n-dimensional space
     */
    protected int[] boardIdxToPosition(int pos) {
        return null;
    }

    /**
     * Checks if the position is occupied by a piece.
     * @param position
     * @return 0 if not occupied
     *         1 if occupied
     *        -1 if not a valid position
     */
    public short isOccupied(int[] position) {
        return 1;
    }

    /**
     * Checks if the position is occupied by an opponent or not.
     * @param position
     * @param player1
     * @return 0 if not occupied by opponent
     *         1 if occupied by opponent
     *        -1 if not a valid position
     */
    public short isOccupiedByOpponent(int[] position, boolean player1) {
        return 1;
    }

    /**
     * Initializes the board with the correct pieces with respect to the game.
     */
    public void initBoard() {
        return;
    }

    /**
     * Function that moves a piece at source_pos to dest_pos if the move is legal.
     * May capture opponent's piece.
     * @param source_pos Current Position of the piece to move
     * @param dest_pos Position to move the piece to.
     * @param player1 boolean specifying if this is player 1's move or not.
     * @return makes the move and returns true if the move is valid/legal
     *         else doesn't make the move and returns false.
     */
    public boolean move(int[] source_pos, int[] dest_pos, boolean player1) {
        return true;
    }

    /**
     * Function to check if the player whose turn it is, is in 'Check'
     * @param player1 boolean specifying which player's turn it is
     * @return true if the player is in check, false otherwise.
     */
    public boolean isInCheck(boolean player1) {
        return false;
    }


    /**
     * Function to check if the game is over (checkmate, stalemate, etc.) based on the rules of the game.
     * @param player1 boolean specifying if it is player 1's turn
     * @return 1 if there is a checkmate and hence the game is over
     *        -1 if there is a stalemate and hence the game is over
     *         0 otherwise
     */
    public int isGameOver(boolean player1) {
        return 0;
    }


    /**
     * Overriding the toString function to aid in printing the board.
     * @return String representation of the Board.
     * Expected to be implemented by each child class based on its structure.
     */
    @Override
    public String toString() {
        return "";
    }

}
