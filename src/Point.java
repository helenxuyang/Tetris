import java.awt.Color;

public enum Point {
    EMPTY(null),
    CURRMINO(null),
    GHOST(null),
    TRASH(null),
    BOMB(null),
    SETMINOI(TetrominoShape.I),
    SETMINOJ(TetrominoShape.J),
    SETMINOL(TetrominoShape.L),
    SETMINOO(TetrominoShape.O),
    SETMINOS(TetrominoShape.S),
    SETMINOT(TetrominoShape.T),
    SETMINOZ(TetrominoShape.Z);

    private TetrominoShape shape;
    private Color color;

    private Point(TetrominoShape t) {
        try {
            shape= t;
            color= t.getColor();
        } catch (Exception e) {
            shape= null;
            color= null;
        }
    }

    @Override
    public String toString() {
        String s= "";
        if (this == EMPTY) {
            s+= "E";
        } else if (this == CURRMINO) {
            s+= "C";
        } else if (this == GHOST) {
            s+= "G";
        } else if (isSetPoint()) {
            s+= "S";
        } else if (this == TRASH) {
            s+= "T";
        } else if (this == BOMB) {
            s+= "B";
        } else {
            s+= "error";
        }
        return s;
    }

    public TetrominoShape getShape() {
        try {
            return shape;
        } catch (Exception e) {
            return null;
        }
    }

    public Color getColor() {
        try {
            return color;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isSetPoint() {
        boolean isSetPt= false;
        for (Point p : getSetMinoList()) {
            if (this == p) {
                isSetPt= true;
            }
        }
        return isSetPt;
    }

    public static Point[] getSetMinoList() {
        Point[] points= { SETMINOI, SETMINOJ, SETMINOL,
                SETMINOO, SETMINOS, SETMINOT, SETMINOZ };
        return points;
    }
}
