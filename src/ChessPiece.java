import com.sun.istack.internal.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static jdk.nashorn.internal.objects.NativeMath.min;


/**
 * ChessPiece -- This class is a blueprint of a chess piece object. These objects can be placed on
 * boards of type 'Board'. Each ChessPiece object contains a pos (position) and a boolean  'player1' indicating
 * whether it belongs to player 1 or not. The 'pos' is a 1-D int[] containing the piece's position on a
 * multi-dimensional Board.
 *
 * @author sahil1105
 */
public abstract class ChessPiece {

    /**
     * Getter for the 'pos' variable of the ChessPiece class.
     * @return the position ('pos') vector of the ChessPiece object.
     */
    public int[] getPos() {
        return pos;
    }

    /**
     * Setter for the 'pos' variable of the ChessPiece class.
     * @param pos : The new position of the ChessPiece object on the Board
     */
    public void setPos(@NotNull int[] pos) {
        this.pos = pos;
    }

    /**
     * Getter for the 'player1' boolean variable of the ChessPiece class.
     * @return player1 : The boolean vector indicating whether the piece belongs to player 1 or player 2. 'true' for
     * player 1, 'false' for player 2.
     */
    public boolean isPlayer1() {
        return player1;
    }

    /**
     * Integer array containing the multi-dimensional vector specifying the ChessPiece's location on the Board
     */
    protected int[] pos;

    /**
     * Boolean variable specifying whether this piece belongs to player1 or not. Cannot be changed after instantiation.
     */
    protected final boolean player1;

    /**
     * Getter for the piece icon for this piece.
     * @return Image corresponding to this piece
     */
    public Image getPieceIcon() {
        return pieceIcon;
    }

    /**
     * A 64x64 image for this piece.
     */
    protected Image pieceIcon;

    /**
     * Constructor for the ChessPiece class.
     * @param pos The postion of the ChessPiece object
     * @param player1 'true' if the piece belongs to player 1, false otherwise.
     */
    public ChessPiece(@NotNull int[] pos, boolean player1) {
        this.pos = Arrays.copyOf(pos, pos.length);
        this.player1 = player1;
        this.pieceIcon = getPieceIcon(getImageIndex());
    }

    /**
     * Location of the image from where to get the images of the chess pieces.
     */
    private final String imagesLocation = "chessPieces.png";

    /**
     * Utility function to get the corresponding image for the piece
     * @param imageIndex the index in the pre-specified image to assign to this piece
     * @return An Image corresponding to the calling piece.
     */
    private Image getPieceIcon(int imageIndex) {
        Image[] images = loadImages(imagesLocation);
        //positions of the image in the imported pic
        int QUEEN = 0, KING = 1, ROOK = 2, KNIGHT = 3, BISHOP = 4, PAWN = 5;
        int offset = this.isPlayer1()? 0 : 6;

        if (imageIndex == 6) { //if a nightrider
            return rotate(images[offset + KNIGHT]); //return a rotated knight
        }
        if (imageIndex == 7) { //if an alfil
            return rotate(images[offset + BISHOP]); //return a rotated bishop
        }

        return images[offset+imageIndex];
    }

    /**
     * Get index based on the specific class of the Piece
     * @return An index to be passed into getPieceIcon method above.
     */
    private int getImageIndex() {

        if (this instanceof Queen) {
            return 1;
        }
        if (this instanceof King) {
            return 0;
        }
        if (this instanceof Rook) {
            return 2;
        }
        if (this instanceof Knight) {
            return 3;
        }
        if (this instanceof Bishop) {
            return 4;
        }
        if (this instanceof Pawn) {
            return 5;
        }
        if (this instanceof Nightrider) {
            return 6;
        }
        if (this instanceof Alfil) {
            return 7;
        }
        return 5;
    }

    /**
     * Utility function to rotate an image by 180 degrees.
     * //Reference: https://stackoverflow.com/questions/23457754/how-to-flip-bufferedimage-in-java
     * @param original Original image. (Not modified)
     * @return 180 degree rotated copy of the original image.
     */
    public static Image rotate(Image original) {
        BufferedImage rotatedImage = new BufferedImage(64,64,BufferedImage.TYPE_INT_ARGB);
        AffineTransform tx = AffineTransform.getRotateInstance(Math.PI, 32, 32);
        Graphics2D g = rotatedImage.createGraphics();
        g.transform(tx);
        g.drawImage(original, 0,0, null);
        g.dispose();
        return rotatedImage;
    }

