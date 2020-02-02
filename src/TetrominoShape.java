import java.awt.Color;

public enum TetrominoShape {
    I(Grid.LIGHTBLUE, "I", new int[][] { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 } },
        new double[] { 0.5,
                1.5 }),
    J(Grid.DARKBLUE, "J", new int[][] { { 0, 0 }, { 1, 0 }, { 1, 1 }, { 1, 2 } },
        new double[] { 1.0,
                1.0 }),
    L(Grid.ORANGE, "L", new int[][] { { 1, 0 }, { 1, 1 }, { 1, 2 }, { 0, 2 } },
        new double[] { 1.0, 1.0 }),
    O(Grid.YELLOW, "O", new int[][] { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } },
        new double[] { 0.5, 0.5 }),
    S(Grid.GREEN, "S", new int[][] { { 1, 0 }, { 1, 1 }, { 0, 1 }, { 0, 2 } },
        new double[] { 1.0, 1.0 }),
    T(Grid.PURPLE, "T", new int[][] { { 1, 0 }, { 1, 1 }, { 1, 2 }, { 0, 1 } },
        new double[] { 1.0, 1.0 }),
    Z(Grid.RED, "Z", new int[][] { { 0, 0 }, { 0, 1 }, { 1, 1 }, { 1, 2 } },
        new double[] { 1.0, 1.0 });

    private Color color;
    private String name;

    private int[][] coords;
    private double[] COR;

    private TetrominoShape(Color clr, String n, int[][] c, double[] cor) {
        color= clr;
        name= n;
        coords= c;
        COR= cor;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public int[][] getCoords() {
        return coords;
    }

    @Override
    public String toString() {
        return name;
    }

    public double[] getCOR() {
        return COR;
    }

}
