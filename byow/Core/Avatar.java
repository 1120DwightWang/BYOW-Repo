package byow.Core;

import byow.InputDemo.InputSource;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;


public class Avatar extends RoomWorld {
    private final Random ran;
    private final TETile[][] world;
    private final TERenderer ter;
    private final boolean inputType;
    private final TETile trailTile;
    private final InputSource input;
    private int x;
    private int y;
    private int a;
    private int b;


    public Avatar(TETile[][] w, long s, TERenderer t, boolean i, InputSource inp) {
        world = w;
        ran = new Random(s);
        ter = t;
        inputType = i;
        trailTile = Tileset.AVATAR;
        input = inp;
    }

    public void moveLeft() {
        if (world[x - 1][y] == Tileset.WALL) {
            return;
        }
        Random random = new Random();
        if (world[x - 1][y] == Tileset.FLOWER) {
            byow.Core.MemoryGame newGame =
                    new byow.Core.MemoryGame(WIDTH, HEIGHT, random.nextInt());
            newGame.startGame();
            ter.initialize(WIDTH, HEIGHT);
        }
        world[x][y] = Tileset.FLOOR;
        this.x--;
        world[x][y] = Tileset.AVATAR;
    }

    public void moveLeftFrench() {
        if (world[x - 1][y] == Tileset.WALL) {
            return;
        }
        Random random = new Random();
        if (world[x - 1][y] == Tileset.FLOWER) {
            byow.Core.MemoryGameFrench newGame =
                    new byow.Core.MemoryGameFrench(WIDTH, HEIGHT, random.nextInt());
            newGame.startGame();
            ter.initialize(WIDTH, HEIGHT);
        }
        world[x][y] = Tileset.FLOOR;
        this.x--;
        world[x][y] = Tileset.AVATAR;
    }

    public void moveRight() {
        if (world[x + 1][y] == Tileset.WALL) {
            return;
        }
        Random random = new Random();
        if (world[x + 1][y] == Tileset.FLOWER) {
            byow.Core.MemoryGame newGame =
                    new byow.Core.MemoryGame(WIDTH, HEIGHT, random.nextInt());
            newGame.startGame();
            ter.initialize(WIDTH, HEIGHT);
        }
        world[x][y] = Tileset.FLOOR;
        this.x++;
        world[x][y] = Tileset.AVATAR;
        return;
    }

    public void moveRightFrench() {
        if (world[x + 1][y] == Tileset.WALL) {
            return;
        }
        Random random = new Random();
        if (world[x + 1][y] == Tileset.FLOWER) {
            byow.Core.MemoryGameFrench newGame =
                    new byow.Core.MemoryGameFrench(WIDTH, HEIGHT, random.nextInt());
            newGame.startGame();
            ter.initialize(WIDTH, HEIGHT);
        }
        world[x][y] = Tileset.FLOOR;
        this.x++;
        world[x][y] = Tileset.AVATAR;
        return;
    }

    public void moveUp() {
        if (world[x][y + 1] == Tileset.WALL) {
            return;
        }
        Random random = new Random();
        if (world[x][y + 1] == Tileset.FLOWER) {
            byow.Core.MemoryGame newGame =
                    new byow.Core.MemoryGame(WIDTH, HEIGHT, random.nextInt());
            newGame.startGame();
            ter.initialize(WIDTH, HEIGHT);
        }
        world[x][y] = Tileset.FLOOR;
        this.y++;
        world[x][y] = Tileset.AVATAR;
        return;
    }

    public void moveUpFrench() {
        if (world[x][y + 1] == Tileset.WALL) {
            return;
        }
        Random random = new Random();
        if (world[x][y + 1] == Tileset.FLOWER) {
            byow.Core.MemoryGameFrench newGame =
                    new byow.Core.MemoryGameFrench(WIDTH, HEIGHT, random.nextInt());
            newGame.startGame();
            ter.initialize(WIDTH, HEIGHT);
        }
        world[x][y] = Tileset.FLOOR;
        this.y++;
        world[x][y] = Tileset.AVATAR;
        return;
    }

    public void moveDown() {
        if (world[x][y - 1] == Tileset.WALL) {
            return;
        }
        Random random = new Random();
        if (world[x][y - 1] == Tileset.FLOWER) {
            byow.Core.MemoryGame newGame =
                    new byow.Core.MemoryGame(WIDTH, HEIGHT, random.nextInt());
            newGame.startGame();
            ter.initialize(WIDTH, HEIGHT);
        }
        world[x][y] = Tileset.FLOOR;
        this.y--;
        world[x][y] = Tileset.AVATAR;
    }
    public void moveDownFrench() {
        if (world[x][y - 1] == Tileset.WALL) {
            return;
        }
        Random random = new Random();
        if (world[x][y - 1] == Tileset.FLOWER) {
            byow.Core.MemoryGameFrench newGame =
                    new byow.Core.MemoryGameFrench(WIDTH, HEIGHT, random.nextInt());
            newGame.startGame();
            ter.initialize(WIDTH, HEIGHT);
        }
        world[x][y] = Tileset.FLOOR;
        this.y--;
        world[x][y] = Tileset.AVATAR;
    }

    public String doNothing() {
        return null;
    }

    public void placePlayer(long seed) {
        boolean setPlayer = true;
        Random random = new Random(seed);
        while (setPlayer) {
            x = random.nextInt(WIDTH / 3, 2 * WIDTH / 3);
            y = random.nextInt(HEIGHT / 3, 2 * WIDTH / 3);
            if (world[x][y].equals(Tileset.FLOOR)) {
                setPlayer = false;
                world[x][y] = Tileset.AVATAR;
            }
        }
    }


    public void placeEncounter(long seed) {
        Random random = new Random(seed);
        boolean setEncounter = true;
        while (setEncounter) {
            a = random.nextInt(WIDTH / 10, 9 * WIDTH / 10);
            b = random.nextInt(HEIGHT / 10, 9 * WIDTH / 10);
            if (world[a][b].equals(Tileset.FLOOR)) {
                setEncounter = false;
                world[a][b] = Tileset.FLOWER;
            }
        }
        setEncounter = true;
        while (setEncounter) {
            a = random.nextInt(WIDTH / 10, 9 * WIDTH / 10);
            b = random.nextInt(HEIGHT / 10, 9 * WIDTH / 10);
            if (world[a][b].equals(Tileset.FLOOR)) {
                setEncounter = false;
                world[a][b] = Tileset.FLOWER;
            }
        }
        setEncounter = true;
        while (setEncounter) {
            a = random.nextInt(WIDTH / 10, 9 * WIDTH / 10);
            b = random.nextInt(HEIGHT / 10, 9 * WIDTH / 10);
            if (world[a][b].equals(Tileset.FLOOR)) {
                setEncounter = false;
                world[a][b] = Tileset.FLOWER;
            }
        }
        setEncounter = true;
        while (setEncounter) {
            a = random.nextInt(WIDTH / 10, 9 * WIDTH / 10);
            b = random.nextInt(HEIGHT / 10, 9 * WIDTH / 10);
            if (world[a][b].equals(Tileset.FLOOR)) {
                setEncounter = false;
                world[a][b] = Tileset.FLOWER;
            }
        }
        setEncounter = true;
        while (setEncounter) {
            a = random.nextInt(WIDTH / 10, 9 * WIDTH / 10);
            b = random.nextInt(HEIGHT / 10, 9 * WIDTH / 10);
            if (world[a][b].equals(Tileset.FLOOR)) {
                setEncounter = false;
                world[a][b] = Tileset.FLOWER;
            }
        }
    }


}
