import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.concurrent.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Window extends JFrame {
    private static boolean useGlobalDictionary = true;
    public static JPanel coloredBlock;
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

        JButton switchDictionaryButton = new JButton("Switch Dictionary");
        switchDictionaryButton.addActionListener(e -> switchDictionary());
        navbar.add(switchDictionaryButton);
        // main container
        JPanel main = new JPanel();
        main.setLayout(new GridLayout(1, 1));
        add(main);

        // text box
        main.add(TextDisplay.GetTextObject());

        // corrections section
        coloredBlock = new JPanel();
        coloredBlock.setBorder(new LineBorder(Color.BLUE));
        main.add(coloredBlock);


        
    }
    private void switchDictionary() {
        useGlobalDictionary = !useGlobalDictionary;
        String dictionaryType = useGlobalDictionary ? "Global" : "User";
        JOptionPane.showMessageDialog(this, "Switched to " + dictionaryType + " Dictionary");
        reloadDictionary();
    }
    private void reloadDictionary() {
        try {
            Dictionary.StartDictionary(useGlobalDictionary);
        } catch (FileNotFoundException e) {
            // Handle the exception (e.g., print an error message)
            e.printStackTrace();
        }
    }

    public void addCorrection(CorrectionType type, Pair<Integer, Integer> location, String[] options) {
        System.out.println("Correction added: " + type + " at location " + location + " with options: " + Arrays.toString(options));
            // Update GUI with correction type
            SwingUtilities.invokeLater(() -> {
                // Update GUI with correction type
                JLabel correctionLabel = new JLabel(Arrays.toString(options));
                coloredBlock.add(correctionLabel);
                coloredBlock.revalidate(); // Refresh the layout
                coloredBlock.repaint();    // Repaint the panel
            });

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

    public static void AddCorrection(CorrectionType type, Pair<Integer, Integer> location, String[] options){
        // todo
        System.out.println("Correction added: " + type + " at location " + location + " with options: " + Arrays.toString(options));
        // JLabel label = new JLabel("Hello, this is some text!");
        // label.setForeground(Color.BLACK); // Set text color
        // coloredBlock.add(label);        
        SwingUtilities.invokeLater(() -> {
                coloredBlock.removeAll();
                JLabel correctionLabel = new JLabel("<html>" + String.join("<br>", options) + "</html>");
                coloredBlock.add(correctionLabel);
                coloredBlock.revalidate(); // Refresh the layout
                coloredBlock.repaint();    // Repaint the panel
                
            });
    }

    public static void ClearCorrectionQueue(){
        // todo
        // ik this isn't in the class diagram but its needed so we dont have multiple of the same correction
        // Thanks, -Ryan
    }

    public static void main(String[] args) {
  SwingUtilities.invokeLater(() -> {
        try {
            Window window = new Window();
            Dictionary.StartDictionary(true);  // Call StartDictionary here
        } catch (FileNotFoundException e) {
            // Handle the exception (e.g., print an error message)
            e.printStackTrace();
        }
    });
    }
}
