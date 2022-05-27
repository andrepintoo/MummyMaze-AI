package mummymaze;

import java.util.Objects;

public class Cell {
    private int line;
    private int column;

    public Cell(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public void setPosition(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;
        Cell cell = (Cell) o;
        return line == cell.line && column == cell.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, column);
    }
}
