import javax.swing.*;
import java.awt.*;

public class SudokuCell extends JPanel {
    private int value;
    private boolean[] candidates;
    private boolean isEditable;
    private boolean showCandidates;
    private boolean isSelected;
    private boolean isIncorrect;
    private boolean isCorrect;

    public SudokuCell() {
        setPreferredSize(new Dimension(60, 60));
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        value = 0;
        candidates = new boolean[9];
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                requestFocusInWindow();
            }
        });
    }

    public void setValue(int value) {
        this.value = value;
        repaint();
    }

    public int getValue() {
        return value;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
        repaint();
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setShowCandidates(boolean show) {
        showCandidates = show;
        repaint();
    }

    public void setCandidates(boolean[] candidates) {
        System.arraycopy(candidates, 0, this.candidates, 0, 9);
        repaint();
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        repaint();
    }

    public void setIncorrect(boolean incorrect) {
        isIncorrect = incorrect;
        if (incorrect) isCorrect = false;
        repaint();
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
        if (correct) isIncorrect = false;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (isSelected) {
            g2.setColor(new Color(255, 200, 200));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        if (value != 0) {
            Color textColor = Color.BLACK;
            if (isCorrect) {
                textColor = new Color(0, 150, 0); // Dark green
            } else if (isEditable) {
                textColor = isIncorrect ? Color.RED : Color.BLUE;
            }

            g2.setColor(textColor);
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            String text = String.valueOf(value);
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(text, x, y);
        } else if (showCandidates) {
            g2.setColor(Color.GRAY);
            g2.setFont(new Font("Arial", Font.PLAIN, 10));
            for (int i = 0; i < 9; i++) {
                if (candidates[i]) {
                    int x = 5 + (i % 3) * 18;
                    int y = 12 + (i / 3) * 15;
                    g2.drawString(String.valueOf(i + 1), x, y);
                }
            }
        }
    }
}