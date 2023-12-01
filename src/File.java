import java.nio.file.Files;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * File is used for file management, such as creating, saving and loading documents
 */
public class File {

    /**
     * file chooser is a popup to choose a file location or a file to open
     */
    private static final JFileChooser fileChooser = new JFileChooser();

    /**
     * the last saved text, used to see if document has been modified
     */
    private static String lastSaved = "";

    /**
     * creates a new blank document
     */
    public static void New(){
        if(lastSaved.equals(TextDisplay.GetText())){
            TextDisplay.LoadText("");
        }
        else{
            int a = JOptionPane.showConfirmDialog(null, "You Have Unsaved Work\n Are you sure?", "Create New File", JOptionPane.YES_NO_OPTION);
            if(a == 0){
                TextDisplay.LoadText("");
            }
        }
    }

    /**
    * method to load a file
    */
    public static void Load(){

        if(!lastSaved.equals(TextDisplay.GetText())){
            int a = JOptionPane.showConfirmDialog(null, "You Have Unsaved Work\n Are you sure?", "Create New File", JOptionPane.YES_NO_OPTION);
            if(a == 1){
                return;
            }
        }

        /*
        * use fileChooser to select a file and open a dialogue box for selection
        */
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text File","txt"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION){
            String path = fileChooser.getSelectedFile().getAbsolutePath();

            /*
            * if the user does not select a text file, ask to select a different file
            */
            if(!path.endsWith(".txt")){
                JOptionPane.showMessageDialog(null, "Unable to read file\nPlease choose a .txt file");
                return;
            }
          
            try {
                String content = new String(Files.readAllBytes(Paths.get(path)));
                TextDisplay.LoadText(content);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error reading file");
            }
        }

        lastSaved = TextDisplay.GetText();
    }

    /**
    * method to save a file
    */
    public static void Save(){
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text File","txt"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        /*
        * allow the user to select a path using fileChooser and a name, and append .txt if the user forgets to add it
        */
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION){
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if(!path.endsWith(".txt")){
                path += ".txt";
            }
            /*
            * confirm the file now exists in the directory chosen, and confirm the save to the user
            */
            
            try {
                Files.write(Paths.get(path), TextDisplay.GetText().getBytes());
                JOptionPane.showMessageDialog(null, "File has been saved.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving file");
            }
        }
    }
}

