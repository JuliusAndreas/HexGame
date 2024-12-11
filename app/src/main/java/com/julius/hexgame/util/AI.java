package com.julius.hexgame.util;

import android.util.Pair;

import com.julius.hexgame.view.HexBoard;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AI {
    private final HexBoard ui;
    private final int numCols;
    private final int numRows;
    private static final int MAX_DEPTH = 3; // Maximum depth

    public AI(HexBoard hexBoard) {
        this.ui = hexBoard;
        this.numRows = hexBoard.getNumRows();
        this.numCols = hexBoard.getNumColumns();
    }

    public void think(byte[][] board) {
        Move bestMove = findBestMove(board);
        if (bestMove != null) {
            ui.applyAIAction(bestMove.source.first, bestMove.source.second,
                    bestMove.destination.first, bestMove.destination.second);
        }
    }

    private Move findBestMove(byte[][] board) {
        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;
        List<Move> possibleMoves = getAllPossibleMoves(board, true);

        for (Move move : possibleMoves) {
            byte[][] newBoard = makeMove(board, move, true);
            int moveValue = minimax(newBoard, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            if (moveValue > bestValue) {
                bestMove = move;
                bestValue = moveValue;
            }
        }
        return bestMove;
    }

    private int minimax(byte[][] board, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        if (depth == 0 || isGameOver(board)) {
            return evaluateBoard(board);
        }

        List<Move> possibleMoves = getAllPossibleMoves(board, isMaximizingPlayer);

        if (isMaximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : possibleMoves) {
                byte[][] newBoard = makeMove(board, move, true);
                int eval = minimax(newBoard, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : possibleMoves) {
                byte[][] newBoard = makeMove(board, move, false);
                int eval = minimax(newBoard, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return minEval;
        }
    }

    private List<Move> getAllPossibleMoves(byte[][] board, boolean isMaximizingPlayerTurn) {
        // Implement logic to generate all possible moves (both replication and jump moves)
        byte playerCode = (byte) (isMaximizingPlayerTurn ? 3 : 2);
        List<Move> possibleMoves = new LinkedList<>();
        Set<Pair<Integer, Integer>> neighbors;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (board[i][j] != playerCode) continue;
                neighbors = neighborsOf(i, j);
                addPossibleReplicationMoves(board, possibleMoves, Pair.create(i, j), neighbors);
                addPossibleJumpMoves(board, possibleMoves, Pair.create(i, j), neighbors);
            }
        }
        return possibleMoves;
    }

    private void addPossibleReplicationMoves(byte[][] board, List<Move> possibleMoves,
                                             Pair<Integer, Integer> source,
                                             Set<Pair<Integer, Integer>> neighbors) {
        for (Pair<Integer, Integer> neighbor : neighbors) {
            if (board[neighbor.first][neighbor.second] == 1) {
                possibleMoves.add(new Move(source, neighbor));
            }
        }
    }

    private void addPossibleJumpMoves(byte[][] board, List<Move> possibleMoves,
                                      Pair<Integer, Integer> source,
                                      Set<Pair<Integer, Integer>> neighbors) {
        Set<Pair<Integer, Integer>> neighborsOfTheNeighbor;
        for (Pair<Integer, Integer> neighbor : neighbors) {
            neighborsOfTheNeighbor = neighborsOf(neighbor.first, neighbor.second);
            neighborsOfTheNeighbor.removeIf(pair -> neighbors.contains(pair) || pair.equals(source));
            for (Pair<Integer, Integer> neighborOfTheNeighbor : neighborsOfTheNeighbor) {
                if (board[neighborOfTheNeighbor.first][neighborOfTheNeighbor.second] == 1) {
                    Move move = new Move(source, neighborOfTheNeighbor);
                    if (!possibleMoves.contains(move)) possibleMoves.add(move);
                }
            }
        }
    }

    private byte[][] makeMove(byte[][] board, Move move, boolean myTurn) {
        byte[][] newBoard = copyBoard(board);
        // Implement logic to make the move on the board (handle replication or jump)
        Set<Pair<Integer, Integer>> neighbors = neighborsOf(move.source.first, move.source.second);
        boolean isReplication = false;
        boolean isJump = false;
        if (validReplicationMove(newBoard, neighbors, move)) {
            isReplication = true;
        } else {
            if (validJumpMove(newBoard, neighbors, move)) {
                isJump = true;
            }
        }
        if (isReplication) {
            commenceReplication(newBoard, move, myTurn);
        } else if (isJump) {
            commenceJump(newBoard, move, myTurn);
        }
        return newBoard;
    }

    private byte[][] copyBoard(byte[][] board) {
        byte[][] newBoard = new byte[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, board[i].length);
        }
        return newBoard;
    }

    private boolean isGameOver(byte[][] board) {
        // Implement logic to determine if the game is over
        return boardIsFull(board) || noPossibleMovesAvailable(board) || noMorePawnsRemain(board);
    }

    private int evaluateBoard(byte[][] board) {
        // Implement logic to evaluate the board and return a score
        int heuristicValue = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (board[i][j] == 3) heuristicValue++;
            }
        }
        return heuristicValue;
    }

    private boolean validReplicationMove(byte[][] newBoard, Set<Pair<Integer, Integer>> neighbors, Move move) {
        int destRow = move.destination.first;
        int destCol = move.destination.second;
        return neighbors.contains(Pair.create(destRow, destCol)) && newBoard[destRow][destCol] == 1;
    }

    private boolean validJumpMove(byte[][] newBoard, Set<Pair<Integer, Integer>> neighbors, Move move) {
        int sourceRow = move.source.first;
        int sourceCol = move.source.second;
        int destRow = move.destination.first;
        int destCol = move.destination.second;
        for (Pair<Integer, Integer> neighbor : neighbors) {
            if (neighbor.first == sourceRow && neighbor.second == sourceCol) continue;
            if (neighborsOf(neighbor.first, neighbor.second).contains(Pair.create(destRow, destCol))
                    && (destRow != sourceRow || destCol != sourceCol)
                    && (newBoard[destRow][destCol] == 1)) {
                return true;
            }
        }
        return false;
    }

    private void commenceReplication(byte[][] newBoard, Move move, boolean myTurn) {
        int destRow = move.destination.first;
        int destCol = move.destination.second;
        for (Pair<Integer, Integer> cell : neighborsOf(destRow, destCol)) {
            replicateToCell(newBoard, cell, myTurn);
        }
        replicateToCoordinates(newBoard, destRow, destCol, myTurn);
    }

    private void commenceJump(byte[][] newBoard, Move move, boolean myTurn) {
        int sourceRow = move.source.first;
        int sourceCol = move.source.second;
        int destRow = move.destination.first;
        int destCol = move.destination.second;
        for (Pair<Integer, Integer> cell : neighborsOf(destRow, destCol)) {
            replicateToCell(newBoard, cell, myTurn);
        }
        replicateToCoordinates(newBoard, destRow, destCol, myTurn);
        jumpFromPreviousPosition(newBoard, sourceRow, sourceCol);
    }

    private Set<Pair<Integer, Integer>> neighborsOf(int row, int col) {
        Set<Pair<Integer, Integer>> neighbors = new HashSet<>(7);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (neighborShouldNotBeGenerated(i, j, col % 2 == 0)) continue;
                Pair<Integer, Integer> neighbor = Pair.create(row + i, col + j);
                if (validCell(neighbor) && (neighbor.first != row || neighbor.second != col)) {
                    neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    }

    private void replicateToCoordinates(byte[][] newBoard, int row, int col, boolean myTurn) {
        newBoard[row][col] = (byte) (myTurn ? 3 : 2);
    }

    private void replicateToCell(byte[][] newBoard, Pair<Integer, Integer> cell, boolean myTurn) {
        if (newBoard[cell.first][cell.second] == 2
                || newBoard[cell.first][cell.second] == 3) {
            newBoard[cell.first][cell.second] = (byte) (myTurn ? 3 : 2);
        }
    }

    private void jumpFromPreviousPosition(byte[][] newBoard, int sourceRow, int sourceCol) {
        newBoard[sourceRow][sourceCol] = 1;
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

    private boolean noMorePawnsRemain(byte[][] board) {
        int playerOnePossibleMoves = 0;
        int playerTwoPossibleMoves = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (board[i][j] == 2) {
                    playerOnePossibleMoves++;
                } else if (board[i][j] == 3) playerTwoPossibleMoves++;
                if (playerOnePossibleMoves > 0 && playerTwoPossibleMoves > 0) return false;
            }
        }
        return true;
    }

    private boolean noPossibleMovesAvailable(byte[][] board) {
        int playerOnePossibleMoves = 0;
        int playerTwoPossibleMoves = 0;
        Set<Pair<Integer, Integer>> neighbors;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (board[i][j] == 1 || board[i][j] == -1) continue;
                neighbors = neighborsOf(i, j);
                if (thereIsReplicationMoveAvailable(board, neighbors)
                        || thereIsJumpMoveAvailable(board, neighbors)) {
                    if (board[i][j] == 2) {
                        playerOnePossibleMoves++;
                    } else if (board[i][j] == 3) playerTwoPossibleMoves++;
                }
                if (playerOnePossibleMoves > 0 && playerTwoPossibleMoves > 0) return false;
            }
        }
        return true;
    }

    private boolean thereIsReplicationMoveAvailable(byte[][] board, Set<Pair<Integer, Integer>> neighbors) {
        for (Pair<Integer, Integer> neighbor : neighbors) {
            if (board[neighbor.first][neighbor.second] == 1) return true;
        }
        return false;
    }

    private boolean thereIsJumpMoveAvailable(byte[][] board, Set<Pair<Integer, Integer>> neighbors) {
        Set<Pair<Integer, Integer>> neighborsOfTheNeighbor;
        for (Pair<Integer, Integer> neighbor : neighbors) {
            neighborsOfTheNeighbor = neighborsOf(neighbor.first, neighbor.second);
            for (Pair<Integer, Integer> neighborOfTheNeighbor : neighborsOfTheNeighbor) {
                if (board[neighborOfTheNeighbor.first][neighborOfTheNeighbor.second] == 1)
                    return true;
            }
        }
        return false;
    }

    private boolean boardIsFull(byte[][] board) {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (board[i][j] == 1) return false;
            }
        }
        return true;
    }

    private class Move {
        private final Pair<Integer, Integer> source;
        private final Pair<Integer, Integer> destination;

        public Move(Pair<Integer, Integer> source, Pair<Integer, Integer> destination) {
            this.source = source;
            this.destination = destination;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Move move = (Move) o;

            if (!source.equals(move.source)) return false;
            return destination.equals(move.destination);
        }

        @Override
        public int hashCode() {
            int result = source.hashCode();
            result = 31 * result + destination.hashCode();
            return result;
        }
    }
}
