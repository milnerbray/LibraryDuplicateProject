import javax.swing.*;
import java.awt.*;
import java.io.File;

public class OpenFile extends Component {
    public String selectFolder(){
        final JFileChooser fc = new JFileChooser(System.getProperty("user.home"));
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int response = fc.showDialog(OpenFile.this, "Select");
        if(response == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            return file.getAbsolutePath();
        } else {
            System.out.println("Cancelled");
        }
        return null;
    }
}
