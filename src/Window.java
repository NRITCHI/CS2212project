import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.concurrent.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class Window extends JFrame {

    private static JPanel corrections;
    private static JButton testFixButton;

    public Window() {

        // window
        setTitle("Group 31 Spell Checker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setVisible(true);

        // navigation container
        JPanel navbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(navbar, BorderLayout.NORTH);

        // File Dropdown
        JButton newFile = new JButton("New");
        JButton load = new JButton("Load");
        JButton save = new JButton("Save");

        // todo: make functions work
        newFile.addActionListener(e -> File.New());
        load.addActionListener(e -> File.Load());
        save.addActionListener(e -> File.Save());

        navbar.add(newFile);
        navbar.add(load);
        navbar.add(save);


        // help button
        JButton helpButton = new JButton("Help");
        helpButton.setMaximumSize(new Dimension(50, 50));
        helpButton.addActionListener(e -> {
            HelpMenu helpMenu = new HelpMenu();
            helpMenu.showWindow();
            scheduleForceClose();
        });
        navbar.add(helpButton, BorderLayout.WEST);


        // main container
        JPanel main = new JPanel();
        main.setLayout(new GridLayout(1, 1));
        add(main);

        // text box
        main.add(TextDisplay.GetTextObject());

        // corrections section
        corrections = new JPanel();
        corrections.setBorder(new LineBorder(Color.BLUE));
        main.add(corrections);


        testFixButton = new JButton("Fix");
        corrections.add(testFixButton);
        testFixButton.setVisible(true);
    }

    public static void AddCorrection(CorrectionType type, Pair<Integer, Integer> location, String[] options) {

        // switch to ui thread
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> AddCorrection(type, location, options));
            return;
        }

        System.out.println("Correction added: " + type + " at location (" + location.first + ", " + location.second + ") with options: " + Arrays.toString(options));
        if(options.length == 1){
            testFixButton.addActionListener(l -> TextDisplay.ReplaceSection(location, options[0]));
        }
    }

    public static void ClearCorrectionQueue(){
        // switch to ui thread
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> ClearCorrectionQueue());
            return;
        }

        for( ActionListener al : testFixButton.getActionListeners() ) {
            testFixButton.removeActionListener( al );
        }
    }
        
    private void scheduleForceClose() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(this::forceCloseProgram, 4, TimeUnit.SECONDS); // Delay for 4 second
        scheduler.shutdown();
    }

    
    private void forceCloseProgram() {

        Window.this.dispatchEvent(new WindowEvent(Window.this, WindowEvent.WINDOW_CLOSING));
    }

    public static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Dictionary.StartDictionary();
        SwingUtilities.invokeLater(() -> new Window());
    }
}
