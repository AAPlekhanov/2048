package game2048;


import java.util.Stack;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Model {

    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
    protected int score;
    protected int maxTile;

    private void saveState(Tile[][] tiles) {
        Tile[][] tiles2 = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {

                tiles2[i][j] = new Tile(tiles[i][j].value);

            }
        }
        previousStates.push(tiles2);
        int aaa = score;
        previousScores.push(aaa);
        isSaveNeeded = false;
    }

    public void rollback() {
        if (!previousScores.empty() && !previousStates.empty()) {
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }
    }

    private Stack<Tile[][]> previousStates;
    private Stack<Integer> previousScores;
    private boolean isSaveNeeded = true;

    public boolean hasBoardChanged() {
        Tile[][] tiles = previousStates.peek();
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (gameTiles[i][j].value != tiles[i][j].value) return true;
            }
        }
        return false;
    }

    public MoveEfficiency getMoveEfficiency(Move move) {
        MoveEfficiency moveEfficiency;
        move.move();
        if (hasBoardChanged()) moveEfficiency = new MoveEfficiency(getEmptyTiles().size(), score, move);
        else moveEfficiency = new MoveEfficiency(-1, 0, move);
        rollback();
        return moveEfficiency;
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    public boolean canMove() {
        if (!getEmptyTiles().isEmpty()) return true;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH - 1; j++) {
                if (gameTiles[i][j].value == gameTiles[i][j + 1].value) return true;
            }
        }
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH - 1; j++) {
                if (gameTiles[j][i].value == gameTiles[j + 1][i].value) return true;
            }
        }
        return false;
    }

    public void autoMove() {
        PriorityQueue<MoveEfficiency> priorityQueue = new PriorityQueue<>(4, Collections.reverseOrder());
        priorityQueue.offer(getMoveEfficiency(() -> left()));
        priorityQueue.offer(getMoveEfficiency(() -> right()));
        priorityQueue.offer(getMoveEfficiency(() -> up()));
        priorityQueue.offer(getMoveEfficiency(() -> down()));

        priorityQueue.peek().getMove().move();
    }

    Model() {
        score = 0;
        maxTile = 2;
        resetGameTiles();
        previousStates = new Stack<>();
        previousScores = new Stack<>();
    }

    private List<Tile> getEmptyTiles() {
        List<Tile> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (gameTiles[i][j].value == 0)
                    list.add(gameTiles[i][j]);
            }
        }
        return list;
    }

    private void addTile() {
        List<Tile> list = getEmptyTiles();
        if (list != null && list.size() != 0) {
            Tile tile = list.get((int) (list.size() * Math.random()));
            tile.value = (Math.random() < 0.9 ? 2 : 4);
        }
    }

    void resetGameTiles() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
    }

    private boolean compressTiles(Tile[] tiles) {
        boolean bool = false;
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                if (tiles[i].value == 0 && tiles[i + 1].value != 0) {
                    tiles[i].value = tiles[i + 1].value;
                    tiles[i + 1].value = 0;
                    bool = true;
                }
            }
        }
        return bool;
    }

    private boolean mergeTiles(Tile[] tiles) {
        boolean bool = false;
        for (int i = 0; i < 3; i++) {
            if (tiles[i].value != 0 && (tiles[i].value == tiles[i + 1].value)) {
                bool = true;
                tiles[i].value = tiles[i].value * 2;
                if (tiles[i].value > maxTile) {
                    maxTile = tiles[i].value;
                }
                tiles[i + 1].value = 0;
                score += tiles[i].value;
            }
        }
        return bool;
    }

    public void left() {
        if (isSaveNeeded) {
            saveState(gameTiles);
        }
        boolean bool = false;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            if (mergeTiles(gameTiles[i]) || compressTiles(gameTiles[i])) {
                bool = true;
            }
        }
        if (bool) {
            addTile();
            isSaveNeeded = true;
        }
    }

    private void rotate() {
        int len = FIELD_WIDTH;
        for (int k = 0; k < len / 2; k++) // border -> center
        {
            for (int j = k; j < len - 1 - k; j++) // left -> right
            {
                Tile tmp = gameTiles[k][j];
                gameTiles[k][j] = gameTiles[j][len - 1 - k];
                gameTiles[j][len - 1 - k] = gameTiles[len - 1 - k][len - 1 - j];
                gameTiles[len - 1 - k][len - 1 - j] = gameTiles[len - 1 - j][k];
                gameTiles[len - 1 - j][k] = tmp;
            }
        }
    }


    public void down() {
        saveState(gameTiles);

        rotate();
        rotate();
        rotate();
        left();
        rotate();
    }

    public void right() {
        saveState(gameTiles);

        rotate();
        rotate();
        left();
        rotate();
        rotate();
    }

    public void up() {
        saveState(gameTiles);
        rotate();
        left();
        rotate();
        rotate();
        rotate();
    }

    public void randomMove() {
        int n = ((int) (Math.random() * 100)) % 4;
        if (n == 0) up();
        if (n == 1) right();
        if (n == 2) down();
        if (n == 3) left();
    }
}
