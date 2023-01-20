package eu.cakihorse.launcher;

import javafx.application.Application;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try{
            Class.forName("javafx.application.Application");
            Application.launch(Launcher.class, args);
        }catch (ClassNotFoundException e){
            JOptionPane.showMessageDialog(
                    null,
                    "Erreur " + e.getMessage() + " not found",
                    "Big Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
