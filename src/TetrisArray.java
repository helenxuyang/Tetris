import java.util.ArrayList;

public class TetrisArray {
    private Point[][] array= new Point[Grid.ROWS][Grid.COLS];
    private Grid grid;

    private boolean lasttetris;
    private boolean lasttspin= false;
    private boolean clearedLines;

    private int prevLines= 0;

    public static final int BELOW= 0;
    public static final int LEFT= 1;
    public static final int RIGHT= 2;

    public TetrisArray(Grid grdi) {
        grid= grdi;
        setAllEmpty();
    }

    public Point getPoint(int r, int c) {
        return array[r][c];
    }

    public Point[] getRow(int r) {
        return array[r];
    }

    public void setAllEmpty() {
        for (int i= 0; i < Grid.ROWS; i++ ) {
            for (int j= 0; j < Grid.COLS; j++ ) {
                array[i][j]= Point.EMPTY;
            }
        }
    }

    public void setPoint(int r, int c, Point p) {
        array[r][c]= p;
    }

    public boolean hasPointAt(int r, int c) {
        boolean hasPoint= false;
        for (Point p : Point.getSetMinoList()) {
            if (array[r][c] == p) {
                hasPoint= true;
            }
        }
        return hasPoint;
    }

    public boolean checkCollision(Tetromino mino, int direction) {
        boolean collides= false;
        for (int[] coord : mino.getPos()) {
            int row= coord[0];
            int col= coord[1];
            if (checkCollision(row, col, direction)) {
                collides= true;
            }
        }
        return collides;
    }

    public boolean checkCollision(int[][] coords, int direction) {
        boolean collides= false;
        for (int[] coord : coords) {
            int row= coord[0];
            int col= coord[1];
            if (checkCollision(row, col, direction)) {
                collides= true;
            }
        }
        return collides;
    }

    public boolean checkCollision(int r, int c, int direction) {
        boolean collides= false;
        Point pointToCheck= null;
        if (r != Grid.ROWS - 1 && direction == BELOW) {
            pointToCheck= array[r + 1][c];
        } else if (c != 0 && direction == LEFT) {
            pointToCheck= array[r][c - 1];
        } else if (c != Grid.COLS - 1 && direction == RIGHT) {
            pointToCheck= array[r][c + 1];
        }
        for (Point p : Point.getSetMinoList()) {
            if (pointToCheck == p) {
                collides= true;
            }
        }
        if (pointToCheck == Point.TRASH || pointToCheck == Point.BOMB) {
            collides= true;
        }
        return collides;
    }

    public boolean lineFilled(int r, int[][] pos) {
        boolean filled= true;
        if (array[r][0] != Point.TRASH && array[r][0] != Point.BOMB) {
            for (Point p : array[r]) {
                if (!p.isSetPoint()) {
                    filled= false;
                }
            }
        } else {
            filled= false;
            for (int[] i : pos) {
                // System.out.println(Arrays.toString(i));
                // System.out.println(array[i[0] + 1][i[1]]);
                if (array[i[0] + 1][i[1]] == Point.BOMB && i[0] + 1 == r) {
                    filled= true;
                }
            }
        }
        return filled;
    }

    public boolean isEmptyRow(int r) {
        boolean empty= true;
        for (Point p : array[r]) {
            if (!(p == Point.EMPTY) && !(p == Point.CURRMINO)) {
                empty= false;
            }
        }
        return empty;
    }

    public boolean isEmptyRow(Point[] row) {
        boolean empty= true;
        for (Point p : row) {
            if (!(p == Point.EMPTY) && !(p == Point.CURRMINO)) {
                empty= false;
            }
        }
        return empty;
    }

    public ArrayList<Point[]> getNotEmptyLines() {
        ArrayList<Point[]> lines= new ArrayList<>();
        for (int r= 0; r < Grid.ROWS; r++ ) {
            if (!isEmptyRow(r)) {
                lines.add(array[r]);
            }
        }
        return lines;
    }

