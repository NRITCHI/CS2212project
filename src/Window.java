import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

public class Window extends JFrame {

    public Window() {
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        
        JButton helpButton = new JButton("Help");
        helpButton.addActionListener(e -> {
            HelpMenu helpMenu = new HelpMenu();
            helpMenu.showWindow();
            scheduleForceClose();
            
            
        });


        helpButton.setMaximumSize(new Dimension(50, 50));

        JPanel navbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navbar.add(helpButton, BorderLayout.WEST);
        add(navbar, BorderLayout.NORTH);



        JPanel coloredBlock = new JPanel();
        coloredBlock.setBorder(new LineBorder(Color.BLUE)); 

        setLayout(new GridLayout(1, 1));
        

        add(new JPanel()); 
        add(coloredBlock);  
        setVisible(true);
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
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Window());
    }
}
