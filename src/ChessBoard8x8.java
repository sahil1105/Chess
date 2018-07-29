import com.sun.istack.internal.NotNull;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * ChessBoard8x8 -- Implementation of a Chess Board extended from the Board Class. Contains of a 8x8 2D Board.
 * Follows the rules of basic Chess. Implements functionality such as moving/capturing pieces, checking if a piece
 * is occupied,
 * initializing the board, checking if the player's king is in check, if the game is over (checkmate or stalemate),
 * etc.
 * @author sahil1105
 */
public class ChessBoard8x8 extends Board {

    /**
     * Constructor for the ChessBoard8x8 class. Initializes a size 64 (8x8) ChessPiece object array.
     * Doesn't place the pieces on the board yet. That is done by initBoard().
     */
    public ChessBoard8x8() {
        this.board = new ChessPiece[64];
    }

    /**
     * Utility function to convert a 2D square on the board to the board index for the actual ChessPiece array
     * (since that is 1D).
     * @param pos an int[2] with the (row,col). Takes 0-indexed coordinates. {(0-7),(0-7)}
     * @return index in the ChessPiece array corresponding to the provided position 2D on the board
     *         -1 if the input doesn't map to a location on the board.
     */
    protected int twoDPositionToBoardIdx(@NotNull int[] pos) {

        //check that the input int[] is of right size
        if (pos.length != 2) {
            return -1;
        }
        return twoDPositionToBoardIdx(pos[0], pos[1]);

    }

    /**
     * Utility function to convert a 2D square on the board to the board index for the actual ChessPiece array
     * (since that is 1D).
     * @param x The column number (zero-indexed) (0-7)
     * @param y The row number (zero-indexed) (0-7)
     * @return index in the ChessPiece array corresponding to the provided position 2D on the board
     *         -1 if the input doesn't map to a location on the board.
     */
    protected int twoDPositionToBoardIdx(int x, int y) {

        //check if the input is valid
        if (x > 7 || x < 0 || y > 7 || y < 0) {
            return -1;
        }

        //return corresponding index
        return ((8*y) + x);


    }

    /**
     * Utility function to convert an index from the ChessPiece array to a 2D coordinate on the board.
     * @param pos the index in the ChessPiece array
     * @return the 2D coordinate as an int[]
     *         null if the input is invalid (out of range (0-63))
     */
    protected int[] boardIdxToPosition(int pos) {

        //check if in range
        if (pos < 0 || pos > 63) {
            return null;
        }

        //get the col and row respectively
        int x = pos % 8;
        int y = pos / 8;

        //return it as an int[]
        return (new int[] {x,y});

    }

    /**
     * Check if a ChessPiece object exists at a given location
     * @param position a int[2] object specifying the position to check
     * @return -1 if the position provided is invalid
     *          1 if the position is valid and occupied
     *          0 if the position is valid and unoccupied
     */
    public short isOccupied(int[] position) {

        if (position == null) {
            return -1;
        }
        //get the corresponding index
        int boardIdx = twoDPositionToBoardIdx(position);

        //if invalid index, return -1
        if (boardIdx == -1) {
            return -1;
        }

        //else return 1 or 0 based on whether the object on that index is null or not
        return ((this.board[boardIdx] != null)? (short)1 : (short)0);

    }

    /**
     * Checks if there exists an opponent piece at the specified location.
     * @param position a int[2] object specifying the position to check
     * @param player1 boolean specifying if it's player 1's turn or not
     * @return -1 if the position provided is invalid
     *          1 if the position is valid and occupied by an opponent
     *          0 if the position is valid and unoccupied or occupied but not by an opponent piece
     */
    public short isOccupiedByOpponent(int[] position, boolean player1) {

        if (position == null) {
            return -1;
        }
        //get the corresponding index
        int boardIdx = twoDPositionToBoardIdx(position);

        //if invalid index, return -1
        if (boardIdx == -1) {
            return -1;
        }

        //return 0 if unoccupied
        if (this.board[boardIdx] == null) {
            return 0;
        }

        //return 0 or 1 based on whether or not it is occupied by an opponent
        return (this.board[boardIdx].isPlayer1() == player1)? (short)0 : (short)1;
    }

