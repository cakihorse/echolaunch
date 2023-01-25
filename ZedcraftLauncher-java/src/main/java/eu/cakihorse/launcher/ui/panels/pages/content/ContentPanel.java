package eu.cakihorse.launcher.ui.panels.pages.content;

import eu.cakihorse.launcher.ui.panel.Panel;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public abstract class ContentPanel extends Panel {
    @Override
    public void onShow() {
        FadeTransition transition = new FadeTransition(Duration.seconds(1), this.layout);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setAutoReverse(true);
        transition.play();
    }


}