    public ArrayList<Point[]> getFilledLines(int[][] pos) {
        ArrayList<Point[]> filledLines= new ArrayList<>();
        int lines= 0;
        clearedLines= false;
        for (int r= 0; r < Grid.ROWS; r++ ) {
            if (lineFilled(r, pos)) {
                filledLines.add(array[r]);
                lines++ ;
                clearedLines= true;
            }
        }
        if (checkPerfClear()) {
            prevLines= 20;
            grid.incrementLinesCleared(10);
        }
        if (lasttetris && lines == 4) {
            grid.addScore(5);
            grid.setWrittenString("Back-to-Back \nTetris");
            grid.incrementLinesCleared(4);
            prevLines= 12;
            if (grid.getGamePlayers() == 2) {
                grid.doBarLines(6);
            }
            return filledLines;
        }
        if (grid.getTspin() && lines > 0) {
            if (lasttspin) {
                grid.addScore(lines + 8);
                if (lines == 1) {
                    grid.setWrittenString("Back-to-Back \nT-Spin Single");
                    grid.incrementLinesCleared(1);
                    prevLines= 12;
                    if (grid.getGamePlayers() == 2) {
                        grid.doBarLines(3);
                    }
                } else if (lines == 2) {
                    prevLines= 18;
                    grid.setWrittenString("Back-to-Back \nT-Spin Double");
                    if (grid.getGamePlayers() == 2) {
                        grid.doBarLines(6);
                    }
                    grid.incrementLinesCleared(2);
                } else if (lines == 3) {
                    prevLines= 24;
                    grid.setWrittenString("Back-to-Back \nT-Spin Triple");
                    if (grid.getGamePlayers() == 2) {
                        grid.doBarLines(9);
                    }
                    grid.incrementLinesCleared(3);
                }
            } else {
                if (lines == 1) {
                    grid.setWrittenString("T-Spin Single");
                    if (grid.getGamePlayers() == 2) {
                        grid.doBarLines(2);
                    }
                    prevLines= 8;
                    grid.incrementLinesCleared(1);
                } else if (lines == 2) {
                    grid.setWrittenString("T-Spin Double");
                    if (grid.getGamePlayers() == 2) {
                        grid.doBarLines(4);
                    }
                    prevLines= 12;
                    grid.incrementLinesCleared(2);
                } else if (lines == 3) {
                    grid.setWrittenString("T-Spin Triple");
                    if (grid.getGamePlayers() == 2) {
                        grid.doBarLines(6);
                    }
                    prevLines= 16;
                    grid.incrementLinesCleared(3);
                }
                grid.addScore(lines + 5);
            }
            lasttspin= true;
            return filledLines;
        } else if (lines != 0) {
            lasttspin= false;
        }
        if (lines == 4) {
            lasttetris= true;
        } else if (lines != 0) {
            lasttetris= false;
        }
        if (lines == 1) {
            grid.setWrittenString("Single");
            grid.incrementLinesCleared(1);
            // doesn't send lines
            prevLines= 1;
        } else if (lines == 2) {
            grid.setWrittenString("Double");
            if (grid.getGamePlayers() == 2) {
                grid.doBarLines(1);
            }
            prevLines= 3;
            grid.incrementLinesCleared(2);
        } else if (lines == 3) {
            grid.setWrittenString("Triple");
            if (grid.getGamePlayers() == 2) {
                grid.doBarLines(2);
            }
            prevLines= 5;
            grid.incrementLinesCleared(3);
        } else if (lines == 4) {
            grid.setWrittenString("Tetris");
            if (grid.getGamePlayers() == 2) {
                grid.doBarLines(4);
            }
            prevLines= 8;
            grid.incrementLinesCleared(4);
        } else if (lines == 0) {
            prevLines= 0;
        }
        grid.addScore(lines);
        return filledLines;
    }