    /**
     * Initialize board with the regular Chess pieces in their normal starting positions
     */
    public void initBoard() {

        this.clearBoard();

        //initialize pawns
        for (int i = 0; i < 8; i ++) {

            this.board[twoDPositionToBoardIdx(i,1)] = new Pawn(new int[] {i,1}, true);
            this.board[twoDPositionToBoardIdx(i,6)] = new Pawn(new int[] {i,6}, false);

        }

        //initialize rooks
        this.board[twoDPositionToBoardIdx(0, 0)] = new Rook(new int[] {0,0}, true);
        this.board[twoDPositionToBoardIdx(7, 0)] = new Rook(new int[] {7,0}, true);
        this.board[twoDPositionToBoardIdx(0, 7)] = new Rook(new int[] {0,7}, false);
        this.board[twoDPositionToBoardIdx(7, 7)] = new Rook(new int[] {7,7}, false);

        //initialize knights
        this.board[twoDPositionToBoardIdx(1, 0)] = new Knight(new int[] {1,0}, true);
        this.board[twoDPositionToBoardIdx(6, 0)] = new Knight(new int[] {6,0}, true);
        this.board[twoDPositionToBoardIdx(1, 7)] = new Knight(new int[] {1,7}, false);
        this.board[twoDPositionToBoardIdx(6, 7)] = new Knight(new int[] {6,7}, false);

        //initialize bishops
        this.board[twoDPositionToBoardIdx(2, 0)] = new Bishop(new int[] {2,0}, true);
        this.board[twoDPositionToBoardIdx(5, 0)] = new Bishop(new int[] {5,0}, true);
        this.board[twoDPositionToBoardIdx(2, 7)] = new Bishop(new int[] {2,7}, false);
        this.board[twoDPositionToBoardIdx(5, 7)] = new Bishop(new int[] {5,7}, false);

        //initialize queens
        this.board[twoDPositionToBoardIdx(3, 0)] = new Queen(new int[] {3,0}, true);
        this.board[twoDPositionToBoardIdx(3, 7)] = new Queen(new int[] {3,7}, false);

        //initialize kings
        this.board[twoDPositionToBoardIdx(4, 0)] = new King(new int[] {4,0}, true);
        this.board[twoDPositionToBoardIdx(4, 7)] = new King(new int[] {4,7}, false);

    }

    /**
     * Utility function to clear all the pieces from the board.
     * Essentially sets all the elements to null.
     */
    private void clearBoard() {

        for (int i = 0; i < this.board.length; i++) {
            this.board[i] = null;
        }

    }

    /**
     * Initializes a custom board, with a few pieces replaced with an alfil and a nightrider
     * for each player
     */
    public void initCustomBoard() {

        this.initBoard();
        this.board[twoDPositionToBoardIdx(0,0)] = new Alfil(new int[] {0,0}, true);
        this.board[twoDPositionToBoardIdx(7, 7)] = new Alfil(new int[] {7,7}, false);
        this.board[twoDPositionToBoardIdx(6, 0)] = new Nightrider(new int[] {6,0}, true);
        this.board[twoDPositionToBoardIdx(1, 7)] = new Nightrider(new int[] {1,7}, false);


    }

    /**
     * Wrapper function for the 'move' function below. This one takes the source and destination indices as arguments.
     * @param startIdx Current Index of the piece to move
     * @param destIdx   Index to move the piece to.
     * @param player1    boolean specifying if this is player 1's move or not.
     * @return makes the move and returns true if the move is legal
     *         doesn't make the move and returns false otherwise
     */
    public boolean move(int startIdx, int destIdx, boolean player1) {

        //check that the indices are valid
        if (startIdx > 63 || startIdx < 0 || destIdx > 63 || destIdx < 0) {
            return false;
        }

        //call the 'move' function below
        return move(boardIdxToPosition(startIdx), boardIdxToPosition(destIdx), player1);

    }

    /**
     * Wrapper function for the 'move' function below. This one takes the source and destination positions as int[]s.
     * @param source_pos Current Position of the piece to move
     * @param dest_pos   Position to move the piece to.
     * @param player1    boolean specifying if this is player 1's move or not.
     * @return makes the move and returns true if the move is legal
     *         doesn't make the move and returns false otherwise
     */
    public boolean move(@NotNull int[] source_pos, @NotNull int[] dest_pos, boolean player1) {

        //check that the inputs are the right size
        if (source_pos.length != 2 || dest_pos.length != 2) {
            return false;
        }
        //call the 'move' function with the correct parameters
        return this.move(source_pos[0], source_pos[1], dest_pos[0], dest_pos[1], player1);
    }


