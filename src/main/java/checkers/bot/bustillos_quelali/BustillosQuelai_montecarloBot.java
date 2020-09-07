package checkers.bot.bustillos_quelali;

import checkers.CheckersBoard;
import checkers.CheckersMove;
import checkers.CheckersPlayer;
import checkers.exception.BadMoveException;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class BustillosQuelai_montecarloBot implements CheckersPlayer {

    static CheckersBoard.Player myPlayer;

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

    public List<CheckersMove>  getSuccessors(CheckersBoard board) {
        List<CheckersMove> possibleCaptures = board.possibleCaptures();
        List<CheckersMove> possibleMoves = board.possibleMoves();
        if (!possibleCaptures.isEmpty()) {
            return possibleCaptures;
        } else {
          return possibleMoves;
        }
    }

    @Override
    public CheckersMove play(CheckersBoard board) {
        CheckersBoard root = board.clone();
        myPlayer = root.getCurrentPlayer();
        List<CheckersMove> successors = getSuccessors(root);
        Map<CheckersMove, Integer> moveScores = new HashMap<>();
        successors.forEach(move -> moveScores.put(move,0));
        for(CheckersMove childMove : successors){
            CheckersBoard randomBoard = root.clone();
            try{
                randomBoard.processMove(childMove);
            } catch (BadMoveException e){
                throw new IllegalStateException("Invalid move" +e.getMessage());
            }
            while(!checkTerminalState(randomBoard)){
                List<CheckersMove> nextMoves = getSuccessors(randomBoard);
                CheckersMove randomMove = nextMoves.get(ThreadLocalRandom.current().nextInt(nextMoves.size()));
                try{
                    randomBoard.processMove(randomMove);
                } catch (BadMoveException e){
                    throw new IllegalStateException("Invalid move" +e.getMessage());
                }
            }
            int currentChildScore = moveScores.get(childMove);
            moveScores.put(childMove,currentChildScore + getFinalScore(myPlayer,randomBoard));
        }
        return null;
        //return moveScores.entrySet().stream().max((move1,move2) -> Integer.compare(move1.getValue(), move2.getValue())).orElseThrow().getKey();
    }

    private int getFinalScore(CheckersBoard.Player myPlayer, CheckersBoard board){
        Optional<CheckersBoard.Player> loser = checkLoser(board);
        if(!loser.isPresent()){
            return 0;
        }
        if(loser.get().equals(myPlayer)){
            return -1;
        }
        return 1;
    }
    private Optional<CheckersBoard.Player> checkLoser(CheckersBoard board){
        int numMyPieces = board.countPiecesOfPlayer(board.getCurrentPlayer());
        if(numMyPieces == 0){
            return Optional.of(board.getCurrentPlayer());
        }
        if (!board.isMovePossible() && !board.isCapturePossible()) { //current player
            board.switchTurn();
            try {
                if (!board.isMovePossible() && !board.isCapturePossible()) { // opponent player
                    return Optional.empty();
                }
            } finally {
                board.switchTurn();
            }
        }
        return Optional.empty();
    }
}
