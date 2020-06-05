package com.appjackstudio.game2048;

import java.util.Random;

public class Game2048 {
    public static final int INITIAL_TILES = 2;
    public static final int BOARD_SIZE = 4;

    public enum Direction {UP, RIGHT, DOWN, LEFT}

    private final Random random = new Random();
    private final int[] board = new int[BOARD_SIZE * BOARD_SIZE];
    private int score = 0;

    public Game2048() {
        for (int i = 0; i < INITIAL_TILES; i++) {
            placeRandomTile();
        }
    }

    public boolean canPlace() {
        for (int n : board) {
            if (n == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Places a random tile on the board
     *
     * @return if the place succeeded
     */
    public boolean placeRandomTile() {
        if (!canPlace()) return false;

        int i = 0;
        do {
            i = random.nextInt(board.length);
        } while (board[i] != 0);

        int value = random.nextFloat() < .9 ? 2 : 4;
        board[i] = value;

        return true;
    }


    public int get(int x, int y) {
        return board[x + y * BOARD_SIZE];
    }

    private void set(int x, int y, int value) {
        board[x + y * BOARD_SIZE] = value;
    }

    private int flip(int n) {
        return BOARD_SIZE - n - 1;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private int get(Direction d, int x, int y) {
        if (x < 0 || x >= BOARD_SIZE || y < 0 || y > BOARD_SIZE)
            throw new IndexOutOfBoundsException();

        switch (d) {
            case UP:
                return get(flip(x), flip(y));
            case RIGHT:
                return get(y, flip(x));
            case DOWN:
                return get(x, y);
            case LEFT:
                return get(flip(y), x);
        }
        throw new IllegalArgumentException();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void set(Direction d, int x, int y, int value) {
        if (x < 0 || x >= BOARD_SIZE || y < 0 || y > BOARD_SIZE)
            throw new IndexOutOfBoundsException();

        switch (d) {
            case UP:
                set(flip(x), flip(y), value);
                return;
            case RIGHT:
                set(y, flip(x), value);
                return;
            case DOWN:
                set(x, y, value);
                return;
            case LEFT:
                set(flip(y), x, value);
                return;
        }
        throw new IllegalArgumentException();
    }

    public boolean slide(Direction d) {
        boolean flag = false;

        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE - 1; y++) {
                if (get(d, x, y) != 0 && get(d, x, y + 1) == 0) {
                    set(d, x, y + 1, get(d, x, y));
                    set(d, x, y, 0);
                    y = -1;
                    flag = true;
                }
            }
        }
        return flag;
    }

    public boolean slideAndMerge(Direction d) {
        boolean flag = slide(d);

        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = BOARD_SIZE - 2; y >= 0; y--) {
                if (get(d, x, y) != 0 && get(d, x, y) == get(d, x, y + 1)) {
                    int newValue = get(d, x, y) * 2;
                    set(d, x, y + 1, newValue);
                    set(d, x, y, 0);

                    y--;
                    score += newValue;
                    flag = true;
                }
            }
        }

        slide(d);

        return flag;
    }

    public boolean isGameOver(){
        if(canPlace()) return false;
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE - 1; y++) {
                if(get(Direction.DOWN, x, y) == get(Direction.DOWN, x, y + 1)) return false;
                if(get(Direction.RIGHT, x, y) == get(Direction.RIGHT, x, y + 1)) return false;
            }
        }
        return true;
    }

    public int getScore() {
        return score;
    }
}
