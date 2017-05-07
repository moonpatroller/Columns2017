import java.util.LinkedList

class BlockManager(val rows: Int, val columns: Int, difficulty: Int) {

    var board = Array.fill(rows, columns)('-')

    var currentPiece = new Piece(difficulty, columns, rows)
    var nextPiece = new Piece(difficulty, columns, rows)

    var score = 0

    var inARowDeletion = new LinkedList[Array[Int]]()

    var over = false

    def isOver(): Boolean = over

    def inBounds(row: Int, col: Int): Boolean = row >= 0 && col >= 0 && row < rows && col < columns

    /**
     * Updates the gameState on the piece's tiles being shifted.
     */
    def blockRotate(): Unit = {
        currentPiece.shiftDown()
        for (i <- 0 until 3 if currentPiece.startingRow + i < board.length) {
            board(currentPiece.startingRow + i)(currentPiece.startingCol) = currentPiece.pieceArray(i)
        }
    }

    /**
     * Moves the piece over by the column shift amount and cleans up the piece's being dropped previous position.
     * @entry param columnsShift: the direction of the piece shift
     * @exit param: NONE
     */
    def blockShiftSideways(columnsShift: Int): Unit = {
        // if next spot over is in bound and empty
        if (inBounds(currentPiece.startingRow, currentPiece.startingCol + columnsShift) &&
                board(currentPiece.startingRow)(currentPiece.startingCol + columnsShift) == '-') {
            // move over all 3 rows of piece that are in bounds
            for (i <- 0 until 3 if inBounds(currentPiece.startingRow + i, currentPiece.startingCol)) {
                board(currentPiece.startingRow + i)(currentPiece.startingCol) = '-'
                board(currentPiece.startingRow + i)(currentPiece.startingCol + columnsShift) = currentPiece.pieceArray(i)
            }
            currentPiece.startingCol = currentPiece.startingCol + columnsShift

            // check for completed chunks
            if (board(currentPiece.startingRow - 1)(currentPiece.startingCol) != '-') {
                boardScan()
            }
        }
    }

    /**
     * Scans the entire board for more in a row
     */
    def boardScan(): Unit = {
        val deletions = visitDown(visitor _) ++ visitRight(visitor _) ++ visitRightUp(visitor _) ++ visitRightDown(visitor _)

        for (ls <- deletions;
            (r, c) <- ls) {
            board(r)(c) = '-'
            score += 1
            println(r + ", " + c)
        }
        reAdjustBoard()
    }

    def visitor(r1: Int, c1: Int, r2: Int, c2: Int, r3: Int, c3: Int): Option[List[(Int, Int)]] = {
        if (board(r1)(c1) != '-' && board(r1)(c1) == board(r2)(c2) && board(r2)(c2) == board(r3)(c3)) Some(List((r1, c1), (r2, c2), (r3, c3))) else None
    }

    def visitDown(visitor: (Int, Int, Int, Int, Int, Int) => Option[List[(Int, Int)]]): IndexedSeq[List[(Int, Int)]] = {
        for {
            col <- 0 until columns
            row <- (rows - 1) to 2 by -1
            list <- visitor(row, col, row - 1, col, row - 2, col)
        }
        yield list
    }

    def visitRight(visitor: (Int, Int, Int, Int, Int, Int) => Option[List[(Int, Int)]]): IndexedSeq[List[(Int, Int)]] = {
        for {
            row <- 0 until rows
            col <- 0 until (columns - 2)
            list <- visitor(row, col, row, col + 1, row, col + 2)
        }
        yield list
    }

    def visitRightUp(visitor: (Int, Int, Int, Int, Int, Int) => Option[List[(Int, Int)]]): IndexedSeq[List[(Int, Int)]] = {
        for {
            row <- 0 until (rows - 2)
            col <- 0 until (columns - 2)
            list <- visitor(row, col, row + 1, col + 1, row + 2, col + 2)
        }
        yield list
    }