    public boolean immobile(int[][] coords) {
        boolean up= false;
        boolean left= false;
        boolean down= false;
        boolean right= false;
        for (int[] i : coords) {
            try {
                if (array[i[0] + 1][i[1]] != Point.EMPTY &&
                    array[i[0] + 1][i[1]] != Point.CURRMINO) {
                    up= true;
                }
            } catch (Exception e) {
                up= true;
            }
            try {
                if (array[i[0]][i[1] + 1] != Point.EMPTY &&
                    array[i[0]][i[1] + 1] != Point.CURRMINO) {
                    right= true;
                }
            } catch (Exception e) {
                right= true;
            }
            try {
                if (array[i[0] - 1][i[1]] != Point.EMPTY &&
                    array[i[0] - 1][i[1]] != Point.CURRMINO) {
                    down= true;
                }
            } catch (Exception e) {
                down= true;
            }
            try {
                if (array[i[0]][i[1] - 1] != Point.EMPTY &&
                    array[i[0]][i[1] - 1] != Point.CURRMINO) {
                    left= true;
                }
            } catch (Exception e) {
                left= true;
            }
        }
        return up && left && down && right;
    }

    public int getTopFilledRow() {
        int index= 0;
        boolean found= false;
        while (!found) {
            for (Point p : array[index]) {
                if (p != Point.EMPTY && p != Point.CURRMINO) {
                    found= true;
                } else {
                    index++ ;
                }
            }
        }
        return index;
    }

    public void addLines(int lines) {
        ArrayList<Point[]> newLines= new ArrayList<>();
        Point[][] newArray= new Point[Grid.ROWS][Grid.COLS];

        for (int r= 0; r < Grid.ROWS; r++ ) {
            for (int c= 0; c < Grid.COLS; c++ ) {
                newArray[r][c]= Point.EMPTY;
            }
        }

        for (int i= 0; i < lines; i++ ) {
            Point[] row= new Point[Grid.COLS];
            for (int j= 0; j < row.length; j++ ) {
                row[j]= Point.TRASH;
            }
            int randIndex= (int) (Math.random() * (Grid.COLS - 1));
            row[randIndex]= Point.BOMB;
            newLines.add(row);
        }

        for (int r= Grid.ROWS - 1; r >= 0; r-- ) {
            newLines.add(array[r]);
        }

        for (int r= Grid.ROWS - 1; r >= 0; r-- ) {
            newArray[Grid.ROWS - 1 - r]= newLines.get(r);
        }

        array= newArray;
        // print();
    }

    public void clearLines(int[][] pos) {
        ArrayList<Point[]> filledLines= getFilledLines(pos);
        ArrayList<Point[]> newArrayRows= new ArrayList<>();
        Point[][] newArray= new Point[Grid.ROWS][Grid.COLS];

        for (int r= 0; r < Grid.ROWS; r++ ) {
            for (int c= 0; c < Grid.COLS; c++ ) {
                newArray[r][c]= Point.EMPTY;
            }
        }
        for (int r= Grid.ROWS - 1; r >= 0; r-- ) {
            Point[] row= array[r];
            if (!filledLines.contains(row)) {
                newArrayRows.add(row);
            }
        }

        for (int r= newArrayRows.size() - 1; r >= 0; r-- ) {
            newArray[Grid.ROWS - r - 1]= newArrayRows.get(r);
        }

        if (grid.getGameMode() == Menu.MARATHON) {
            grid.setMGoal(grid.getMGoal() - prevLines);
            if (grid.getLinesCleared() >= grid.getLevel() * 5) {
                grid.setLinesCleared(0);
                grid.nextLevel();
            }
            if (grid.getLevel() == 15) {
                grid.setWin(true);
            }
        } else if (grid.getGameMode() == Menu.SPRINT1P || grid.getGameMode() == Menu.SPRINT2P) {
            grid.setSGoal(grid.getSGoal() - filledLines.size());
            if (grid.getLinesCleared() >= 40) {
                grid.setWin(true);
                if (grid.getGameMode() == Menu.SPRINT2P) {
                    grid.getOpponent().stopTimer();
                }
            }
        }
        array= newArray;
    }

    public boolean getCleared() {
        return clearedLines;
    }

    public boolean checkPerfClear() {
        for (Point[] i : array) {
            for (Point j : i) {
                if (j != Point.EMPTY && j != Point.CURRMINO) { return false; }
            }
        }
        return true;
    }

    public void print() {
        for (Point[] row : array) {
            for (Point p : row) {
                System.out.print(p + " ");
            }
            System.out.println();
        }
    }
}
