import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * Text Display handles how the text should display and when corrections should be checked for
 * @author Ryan Lambe
 */
public class TextDisplay {


    /**
     * helper class to handle when corrections should be checked automatically
     */
    private static class Listener implements DocumentListener {

        /**
         * timer to reduce the number of times corrections are checked
         */
        private final Timer timer = new Timer(500, l -> StartCorrections());

        /**
         * resets timer, should be called when text is changed so after a moment of not changing then corrections can be checked
         */
        private void resetTimer(){
            timer.stop();
            timer.setRepeats(false);
            timer.start();
        }

        public void insertUpdate(DocumentEvent e) {
            resetTimer();
        }

        public void removeUpdate(DocumentEvent e) {
            resetTimer();
        }

        public void changedUpdate(DocumentEvent e) {
            //do nothing :)
        }
    }


    /**
     * the swing object that handles the text being displayed and user interaction
     */
    private static JTextPane textbox;

    /**
     * a separate thread checking for the corrections to avoid slow-downs
     */
    private static Thread correctionsThread;

    /**
     * how text should look normally
     */
    private static final SimpleAttributeSet baseAttribute = new SimpleAttributeSet();

    /**
     * how text should look when there is an error
     */
    private static final SimpleAttributeSet redAttribute = new SimpleAttributeSet();


    /**
     * Gets the object to be displayed on the window. NOTE: This acts as an initialization and should only be called once
     * @return the object to be added to the window
     */
    public static JScrollPane GetTextObject(){
        // initialize textbox
        textbox = new JTextPane();
        JScrollPane scroll = new JScrollPane(textbox);
        textbox.getStyledDocument().addDocumentListener(new Listener());

        textbox.setFont(new Font("baseFont", Font.PLAIN, 20));
        StyleConstants.setUnderline(redAttribute, Boolean.TRUE );
        StyleConstants.setForeground(redAttribute, Color.RED);

        return scroll;
    }

    /**
     * replaces a section of text and starts checking for corrections again
     * @param location what should be replaced
     * @param replacement what it should be replaced with
     */
    public static void ReplaceSection(Pair<Integer, Integer> location, String replacement) {
        String text = textbox.getText();
        textbox.setText(text.substring(0, location.first) + replacement + text.substring(location.first + location.second));

        StartCorrections();
    }

    /**
     * Helper function to start checking for corrections
     */
    private static void StartCorrections(){

        Window.ClearCorrectionQueue();

        // wait for thread to end
        try{
            while (true){
                if (!correctionsThread.isAlive()) break;
            }
        }
        catch (NullPointerException e){
            // if thread doesn't exist then don't wait for it to finish
        }

        // start next thread
        correctionsThread = new Thread(TextDisplay::CorrectionThread);
        correctionsThread.start();
    }

    /**
     * Helper function that runs the Correction thread
     */
    private static void CorrectionThread(){
        Pair<Integer, Integer>[] locations = Correction.GetCorrections(textbox.getText());

        textbox.getStyledDocument().setCharacterAttributes(0, textbox.getText().length(), baseAttribute, true);
        for (Pair<Integer, Integer> location: locations){
            textbox.getStyledDocument().setCharacterAttributes(location.first, location.second, redAttribute, true);
        }
    }

    /**
     * Sets the text value of the textbox
     * @param text value to set
     */
    public static void LoadText(String text){
        textbox.setText(text);
    }

    /**
     * gets the current value of the textbox
     * @return the contents of the textbox
     */
    public static String GetText(){
        return textbox.getText();
    }
}