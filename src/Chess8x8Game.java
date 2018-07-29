import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.Scanner;

/**
 * Chess8x8Game -- Class to hold the driver function of a regular Chess game with an 8x8 board,
 * 2 players and 16 pieces for each player
 * @author sahil1105
 */
public class Chess8x8Game {

    /**
     * Driver function for the chess game
     * @param args Command line arguments
     */
    public static void main(String[] args) {


        ChessBoard8x8 gameBoard = new ChessBoard8x8(); //declare the Board
        gameBoard.initBoard(); //initialize it
        Scanner reader = new Scanner(System.in); //scanner object to read user input

        boolean player1 = true; //boolean variable to keep track of whose turn it is
        int game_state = gameBoard.isGameOver(player1);

        while (game_state == 0) { //go while the game is not over
            System.out.printf("Player %d's turn!", (player1? 1:2)); //Prompt the players whose turn it is
            System.out.println("");
            //print the board
            System.out.println(gameBoard); //Print the GameBoard (calls the toString function internally)

            //if in check, prompt it
            if (gameBoard.isInCheck(player1)) {
                System.out.println("You are in check");
            }

            //ask for a move
            //try to execute the move, if it is not a valid move, ask for another one
            boolean moveSuccessful = false;
            do {
                System.out.println("Please enter your next move"); //expecting (sourceIdx \n destIdx)
                int startIdx = reader.nextInt();
                int destIdx = reader.nextInt();
                System.out.println("You entered: " + startIdx + " -> " + destIdx);
                //check if the move is a legal one
                moveSuccessful = gameBoard.move(startIdx, destIdx, player1);
                //if not, prompt the user
                if (!moveSuccessful) {
                    System.out.println("Invalid Move!");
                }
            }
            while (!moveSuccessful);

            player1 = !player1; //switch turns
            game_state = gameBoard.isGameOver(player1); //update game state
        }

        if (game_state == 1) {
            System.out.println("GAME OVER! YOU LOSE!");
        }
        else {
            System.out.println("GAME OVER! TIE!");
        }


    }


}
