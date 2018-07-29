import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Pawn -- Extension of the ChessPiece class. Used to represent a pawn object in a game of Chess.
 * @author sahil1105
 */
public class Pawn extends ChessPiece{


    /**
     * Constructor for the Pawn class. Simply calls the constructor for the ChessPiece class.
     * @param pos The position piece is at on the Board.
     * @param player1 Whether or not the piece belongs to player 1.
     */
    public Pawn(@NotNull int[] pos, boolean player1) {
        super(pos, player1);
    }

    /**
     * Implementation of the getPossibleMoves from the parent ChessPiece class.
     * @param gameBoard The Board object that the piece is placed on
     * @return A list of int[] objects specifying the positions the object can move to.
     * Doesn't check if the move would be legal (eg. leave the king in a check, that must be decided
     * by the Board object).
     */
    public List getPossibleMoves(@NotNull Board gameBoard) {

        List<int[]> possibleMoves = new ArrayList<>(); //create a list of int[] objects
        int tempY = this.player1? 1 : (-1); //the direction the piece can move in given whether it is player 1 or not
        //starting Y position based on whether Pawn belongs to the first player
        int startingYPosition = this.player1? 1 : 6;

        //one ahead
        //checks if the Pawn can move to the square in front of it
        int[] possibility1 = incrementedInDirection(this.pos, new int[] {0, tempY});
        if (gameBoard.isOccupied(possibility1) == 0) {
            possibleMoves.add(possibility1);
        }
        //two ahead if first move
        //checks if the pawn can move to the square two steps in front of it
        //can only do so if this is his first move and the square directly in front is also unoccupied.
        int[] possibility2 = incrementedInDirection(this.pos, new int[] {0, (2*tempY)});
        if (gameBoard.isOccupied(possibility2) == 0 && gameBoard.isOccupied(possibility1) == 0 &&
                this.pos[1] == startingYPosition) {
            possibleMoves.add(possibility2);
        }
        //diagonal left if opponent's piece there
        //checks if the Pawn can move diagonally to its left and capture an opponent
        int[] possibility3 = incrementedInDirection(this.pos, new int[] {-1, tempY});
        if (gameBoard.isOccupiedByOpponent(possibility3, this.player1) == 1) {
            possibleMoves.add(possibility3);
        }
        //diagonal right if opponent's piece there
        //checks if the Pawn can move diagonally to its right and capture an opponent
        int[] possibility4 = incrementedInDirection(this.pos, new int[] {1, tempY});
        if (gameBoard.isOccupiedByOpponent(possibility4, this.player1) == 1) {
            possibleMoves.add(possibility4);
        }

        return possibleMoves;

    }


    /**
     * Overriding toString function for the Pawn class.
     * @return String representation of the Pawn class. Capital if belongs to player 1, uncapitalized otherwise.
     */
    @Override
    public String toString() {
        return this.player1? "P ": " p";
    }

}
