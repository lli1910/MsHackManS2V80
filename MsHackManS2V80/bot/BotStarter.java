package bot;

import java.util.ArrayList;
import java.util.Random;

import move.Move;
import move.MoveType;
import player.CharacterType;
import player.Player;

/**
 * bot.BotStarter
 *
 * Magic happens here. You should edit this file, or more specifically
 * the doMove() method to make your bot do more than random moves.
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class BotStarter {

    private Random random;
    private int notRandom;

    private BotStarter() {
        this.random = new Random();
    }

    /**
     * Return a random character to play as
     * @return A random character
     */
    public CharacterType getCharacter() {
        CharacterType[] characters = CharacterType.values();
        return characters[this.random.nextInt(characters.length)];
    }

    /**
     * Does a move action. Edit this to make your bot smarter.
     * @param state The current state of the game
     * @return A Move object
     */
    public Move doMove(BotState state) {
       
       // this.notRandom = 0;
        Player me = state.getPlayers().get(state.getMyName());
        MoveType bestMoveType = state.getField().getBestMoveTypes();

        // Get random but valid move type
       //MoveType randomMoveType = state.getField().getValidMoveTypes().get(this.random.nextInt(validMoveTypes.size()));
        
        //-------------------------------------------Lily Debugs
       // System.out.println("Lily in BotStarter doMove Debugs: "+ validMoveTypes.get(validMoveTypes.size()-1)+"size" + validMoveTypes.size());
        if (bestMoveType == null ) {
            //return new Move(randomMoveType);
            // ---------------------------------------------          Lily Debugs
            System.out.println("Lily in BotStarter doMove debugs: no valid move, pass");
            return new Move(); // No valid moves, pass
        }
        
        if (me.getBombs() <= 0) {
            return new Move(bestMoveType); // No bombs available
        }
        
        int bombTicks = this.random.nextInt(4) + 2; // random number 2 - 5

        return new Move(bestMoveType, bombTicks); // Drop bomb if available
    }

    public static void main(String[] args) {
        //5/26/18 get snippet coordinates, make player go to it. 
        //if no snippets, random move. 
        //5/28/18 Where are the invalid moves coming from?
        //Why does it keep on going back and forth
        BotParser parser = new BotParser(new BotStarter());
        parser.run();
    }
}
