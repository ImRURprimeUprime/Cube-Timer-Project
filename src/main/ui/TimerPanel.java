package ui;

import model.Session;
import model.Solve;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.*;

// Panel that the timer renders in
public class TimerPanel extends JPanel {

    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private Session session;
    private boolean timing;
    private long startTime;
    private JLabel displayedText;
    private JLabel scramble;
    private JLabel average;
    private final String[] possibleMoves =
            {"U", "U'", "U2", "R", "R'", "R2", "D", "D'",
                    "D2", "L", "L'", "L2", "F", "F'", "F2", "B", "B'", "B2"};

    // EFFECTS: sets size and background colour of panel, adds scramble, displayedText, and average to this
    public TimerPanel(Session session) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.pink);
        this.session = session;
        timing = false;
        setDisplayedText();
        scramble = new JLabel(generateScramble());
        scramble.setFont((new Font("TimesNewRoman", Font.BOLD, 20)));
        scramble.setBounds(60, 10, 750, 70);
        average = new JLabel("AO5: 0.00");
        average.setFont((new Font("TimesNewRoman", Font.BOLD, 20)));
        average.setBounds(300, 170, WIDTH, HEIGHT);
        add(scramble);
        add(displayedText);
        add(average);
    }

    // MODIFIES: this
    // EFFECTS: keeps track of the time between user pressing space bar. Adds the new solve
    //          to the session.
    public void startTimer() {
        if (!timing) {
            startTime = System.currentTimeMillis();
            displayedText.setText("Solve!");
            timing = true;
        } else {
            long finishTime = System.currentTimeMillis();
            long timeElapsed = finishTime - startTime;
            String time = String.valueOf(timeElapsed);
            time = time.substring(0,(time.length() - 3)) + "." + time.substring(time.length() - 2);
            displayedText.setText(time);
            timing = false;
            double timeSeconds = Double.parseDouble(time);
            Solve solve = new Solve(timeSeconds, scramble.getText(), session.getSolveList().size() + 1);
            scramble.setText(generateScramble());
            session.addSolve(solve);
        }
    }

    // EFFECTS: sets the font and style of displayedText
    public void setDisplayedText() {
        displayedText = new JLabel("");
        displayedText.setForeground(Color.white);
        displayedText.setFont((new Font("TimesNewRoman", Font.BOLD, 100)));
        displayedText.setBounds(230, 80, 350, 250);
    }

    // EFFECTS: randomly generates a new scramble for the solve
    public String generateScramble() {
        List<String> moveList = new ArrayList<>();
        String firstMove = possibleMoves[new Random().nextInt(possibleMoves.length)];
        moveList.add(firstMove);

        while (moveList.size() <= 20) {
            String randomNext = possibleMoves[new Random().nextInt(possibleMoves.length)];
            String prev = moveList.get(moveList.size() - 1);
            char randomFirstMove = randomNext.charAt(0);
            char previousFirstMove = prev.charAt(0);
            if (randomFirstMove != previousFirstMove) {
                moveList.add(randomNext);
            }
        }
        String moves = "";
        for (String move:moveList) {
            moves = moves + " " + move;
        }
        return moves;
    }

    // MODIFIES: this
    // EFFECTS: sets average and displays the average on screen
    public void setAverage() {
        average.setText("AO5: " + session.getAverageOfFive());
    }

    // EFFECTS: sets this.session to session, but does not update session values
    public void setSession(Session session) {
        this.session = session;
    }

}
