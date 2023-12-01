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
     * @param location what should be replaced, first value is index, second value is size
     * @param replacement what it should be replaced with
     */
    public static void ReplaceSection(Pair<Integer, Integer> location, String replacement) {
        try {
            textbox.getStyledDocument().remove(location.first, location.second);
            textbox.getStyledDocument().insertString(location.first, replacement, baseAttribute);
        } catch (BadLocationException e) {
            // should not happen, if it does it can be ignored
        }

        StartCorrections();
    }

    /**
     * Starts checking for corrections
     */
    public static void StartCorrections(){

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

        try{
            Pair<Integer, Integer>[] locations = Correction.GetCorrections(textbox.getStyledDocument().getText(0, textbox.getStyledDocument().getLength()));

            textbox.getStyledDocument().setCharacterAttributes(0, textbox.getText().length(), baseAttribute, true);
            for (Pair<Integer, Integer> location: locations){
                textbox.getStyledDocument().setCharacterAttributes(location.first, location.second, redAttribute, false);
            }
        }
        catch (Exception e){
            // this is unreachable since the getText values are from the text object
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

    /**
     * Gets a section of the whole text
     * @param location the section to get, first value is index, second value is size
     * @return the text in the location
     */
    public static String GetSection(Pair<Integer, Integer> location){
        try {
            return textbox.getText(location.first, location.second);//getStyledDocument().getText(location.first, location.second);
        } catch (BadLocationException e) {
            return "";
        }
    }
}
