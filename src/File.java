public class File {

    public static void New(){

    }

    public static void Load(){
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text File","txt"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION){
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if(!path.endsWith(".txt")){
                JOptionPane.showMessageDialog(null, "Unable to read file\nPlease choose a .txt file");
            }
        }
    }

    public static void Save(){
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text File","txt"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION){
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if(!path.endsWith(".txt")){
                path += ".txt";
            }
        }
    }
}
