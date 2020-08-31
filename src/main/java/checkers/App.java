/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package checkers;

import checkers.bot.bustillos_quelali.BustillosQuelaliBot;
import checkers.bot.gray.GrayRandomBot;

import java.util.Optional;

public class App {

    public static void main(String[] args) {

        CheckersBoard game = CheckersBoard.initBoard();
        //CheckersPlayer player1 = new KeyboardPlayer();
        CheckersPlayer player2 = new BustillosQuelaliBot();
        CheckersPlayer player1 = new GrayRandomBot();
        Optional<CheckersPlayer> loser = game.play(player1, player2);
        loser.ifPresent(//
                checkersPlayer -> System.out.println("LOSER! " + checkersPlayer.getClass().getName()));
    }
}
