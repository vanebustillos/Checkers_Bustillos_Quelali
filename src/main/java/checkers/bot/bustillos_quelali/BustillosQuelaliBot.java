package checkers.bot.bustillos_quelali;

import checkers.CheckersBoard;
import checkers.CheckersMove;
import checkers.CheckersPlayer;
import checkers.exception.BadMoveException;

import java.util.*;

public class BustillosQuelaliBot extends CheckersBoard implements CheckersPlayer {

    Player currentPlayer = Player.RED;
    Map<CheckersMove,Integer> utilities = new HashMap<>();
    Map<CheckersBoard, CheckersMove> strategy = new HashMap<>();

    private class BestAction {
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

    public Map<CheckersBoard,CheckersMove> getSuccessors(CheckersBoard board) {
        Map<CheckersBoard,CheckersMove> successors = new HashMap<>();

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
                    System.err.println(ex.getMessage());
                }

            }
        } else {
            for (CheckersMove move : possibleMoves) {
                try {
                    CheckersBoard child = board.clone();
                    child.processMove(move);
                    successors.put(child, move);
                } catch (BadMoveException ex) {
                    System.err.println(ex.getMessage());
                }

            }
        }
        return successors;
    }

    public Integer getUtility( CheckersBoard board) {
        if (checkTerminalState(board)) {
            if (currentPlayer == board.getCurrentPlayer()) {
                return 1;
            } else {
                return -1;
            }
        }
        return 0;
    }
    public BestAction miniMax(CheckersBoard board, int depth) {
    //public BestAction miniMax(CheckersBoard board)  {
        Map<CheckersBoard, CheckersMove> successors = getSuccessors(board);
        if (successors.isEmpty() || depth == 4) {
        //if (successors.isEmpty()) { //if is terminal state
            getUtility(board);
        }
        return getBestAction(board,depth);
        //return getBestAction(board);
    }
    public BestAction getBestAction(CheckersBoard board, int depth){
    //public BestAction getBestAction(CheckersBoard board)  {
        Map<CheckersBoard, CheckersMove> successors = getSuccessors(board);
        int currentPunctuation;
        CheckersMove currentMove = null;

        if(board.getCurrentPlayer() == currentPlayer){
            currentPunctuation = Integer.MIN_VALUE;
        } else{
            currentPunctuation = Integer.MAX_VALUE;
        }

        //for (CheckersBoard successor: successors.keySet()) {
        for ( Map.Entry<CheckersBoard, CheckersMove> successor : successors.entrySet()) {

            BestAction bestAction = miniMax(successor.getKey(), depth + 1);
            //BestAction bestAction = miniMax(successor);
               //utilities.put(bestAction.move, bestAction.punctuation);

               if(board.getCurrentPlayer() == currentPlayer){
                   if(bestAction.punctuation > currentPunctuation){
                       currentPunctuation = bestAction.punctuation;
                       //currentMove = bestAction.move;
                       currentMove = successor.getValue();
                       //utilities.put(currentMove,currentPunctuation);
                       //strategy.put(successor, currentMove);
                   }
               } else {
                   if(bestAction.punctuation < currentPunctuation){
                       currentPunctuation = bestAction.punctuation;
                       //currentMove = bestAction.move;
                       currentMove = successor.getValue();
                       //utilities.put(currentMove,currentPunctuation);
                       //strategy.put(successor, currentMove);
                   }
               }
        }
        return new BestAction(currentMove,currentPunctuation);
    }


    @Override
    public CheckersMove play(CheckersBoard board) {
        return miniMax(board, 0).move; ////

    }

}
