import java.util.*;

public class SudokuBoard {
    private int[][] grid;
    private int[][] solution;
    private boolean validPuzzle;

    public SudokuBoard() {
        grid = new int[9][9];
        solution = new int[9][9];
        validPuzzle = false;
    }

    public void generateNewPuzzle(int difficultyLevel) {
        int clues = switch (difficultyLevel) {
            case 0 -> 36;
            case 1 -> 20;
            case 2 -> 18;
            default -> 36;
        };

        int attempts = 0;
        do {
            generateFilledGrid();
            for (int i = 0; i < 9; i++) {
                System.arraycopy(grid[i], 0, solution[i], 0, 9);
            }
            attempts++;
        } while ((!removeCells(clues) || countSolutions(grid) != 1) && attempts < 100);

        validPuzzle = attempts < 100;
    }

    private boolean removeCells(int clues) {
        int cellsToRemove = 81 - clues;
        List<int[]> positions = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                positions.add(new int[]{i, j});
            }
        }
        Collections.shuffle(positions);

        int[][] tempGrid = deepCopy(grid);
        int removed = 0;

        for (int[] pos : positions) {
            if (removed >= cellsToRemove) break;

            int row = pos[0];
            int col = pos[1];
            if (tempGrid[row][col] == 0) continue;

            int original = tempGrid[row][col];
            tempGrid[row][col] = 0;

            if (countSolutions(deepCopy(tempGrid)) == 1) {
                removed++;
            } else {
                tempGrid[row][col] = original;
            }
        }

        if (removed >= cellsToRemove) {
            grid = tempGrid;
            return true;
        }
        return false;
    }

    private int countSolutions(int[][] puzzle) {
        return countSolutionsHelper(deepCopy(puzzle), 0, 0, 0);
    }

    private int countSolutionsHelper(int[][] currentGrid, int row, int col, int count) {
        if (row == 9) return count + 1;
        if (count > 1) return count;

        int nextRow = col == 8 ? row + 1 : row;
        int nextCol = (col + 1) % 9;

        if (currentGrid[row][col] != 0) {
            return countSolutionsHelper(currentGrid, nextRow, nextCol, count);
        }

        for (int num = 1; num <= 9; num++) {
            if (isValidPlacement(currentGrid, row, col, num)) {
                currentGrid[row][col] = num;
                count = countSolutionsHelper(currentGrid, nextRow, nextCol, count);
                currentGrid[row][col] = 0;
                if (count > 1) break;
            }
        }
        return count;
    }

    private boolean isValidPlacement(int[][] grid, int row, int col, int num) {
        return !isInRow(grid, row, num) &&
                !isInCol(grid, col, num) &&
                !isInBox(grid, row - row%3, col - col%3, num);
    }

    public boolean isValidPlacement(int row, int col, int num) {
        return isValidPlacement(this.grid, row, col, num);
    }

    private boolean isInRow(int[][] grid, int row, int num) {
        for (int c = 0; c < 9; c++)
            if (grid[row][c] == num) return true;
        return false;
    }

    private boolean isInCol(int[][] grid, int col, int num) {
        for (int r = 0; r < 9; r++)
            if (grid[r][col] == num) return true;
        return false;
    }

    private boolean isInBox(int[][] grid, int boxRow, int boxCol, int num) {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                if (grid[r+boxRow][c+boxCol] == num) return true;
        return false;
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

        List<Integer> nums = new ArrayList<>(List.of(1,2,3,4,5,6,7,8,9));
        Collections.shuffle(nums);
        for (int num : nums) {
            if (isValidPlacement(grid, row, col, num)) {
                grid[row][col] = num;
                if (solveGrid(nextRow, nextCol)) return true;
                grid[row][col] = 0;
            }
        }
        return false;
    }

    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++)
            System.arraycopy(original[i], 0, copy[i], 0, 9);
        return copy;
    }

    public boolean isValidPuzzle() {
        return validPuzzle;
    }

    public int getSolutionCell(int row, int col) {
        return solution[row][col];
    }

    public int getCell(int row, int col) {
        return grid[row][col];
    }

    public void setCell(int row, int col, int num) {
        if (validPuzzle) grid[row][col] = num;
    }
}