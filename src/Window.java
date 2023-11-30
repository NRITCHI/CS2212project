import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class Window extends JFrame {

    private static JPanel corrections;
    private static ArrayList<JPanel> correctionTiles = new ArrayList<>();
    private static ArrayList<CorrectionCase> ignoredCases = new ArrayList<>();


    private static class CorrectionCase{
        public CorrectionType type;
        public Pair<Integer, Integer> location;
        public String[] options;

        public CorrectionCase(CorrectionType type, Pair<Integer, Integer> location, String[] options){
            this.type = type;
            this.location = location;
            this.options = options;
        }

        @Override
        public boolean equals(Object obj){
            if (obj == null)
                return false;
            if (obj.getClass() != this.getClass())
                return false;

            final CorrectionCase other = (CorrectionCase) obj;
            if(this.options.length != other.options.length)
                return false;

            for(int i = 0; i < this.options.length; i++)
                if(!this.options[i].equals(other.options[i]))
                    return false;

            return this.type == other.type && this.location.equals(other.location);
        }
    }

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

        // switch dictionary button

        JButton dictionaryButton = new JButton("Dictionary");
        dictionaryButton.addActionListener(e -> ModifyDictionary());
        navbar.add(dictionaryButton);


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

        corrections.setLayout(new GridBagLayout());
    }

    private void ModifyDictionary(){

        // create popup
        JFrame popup = new JFrame("Modify Dictionary");
        popup.setSize(800, 500);
        popup.setLocationRelativeTo(this);
        popup.setVisible(true);

        JTextPane textbox = new JTextPane();
        JScrollPane scroll = new JScrollPane(textbox);
        popup.add(scroll);

        // fill textbox
        String[] words = Dictionary.GetUserDictionary();
        String text = "";
        for(String word: words)
            text += word + "\n";
        textbox.setText(text);

        // save when closed
        popup.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {

                try {
                    String text = textbox.getStyledDocument().getText(0, textbox.getStyledDocument().getLength());
                    String[] words = text.replaceAll(" ", "").split("\n");
                    Dictionary.SetUserDictionary(words);

                } catch (BadLocationException ex) {
                    // unreachable
                }
            }
        });

        popup.revalidate();
        popup.repaint();
    }

    static int count = 0;
    private static JPanel CreateCorrectionBlock(CorrectionType type, Pair<Integer, Integer> location, String[] options){
        JPanel panel = new JPanel();

        panel.setBackground(new Color(255, 102, 102));
        panel.setBorder(new LineBorder(Color.RED));
        panel.setPreferredSize(new Dimension(10, 150));
        panel.setLayout(new GridLayout(3, 1));

        JLabel title;
        JPanel choices = new JPanel();
        choices.setBackground(new Color(255, 102, 102));
        switch (type){
            case Misspelling:
                title = new JLabel("Misspelling", SwingConstants.CENTER);

                JComboBox<String> dropdown = new JComboBox<>(options);
                choices.add(dropdown);

                JButton replace = new JButton("Replace");
                replace.addActionListener(l -> {
                    String choice = (String)dropdown.getSelectedItem();
                    TextDisplay.ReplaceSection(location, choice);
                });
                choices.add(replace);

                break;
            case Miscapitalization:
                title = new JLabel("Miscapitalization", SwingConstants.CENTER);

                JButton change = new JButton("Change");
                change.addActionListener(l -> TextDisplay.ReplaceSection(location, options[0]));
                choices.add(change);

                break;
            case DoubleWords:
                title = new JLabel("Double Words", SwingConstants.CENTER);

                JButton remove = new JButton("Remove");
                remove.addActionListener(l -> TextDisplay.ReplaceSection(location, options[0]));
                choices.add(remove);

                break;
            default:
                return null;
        }
        title.setFont(new Font("", Font.PLAIN, 28));

        JLabel section = new JLabel("\"" + TextDisplay.GetSection(location) + "\"", SwingConstants.CENTER);
        section.setFont(new Font("", Font.PLAIN, 24));

        //todo: add functionality
        JButton ignore = new JButton("Ignore");
        ignore.addActionListener(l -> {
            ignoredCases.add(new CorrectionCase(type, location, options));
            TextDisplay.StartCorrections();
        });
        choices.add(ignore);

        panel.add(title);
        panel.add(section);
        panel.add(choices);

        correctionTiles.add(panel);
        return panel;
    }

    public static void AddCorrection(CorrectionType type, Pair<Integer, Integer> location, String[] options) {

        // switch to ui thread
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> AddCorrection(type, location, options));
            return;
        }

        System.out.println("Correction added: " + type + " at location (" + location.first + ", " + location.second + ") with options: " + Arrays.toString(options));

        if(ignoredCases.contains(new CorrectionCase(type, location, options)))
            return;

        JPanel correctionBlock = CreateCorrectionBlock(type, location, options);
        if(correctionBlock == null)
            return;

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;

        corrections.add(correctionBlock, c);
        corrections.revalidate();
        corrections.repaint();
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

    public static boolean IsIgnoredCorrection(CorrectionType type, Pair<Integer, Integer> location, String[] options){
        return ignoredCases.contains(new CorrectionCase(type, location, options));
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
