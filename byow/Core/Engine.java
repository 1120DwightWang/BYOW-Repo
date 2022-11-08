package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Engine extends RoomWorld {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    private static final int MENU_WIDTH = 35;
    private static final int MENU_HEIGHT = 40;
    private static final boolean KEYBOARD = true;
    private static final boolean STRING = false;
    private final boolean ENGLISH = true;
    private final boolean French = false;
    TERenderer ter = new TERenderer();
    private boolean language = ENGLISH;
    private List<String> commandRecord;
    private boolean inputType = STRING;
    private boolean continueGame;
    private Avatar avatar;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        Play0 play0=new Play0("/Users/wanghaoran/Documents/cs61bl/su22-s77/proj3/byow/Core/mysoul.mp3");
        play0.start();
        if (commandRecord == null) {
            commandRecord = new ArrayList<>();
        }
        inputType = KEYBOARD;
        continueGame = true;
        KeyboardInputSource input = new KeyboardInputSource(ter, world, true);
        if (language) {
            drawMenu();
        } else {
            drawFrenchMenu();
        }
        char command = input.getNextKey();
        if (command == 'N') {
            StdDraw.clear(StdDraw.BLACK);
            long seed = getSeed(input);
            ter.initialize(WIDTH, HEIGHT);
            world = createWorld(seed, input);
            input = new KeyboardInputSource(ter, world, false);
            runGame(input);
        } else if (command == 'L') {
            String pastCommands = loadGame();
            world = interactWithInputString(pastCommands);
            inputType = KEYBOARD;
            ter.initialize(WIDTH, HEIGHT);
            input = new KeyboardInputSource(ter, world, true);
            runGame(input);
        } else if (command == 'Q') {
            System.exit(0);
        } else if (command == 'C') {
            commandRecord.add("C");
            if (language) {
                drawAvatarMenu();
            } else {
                drawFrenchAvatarMenu();
            }
            String result = "";
            int counter = 0;
            while (true) {
                char name = input.getNextKey();
                commandRecord.add(String.valueOf(name));
                if (name == '*') {
                    break;
                }
                result += name;
                StdDraw.setPenColor(Color.orange);
                StdDraw.text(counter + MENU_WIDTH / 2 - 2,
                        MENU_HEIGHT / 2 - 2, String.valueOf(name));
                StdDraw.show();
                counter += 1;
            }
            Tileset.AVATAR.setDescription(result);
            interactWithKeyboard();
        } else if (command == 'F') {
            if (language) {
                language = French;
            } else {
                language = ENGLISH;
            }
            interactWithKeyboard();
        } else {
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2,
                    "Please enter a valid command");
            for (int j = 0; j < 10000; j++) {
                StdDraw.show();
            }
            interactWithKeyboard();
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        inputType = STRING;
        if (commandRecord == null) {
            commandRecord = new ArrayList<>();
        }
        continueGame = true;
        InputSource inputString = new StringInputDevice(input);
        char command = inputString.getNextKey();
        if (command == 'N') {
            long seed = getSeed(inputString);
            world = createWorld(seed, inputString);
            runGame(inputString);
        } else if (command == 'L') {
            String pastCommands = loadGame();
            world = interactWithInputString(pastCommands);
            runGame(inputString);
        } else if (command == 'C') {
            drawAvatarMenu();
            String name = getName(inputString);
            Tileset.AVATAR.setDescription(name);
            String pastCommands = loadGame();
            String remainingCommands = pastCommands.substring(name.length() + 2);
            world = interactWithInputString(remainingCommands);
        } else {
            inputString = new StringInputDevice(input);
            long seed = getSeed(inputString);
            world = createWorld(seed, inputString);
            ter.initialize(WIDTH, HEIGHT);
            runGame(inputString);
            inputType = KEYBOARD;
            KeyboardInputSource input2 = new KeyboardInputSource(ter, world, true);
            runGame(input2);
        }
        return world;

    }

    public TETile[][] createWorld(long seed, InputSource inp) {
        world = roomWorld(seed);
        avatar = new Avatar(world, seed, ter, inputType, inp);
        avatar.placePlayer(seed);
        avatar.placeEncounter(seed);
        return world;
    }

    private long getSeed(InputSource input) {
        ter.initialize(WIDTH, HEIGHT);
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.WHITE);

        if (language) {
            StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 4 * 3 + 4,
                    "Please enter a seed and end with S");
        } else {
            StdDraw.text(MENU_WIDTH / 2 + 4, MENU_HEIGHT / 4 * 3 + 4,
                    "Veuillez entrer une graine et terminer par S.");
        }
        StdDraw.show();
        String result = "";
        int counter = 1;
        while (input.possibleNextInput()) {
            char next = input.getNextKey();
            commandRecord.add(String.valueOf(next));
            if (next == 'N') {
                continue;
            } else if (next == 'S') {
                break;
            } else {
                result += next;
                StdDraw.text(counter + MENU_WIDTH / 5,
                        MENU_HEIGHT / 4 * 3, String.valueOf(next));
                counter += 1;
                StdDraw.show();
            }
        }
        long seed = 0;
        for (int i = 0; i < result.length(); i++) {
            seed = seed * 10 + Character.getNumericValue(result.charAt(i));
        }
        return seed;
    }

    public String getName(InputSource input) {
        String result = "";
        while (input.possibleNextInput()) {
            char next = input.getNextKey();
            if (next == 'L') {
                continue;
            } else if (next == '*') {
                break;
            } else {
                result += next;
            }
        }
        return result;
    }

    public void runGame(InputSource input) {
        while (continueGame) {
            ter.renderFrame(world);
            if (input.possibleNextInput() && StdDraw.hasNextKeyTyped()) {
                char next = input.getNextKey();
                if (next == ':') {
                    if (input.possibleNextInput()) {
                        next = input.getNextKey();
                        if (next == 'Q') {
                            continueGame = false;
                            saveGame();
                        }
                    }
                }
                commandRecord.add(String.valueOf(next));
                moveAvatar(next);
                if (inputType == KEYBOARD) {
                    ter.renderFrame(world);
                }
            }
            if (!input.possibleNextInput()) {
                input = new KeyboardInputSource(ter, world, false);
            }
        }
        saveGame();
    }

    private void saveGame() {
        try {
            FileWriter writeFile = new FileWriter("load.txt");
            String commands = "";
            for (int i = 0; i < commandRecord.size(); i++) {
                commands += commandRecord.get(i);
            }
            writeFile.write(commands);
            writeFile.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing the file.");
        }
        if (inputType == KEYBOARD) {
            System.exit(0);
        }
    }


    private String loadGame() {
        File loadData = new File("load.txt");
        return Utils.readContentsAsString(loadData);
    }


    private void moveAvatar(char input) {
        switch (input) {
            case 'W':
                if (language) {
                    avatar.moveUp();
                } else {
                    avatar.moveUpFrench();
                }
                break;
            case 'A':
                if (language) {
                    avatar.moveLeft();
                } else {
                    avatar.moveLeftFrench();
                }
                break;
            case 'S':
                if (language) {
                    avatar.moveDown();
                } else {
                    avatar.moveDownFrench();
                }
                break;
            case 'D':
                if (language) {
                    avatar.moveRight();
                } else {
                    avatar.moveRightFrench();
                }
                break;
            default:
                avatar.doNothing();
                break;
        }

    }

    private void drawMenu() {
        StdDraw.setCanvasSize(MENU_WIDTH * 10, MENU_HEIGHT * 10);
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, MENU_WIDTH);
        StdDraw.setYscale(0, MENU_HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.WHITE);
        String title = "The Game";
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 4 * 3, title);
        font = new Font("Arial", Font.BOLD, 15);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.orange);
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2 + 2, "New Game (N)");
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2, "Load Game (L)");
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2 - 2, "Change Avatar (C)");
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2 - 4, "Change Language to French (F)");
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2 - 6, "Quit (Q)");
        StdDraw.show();
    }

    private void drawFrenchMenu() {
        StdDraw.setCanvasSize(MENU_WIDTH * 10, MENU_HEIGHT * 10);
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, MENU_WIDTH);
        StdDraw.setYscale(0, MENU_HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.WHITE);
        String title = "Le Jeu";
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 4 * 3, title);
        font = new Font("Arial", Font.BOLD, 15);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.orange);
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2 + 2, "Nouveau Jeu (N)");
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2, "Chargement Du Jeu (L)");
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2 - 2, "Changer D'avatar (C)");
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2 - 4, "Changer La Langue En Anglais (F)");
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2 - 6, "Quitter (Q)");
        StdDraw.show();
    }

    private void drawAvatarMenu() {
        ter.initialize(WIDTH, HEIGHT);
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, MENU_WIDTH);
        StdDraw.setYscale(0, MENU_HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.WHITE);
        String title = "Avatar Name Changing Menu";
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 4 * 3, title);
        font = new Font("Arial", Font.BOLD, 15);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2,
                "Please enter your avatar's name and enter * when finished.");
        StdDraw.show();
    }

    private void drawFrenchAvatarMenu() {
        ter.initialize(WIDTH, HEIGHT);
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, MENU_WIDTH);
        StdDraw.setYscale(0, MENU_HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.WHITE);
        String title = "Menu De Changement De Nom D'avatar";
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 4 * 3, title);
        font = new Font("Arial", Font.BOLD, 15);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2,
                "Veuillez saisir le nom de votre avatar et saisir * lorsque vous avez terminé.");
        StdDraw.show();
    }


}