    /**
     * Function to perform a move on the Game board. Makes the move only if it is legal (the piece belongs to
     * the player whose turn
     * it is, the piece can make this move and this move
     * doesn't leave the player's king in check).
     * @param source_x Starting X coordinate (column)
     * @param source_y Starting Y coordinate (row)
     * @param destination_x X coordinate to move to (Column)
     * @param destination_y Y coordinate to move to (Row)
     * @param player1 boolean specifying if it is player 1's turn
     * @return true if the move is legal (the move is performed)
     *         false otherwise (the mode is not performed)
     */
    public boolean move(int source_x, int source_y, int destination_x, int destination_y, boolean player1) {

        //get the corresponding array indices for the starting and destination coordinates
        int sourceIdx = twoDPositionToBoardIdx(source_x, source_y);
        int destinationIdx = twoDPositionToBoardIdx(destination_x, destination_y);

        //return false if either of the coordinates are invalid
        if (sourceIdx == -1 || destinationIdx == -1) {
            return false;
        }

        //check that there exists a piece on the specified starting location
        //check that it belongs to the player whose turn it is
        //check that this piece can be moved to this position
        //check that this move wouldn't leave the player's king in check
        //if all satisfied, make the move, and return true
        if (this.isOccupied(boardIdxToPosition(sourceIdx)) == 1 && this.board[sourceIdx].player1 == player1
                && this.board[sourceIdx].canMoveToPos(this, boardIdxToPosition(destinationIdx))
                && !moveLeavesKingInCheck(sourceIdx, destinationIdx, player1)) {
            moveHelper(sourceIdx, destinationIdx);
            return true;
        }

        //otherwise the move is invalid, for one of the above reasons, hence do not make the move and
        // return false instead
        return false;

    }


    /**
     * Function that lists all the possible moves for a chess piece. Ensures that returned moves do
     * not lead to a check for the player.
     * @param pieceIndex Index on the board where the piece is located
     * @param player1 Boolean indicating whether the piece belongs to player 1
     * @return List of legal moves for the given piece
     */
    public List possibleMovesForAPiece(int pieceIndex, boolean player1) {

        List<int[]> possibleMoves = new ArrayList<>();
        //base condition checks
        if (pieceIndex < 0 || pieceIndex >= board.length || board[pieceIndex] == null || board[pieceIndex].isPlayer1() != player1) {
            return possibleMoves;
        }
        //get the possible moves
        possibleMoves = board[pieceIndex].getPossibleMoves(this);
        //collect the illegal moves (that would lead to king being in check)
        List<int[]> illegalMoves = new ArrayList<>();
        for (int[] curr_move: possibleMoves) {
            if (moveLeavesKingInCheck(pieceIndex, this.twoDPositionToBoardIdx(curr_move), player1)) {
                illegalMoves.add(curr_move);
            }
        }
        //remove the illegal moves
        possibleMoves.removeAll(illegalMoves);
        return possibleMoves;
    }

    /**
     * Helper function to actually make the specified move.
     * Doesn't check for the legality of the move.
     * Might lead to a capture. In that case the captured object is simply destroyed (by the garbage collector,
     * unless there is another reference to it somewhere)
     * @param sourceIdx Index on the ChessPiece array from where to move the piece
     * @param destinationIdx Index on the ChessPiece array to move the piece to
     */
    private void moveHelper(int sourceIdx, int destinationIdx) {
        //set the new position in the corresponding piece
        this.board[sourceIdx].setPos(boardIdxToPosition(destinationIdx));
        //move the piece
        this.board[destinationIdx] = this.board[sourceIdx];
        //empty the starting location
        this.board[sourceIdx] = null;
    }

    /**
     * Checks whether a certain move would leave the player's king in check.
     * Assumes the move is possible for the piece to make.
     * @param sourceIdx Index on the ChessPiece array from where to move the piece
     * @param destIdx Index on the ChessPiece array to move the piece to
     * @param player1 boolean specifying if it is player 1's turn
     * @return true if the move would leave the player's king in check
     *         false if the move wouldn't leave the player's king in check
     */
    protected boolean moveLeavesKingInCheck(int sourceIdx, int destIdx, boolean player1) {

        //store what was originally at the destination position, so it can be re-placed there
        ChessPiece originallyAtDest = this.board[destIdx];
        //make the move
        moveHelper(sourceIdx, destIdx);
        //check if the player is now in check
        boolean inCheck = this.isInCheck(player1);
        //undo the move
        this.undoTheMove(sourceIdx, destIdx, originallyAtDest);

        //return whether the move led to the king being in check
        return inCheck;
    }

    /**
     * Utility function to find the specified player's King on the Chess Board
     * @param player1 boolean specifying whether to find player 1 (true) or player 2's (false) King
     * @return int[] specifying the location of the player's king
     *         null if there is no king (probably captured)
     */
    protected int[] findKing(boolean player1) {

        //holder to store the position in
        int [] kingPosition = null;

        //go through the ChessPiece array
        for (int i = 0; i < this.board.length; i++) {
            if (this.board[i] != null && (this.board[i] instanceof King) && this.board[i].isPlayer1() == player1) {
                //found the king
                kingPosition = boardIdxToPosition(i);
                break;
            }
        }
        return kingPosition;

    }

