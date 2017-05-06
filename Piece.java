/**
 * Created by bucky on 4/29/2017.
 */
import java.util.Random;
public class Piece {
    char[] pieceArray = new char[3];
    public int startingCol = 0, startingRow = 0;

    public Piece(int difficulty, int columns, int rows){
        pieceArray = pieceGenerator(difficulty);
        startingCol = columns/2;
        startingRow = rows;
    }

    /*
    Generates a random piece
    @entry param difficulty: the range of potentially piece types
     */
    public char[] pieceGenerator(int difficulty){
        char[] piece = new char[3];
        Random rng = new Random();
        for(int i = 0; i < 3; i++){
            int charNum =97 + rng.nextInt(difficulty+1);
            char placementChar = (char) charNum;
            piece[i] = placementChar;
        }
        return piece;
    }

    /*
    Shifts all the tiles of the piece down by one except the bottom tile which goes to the top.
     */
    public void shiftDown(){
        char tempChar = pieceArray[0];
        pieceArray[0] = pieceArray[1];
        pieceArray[1] = pieceArray[2];
        pieceArray[2] = tempChar;
    }
}
