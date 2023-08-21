package ui;

import model.Session;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Written with reference to B02-SpaceInvadersBase
// Class that represents the panel on the left that stores information about the solves
public class SessionPanel extends JPanel {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 500;
    private Session session;
    private JLabel mean;
    private JPanel timePanel;
    private JScrollPane scrollPane;
    private JLabel name;

    // written with reference to:
    // https://stackoverflow.com/questions/18408668/how-to-make-scrollable-to-jpanel
    // EFFECTS: creates a new panel on the left side with the screen with a scrollable pane
    public SessionPanel(Session session) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.LIGHT_GRAY);
        this.session = session;
        name = new JLabel("Solves of " + session.getName());
        add(name);
        mean = new JLabel("Mean: 0.00");
        timePanel = new JPanel();
        timePanel.setPreferredSize(new Dimension(150, 900));
        scrollPane = new JScrollPane(timePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(150, 250));
        this.add(scrollPane);
        this.add(mean);
    }

    // MODIFIES: this
    // EFFECTS: puts all the solve times into timePanel, displays the solve number as well
    public void updateTable() {
        timePanel.removeAll();
        timePanel.repaint();
        List<Double> times = session.getTimeList();
        int size = times.size();
        for (int i = size - 1; i >= 0; i -= 1) {
            JLabel time = new JLabel("Solve " + (i + 1) + ": " + times.get(i));
            timePanel.add(time);
        }
        mean.setText("Mean: " + session.getMean());
    }

    // EFFECTS: sets this.session to session, but does not update session values
    public void setSession(Session session) {
        this.session = session;
    }

    public void rename() {
        String response = JOptionPane.showInputDialog("Enter a new name for the session");
        session.setName(response);
        name.setText("Solves of: " + response);
        repaint();
    }
}
