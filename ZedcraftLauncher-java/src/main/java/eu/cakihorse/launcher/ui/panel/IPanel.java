package eu.cakihorse.launcher.ui.panel;

import eu.cakihorse.launcher.ui.PanelManager;
import javafx.scene.layout.GridPane;

public interface IPanel {
    void init (PanelManager panelManager);
    GridPane getLayout();
    void onShow();
    String getName();
    String getStylesheetPath();
}
