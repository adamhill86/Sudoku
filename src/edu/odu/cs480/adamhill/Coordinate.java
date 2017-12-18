package edu.odu.cs480.adamhill;

public class Coordinate {
    private int row, col;

    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setCoordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