    def visitRightDown(visitor: (Int, Int, Int, Int, Int, Int) => Option[List[(Int, Int)]]): IndexedSeq[List[(Int, Int)]] = {
        for {
            row <- 2 until rows
            col <- 0 until (columns - 2)
            list <- visitor(row, col, row - 1, col + 1, row - 2, col + 2)
        }
        yield list
    }

    /**
     * Re-adjusts the board to comply with gravity
     */
    def reAdjustBoard() {
        var removedPieces = false
        for (col <- 0 until columns) {
            if (makeColumnFall(col)) {
                removedPieces = true
            }
        }
        if (removedPieces) {
            boardScan()
        }
    }

    def makeColumnFall(col: Int): Boolean = {
        var row = 0
        while (row < rows && board(row)(col) != '-') {
            row += 1
        }
        val firstEmptyRow = row
        println(s"makeColumnFall col ${col}, firstEmptyRow ${firstEmptyRow}")

        while (row < rows && board(row)(col) == '-') {
            row += 1
        }
        // at first floating piece row or above board
        val firstFloatingPieceRow = row
        println(s"makeColumnFall col ${col}, firstFloatingPieceRow ${firstFloatingPieceRow}")

        var removedPieces = false
        // move squares downwards
        while (row < rows && board(row)(col) != '-') {
            val heightAboveFirstFloatingRow = row - firstFloatingPieceRow
            board(firstEmptyRow + heightAboveFirstFloatingRow)(col) = board(firstFloatingPieceRow + heightAboveFirstFloatingRow)(col)
            board(firstFloatingPieceRow + heightAboveFirstFloatingRow)(col) = '-'
            row += 1
            removedPieces = true
        }
        println(toString())
        removedPieces
    }

    /**
     * Drops a column piece in the specified column
     * @entry param piece: The column piece being dropped
     * @exit param: NONE
     */
    def hardDropPiece(piece: Piece): Unit = {
        var startRow = 0
        var currentCol = piece.startingCol

        for (i <- 0 until 3 if inBounds(currentPiece.startingRow + i, currentPiece.startingCol)) {
            board(piece.startingRow + i)(piece.startingCol) = '-'
        }

        startRow = rows - ((rows - 1) to 0 by -1).takeWhile((dropDown: Int) => board(dropDown)(currentCol) == '-').length
        if (startRow < rows - 2) {
            println(startRow)
            board(startRow)(currentCol) = piece.pieceArray(0)
            board(startRow + 1)(currentCol) = piece.pieceArray(1)
            board(startRow + 2)(currentCol) = piece.pieceArray(2)
            currentPiece = nextPiece
            nextPiece = new Piece(difficulty, columns, rows)
        }
        boardScan()
        println(toString())
    }

    /**
     * Drops the piece object down one row in on the board
     * @entry param piece: Piece being dropped down by one
     * @exit param: NONE
     */
    def oneDropPiece(piece: Piece): Unit = {
        var hitSomething = false
        if (piece.startingRow - 1 < 0 || board(piece.startingRow - 1)(piece.startingCol) != '-') {
            hitSomething = true
            if (piece.startingRow >= rows - 1) {
                over = true
            }
        }

        if (hitSomething) {
            currentPiece = nextPiece
            nextPiece = new Piece(difficulty, columns, rows)
            boardScan()
        } else {
            if (inBounds(piece.startingRow - 1, piece.startingCol)) {


                for (i <- 0 until 3 if inBounds(currentPiece.startingRow - 1 + i, currentPiece.startingCol)) {
                    board(piece.startingRow - 1 + i)(piece.startingCol) = piece.pieceArray(i)
                }
                if (inBounds(currentPiece.startingRow + 2, currentPiece.startingCol)) {
                    board(piece.startingRow + 2)(piece.startingCol) = '-'
                }
            }
            currentPiece.startingRow -= 1
        }
    }

    override
    def toString(): String = {
        var printingLine = ""
        for (rowCount <- (rows -1) to 0 by -1) {

            for (colCount <- 0 until columns) {
                printingLine += board(rowCount)(colCount)
            }
            printingLine += "\n"
        }
        printingLine
    }
}
