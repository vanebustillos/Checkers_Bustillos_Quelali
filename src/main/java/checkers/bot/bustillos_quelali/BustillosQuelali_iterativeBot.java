package checkers.bot.bustillos_quelali;

import checkers.CheckersBoard;
import checkers.CheckersMove;
import checkers.CheckersPlayer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class BustillosQuelali_iterativeBot implements CheckersPlayer {

    //static CheckersBoard.Player myPlayer;
    public List<CheckersMove> getSuccessors(CheckersBoard board) {
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
        CheckersBoard.Player myPlayer = board.getCurrentPlayer();
        LinkedList<CheckersBoard> nodesToExpand = new LinkedList<>();
        Map<CheckersBoard, Integer> strategy = new HashMap<>();
        nodesToExpand.add(board);
        while (!nodesToExpand.isEmpty()) {
            int utility = 0;
            CheckersBoard nodeToExpand = nodesToExpand.removeLast();
            List<CheckersMove> nodeSuccessors = getSuccessors(nodeToExpand);
            Map<CheckersMove, Integer> moveScores = new HashMap<>();
            if (nodeSuccessors.isEmpty()) {
                utility = getUtility(nodeToExpand, myPlayer);
                moveScores.put(nodeSuccessors.get(0), utility);
            }
            for (CheckersMove node : nodeSuccessors) {
                utility = getUtility(nodeToExpand, myPlayer);
                moveScores.put(node, 0);
            }
            if (nodeToExpand.getCurrentPlayer() == myPlayer) {
                //int max
            }

        }
        return null;
    }



    public Integer getUtility(CheckersBoard board, CheckersBoard.Player myPlayer) {
        int numberOfPiecesMyPlayer = board.countPiecesOfPlayer(myPlayer);
        int numberOfPiecesOtherPlayer = board.countPiecesOfPlayer(getOtherPlayer(myPlayer));
        return numberOfPiecesMyPlayer - numberOfPiecesOtherPlayer;
    }
    public CheckersBoard.Player getOtherPlayer(CheckersBoard.Player myPlayer) {
        if (myPlayer == CheckersBoard.Player.BLACK) {
            return CheckersBoard.Player.RED;
        } else {
            return CheckersBoard.Player.BLACK;
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
}

