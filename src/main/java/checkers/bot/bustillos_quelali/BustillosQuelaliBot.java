package checkers.bot.bustillos_quelali;

import checkers.CheckersBoard;
import checkers.CheckersMove;
import checkers.CheckersPlayer;
import checkers.exception.BadMoveException;

import java.util.*;
import java.util.stream.Stream;

public class BustillosQuelaliBot extends CheckersBoard implements CheckersPlayer {

    Player currentPlayer = Player.BLACK;
    //Set<BestAction> strategy = new HashSet<BestAction>();
    Map<CheckersMove,Integer> strategy = new HashMap<>();


    private class BestAction {
        CheckersMove move;
        int punctuation;

        public BestAction(CheckersMove bestMove, int bestPunctuation) {
            move = bestMove;
            punctuation = bestPunctuation;
        }
    }

    public boolean checkTerminalState(CheckersBoard board) {
        List<CheckersMove> possibleCaptures = board.possibleCaptures();
        if (board.countPiecesOfPlayer(Player.BLACK) == 0 || board.countPiecesOfPlayer(Player.RED) == 0) {
            return true;
        }
        if (!board.isMovePossible() && !board.isCapturePossible()) { //current player
            board.switchTurn();
            if (!board.isMovePossible() && !board.isCapturePossible()) { // opponent player
                board.switchTurn();
                return true;//if is a tie.
            }
        }
        return false;
    }

    public Map<CheckersBoard,CheckersMove> successors(CheckersBoard board) {
        //List<CheckersBoard> children = new ArrayList<>();
        Map<CheckersBoard, CheckersMove> checkers = new HashMap<>();

        if (checkTerminalState(board)) {
            return checkers;
        }
        CheckersBoard child = board.clone();
        List<CheckersMove> possibleCaptures = child.possibleCaptures();
        List<CheckersMove> possibleMoves = child.possibleMoves();

        for (CheckersMove capture : possibleCaptures) {
            try {
                child.processMove(capture);
                //children.add(child);
                checkers.put(child,capture);
            } catch (BadMoveException ex) {
                System.err.println(ex.getMessage());
            }
        }
        for (CheckersMove move : possibleMoves) {
            try {
                child.processMove(move);
                //children.add(child);
                checkers.put(child,move);
            } catch (BadMoveException ex) {
                System.err.println(ex.getMessage());
            }
        }
        //return children;
        return checkers;
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
        Map<CheckersBoard, CheckersMove> successors = successors(board);
        if (successors.isEmpty() || depth == 30) {
            getUtility(board);
        }
        return getBestAction(board,depth);
    }

    public BestAction getBestAction(CheckersBoard board, int depth){
        Map<CheckersBoard, CheckersMove> successors = successors(board);
        int currentPunctuation;
        CheckersMove currentMove = null;

        if(board.getCurrentPlayer() == currentPlayer){
            currentPunctuation = Integer.MIN_VALUE;
        } else{
            currentPunctuation = Integer.MAX_VALUE;
        }

        for (CheckersBoard successor: successors.keySet()) {
               BestAction bestAction = miniMax(successor, depth + 1);
               strategy.put(bestAction.move, bestAction.punctuation);

               if(board.getCurrentPlayer() == currentPlayer){
                   if(bestAction.punctuation > currentPunctuation){
                       currentPunctuation = bestAction.punctuation;
                       currentMove = bestAction.move;
                       strategy.put(currentMove,currentPunctuation);
                       successors.put(successor, currentMove);
                   }
               } else {
                   if(bestAction.punctuation < currentPunctuation){
                       currentPunctuation = bestAction.punctuation;
                       currentMove = bestAction.move;
                       strategy.put(currentMove,currentPunctuation);
                       successors.put(successor, currentMove);
                   }
               }
        }
        return new BestAction(currentMove,currentPunctuation);
    }


    @Override
    public CheckersMove play(CheckersBoard board) {
        return null;
    }

}
