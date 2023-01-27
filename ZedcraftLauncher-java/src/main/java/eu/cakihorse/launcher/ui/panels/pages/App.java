package eu.cakihorse.launcher.ui.panels.pages;

import eu.cakihorse.launcher.Launcher;
import eu.cakihorse.launcher.ui.PanelManager;
import eu.cakihorse.launcher.ui.panel.IPanel;
import eu.cakihorse.launcher.ui.panel.Panel;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import eu.cakihorse.launcher.ui.panels.pages.content.ContentPanel;
import eu.cakihorse.launcher.ui.panels.pages.content.Home;
import eu.cakihorse.launcher.ui.panels.pages.content.Settings;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class App extends Panel {
    GridPane sidemenu = new GridPane();
    GridPane navContent = new GridPane();

    Node activeLink = null;
    ContentPanel currentPage = null;

    Button homeBtn, settingsBtn;

    Saver saver = Launcher.getInstance().getSaver();

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getStylesheetPath() {
        return "css/app.css";
    }

    @Override
    public void init(PanelManager panelManager) {
        super.init(panelManager);

        // Background
        this.layout.getStyleClass().add("app-layout");
        setCanTakeAllSize(this.layout);

        ColumnConstraints columnConstraints = new ColumnConstraints();

        this.layout.getColumnConstraints().addAll(columnConstraints, new ColumnConstraints());

        // Side menu
        //this.layout.add(sidemenu, 0, 1);
        //sidemenu.getStyleClass().add("sidemenu");
        setCenterH(sidemenu);
        setCenterV(sidemenu);
        setBottom(sidemenu);

        // Background Image
        GridPane bgImage = new GridPane();
        setCanTakeAllSize(bgImage);
        bgImage.getStyleClass().add("bg-image");
        this.layout.add(bgImage, 0, 0);

        // Nav content
        this.layout.add(navContent, 0, 0);
        navContent.getStyleClass().add("nav-content");
        setBottom(navContent);
        setCenterH(navContent);
        setCenterV(navContent);

        /*
         * Side menu
         */
        // Titre

        //create buttons
        Button quit = new Button("Deconnexion");
        Button settings = new Button("Paramètres");
        Button play = new Button("Jouer !");
        //create a Hbox
        HBox topPane = new HBox();
        topPane.getChildren().addAll(quit, settings, play);
        topPane.getStyleClass().add("Hbox");
        //confog hbox
        topPane.setSpacing(10);
        topPane.setPadding(new Insets(15, 20, 10, 10));
        GridPane.setHgrow(topPane, Priority.ALWAYS);
        //GridPane.setConstraints(topPane, 0, 1, 1, 1);
        GridPane.setHalignment(topPane, HPos.CENTER);
        topPane.setMaxHeight(10);
        topPane.setTranslateY(-310);

        
        //add Hbox to layout
        this.layout.getChildren().add(topPane);
        
        

        //config buttons
        //play btnplay
        play.getStyleClass().clear();
        play.getStyleClass().add("btn");

        play.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.GAMEPAD));
        setCanTakeAllSize(play);
        setTop(play);
        play.setOnMouseClicked(e -> setPage(new Home(), play));

        //config btn

        settings.getStyleClass().clear();
        settings.getStyleClass().add("btn");
        settings.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.GEARS));
        setCanTakeAllSize(settings);
        setTop(settings);
        settings.setOnMouseClicked(e -> setPage(new Settings(), settings));

        //log-out btn
        FontAwesomeIconView logoutIcon = new FontAwesomeIconView(FontAwesomeIcon.SIGN_OUT);
        logoutIcon.getStyleClass().add("logout-icon");
        setCanTakeAllSize(quit);
        setCenterV(quit);
        setRight(quit);
        quit.getStyleClass().clear();
        quit.getStyleClass().add("btn");
        quit.setGraphic(logoutIcon);
        quit.setOnMouseClicked(e -> {
            if(currentPage instanceof  Home &&((Home) currentPage).isDownloading){
                return;
            }
            saver.remove("accessToken");
            saver.remove("clientToken");
            saver.remove("msAccessToken");
            saver.remove("msRefreshToken");
            saver.remove("offline-username");
            saver.save();
            Launcher.getInstance().setAuthInfos(null);
            this.panelManager.showPanel(new Login());
        });




        // Navigation
        homeBtn = new Button("");
        homeBtn.getStyleClass().clear();
        homeBtn.getStyleClass().add("home-btn");
        homeBtn.setMinSize(54, 45);
        //homeBtn.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.HOME));
        setCanTakeAllSize(homeBtn);
        setTop(homeBtn);
        homeBtn.setTranslateY(15d);
        homeBtn.setTranslateX(1000d);
        homeBtn.setOnMouseClicked(e -> setPage(null, homeBtn));

        settingsBtn = new Button("");



        Separator separator = new Separator(Orientation.VERTICAL);





        // Pseudo + avatar
        GridPane userPane = new GridPane();
        setCanTakeAllWidth(userPane);
        userPane.setMaxHeight(80);
        userPane.setMinWidth(80);
        userPane.getStyleClass().add("user-pane");
        setBottom(userPane);

        String avatarUrl = "https://minotar.net/avatar/" + (
                saver.get("offline-username") != null ?
                        "MHF_Steve.png" :
                        Launcher.getInstance().getAuthInfos().getUuid() + ".png"
        );
        ImageView avatarView = new ImageView();
        Image avatarImg = new Image(avatarUrl);
        avatarView.setImage(avatarImg);
        avatarView.setPreserveRatio(true);
        avatarView.setFitHeight(50d);
        setCenterV(avatarView);
        setCanTakeAllSize(avatarView);
        setLeft(avatarView);
        avatarView.setTranslateX(15d);
        userPane.getChildren().add(avatarView);

        Label usernameLabel = new Label(Launcher.getInstance().getAuthInfos().getUsername());
        usernameLabel.setFont(Font.font("Consolas", FontWeight.BOLD, FontPosture.REGULAR, 25f));
        setCanTakeAllSize(usernameLabel);
        setCenterV(usernameLabel);
        setLeft(usernameLabel);
        usernameLabel.getStyleClass().add("username-label");
        usernameLabel.setTranslateX(75d);
        setCanTakeAllWidth(usernameLabel);
        userPane.getChildren().add(usernameLabel);

        Separator sepa = new Separator(Orientation.VERTICAL);


        Button logoutBtn = new Button();



        this.layout.getChildren().add(userPane);

    }

    @Override
    public void onShow() {
        super.onShow();
        setPage(new Home(), homeBtn);
    }

    public void setPage(ContentPanel panel, Node navButton) {
        if (currentPage instanceof  Home && ((Home) currentPage).isDownloading){
            return;
        }
        if (activeLink != null)
            activeLink.getStyleClass().remove("active");
        activeLink = navButton;
        activeLink.getStyleClass().add("active");

        this.navContent.getChildren().clear();
        if (panel != null) {
            this.navContent.getChildren().add(panel.getLayout());
            currentPage = panel;
            if (panel.getStylesheetPath() != null) {
                this.panelManager.getStage().getScene().getStylesheets().clear();
                this.panelManager.getStage().getScene().getStylesheets().addAll(
                        this.getStylesheetPath(),
                        panel.getStylesheetPath()
                );

            }

            panel.init(this.panelManager);
            panel.onShow();
        }
    }
}