    /**
     * Utility function to load images of standard chess pieces from a predefined image.
     * @param imagesLocation Location of the image to load from
     * @return Image[] of size 12 containing 64x64 images of various standard chess pieces
     * @throws IOException
     */
    private Image[] loadImages(String imagesLocation) {

        Image[] images = new Image[12];
        try {
            File imageFile = new File(imagesLocation);
            BufferedImage mainImage = ImageIO.read(imageFile);
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 6; col++) {
                    images[row*6 + col] = mainImage.getSubimage(col*64, row*64, 64, 64);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return images;
    }

    /**
     * Function that enumerates all the possible moves that this ChessPiece can make, given its current
     * position and the state of the board.
     * Expected to be implemented by each of the children classes separately.
     * @param gameBoard The Board object that the piece is placed on
     * @return List of positions on the Board object that this piece can move to.
     */
    public abstract List getPossibleMoves(@NotNull Board gameBoard);

    /**
     * @return String representation of the ChessPiece object.
     * Expected to be implemented by the children classes.
     */
    @Override
    public abstract String toString();


    /**
     * A helper function which adds to the possibleMoves list all the moves that the ChessPiece can make from
     * its current position
     * in a given particular direction. direction[i] specifies the number of steps to take in each direction at
     * any point.
     * Keeps going until either goes outside the board, or hits an obstacle (another piece). Includes move that
     * captures an opponents piece.
     * @param gameBoard The Board object that the ChessPiece is currently on. Is not modified.
     * @param possibleMoves A list of int[] objects to add to. Is modified.
     * @param direction A direction vector indicating the direction to move in. Is not modified.
     */
    protected void possibleMovesHelper_multipleSteps(@NotNull Board gameBoard, @NotNull List<int[]> possibleMoves,
                                                     @NotNull int[] direction) {

        int[] nextPos = Arrays.copyOf(this.pos, this.pos.length); //make a copy of the current position vector
        //increment it in the given direction to the get the next possible position
        incrementInDirection(nextPos, direction);
        while(gameBoard.isOccupied(nextPos) == 0) { //keep going while there are no obstacles (other pieces)
            //if the position is empty, add this position to the list of possible moves
            possibleMoves.add(Arrays.copyOf(nextPos, nextPos.length));
            incrementInDirection(nextPos, direction); //increment further in the given direction
        }
        //for the last position checked, if it is an opponent piece
        if (gameBoard.isOccupiedByOpponent(nextPos, this.player1) == 1) {
            possibleMoves.add(Arrays.copyOf(nextPos, nextPos.length)); //then add it, otherwise not
        }
    }

    /**
     * A helper function which adds to the possibleMoves list a move in the 'direction' direction from the
     * ChessPiece's current location,
     * if it is a valid move, i.e. either it is empty or has an opponent's piece on it.
     * @param gameBoard The Board object that the ChessPiece is currently on. Is not modified.
     * @param possibleMoves A list of int[] objects to add to. Is modified.
     * @param direction A direction vector indicating the step to take. Is not modified.
     */
    protected void possibleMovesHelper_singleStep(@NotNull Board gameBoard, @NotNull List<int[]> possibleMoves,
                                                  @NotNull int[] direction) {
        //get a copy of the position vector incremented in the given direction
        int[] possibility = incrementedInDirection(this.pos, direction);
        //if the position is either empty or occupied by an opponent
        if (gameBoard.isOccupied(possibility) == 0 || gameBoard.isOccupiedByOpponent(possibility, this.player1) == 1) {
            possibleMoves.add(possibility); //then add it to the list of possible moves, otherwise not
        }
    }

    /**
     * Wrapper function to the above two functions. Redirected to the multipleSteps version if the
     * boolean 'multipleSteps'
     * is true, otherwise to the single_step version.
     * @param gameBoard The Board object that the ChessPiece is currently on. Is not modified.
     * @param possibleMoves A list of int[] objects to add to. Is modified.
     * @param direction A direction vector indicating the step to take. Is not modified.
     * @param multipleSteps boolean indicating whether to try multiple steps in the specified direction or take a
     *                      single step in the direction
     */
    protected void possibleMovesHelper(@NotNull Board gameBoard, @NotNull List<int[]> possibleMoves,
                                       @NotNull int[] direction, boolean multipleSteps) {
        if (multipleSteps) {
            possibleMovesHelper_multipleSteps(gameBoard, possibleMoves, direction);
        }
        else {
            possibleMovesHelper_singleStep(gameBoard, possibleMoves, direction);
        }
    }

    /**
     * Utility function that increments the 'next_pos' array, by the corresponding element in the
     * 'incrementVector' array
     * If the lengths of the two arrays is not equal, the increments are made to either all the dimensions
     * that an increment has been specified for,
     * or can be made in overall (min(length(next_pos), length(incrementVector))).
     * @param next_pos The array to increment. Is modified.
     * @param incrementVector The increments to make in each dimension. Is not modified.
     */
    public static void incrementInDirection(@NotNull int[] next_pos, @NotNull int[] incrementVector) {
        for (int i = 0; i < min(next_pos.length, incrementVector.length); i++) {
            next_pos[i] += incrementVector[i];
        }
    }

    /**
     * Utility function that wraps around the above 'incrementInDirection' function. Used when
     * the original 'pos' vector is not
     * to be modified.
     * @param pos The starting array to which the increment is to be made. Is not modified.
     * @param incrementVector The increments to make in each dimension. Is not modified.
     * @return An array which is a copy of the 'pos' array incremented in the 'incrementVector' direction.
     */
    public static int[] incrementedInDirection(@NotNull int[] pos, @NotNull int[] incrementVector) {
        int[] copy = Arrays.copyOf(pos, pos.length);
        incrementInDirection(copy, incrementVector);
        return copy;
    }

    /**
     * Utility function to set the first two elements of an int[].
     * @param vector The integer array to modify. Is modified.
     * @param dim1 The integer to set vector[0] to.
     * @param dim2 The integer to set vector[1] to.
     * Returns without effect if the size of the vector is less than 2.
     */
    public static void setFirstTwoDims(@NotNull int[] vector, int dim1, int dim2) {
        if (vector.length < 2) {
            return;
        }
        vector[0] = dim1;
        vector[1] = dim2;
    }

    /**
     * Utility function which checks whether a piece can be moved to a certain point (in one step). Doesn't check for
     * move legality (will return true even if the move leads to a check for the player in the next step).
     * @param gameBoard The Board object that the ChessPiece is currently on. Is not modified.
     * @param pos The position to move the ChessPiece object to on the
     * @return boolean indicating whether or not the ChessPiece has the ability to move to a 'pos' on the Board.
     */
    public boolean canMoveToPos(@NotNull Board gameBoard, int[] pos) {

        List<int[]> possibleMoves = this.getPossibleMoves(gameBoard);
        for (int i = 0; i < possibleMoves.size(); i++) {
            if (Arrays.equals(possibleMoves.get(i), pos)) {
                return true;
            }
        }
        return false;

    }


}
