import javax.swing.*;

/**
 * Used to close the program if frozen
 */
public class HelpMenu extends JFrame {

    /**
     * sets up window
     */
    public HelpMenu() {
        // Set up the help menu window
        setTitle("Help Menu");
        setSize(300, 200);
        setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea("This is the help menu for the spell checker. \n Exception has occured. Please restart the program");
        textArea.setEditable(false);

        add(textArea);

        setVisible(false);
    }

    /**
     * Method to show the help menu
     */
    public void showWindow() {
        setVisible(true);
    }
}

