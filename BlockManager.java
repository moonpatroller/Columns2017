/**
 * @Author: Manfred Hayes
 * @Date: 4/25/2017
 * @Assignment: Block Manager/Tester CS 251-004
 */
import java.util.LinkedList;
class BlockManager {

    public BlockManager(int rows, int columns, int difficulty){
        this.rows = rows;
        this.columns = columns;
        this.difficulty = difficulty;
        this.board = new char[rows][columns];
        this.currentPiece = new Piece(difficulty, columns, rows);
        this.nextPiece = new Piece(difficulty, columns, rows);
        fillBlankBoard();
    }

    public int rows = 0, columns = 0, score = 0, difficulty = 0;

    public boolean tooTall = false;

    char[][] board;

    Piece currentPiece, nextPiece;

    LinkedList<int[]> inARowDeletion = new LinkedList<>();

    /*
    Scans the three placed pieces of a column being dropped
    @entry param row: bottom row of the column
    @entry param column: column placement of the piece
     */
    public void findThreeScan(int row, int column){
        completeScan(row, column);
        completeScan(row + 1, column);
        completeScan(row + 2, column);
        if(inARowDeletion.size() > 0){
            clearInARow(inARowDeletion);
            readjustBoard();
        }
    }

    /*
    Scans all directions around a central piece
    @entry param row: central row being scanned
    @entry param column: central column being scanned
     */
    public void completeScan(int row, int column){
        scanMove(row, column, 1, 0);
        scanMove(row, column, 0, 1);
        scanMove(row, column, 1, -1);
        scanMove(row, column, 1, 1);
    }

    /*
    Scans the entire board for more in a row
     */
    public void boardScan(){
        for(int rowCount = 0; rowCount < rows; rowCount++) {
            for (int colCount = 0; colCount < columns; colCount++) {
                if(board[rowCount][colCount] != '-'){
                    completeScan(rowCount, colCount);
                }
            }
        }
        if(inARowDeletion.size() > 0){
            clearInARow(inARowDeletion);
            readjustBoard();
        }
    }
    /*
    Scans for a number in a specified direction
    @entry param row: central row
    @entry param col: central col
    @entry param rowMove: the direction of row movement
    @entry param colMove: the direction of column movement
     */
    public void scanMove(int row, int col, int rowMove, int colMove) {
        int numInRow = 0;
        int trackingRow = 0, trackingCol = 0;
        for (int i = -3; i < 3; i++) {
            if(inBounds(row + i * rowMove, col + i * colMove)) {
                if(board[row + i * rowMove][col +  i * colMove] == board[row][col]) {
                    if (numInRow == 0) {
                        trackingCol = col + i * colMove;
                        trackingRow = row + i * rowMove;
                        numInRow++;
                    } else if (numInRow > 0) {
                        numInRow++;
                    }
                }else if(numInRow >= 3){
                    for(int j = 0; j < numInRow; j++){
                        int[] holdingArray = new int[2];
                        holdingArray[0] = trackingRow + j*rowMove;
                        holdingArray[1] = trackingCol + j*colMove;
                        inARowDeletion.add(holdingArray);
                    }
                }else{
                    numInRow = 0;
                }
            }
        }
    }

    /*
    Checks if the given coordinate is in the array bounds
    @entry param row: row coordinate
    @entry param col: col coordinate
     */
    public boolean inBounds(int row, int col){
        return !(row < 0 || col < 0 || row >= rows || col >= columns);
    }

    /*
    Creates and prints a string representation of the board state
     */
    @Override
    public String toString(){
        String printingLine = "";
        for(int rowCount = rows -1; rowCount >= 0; rowCount--){

            for(int colCount = 0; colCount < columns; colCount++){
                printingLine += board[rowCount][colCount];
            }
            printingLine += "\n";
        }
        return printingLine;
    }

    /*
    Fills the board with the blank char representation of '-'
     */
    public void fillBlankBoard(){
        for(int rowCount = 0; rowCount < rows; rowCount++){
            for(int colCount = 0; colCount < columns; colCount++){
                board[rowCount][colCount] = '-';
            }
        }
    }

    /*
    Clear all of the coordinates in the linked list with '-'
    @entry param listToDelete: list of coordinates required to be deleted
     */
    public void clearInARow(LinkedList<int[]> listToDelete){
        System.out.println(toString());
        int piecesDeleted = 0;
        while(listToDelete.size() > 0){
            int[] coordinateArray = listToDelete.getFirst();
            if(board[coordinateArray[0]][coordinateArray[1]] != '-'){
                score++;
                board[coordinateArray[0]][coordinateArray[1]] = '-';
                piecesDeleted++;
            }
            listToDelete.removeFirst();

        }
        System.out.println(toString());
    }

