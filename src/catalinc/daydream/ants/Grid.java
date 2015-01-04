package catalinc.daydream.ants;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class Grid {

    private int rows;
    private int cols;
    private boolean[][] cells;

    private List<Ant> ants;

    Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new boolean[rows][cols];
        this.ants = new LinkedList<Ant>();
    }

    int getRows() {
        return rows;
    }

    int getCols() {
        return cols;
    }

    boolean getCell(int row, int col) {
        return cells[row][col];
    }

    private void flipCell(int row, int col) {
        cells[row][col] = !cells[row][col];
    }

    class Ant {
        static final int NORTH = 0;
        static final int SOUTH = 1;
        static final int EAST = 2;
        static final int WEST = 3;

        private int row;
        private int col;
        private int heading;

        Ant(int row, int col, int heading) {
            this.row = row;
            this.col = col;
            this.heading = heading;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        void turnRight() {
            switch (heading) {
                case NORTH:
                    heading = WEST;
                    break;
                case SOUTH:
                    heading = EAST;
                    break;
                case EAST:
                    heading = NORTH;
                    break;
                case WEST:
                    heading = SOUTH;
                    break;
            }
        }

        void turnLeft() {
            switch (heading) {
                case NORTH:
                    heading = EAST;
                    break;
                case SOUTH:
                    heading = WEST;
                    break;
                case EAST:
                    heading = SOUTH;
                    break;
                case WEST:
                    heading = NORTH;
                    break;
            }
        }

        void moveForward() {
            switch (heading) {
                case NORTH:
                    if (row == 0) row = rows - 1;
                    else row--;
                    break;
                case SOUTH:
                    if (row == rows - 1) row = 0;
                    else row++;
                    break;
                case EAST:
                    if (col == 0) col = cols - 1;
                    else col--;
                    break;
                case WEST:
                    if (col == cols - 1) col = 0;
                    else col++;
                    break;
            }
        }
    }

    void spawnAnt() {
        Random random = new Random();
        int row = random.nextInt(rows);
        int col = random.nextInt(cols);
        int heading = random.nextInt(4);
        ants.add(new Ant(row, col, heading));
    }

    void moveAnts() {
        for (Ant ant : ants) {
            int r = ant.row;
            int c = ant.col;
            if (getCell(r, c)) {
                ant.turnLeft();
            } else {
                ant.turnRight();
            }
            flipCell(r, c);
            ant.moveForward();
        }
    }

    List<Ant> getAnts() {
        return ants;
    }
}
