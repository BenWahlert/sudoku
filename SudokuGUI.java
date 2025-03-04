import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class SudokuGUI extends JFrame {
    private final SudokuBoard board = new SudokuBoard();
    private final SudokuCell[][] cells = new SudokuCell[9][9];
    private JCheckBox candidateModeCheckbox;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public SudokuGUI() {
        initializeUI();
        newGame(0);
    }

    private void initializeUI() {
        setTitle("Sudoku");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(9, 9));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new SudokuCell();
                applyCellBorders(i, j);
                int finalI = i, finalJ = j;
                cells[i][j].addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent evt) {
                        selectCell(finalI, finalJ);
                    }
                });
                cells[i][j].addKeyListener(new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        handleCellInput(finalI, finalJ, e.getKeyChar());
                    }
                });
                boardPanel.add(cells[i][j]);
            }
        }

        JPanel controlPanel = new JPanel();
        String[] difficulties = {"Easy", "Medium", "Hard"};
        JComboBox<String> difficultyDropdown = new JComboBox<>(difficulties);
        candidateModeCheckbox = new JCheckBox("Show Candidates");
        JButton newGameButton = new JButton("New Game");
        JButton checkButton = new JButton("Check Solution");

        controlPanel.add(new JLabel("Difficulty:"));
        controlPanel.add(difficultyDropdown);
        controlPanel.add(candidateModeCheckbox);
        controlPanel.add(newGameButton);
        controlPanel.add(checkButton);

        newGameButton.addActionListener(e -> newGame(difficultyDropdown.getSelectedIndex()));
        checkButton.addActionListener(e -> checkSolution());
        candidateModeCheckbox.addActionListener(e -> toggleCandidates());

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private void applyCellBorders(int i, int j) {
        Border border = BorderFactory.createLineBorder(Color.DARK_GRAY);
        if (i % 3 == 2 && i != 8) {
            border = BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK),
                    border
            );
        }
        if (j % 3 == 2 && j != 8) {
            border = BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 0, 2, Color.BLACK),
                    border
            );
        }
        cells[i][j].setBorder(border);
    }

    private void selectCell(int row, int col) {
        if (selectedRow != -1 && selectedCol != -1) {
            cells[selectedRow][selectedCol].setSelected(false);
        }
        selectedRow = row;
        selectedCol = col;
        cells[row][col].setSelected(true);
    }

    private void handleCellInput(int row, int col, char input) {
        if (!Character.isDigit(input) || input == '0') return;
        int num = Character.getNumericValue(input);
        SudokuCell cell = cells[row][col];

        if (cell.isEditable()) {
            cell.setValue(num);
            board.setCell(row, col, num);
            cell.setIncorrect(false);
            if (candidateModeCheckbox.isSelected()) updateCandidates();
        }
    }

    private void checkSolution() {
        boolean allCorrect = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                SudokuCell cell = cells[i][j];
                if (cell.isEditable()) {
                    boolean correct = (cell.getValue() == board.getSolutionCell(i, j));
                    if (correct) {
                        cell.setCorrect(true);
                        cell.setEditable(false);
                        cell.setIncorrect(false);
                    } else {
                        cell.setIncorrect(true);
                        allCorrect = false;
                    }
                }
            }
        }

        String message = allCorrect ? "Puzzle Solved Correctly!" : "Some cells are incorrect!";
        JOptionPane.showMessageDialog(this, message, "Game Status", JOptionPane.INFORMATION_MESSAGE);
    }

    private void newGame(int difficulty) {
        board.generateNewPuzzle(difficulty);
        selectedRow = -1;
        selectedCol = -1;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int value = board.getCell(i, j);
                SudokuCell cell = cells[i][j];
                cell.setValue(value);
                boolean editable = (value == 0);
                cell.setEditable(editable);
                cell.setSelected(false);
                cell.setIncorrect(false);
                cell.setCorrect(false);
            }
        }
        if (candidateModeCheckbox.isSelected()) updateCandidates();
    }

    private void toggleCandidates() {
        boolean show = candidateModeCheckbox.isSelected();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].setShowCandidates(show && cells[i][j].getValue() == 0);
            }
        }
        if (show) updateCandidates();
    }

    private void updateCandidates() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (cells[i][j].getValue() == 0) {
                    boolean[] valid = new boolean[9];
                    for (int num = 1; num <= 9; num++) {
                        valid[num-1] = board.isValidPlacement(i, j, num);
                    }
                    cells[i][j].setCandidates(valid);
                }
            }
        }
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new SudokuGUI().setVisible(true));
    }
}