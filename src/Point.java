import java.util.Objects;

public class Point {
    private int row, col;

    public Point(int y, int x){
        row = y;
        col = x;
    }

    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
    /**
     * Return a new point to the north of the current point
     *
     */



    public String toString(){
        return "(" + row + ", " + col + ")";
    }
    public Point north(){
        return new Point(row - 1, col);
    }
    public Point south(){
        return new Point(row + 1, col);
    }
    public Point east(){
        return new Point(row, col +1);
    }
    public Point west(){
        return new Point(row, col -1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return getRow() == point.getRow() && getCol() == point.getCol();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getCol());
    }
    // make east, south, west
}
