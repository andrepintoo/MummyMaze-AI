package mummymaze;

import java.util.Objects;

public class Cell {
    private int line;
    private int column;
    private boolean steppedOnKey;
    private boolean steppedOnTrap;
//    private int lineTrap;
//    private int columnTrap;
//    private int lineKey;
//    private int columnKey;

    public Cell() {
        steppedOnKey = false;
        steppedOnTrap = false;
//        lineTrap = columnTrap = lineKey = columnKey = 0;
    }

    public Cell(int line, int column) {
        this.line = line;
        this.column = column;
        steppedOnKey = false;
        steppedOnTrap = false;
//        lineTrap = columnTrap = lineKey = columnKey = 0;
    }

    public void setPosition(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public void setPositionSteppedOnKey(int line, int column) {
//        this.lineKey = line;
//        this.columnKey = column;
        this.line = line;
        this.column = column;
        steppedOnKey = true;
    }

    public void setPositionSteppedOnTrap(int line, int column) {
//        this.lineTrap = line;
//        this.columnTrap = column;
        this.line = line;
        this.column = column;
        steppedOnTrap = true;
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

//    public int getLineKey() {
//        return lineKey;
//    }
//
//    public int getColumnKey() {
//        return columnKey;
//    }
//
//    public int getLineTrap() {
//        return lineTrap;
//    }
//
//    public int getColumnTrap() {
//        return columnTrap;
//    }

    public void setColumn(int column) {
        this.column = column;
    }

//    public void setColumnWithTrap(int column, int lineTrap, int columnTrap) {
//        this.column = column;
//    }

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