    /**
     * Check if the player's king is in 'check'
     * @param player1 boolean specifying which player's turn it is
     * @return true if the player's king is in check
     *         false otherwise
     */
    public boolean isInCheck(boolean player1) {

        //get the king's location
        //assuming this is not null
        int[] kingPos = this.findKing(player1);

        //go through all of the opponent's pieces and their possible moves
        for (int i = 0; i < this.board.length; i++) {
            if (this.board[i] != null && this.board[i].isPlayer1() != player1) {
                //check if this piece can attack the king in its next move
                if (this.board[i].canMoveToPos(this, kingPos)) {
                    //if it can, return true
                    return true;
                }
            }
        }

        //none of the opponent's piece can attack the player's king in their next move, so return false
        return false;
    }

    /**
     * Checks if the game is over i.e. there is either a checkmate or a stalemate
     * @param player1 boolean specifying if it is player 1's turn
     * @return 1 if there is a checkmate and hence the game is over
     *        -1 if there is a stalemate and hence the game is over
     *         0 otherwise
     */
    public int isGameOver(boolean player1) {

        //if your king is dead, then game over
        if (findKing(player1) == null) {
            return 1;
        }

        //check if you have any legal moves left (moves that do not put your own king in check)
        boolean legalMovesLeft = legalMovesLeft(player1);
        //if you are in check
        if (isInCheck((player1))) {
            //but have legal moves left, then it's not a checkmate or a stalemate
            if (legalMovesLeft) {
                return 0;
            }
            //if no legal moves left, but in check, then it's a checkmate and hence game over
            return 1;
        }

        //stalemate check, i.e. if not in check, then game is over if you do not have any legal moves left,
        // otherwise it isn't
        return (!legalMovesLeft? -1 : 0);

    }

    /**
     * Checks if you have any legal moves left, in particular moves that do not put your own king in check
     * @param player1 boolean specifying if it is player 1's turn
     * @return true if you have legal moves left, false if not
     */
    private boolean legalMovesLeft(boolean player1) {

        for (int idx = 0; idx < this.board.length; idx++) {
            if (this.board[idx] != null && this.board[idx].isPlayer1() == player1) {
                //for each of your pieces, get their possible moves
                List<int[]> possibleMoves = this.board[idx].getPossibleMoves(this);
                for (int i = 0; i < possibleMoves.size(); i++) {
                    int destIdx = twoDPositionToBoardIdx(possibleMoves.get(i)); //destIdx on the ChessPiece array
                    //store what was originally at dest so it can be restored later
                    ChessPiece originallyAtDest = this.board[destIdx];
                    int[] currentPosition = boardIdxToPosition(idx); //get current location as 2D coordinates
                    //execute the move and check if it is a legal move (doesn't put the king in check)
                    boolean isALegalMove = this.move(currentPosition[0], currentPosition[1],
                                                     possibleMoves.get(i)[0], possibleMoves.get(i)[1],
                                                     player1);
                    //if not a legal move, then check the next one (must not have been performed, so no need to undo it)
                    if (!isALegalMove) {
                        continue;
                    }
                    //since the move is legal, it must have been performed, hence we must undo the move
                    this.undoTheMove(idx, destIdx, originallyAtDest);
                    //since there is a legal move left, return true
                    return true;

                }

            }
        }
        //if here, that means there were no legal moves, hence return false
        return false;
    }

    /**
     * Utility function to undo a move.
     * @param startIdx Index in the ChessPiece array of the position the move was originally made from
     * @param destIdx Index in the ChessPiece array of the position the move was originally made to
     * @param originallyAtDest The ChessPiece object that was originally at the destination (to undo captures)
     */
    protected void undoTheMove(int startIdx, int destIdx, ChessPiece originallyAtDest) {
        //point the original location back to the piece that was moved
        this.board[startIdx] = this.board[destIdx];
        //restore the piece originally at the destination (undo capture if there was one)
        this.board[destIdx] = originallyAtDest;
        //reset the position of the piece that was moved. Needn't do this was the ChessPiece originallyAtDest,
        //since it's position was never modified in the first place
        this.board[startIdx].setPos(boardIdxToPosition(startIdx));
    }

    /**
     * Overriding the toString function
     * @return String representation of the Chess Board, complete with all the pieces on it (if initialized)
     */
    @Override
    public String toString() {

        StringBuilder board = new StringBuilder(""); //StringBuilder object to hold the string representation
        for (int row = 0; row < 8; row++) { //going over the ChessPiece array
            board.append("\n-----------------------------------------\n");
            for (int col = 0; col < 8; col++) {
                board.append("|");
                int idx = twoDPositionToBoardIdx(col, row);
                board.append((idx < 10)? "  " + idx + " " : " " + idx + " ");
            }
            board.append("|");
            board.append("\n");
            for (int col = 0; col < 8; col++) {
                board.append("|");
                int idx = twoDPositionToBoardIdx(col,row);
                String piece_string = (this.board[idx] != null)? " " + this.board[idx].toString() + " " : ("    ");
                board.append(piece_string);
            }
            board.append("|");
        }
        board.append("\n-----------------------------------------\n");
        return board.toString(); //return it as a string object
    }

}
