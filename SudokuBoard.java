import java.util.*;

public class SudokuBoard {
    private int[][] grid;
    private int[][] solution;

    public SudokuBoard() {
        grid = new int[9][9];
        solution = new int[9][9];
    }

    public void generateNewPuzzle(int difficultyLevel) {
        generateFilledGrid();
        for (int i = 0; i < 9; i++) {
            System.arraycopy(grid[i], 0, solution[i], 0, 9);
        }
        int clues = switch (difficultyLevel) {
            case 0 -> 36;
            case 1 -> 27;
            case 2 -> 18;
            default -> 36;
        };
        removeCells(clues);
    }

    private void generateFilledGrid() {
        grid = new int[9][9];
        solveGrid(0, 0);
    }

    private boolean solveGrid(int row, int col) {
        if (row == 9) return true;
        int nextRow = col == 8 ? row + 1 : row;
        int nextCol = (col + 1) % 9;
        if (grid[row][col] != 0) return solveGrid(nextRow, nextCol);

        List<Integer> nums = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        Collections.shuffle(nums);
        for (int num : nums) {
            if (isValidPlacement(row, col, num)) {
                grid[row][col] = num;
                if (solveGrid(nextRow, nextCol)) return true;
                grid[row][col] = 0;
            }
        }
        return false;
    }

    private void removeCells(int clues) {
        Random rand = new Random();
        int cellsToRemove = 81 - clues;
        while (cellsToRemove > 0) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);
            if (grid[row][col] != 0) {
                grid[row][col] = 0;
                cellsToRemove--;
            }
        }
    }

    public int getSolutionCell(int row, int col) {
        return solution[row][col];
    }

    public boolean isValidPlacement(int row, int col, int num) {
        return !isInRow(row, num) && !isInCol(col, num) && !isInBox(row, col, num);
    }

    private boolean isInRow(int row, int num) {
        for (int c = 0; c < 9; c++)
            if (grid[row][c] == num) return true;
        return false;
    }

    private boolean isInCol(int col, int num) {
        for (int r = 0; r < 9; r++)
            if (grid[r][col] == num) return true;
        return false;
    }

    private boolean isInBox(int row, int col, int num) {
        int boxRow = row - row % 3;
        int boxCol = col - col % 3;
        for (int r = boxRow; r < boxRow + 3; r++)
            for (int c = boxCol; c < boxCol + 3; c++)
                if (grid[r][c] == num) return true;
        return false;
    }

    public boolean isSolutionValid() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (grid[i][j] != solution[i][j]) return false;
        return true;
    }

    public int getCell(int row, int col) {
        return grid[row][col];
    }

    public void setCell(int row, int col, int num) {
        grid[row][col] = num;
    }
}