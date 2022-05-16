package mummymaze;

import java.util.Objects;

public class Cell {
    private int line;
    private int column;
    private boolean steppedOnKey;
    private boolean steppedOnTrap;

    public Cell() {
        steppedOnKey = false;
        steppedOnTrap = false;
    }

    public Cell(int line, int column) {
        this.line = line;
        this.column = column;
        steppedOnKey = false;
        steppedOnTrap = false;
    }

    public void setPosition(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public void setPositionSteppedOnKey(int line, int column) {
        this.line = line;
        this.column = column;
        steppedOnKey = true;
    }

    public void setPositionSteppedOnTrap(int line, int column) {
        this.line = line;
        this.column = column;
        steppedOnTrap = true;
    }
    public void leftTrapPosition(){
        steppedOnTrap = false;
    }

    public void leftKeyPosition(){
        steppedOnKey = false;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public boolean hasSteppedOnKey() {
        return steppedOnKey;
    }

    public boolean hasSteppedOnTrap() {
        return steppedOnTrap;
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
        return line == cell.line && column == cell.column && steppedOnKey == cell.steppedOnKey && steppedOnTrap == cell.steppedOnTrap;
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, column, steppedOnKey, steppedOnTrap);
    }
}
