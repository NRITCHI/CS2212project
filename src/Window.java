import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.concurrent.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class Window extends JFrame {

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
        JPanel coloredBlock = new JPanel();
        coloredBlock.setBorder(new LineBorder(Color.BLUE));
        main.add(coloredBlock);
    }

    public void addCorrection(CorrectionType type, Pair<Integer, Integer> location, String[] options) {
        System.out.println("Correction added: " + type + " at location " + location + " with options: " + Arrays.toString(options));
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
    }

    public static void ClearCorrectionQueue(){
        // todo
        // ik this isn't in the class diagram but its needed so we dont have multiple of the same correction
        // Thanks, -Ryan
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Window());
    }
}
