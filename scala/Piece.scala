
class Piece(difficulty: Int, columns: Int, rows: Int)
{
    val rng = new java.util.Random()
    var pieceArray = Array.fill(3) { (97 + rng.nextInt(difficulty + 1)).toChar }
    var startingCol = columns / 2
    var startingRow = rows

    /**
     * Shifts all the tiles of the piece down by one except the bottom tile which goes to the top.
     */
    def shiftDown(): Unit = pieceArray = pieceArray.drop(1) ++ pieceArray.take(1)
}
