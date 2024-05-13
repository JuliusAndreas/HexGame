package com.julius.hexgame.util;

public class GameLogic {
    private short numRows;
    private short numCols;

    // True for player 1, False for player 2

    private final boolean playerOne = true;
    private final boolean playerTwo = false;
    private boolean turn = playerOne;
    private boolean chosenState = false;
    /*
        1 for empty cell
        2 for player one
        3 for player two
        4 for chosen cell
     */
    private byte[][] board;
    private byte chosenRow = -1;
    private byte chosenCol = -1;

    public GameLogic(int numRows, int numCols) {
        this.numRows = (short) numRows;
        this.numCols = (short) numCols;
    }

    public GameLogic() {
    }

    public void setRowsAndCols(int rows, int cols) {
        this.numRows = (short) rows;
        this.numCols = (short) cols;
        this.board = new byte[numRows][numCols];
        fillEmptyBoard();
    }

    private void fillEmptyBoard() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (i == 0 && j == numCols / 2) {
                    board[i][j] = 3;
                    continue;
                }
                if (i == numRows - 1 && j == numCols / 2) {
                    board[i][j] = 2;
                    continue;
                }
                board[i][j] = 1;
            }
        }
    }

    public int getHexState(int row, int col) {
        if (chosenState && chosenRow == row && chosenCol == col) return 4;
        return board[row][col];
    }

    public void act(int row, int col) {
        if (!chosenState) {
            if (!validCellChoice(row, col)) return;
            switchToChosenState(row, col);
        } else {
            // simply change the state of the cell for now
            if (turn == playerOne) {
                if (chosenCellIsSelected(row, col)) {
                    SwitchBackFromChosenState();
                    return;
                }
                board[row][col] = 2;
                SwitchBackFromChosenState();
                switchTurn();
                return;
            }
            if (turn == playerTwo) {
                if (chosenCellIsSelected(row, col)) {
                    SwitchBackFromChosenState();
                    return;
                }
                board[row][col] = 3;
                SwitchBackFromChosenState();
                switchTurn();
                return;
            }
        }
    }

    private boolean chosenCellIsSelected(int row, int col) {
        return row == chosenRow && col == chosenCol;
    }

    private void SwitchBackFromChosenState() {
        chosenState = false;
        chosenRow = -1;
        chosenCol = -1;
    }

    private void switchToChosenState(int row, int col) {
        chosenState = true;
        chosenRow = (byte) row;
        chosenCol = (byte) col;
    }

    private boolean validCellChoice(int row, int col) {
        if (board[row][col] == 1 || board[row][col] == 4) return false;
        if ((turn == playerOne && board[row][col] == 3)
                || (turn == playerTwo && board[row][col] == 2)) return false;

        return true;
    }

    public boolean getTurn() {
        return turn;
    }

    public void switchTurn() {
        this.turn = !turn;
    }

    public short getNumRows() {
        return numRows;
    }

    public short getNumCols() {
        return numCols;
    }

    public boolean isChosenState() {
        return chosenState;
    }

    public boolean getPlayerOne() {
        return playerOne;
    }

    public boolean getPlayerTwo() {
        return playerTwo;
    }
}
