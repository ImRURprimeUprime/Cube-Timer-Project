package ui;

import model.Session;
import model.Solve;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Class that represents the window to view bookmarked solves
public class BookmarkWindow extends JFrame {
    private Session session;
    private JPanel bookmarks;
    private JScrollPane scrollPane;

    // written with reference to:
    // https://stackoverflow.com/questions/18408668/how-to-make-scrollable-to-jpanel
    // EFFECTS: creates a new BookmarkWindow
    public BookmarkWindow(Session session) {
        super("Bookmarked solves");
        this.session = session;
        bookmarks = new JPanel();
        bookmarks.setPreferredSize(new Dimension(500, 900));
        scrollPane = new JScrollPane(bookmarks, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(500, 250));
        add(scrollPane);
        setVisible(true);
        pack();
    }

    // MODIFIES: this
    // EFFECTS: puts all the solve details of bookmarked solves onto the bookmarks panel
    public void update() {
        bookmarks.removeAll();
        List<Solve> bookmarkedSolves = session.getInterestingSolves();
        List<Solve> solves = session.getSolveList();
        for (Solve solve : bookmarkedSolves) {
            JLabel solveDetails = new JLabel();
            solveDetails.setText("Solve " + (solves.indexOf(solve) + 1)  + ": "
                    + solve.getTime() + ", " + solve.getScramble());
            bookmarks.add(solveDetails);
        }
    }
}
