import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DataHandler.readDataFromJSON();
        // https://docs.oracle.com/javase/tutorial/uiswing/concurrency/dispatch.html
        // https://tips4java.wordpress.com/2015/04/05/swing-and-java-8/
        SwingUtilities.invokeLater(Interface::new);
    }
}
