package ui;

import javax.swing.*;
import java.awt.event.ActionListener;

// Represents button shown on screen
public class Button extends JButton {

    // setFocusable written with reference to:
    // https://stackoverflow.com/questions/54398124/keylistener-doesnt-work-after-click-button
    // EFFECTS: sets up button based on name and command
    public Button(String name, String command, ActionListener al) {
        super(name);
        setActionCommand(command);
        addActionListener(al);
        setFocusable(false);
    }

}