    /*
    Re-adjusts the board to comply with gravity
     */
    public void readjustBoard(){
        for(int rowCount = 0; rowCount < rows; rowCount++) {
            for(int colCount = 0; colCount < columns; colCount++) {
                if(board[rowCount][colCount] != '-' && inBounds(rowCount-1, colCount)&& board[rowCount-1][colCount] == '-'){
                    char droppingPiece = board[rowCount][colCount];
                    int dropPoint = rowCount, col = colCount;
                    while(inBounds(dropPoint - 1, col) && board[dropPoint - 1][col] == '-'){
                        board[dropPoint][col] = '-';
                        dropPoint -= 1;
                    }
                    board[dropPoint][col] = droppingPiece;
                }
            }
        }
        boardScan();
        System.out.println(toString());
    }

    /*
    Drops a column piece in the specified column
    @entry param piece: The column piece being dropped
    @exit param: NONE
     */
    public void hardDropPiece(Piece piece){
        int startRow = 0, currentCol = piece.startingCol;
        if(inBounds(piece.startingRow, currentCol)){
            board[piece.startingRow][currentCol] = '-';
            if(inBounds(piece.startingRow+1, currentCol)){
                board[piece.startingRow+1][currentCol] = '-';
                if(inBounds(piece.startingRow+2, currentCol)){
                    board[piece.startingRow+2][currentCol] = '-';
                }
            }
        }

        for(int dropDown = rows -1; dropDown >= 0; dropDown--){
            if(board[dropDown][currentCol] != '-'){
                startRow = dropDown+1;
                break;
            }
        }
        if(startRow >= rows-2){
            tooTall = true;
        }else{
            System.out.println(startRow);
            board[startRow][currentCol] = piece.pieceArray[0];
            board[startRow+1][currentCol] = piece.pieceArray[1];
            board[startRow+2][currentCol] = piece.pieceArray[2];
            currentPiece = nextPiece;
            nextPiece = new Piece(difficulty, columns, rows);
            toString();
        }
        boardScan();
        System.out.println(toString());
    }

    /*
    Drops the piece object down one row in on the board
    @entry param piece: Piece being dropped down by one
    @exit param: NONE
     */
    public void oneDropPiece(Piece piece){
        boolean hitSomething = false;
        if(piece.startingRow - 1 < 0 || board[piece.startingRow -1][piece.startingCol] != '-'){
            hitSomething = true;
        }
        if(hitSomething && piece.startingRow >= rows-2){
            tooTall = true;
        }else if(hitSomething){
            currentPiece = nextPiece;
            nextPiece = new Piece(difficulty, columns, rows);
            boardScan();
        }
        if(!hitSomething){
            if(inBounds(piece.startingRow -1, piece.startingCol)){
                board[piece.startingRow -1][piece.startingCol] = piece.pieceArray[0];
                if(inBounds(piece.startingRow, piece.startingCol)){
                    board[piece.startingRow][piece.startingCol] = piece.pieceArray[1];
                    if(inBounds(piece.startingRow +1, piece.startingCol)){
                        board[piece.startingRow +1][piece.startingCol] = piece.pieceArray[2];
                        if(inBounds(piece.startingRow +2, piece.startingCol)){
                            board[piece.startingRow +2][piece.startingCol] = '-';
                        }
                    }
                }
            }
            currentPiece.startingRow--;
        }
    }

    /*
    Moves the piece over by the column shift amount and cleans up the piece's being dropped previous position.
    @entry param columnsShift: the direction of the piece shift
    @exit param: NONE
     */
    public void blockCleanUp(int columnsShift){
        if(inBounds(currentPiece.startingRow,currentPiece.startingCol + columnsShift) &&
                board[currentPiece.startingRow][currentPiece.startingCol + columnsShift] == '-'){
            board[currentPiece.startingRow][currentPiece.startingCol] = '-';
            if(inBounds(currentPiece.startingRow +1, currentPiece.startingCol)) {
                board[currentPiece.startingRow + 1][currentPiece.startingCol] = '-';
                if (inBounds(currentPiece.startingRow + 2, currentPiece.startingCol)) {
                    board[currentPiece.startingRow+2][currentPiece.startingCol] = '-';
                }
            }
            currentPiece.startingCol = currentPiece.startingCol + columnsShift;
            board[currentPiece.startingRow][currentPiece.startingCol] = currentPiece.pieceArray[0];

            if(inBounds(currentPiece.startingRow +1, currentPiece.startingCol)) {
                board[currentPiece.startingRow + 1][currentPiece.startingCol] = currentPiece.pieceArray[1];
                if (inBounds(currentPiece.startingRow + 2, currentPiece.startingCol)) {
                    board[currentPiece.startingRow+2][currentPiece.startingCol] = currentPiece.pieceArray[2];
                }
            }
            if(board[currentPiece.startingRow - 1][currentPiece.startingCol] != '-'){
                boardScan();
            }
        }
    }

    /*
    Updates the gameState on the piece's tiles being shifted.
     */
    public void blockShiftCleanUp(){
        currentPiece.shiftDown();
        board[currentPiece.startingRow][currentPiece.startingCol] = currentPiece.pieceArray[0];
        board[currentPiece.startingRow + 1][currentPiece.startingCol] = currentPiece.pieceArray[1];
        board[currentPiece.startingRow+2][currentPiece.startingCol] = currentPiece.pieceArray[2];
    }

}
