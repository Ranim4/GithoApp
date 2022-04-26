package training.GithoApp.Utilities;

import javax.swing.*;
import java.awt.*;

public class Alert {
    public static void displayError(Component parentComponent,String message, String title){
        JOptionPane.showMessageDialog(parentComponent, message, title, JOptionPane.ERROR_MESSAGE);
    }
    public static void displayError(Component parentComponent,String message){
        JOptionPane.showMessageDialog(parentComponent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
