import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class Window extends JFrame {

    private static JPanel corrections;
    private static ArrayList<JPanel> correctionTiles = new ArrayList<>();

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
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1;
        main.setLayout(new GridBagLayout());
        main.setAlignmentX(0.75f);
        add(main);

        // text box
        constraints.gridx = 0;
        constraints.weightx = 0.67; // 2/3 of the available width
        main.add(TextDisplay.GetTextObject(), constraints);

        // corrections section
        corrections = new JPanel();
        JScrollPane scroll = new JScrollPane(corrections, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);
        constraints.gridx = 1;
        constraints.weightx = 0.33; // 1/3 of the available width
        main.add(scroll, constraints);

        /*testFixButton = new JButton("Fix");
        corrections.add(testFixButton);
        testFixButton.setVisible(true);*/
        corrections.setLayout(new GridBagLayout());
    }


    static int count = 0;
    private static JPanel CreateCorrectionBlock(){
        JPanel panel = new JPanel();

        panel.setBackground(Color.darkGray);
        panel.setBorder(new LineBorder(Color.RED));
        panel.setPreferredSize(new Dimension(10, 150));
        count++;
        JLabel text = new JLabel("" + count);
        panel.add(text);

        correctionTiles.add(panel);
        return panel;
    }

    public static void AddCorrection(CorrectionType type, Pair<Integer, Integer> location, String[] options) {

        // switch to ui thread
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> AddCorrection(type, location, options));
            return;
        }

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;

        corrections.add(CreateCorrectionBlock(), c);
        corrections.revalidate();
        corrections.repaint();

        System.out.println("Correction added: " + type + " at location (" + location.first + ", " + location.second + ") with options: " + Arrays.toString(options));
    }

    public static void ClearCorrectionQueue(){
        // switch to ui thread
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(Window::ClearCorrectionQueue);
            return;
        }

        for(JPanel panel: correctionTiles){
            corrections.remove(panel);
        }
        corrections.revalidate();
        corrections.repaint();
        count = 0;
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
