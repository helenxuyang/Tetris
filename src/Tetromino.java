import java.awt.Color;
import java.util.Arrays;

public class Tetromino {

    private TetrominoShape shape;
    private int[][] position= new int[4][2];
    private double[] COR= new double[2];
    private Color color;
    private TetrisArray array;
    private int rotstate= 0;

    public Tetromino(TetrominoShape s, TetrisArray arrya) {
        shape= s;
        position= getInitialPosCopy();
        color= shape.getColor();
        COR[0]= shape.getCOR()[0];
        COR[1]= shape.getCOR()[1];
        array= arrya;
    }

    public Tetromino(TetrisArray arrya) {
        TetrominoShape[] shapes= { TetrominoShape.I, TetrominoShape.J,
                TetrominoShape.L, TetrominoShape.O, TetrominoShape.S, TetrominoShape.T,
                TetrominoShape.Z };
        int randomIndex= (int) (Math.random() * shapes.length);
        shape= shapes[randomIndex];
        position= getInitialPosCopy();
        color= shape.getColor();
        COR[0]= shape.getCOR()[0];
        COR[1]= shape.getCOR()[1];
        array= arrya;
    }

    public TetrominoShape getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }

    public void printPos() {
        for (int[] coords : position) {
            System.out.print("row " + coords[0] + ", col " + coords[1] + " | ");
        }
    }

    public int[][] getInitialPosCopy() {
        int[][] orig= shape.getCoords();
        int[][] copy= new int[4][2];
        for (int i= 0; i < orig.length; i++ ) {
            for (int j= 0; j < orig[0].length; j++ ) {
                copy[i][j]= orig[i][j];
            }
        }
        return copy;
    }

    public int[][] getPos() {
        return position;
    }

    public double[] getCOR() {
        return COR;
    }

    public Point getSetMino() {
        Point setMino= null;
        for (Point p : Point.getSetMinoList()) {
            if (shape == p.getShape()) {
                setMino= p;
            }
        }
        return setMino;
    }

    public int[] getLowestCoord() {
        int[] lowest= new int[2];
        for (int[] coords : position) {
            if (coords[0] > lowest[0]) {
                lowest= coords;
            }
        }
        return lowest;
    }

    public void setInitialPos() {
        position= getInitialPosCopy();
        COR[0]= shape.getCOR()[0];
        COR[1]= shape.getCOR()[1];
        if (shape == TetrominoShape.O) {
            changePos(0, 4);
        } else {
            changePos(0, 3);
        }
    }

    public void changePos(int rShift, int cShift) {
        for (int[] coord : position) {
            coord[0]+= rShift;
            coord[1]+= cShift;
        }
        COR[0]+= rShift;
        COR[1]+= cShift;
    }

    public void rotateCW() {
        double[] initCOR= new double[COR.length];
        int[][] initPos= new int[position.length][position[0].length];
        for (int i= 0; i < position.length; i++ ) {
            for (int j= 0; j < position[0].length; j++ ) {
                initPos[i][j]= position[i][j];
            }
        }
        for (int i= 0; i < COR.length; i++ ) {
            initCOR[i]= COR[i];
        }
        double[][] tempPos= new double[position.length][position[0].length];
        for (int i= 0; i < position.length; i++ ) {
            for (int j= 0; j < position[0].length; j++ ) {
                tempPos[i][j]= position[i][j] - COR[j];
            }
        }
        Matrix tempMat= new Matrix(tempPos);
        tempMat.rotCClws();
        tempPos= tempMat.getMatrix();
        double[][] almostPos= new double[position.length][position[0].length];
        for (int i= 0; i < position.length; i++ ) {
            for (int j= 0; j < position[0].length; j++ ) {
                almostPos[i][j]= tempPos[i][j] + COR[j];
            }
        }
        int[][] intPos= new int[position.length][position[0].length];
        for (int i= 0; i < position.length; i++ ) {
            for (int j= 0; j < position[0].length; j++ ) {
                intPos[i][j]= (int) almostPos[i][j];
            }
        }
        position= intPos;
        ceilKick(array);
        rotstate= ((rotstate + 1) % 4 + 4) % 4;
        if (!CWSRS(array)) {
            position= initPos;
            COR= initCOR;
            rotstate= ((rotstate - 1) % 4 + 4) % 4;
        }
    }

    public void rotateCCW() {
        int[][] initPos= new int[position.length][position[0].length];
        for (int i= 0; i < position.length; i++ ) {
            for (int j= 0; j < position[0].length; j++ ) {
                initPos[i][j]= position[i][j];
            }
        }
        double[][] tempPos= new double[position.length][position[0].length];
        for (int i= 0; i < position.length; i++ ) {
            for (int j= 0; j < position[0].length; j++ ) {
                tempPos[i][j]= position[i][j] - COR[j];
            }
        }
        Matrix tempMat= new Matrix(tempPos);
        tempMat.rotClws();
        tempPos= tempMat.getMatrix();
        double[][] almostPos= new double[position.length][position[0].length];
        for (int i= 0; i < position.length; i++ ) {
            for (int j= 0; j < position[0].length; j++ ) {
                almostPos[i][j]= tempPos[i][j] + COR[j];
            }
        }
        int[][] intPos= new int[position.length][position[0].length];
        for (int i= 0; i < position.length; i++ ) {
            for (int j= 0; j < position[0].length; j++ ) {
                intPos[i][j]= (int) almostPos[i][j];
            }
        }
        position= intPos;
        ceilKick(array);
        rotstate= ((rotstate - 1) % 4 + 4) % 4;
        if (!CCWSRS(array)) {
            position= initPos;
            rotstate= ((rotstate + 1) % 4 + 4) % 4;
        }
    }

    public boolean atBottom() {
        boolean atBot= false;
        for (int[] coord : position) {
            if (coord[0] == Grid.ROWS - 1) {
                atBot= true;
            }
        }
        return atBot;
    }

    public static boolean atBottom(int[][] coords) {
        boolean atBot= false;
        for (int[] coord : coords) {
            if (coord[0] == Grid.ROWS - 1) {
                atBot= true;
            }
        }
        return atBot;
    }

    public boolean collidesLeft(TetrisArray array) {
        boolean collides= false;
        for (int[] coord : position) {
            for (Point p : Point.getSetMinoList()) {
                int row= coord[0];
                int col= coord[1];
                if (col >= 0 && array.getPoint(row, col - 1) == p) {
                    collides= true;
                }
            }
        }
        return collides;
    }

    public boolean collidesRight(TetrisArray array) {
        boolean collides= false;
        for (int[] coord : position) {
            for (Point p : Point.getSetMinoList()) {
                int row= coord[0];
                int col= coord[1];
                if (col < Grid.COLS && array.getPoint(row, col + 1) == p) {
                    collides= true;
                }
            }
        }
        return collides;
    }

    public boolean collidesBot(TetrisArray array) {
        boolean collides= false;
        for (int[] coord : position) {
            for (Point p : Point.getSetMinoList()) {
                int row= coord[0];
                int col= coord[1];
                if (row < Grid.ROWS && array.getPoint(row - 1, col) == p) {
                    collides= true;
                }
            }
        }
        return collides;
    }

    public boolean CWSRS(TetrisArray array) {
        if (rotstate == 0) {
            if (shape == TetrominoShape.I) {
                if (!collides(array)) {
                    return true;
                } else {
                    changePos(0, 1);
                    if (!collides(array)) {
                        return true;
                    } else {
                        changePos(0, -3);
                        if (!collides(array)) {
                            return true;
                        } else {
                            changePos(2, 3);
                            if (!collides(array)) {
                                return true;
                            } else {
                                changePos(-3, -3);
                                if (!collides(array)) { return true; }
                            }
                        }
                    }
                }
                return false;
            } else if (shape != TetrominoShape.O) {
                if (!collides(array)) {
                    return true;
                } else {
                    changePos(0, -1);
                    if (!collides(array)) {
                        return true;
                    } else {
                        changePos(1, 0);
                        if (!collides(array)) {
                            return true;
                        } else {
                            changePos(-3, 1);
                            if (!collides(array)) {
                                return true;
                            } else {
                                changePos(0, -1);
                                if (!collides(array)) { return true; }
                            }
                        }
                    }
                }
                return false;
            }
        } else if (rotstate == 1) {
            if (shape == TetrominoShape.I) {
                if (!collides(array)) {
                    return true;
                } else {
                    changePos(0, -2);
                    if (!collides(array)) {
                        return true;
                    } else {
                        changePos(0, 3);
                        if (!collides(array)) {
                            return true;
                        } else {
                            changePos(1, -3);
                            if (!collides(array)) {
                                return true;
                            } else {
                                changePos(-3, 3);
                                if (!collides(array)) { return true; }
                            }
                        }
                    }
                }
                return false;
            } else if (shape != TetrominoShape.O) {
                if (!collides(array)) {
                    return true;
                } else {
                    changePos(0, -1);
                    if (!collides(array)) {
                        return true;
                    } else {
                        changePos(-1, 0);
                        if (!collides(array)) {
                            return true;
                        } else {
                            changePos(3, 1);
                            if (!collides(array)) {
                                return true;
                            } else {
                                changePos(0, -1);
                                if (!collides(array)) { return true; }
                            }
                        }
                    }
                }
                return false;
            }
        } else if (rotstate == 2) {
            if (shape == TetrominoShape.I) {
                if (!collides(array)) {
                    return true;
                } else {
                    changePos(0, -1);
                    if (!collides(array)) {
                        return true;
                    } else {
                        changePos(0, 3);
                        if (!collides(array)) {
                            return true;
                        } else {
                            changePos(-2, -3);
                            if (!collides(array)) {
                                return true;
                            } else {
                                changePos(3, 3);
                                if (!collides(array)) { return true; }
                            }
                        }
                    }
                }
                return false;
            } else if (shape != TetrominoShape.O) {
                if (!collides(array)) {
                    return true;
                } else {
                    changePos(0, 1);
                    if (!collides(array)) {
                        return true;
                    } else {
                        changePos(1, 0);
                        if (!collides(array)) {
                            return true;
                        } else {
                            changePos(-3, -1);
                            if (!collides(array)) {
                                return true;
                            } else {
                                changePos(0, 1);
                                if (!collides(array)) { return true; }
                            }
                        }
                    }
                }
                return false;
            }
        } else if (rotstate == 3) {
            if (shape == TetrominoShape.I) {
                if (!collides(array)) {
                    return true;
                } else {
                    changePos(0, 2);
                    if (!collides(array)) {
                        return true;
                    } else {
                        changePos(0, -3);
                        if (!collides(array)) {
                            return true;
                        } else {
                            changePos(-1, 3);
                            if (!collides(array)) {
                                return true;
                            } else {
                                changePos(3, -3);
                                if (!collides(array)) { return true; }
                            }
                        }
                    }
                }
                return false;
            } else if (shape != TetrominoShape.O) {
                if (!collides(array)) {
                    return true;
                } else {
                    changePos(0, 1);
                    if (!collides(array)) {
                        return true;
                    } else {
                        changePos(-1, 0);
                        if (!collides(array)) {
                            return true;
                        } else {
                            changePos(3, -1);
                            if (!collides(array)) {
                                return true;
                            } else {
                                changePos(0, 1);
                                if (!collides(array)) { return true; }
                            }
                        }
                    }
                }
                return false;
            }
        }

        return false;
    }

    public boolean CCWSRS(TetrisArray array) {
        if (rotstate == 3) {
            if (shape == TetrominoShape.I) {
                if (!collides(array)) {
                    return true;
                } else {
                    changePos(0, -1);
                    if (!collides(array)) {
                        return true;
                    } else {
                        changePos(0, 3);
                        if (!collides(array)) {
                            return true;
                        } else {
                            changePos(-2, -3);
                            if (!collides(array)) {
                                return true;
                            } else {
                                changePos(3, 3);
                                if (!collides(array)) { return true; }
                            }
                        }
                    }
                }
                return false;
            } else if (shape != TetrominoShape.O) {
                if (!collides(array)) {
                    return true;
                } else {
                    changePos(0, 1);
                    if (!collides(array)) {
                        return true;
                    } else {
                        changePos(-1, 0);
                        if (!collides(array)) {
                            return true;
                        } else {
                            changePos(3, -1);
                            if (!collides(array)) {
                                return true;
                            } else {
                                changePos(0, 1);
                                if (!collides(array)) { return true; }
                            }
                        }
                    }
                }
                return false;
            }
        } else if (rotstate == 0) {
            if (shape == TetrominoShape.I) {
                if (!collides(array)) {
                    return true;
                } else {
                    changePos(0, 2);
                    if (!collides(array)) {
                        return true;
                    } else {
                        changePos(0, -3);
                        if (!collides(array)) {
                            return true;
                        } else {
                            changePos(-1, 3);
                            if (!collides(array)) {
                                return true;
                            } else {
                                changePos(3, -3);
                                if (!collides(array)) { return true; }
                            }
                        }
                    }
                }
                return false;
            } else if (shape != TetrominoShape.O) {
                if (!collides(array)) {
                    return true;
                } else {
                    changePos(0, 1);
                    if (!collides(array)) {
                        return true;
                    } else {
                        changePos(1, 0);
                        if (!collides(array)) {
                            return true;
                        } else {
                            changePos(-3, -1);
                            if (!collides(array)) {
                                return true;
                            } else {
                                changePos(0, 1);
                                if (!collides(array)) { return true; }
                            }
                        }
                    }
                }
                return false;
            }
        } else if (rotstate == 1) {
            if (shape == TetrominoShape.I) {
                if (!collides(array)) {
                    return true;
                } else {
                    changePos(0, 1);
                    if (!collides(array)) {
                        return true;
                    } else {
                        changePos(0, -3);
                        if (!collides(array)) {
                            return true;
                        } else {
                            changePos(2, 3);
                            if (!collides(array)) {
                                return true;
                            } else {
                                changePos(-3, -3);
                                if (!collides(array)) { return true; }
                            }
                        }
                    }
                }
                return false;
            } else if (shape != TetrominoShape.O) {
                if (!collides(array)) {
                    return true;
                } else {
                    changePos(0, -1);
                    if (!collides(array)) {
                        return true;
                    } else {
                        changePos(-1, 0);
                        if (!collides(array)) {
                            return true;
                        } else {
                            changePos(3, 1);
                            if (!collides(array)) {
                                return true;
                            } else {
                                changePos(0, -1);
                                if (!collides(array)) { return true; }
                            }
                        }
                    }
                }
                return false;
            }
        } else if (rotstate == 2) {
            if (shape == TetrominoShape.I) {
                if (!collides(array)) {
                    return true;
                } else {
                    changePos(0, -2);
                    if (!collides(array)) {
                        return true;
                    } else {
                        changePos(0, 3);
                        if (!collides(array)) {
                            return true;
                        } else {
                            changePos(1, -3);
                            if (!collides(array)) {
                                return true;
                            } else {
                                changePos(-3, 3);
                                if (!collides(array)) { return true; }
                            }
                        }
                    }
                }
                return false;
            } else if (shape != TetrominoShape.O) {
                if (!collides(array)) {
                    return true;
                } else {
                    changePos(0, -1);
                    if (!collides(array)) {
                        return true;
                    } else {
                        changePos(1, 0);
                        if (!collides(array)) {
                            return true;
                        } else {
                            changePos(-3, 1);
                            if (!collides(array)) {
                                return true;
                            } else {
                                changePos(0, -1);
                                if (!collides(array)) { return true; }
                            }
                        }
                    }
                }
                return false;
            }
        }

        return false;
    }

    public void ceilKick(TetrisArray array) {
        int max= 0;
        for (int[] coord : position) {
            if (coord[0] < 0) {
                max= -coord[0];
            }
        }
        changePos(max, 0);
    }

    public boolean collides(TetrisArray array) {
        for (int[] coord : position) {
            int row= coord[0];
            int col= coord[1];
            if (row < 0 || row >= Grid.ROWS) { return true; }
            if (col < 0 || col >= Grid.COLS) { return true; }
            if (array.getPoint(row, col) != Point.EMPTY &&
                array.getPoint(row, col) != Point.CURRMINO) {
                return true;
            }
        }
        return false;
    }

    public static void print(int[][] array) {
        for (int[] i : array) {
            System.out.println(Arrays.toString(i));
        }
    }

    public static void print(double[][] array) {
        for (double[] i : array) {
            System.out.println(Arrays.toString(i));
        }
    }
}
