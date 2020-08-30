package checkers.bot.bustillos_quelali;

import checkers.CheckersBoard;
import checkers.CheckersMove;
import checkers.CheckersPlayer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BustillosQuelaliBot implements CheckersPlayer{

    public boolean checkTerminalState(CheckersBoard board) {
        List<CheckersMove> possibleCaptures = board.possibleCaptures();
        CheckersBoard.Player otherPlayer = board.otherPlayer(); //del oponente
        if (possibleCaptures.isEmpty()){
            return true;
        } else if(!board.isMovePossible() && !board.isCapturePossible()){ //current player
            board.switchTurn();
            if(!board.isMovePossible() && !board.isCapturePossible()){ // opponent player
                board.switchTurn();
                return true;//if the opponent doesn't have more pieces or if is a tie.
            }
        }
        return false;
    }

    @Override
    public CheckersMove play(CheckersBoard board) {
       return null;
    }

}
