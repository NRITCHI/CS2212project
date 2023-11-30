import java.nio.file.Files;
import javax.swing.*;
import javax.swing.filechooser.*;

public class File {

    private static JFileChooser fileChooser = new JFileChooser();

  
    public static void New(){

    }

    /**
    * method to load a file
    */
    public static void Load(){

        /**
        * use fileChooser to select a file and open a dialogue box for selection
        */
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text File","txt"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION){
            String path = fileChooser.getSelectedFile().getAbsolutePath();

            /**
            * if the user does not select a text file, ask to select a different file
            */
            if(!path.endsWith(".txt")){
                JOptionPane.showMessageDialog(null, "Unable to read file\nPlease choose a .txt file");
            }
        }
    }

    /**
    * method to save a file
    */
    public static void Save(){
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text File","txt"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        /**
        * allow the user to select a path using fileChooser and a name, and append .txt if the user forgets to add it
        */
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION){
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if(!path.endsWith(".txt")){
                path += ".txt";
            }
            /**
            * confirm the file now exists in the directory chosen, and confirm the save to the user
            */
            if(Files.exists(path) && !Files.isDirectory(path)) {
                JOptionPane.showMessageDialog(null, "File has been saved.");
            }

        }
    }
}

