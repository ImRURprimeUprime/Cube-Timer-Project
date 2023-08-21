package ui;

import model.Event;
import model.EventLog;
import model.Session;
import persistence.JsonReader;
import persistence.JsonWriter;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.*;

/**
 * Represents application's main window frame.
 */
class TimerUI extends JFrame implements ActionListener {
    private Session session;
    private static final String JSON_STORE = "./data/session.json";
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private TimerPanel tp;
    private SessionPanel sp;

    // EFFECTS: creates the frame for the application, adds panels and buttons
    public TimerUI() {
        super("3x3 Rubik's Cube Timer");
        session = new Session("New Session");
        addKeyListener(new KeyHandler());
        sp = new SessionPanel(session);
        tp = new TimerPanel(session);
        tp.setLayout(null);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        add(sp, BorderLayout.WEST);
        add(tp);
        setUpButtons();
        setVisible(true);
        pack();
        centreOnScreen();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        closeWindow();
    }

    // Written with reference to AlarmSystem
    private void closeWindow() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (Event next : EventLog.getInstance()) {
                    System.out.println(next.toString());
                }
            }
        });
    }


    // Written with reference to B02-SpaceInvadersBase
    // Responds to key events
    private class KeyHandler extends KeyAdapter {
        @Override
        // MODIFIES: this
        // EFFECTS: responds to key events
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_SPACE) {
                tp.startTimer();
                update();
            }
        }
    }



    // Written with reference to AlarmSystemC4
    @Override
    // MODIFIES: this
    // EFFECTS: performs different operations based on different actions taken
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("save")) {
            saveSession();
        }
        if (e.getActionCommand().equals("load")) {
            loadSession();
        }
        if (e.getActionCommand().equals("delete")) {
            deleteSolve();
        }
        if (e.getActionCommand().equals("penalty")) {
            timePenalty();
        }
        if (e.getActionCommand().equals("mark")) {
            bookmarkSolve();
        }
        if (e.getActionCommand().equals("view")) {
            BookmarkWindow bookmarks = new BookmarkWindow(session);
            bookmarks.update();
        }
        if (e.getActionCommand().equals("rename")) {
            sp.rename();
        }
    }

    // MODIFIES: this
    // EFFECTS:  location of frame is set so frame is centred on desktop
    private void centreOnScreen() {
        Dimension scrn = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((scrn.width - getWidth()) / 2, (scrn.height - getHeight()) / 2);
    }

    // MODIFIES: session, this
    // EFFECTS: updates and recalculates mean, bestSolve, currentAverageOfFive of session. Implements these
    //          changes on the screen
    public void update() {
        session.convertSolveTimes();
        session.filterInteresting();
        session.calculateBestSolve();
        session.calculateMean();
        session.calculateAverage();
        session.refreshId();
        sp.updateTable();
        tp.setAverage();
    }

    // Written with reference to JsonSerializationDemo
    // EFFECTS: saves the session to file
    private void saveSession() {
        try {
            jsonWriter.open();
            jsonWriter.write(session);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null, "Session has been saved to " + JSON_STORE,
                    "Session saved", JOptionPane.PLAIN_MESSAGE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // Written with reference to JsonSerializationDemo
    // MODIFIES: this
    // EFFECTS: loads session from file
    private void loadSession() {
        try {
            session = jsonReader.read();
            tp.setSession(session);
            sp.setSession(session);
            tp.setAverage();
            sp.updateTable();
            JOptionPane.showMessageDialog(null, "Loaded " + session.getName() + " from "
                            + JSON_STORE, "Session loaded", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "File could not be loaded from "
                    + JSON_STORE, "Could not load session", JOptionPane.PLAIN_MESSAGE);
        }
    }

    // Image pop-up written with reference to: https://www.youtube.com/watch?v=Gy3odNyYzhg
    // JOptionPanel written with reference to: https://www.youtube.com/watch?v=4edL_cwmiZ4
    // MODIFIES: session, sp
    // EFFECTS: prompts user to enter the number of solve to be deleted and deletes the solve from session. Updates
    //          the session panel
    private void deleteSolve() {
        String response = JOptionPane.showInputDialog("Enter the number of the solve to delete:");
        try {
            int index = processInput(response);
            session.removeSolve(index);
            update();

            JOptionPane.showMessageDialog(null, "Solve " + response + " has been deleted",
                    "Solve deleted", JOptionPane.PLAIN_MESSAGE);
            ImageIcon delete = new ImageIcon(
                    "/Users/allanwang/Desktop/CPSC 210/project_l9e0i/data/DeleteSolve.png");
            JOptionPane.showMessageDialog(null,
                    "Was the time a mistake or did you delete the solve just because the time is slow",
                    "Display Image", JOptionPane.INFORMATION_MESSAGE, delete);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid index",
                    "Invalid index", JOptionPane.PLAIN_MESSAGE);
        }
    }

    // MODIFIES: session, solve, sp
    // EFFECTS: changes the penalty state of the solve that the user entered. Changes time of the solve and updates
    //          the session panel
    private void timePenalty() {
        String response = JOptionPane.showInputDialog("Enter the number of the solve to add/remove penalty from:");
        try {
            int index = processInput(response);
            session.getSolveList().get(index).timePenalty();
            update();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid index",
                    "Invalid index", JOptionPane.PLAIN_MESSAGE);
        }
    }

    // MODIFIES: session, solve
    // EFFECTS: prompts user to input the number of the solve that they would like to bookmark. Changes the interesting
    //          state of the solve.
    private void bookmarkSolve() {
        String response = JOptionPane.showInputDialog("Enter the number of the solve to bookmark/remove bookmark:");
        try {
            int index = processInput(response);
            session.getSolveList().get(index).invertInteresting();
            if (session.getSolveList().get(index).getIsInteresting()) {
                ImageIcon bookmarked = new ImageIcon(
                        "/Users/allanwang/Desktop/CPSC 210/project_l9e0i/data/Bookmarked.png");
                JOptionPane.showMessageDialog(null,
                        null, "Bookmarked!", JOptionPane.INFORMATION_MESSAGE, bookmarked);
            } else {
                JOptionPane.showMessageDialog(null, "Bookmark removed",
                        "Bookmark removed", JOptionPane.PLAIN_MESSAGE);
            }
            session.filterInteresting();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid index",
                    "Invalid index", JOptionPane.PLAIN_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds all necessary buttons to their panels
    private void setUpButtons() {
        sp.add(new Button("Delete solve...", "delete", this));
        sp.add(new Button("Penalty...", "penalty", this));
        sp.add(new Button("Bookmark solve...", "mark", this));
        sp.add(new Button("View bookmarks", "view", this));
        sp.add(new Button("Rename session", "rename", this));
        JButton saveButton = new Button("Save session", "save", this);
        saveButton.setBounds(220, 450, 100, 50);
        JButton loadButton = new Button("Load session", "load", this);
        loadButton.setBounds(350, 450, 100, 50);
        tp.add(saveButton);
        tp.add(loadButton);
    }

    // EFFECTS: returns index of user input, throws IndexOutOfBoundsException if index is
    //          invalid
    public int processInput(String response) throws IndexOutOfBoundsException, NumberFormatException {
        int n = Integer.parseInt(response);
        int index = n - 1;
        if (n > session.getTimeList().size() || n <= 0) {
            throw new IndexOutOfBoundsException();
        }
        return index;
    }


}

