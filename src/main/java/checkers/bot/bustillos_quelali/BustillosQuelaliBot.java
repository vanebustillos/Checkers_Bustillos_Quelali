package checkers.bot.bustillos_quelali;

import checkers.CheckersBoard;
import checkers.CheckersMove;
import checkers.CheckersPlayer;
import checkers.exception.BadMoveException;

import java.util.*;

public class BustillosQuelaliBot implements CheckersPlayer {

    static CheckersBoard.Player myPlayer;

    private static class BestAction {
        CheckersMove move;
        int punctuation;

        public BestAction(CheckersMove bestMove, int bestPunctuation) {
            move = bestMove;
            punctuation = bestPunctuation;
        }
    }

    public boolean checkTerminalState(CheckersBoard board) {
        if (board.countPiecesOfPlayer(board.getCurrentPlayer()) == 0) {
            return true;
        }
        if (!board.isMovePossible() && !board.isCapturePossible()) { //current player
            board.switchTurn();
            try {
                if (!board.isMovePossible() && !board.isCapturePossible()) { // opponent player
                    return true;//if is a tie.
                }
            } finally {
                board.switchTurn();
            }
        }
        return false;
    }

    public Map<CheckersBoard, CheckersMove> getSuccessors(CheckersBoard board) {
        Map<CheckersBoard, CheckersMove> successors = new HashMap<>();

        if (checkTerminalState(board)) {
            return successors;
        }
        List<CheckersMove> possibleCaptures = board.possibleCaptures();
        List<CheckersMove> possibleMoves = board.possibleMoves();
        if (!possibleCaptures.isEmpty()) {
            for (CheckersMove capture : possibleCaptures) {
                try {
                    CheckersBoard child = board.clone();
                    child.processMove(capture);
                    successors.put(child, capture);
                } catch (BadMoveException ex) {
                    throw new IllegalStateException(ex.getMessage());
                }

            }
        } else {
            for (CheckersMove move : possibleMoves) {
                try {
                    CheckersBoard child = board.clone();
                    child.processMove(move);
                    successors.put(child, move);
                } catch (BadMoveException ex) {
                    throw new IllegalStateException(ex.getMessage());
                }

            }
        }
        return successors;
    }

    public Integer getUtility(CheckersBoard board) {
        int numberOfPiecesMyPlayer = board.countPiecesOfPlayer(myPlayer);
        int numberOfPiecesOtherPlayer = board.countPiecesOfPlayer(getOtherPlayer());
        return numberOfPiecesMyPlayer - numberOfPiecesOtherPlayer;
    }
    public CheckersBoard.Player getOtherPlayer() {
        if (myPlayer == CheckersBoard.Player.BLACK) {
            return CheckersBoard.Player.RED;
        } else {
            return CheckersBoard.Player.BLACK;
        }
    }
    public BestAction miniMax(CheckersBoard board, int depth) {
        Map<CheckersBoard, CheckersMove> successors = getSuccessors(board);
        CheckersMove bestMove = null;
        if (successors.size() == 1) {
            bestMove = successors.get(0);
        }
        if (checkTerminalState(board) || successors.isEmpty() || depth == 6 ) { //if is terminal state
            return new BestAction(bestMove, getUtility(board));
        }
        return getBestAction(board, depth);
    }

    public BestAction getBestAction(CheckersBoard board, int depth) {
        Map<CheckersBoard, CheckersMove> successors = getSuccessors(board);
        int currentPunctuation;
        CheckersMove currentMove = null;

        if (board.getCurrentPlayer() == myPlayer) {
            currentPunctuation = Integer.MIN_VALUE;
        } else {
            currentPunctuation = Integer.MAX_VALUE;
        }

        for (Map.Entry<CheckersBoard, CheckersMove> successor : successors.entrySet()) {
            BestAction bestAction = miniMax(successor.getKey(), depth + 1);

            if (board.getCurrentPlayer() == myPlayer) {
                if (bestAction.punctuation > currentPunctuation) {
                    currentPunctuation = bestAction.punctuation;
                    //currentMove = bestAction.move;
                    currentMove = successor.getValue();
                }
            } else {
                if (bestAction.punctuation < currentPunctuation) {
                    currentPunctuation = bestAction.punctuation;
                    //currentMove = bestAction.move;
                    currentMove = successor.getValue();
                }
            }
        }
        return new BestAction(currentMove, currentPunctuation);
    }

    @Override
    public CheckersMove play(CheckersBoard board) {
        myPlayer = board.getCurrentPlayer();
        return miniMax(board, 0).move;
    }
}
