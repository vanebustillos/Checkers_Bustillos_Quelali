package checkers.bot.bustillos_quelali;

import checkers.CheckersBoard;
import checkers.CheckersMove;
import checkers.CheckersPlayer;
import checkers.KeyboardPlayer;
import checkers.exception.BadMoveException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class BustillosQuelaliBot extends CheckersBoard implements CheckersPlayer{

    public boolean checkTerminalState(CheckersBoard board) {
        List<CheckersMove> possibleCaptures = board.possibleCaptures();
        if(board.countPiecesOfPlayer(Player.BLACK) == 0 || board.countPiecesOfPlayer(Player.RED) == 0){
            return true;
        }
        if(!board.isMovePossible() && !board.isCapturePossible()){ //current player
            board.switchTurn();
            if(!board.isMovePossible() && !board.isCapturePossible()){ // opponent player
                board.switchTurn();
                return true;//if is a tie.
            }
        }
        return false;
    }
    public List<CheckersBoard> successors(CheckersBoard board){
        List<CheckersBoard> children = new ArrayList<>();
        if(checkTerminalState(board)){
            return children;
        }
        CheckersBoard child = board.clone();
        List<CheckersMove> possibleCaptures = child.possibleCaptures();
        List<CheckersMove> possibleMoves = child.possibleMoves();

            for (CheckersMove capture : possibleCaptures) {
                try {
                    child.processMove(capture);
                    children.add(child);
                } catch (BadMoveException ex) {
                    System.err.println(ex.getMessage());
                }
            }
            for(CheckersMove move: possibleMoves){
                try {
                    child.processMove(move);
                    children.add(child);
                } catch (BadMoveException ex) {
                    System.err.println(ex.getMessage());
                }
            }


        return children;
    }

    public Integer getUtility(Player player,CheckersBoard board){
        if (checkTerminalState(board)) {
            if (player == board.getCurrentPlayer()) {
                return 1;
            } else {
                return -1;
            }
        }
        return 0;
    }

    public Integer miniMax(Player player, CheckersBoard board){
        List<CheckersBoard> successors = successors(board);
        if(!successors.isEmpty()){
            getUtility(player,board);
        }
        Stream<Integer> utilities = successors.stream()//
                .map(successor -> miniMax(player, successor));
        if (board.getCurrentPlayer() == player) {
            return utilities.max(Integer::compareTo).orElseThrow();
        }
        return utilities.min(Integer::compareTo).orElseThrow();
    }

    @Override
    public CheckersMove play(CheckersBoard board) {
       return null;
    }

}
