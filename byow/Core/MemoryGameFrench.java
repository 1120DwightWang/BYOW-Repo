package byow.Core;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.Random;

public class MemoryGameFrench {
    /**
     * The characters we generate random Strings from.
     */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /**
     * Encouraging phrases. Used in the last section of the spec, 'Helpful UI'.
     */
    private static final String[] ENCOURAGEMENT = {"Tu peux le faire!", "Je crois en toi!"};
    /**
     * The width of the window of this game.
     */
    private final int width;
    /**
     * The height of the window of this game.
     */
    private final int height;
    /**
     * The Random object used to randomly generate Strings.
     */
    private final Random rand;
    /**
     * The current round the user is on.
     */
    private int round;
    /**
     * Whether or not the game is over.
     */
    private boolean gameOver;
    /**
     * Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'.
     */
    private boolean playerTurn;

    public MemoryGameFrench(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        this.rand = new Random(seed);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Veuillez entrer une graine");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGameFrench game = new MemoryGameFrench(40, 40, seed);
        game.startGame();
    }

    public String generateRandomString(int n) {
        String output = "";
        for (int i = 0; i < n; i++) {
            int random = RandomUtils.uniform(rand, 26);
            output = output + CHARACTERS[random];
        }
        return output;
    }

    public void drawFrame(String s) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.width / 2, this.height / 2, s);

        /* If the game is not over, display encouragement, and let the user know if they
         * should be typing their answer or watching for the next round. */
        if (!gameOver) {
            Font fontSmall = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(fontSmall);
            StdDraw.line(0, this.height - 2, this.width, this.height - 2);
            StdDraw.textLeft(0, this.height - 1, "Round: " + this.round);
            if (this.playerTurn) {
                StdDraw.text(this.width / 2, this.height - 1, "Taper!");
            } else {
                StdDraw.text(this.width / 2, this.height - 1, "Regardez!");
            }
            int randomEncouragement = RandomUtils.uniform(this.rand, ENCOURAGEMENT.length);
            StdDraw.textRight(this.width, this.height - 1, ENCOURAGEMENT[randomEncouragement]);
        }
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        for (int i = 0; i < letters.length(); i++) {
            drawFrame("" + letters.charAt(i));
            StdDraw.pause(1000);
            drawFrame("");
            StdDraw.pause(500);
        }
    }


    public String solicitNCharsInput(int n) {
        String output = "";
        for (int i = 0; i < n; ) {
            if (StdDraw.hasNextKeyTyped()) {
                char k = StdDraw.nextKeyTyped();
                output = output + k;
                drawFrame(output);
                i++;
            }
        }
        return output;
    }

    public void startGame() {
        new Play0("/Users/wanghaoran/Documents/cs61bl/su22-s77/proj3/byow/Core/memogame.mp3").start();
        this.round = 1;
        this.gameOver = false;

        while (!gameOver) {
            drawFrame("Rond: Vous devriez implémenter ce jeu!" + this.round);
            StdDraw.pause(1000);
            String word = generateRandomString(this.round);
            flashSequence(word);
            String user = solicitNCharsInput(this.round);
            if (user.equals(word)) {
                this.round++;
            } else {
                gameOver = true;
            }
        }

        this.drawFrame("Jeu terminé! Tu l'as fait pour arrondir rond: " + this.round);
    }


}

