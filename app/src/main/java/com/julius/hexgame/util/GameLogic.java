package com.julius.hexgame.util;

import android.util.Pair;

import java.util.HashSet;
import java.util.Set;

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
            if (chosenCellIsSelected(row, col)) {
                switchBackFromChosenState();
                return;
            }
            march(row, col);
        }
    }

    private void march(int row, int col) {
        Set<Pair<Integer, Integer>> neighbors;
        if ((neighbors = validReplicationMove(row, col)) == null) {
            return;
        }
        for (Pair<Integer, Integer> cell : neighborsOf(row, col)) {
            replicateToCell(cell);
        }
        replicateToCoordinates(row, col);
        switchBackFromChosenState();
        switchTurns();
    }

    private void replicateToCoordinates(int row, int col) {
        board[row][col] = (byte) (turn == playerOne ? 2 : 3);
    }

    private void replicateToCell(Pair<Integer, Integer> cell) {
        if (board[cell.first][cell.second] == 2
                || board[cell.first][cell.second] == 3) {
            board[cell.first][cell.second] = (byte) (turn == playerOne ? 2 : 3);
        }
    }

    private Set<Pair<Integer, Integer>> validReplicationMove(int row, int col) {
        Set<Pair<Integer, Integer>> neighbors = neighborsOf(chosenRow, chosenCol);
        if (neighbors.contains(Pair.create(row, col)) && board[row][col] == 1) {
            return neighbors;
        }
        return null;
    }

    private Set<Pair<Integer, Integer>> neighborsOf(int row, int col) {
        Set<Pair<Integer, Integer>> neighbors = new HashSet<>(7);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (neighborShouldNotBeGenerated(i, j, col % 2 == 0)) continue;
                Pair<Integer, Integer> neighbor = Pair.create(row + i, col + j);
                if (validCell(neighbor)) {
                    neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    }

    private boolean neighborShouldNotBeGenerated(int i, int j, boolean isEvenColumn) {
        if (isEvenColumn) {
            return (i == 1 && j == -1) || (i == 1 && j == 1);
        } else {
            return (i == -1 && j == -1) || (i == -1 && j == 1);
        }
    }

    private boolean validCell(Pair<Integer, Integer> cell) {
        return cell.first < numRows && cell.first >= 0
                && cell.second < numCols && cell.second >= 0;
    }

    private boolean chosenCellIsSelected(int row, int col) {
        return row == chosenRow && col == chosenCol;
    }

    private void switchBackFromChosenState() {
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

    public void switchTurns() {
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
