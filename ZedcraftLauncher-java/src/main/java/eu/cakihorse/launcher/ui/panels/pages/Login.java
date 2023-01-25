package eu.cakihorse.launcher.ui.panels.pages;

import eu.cakihorse.launcher.Launcher;
import eu.cakihorse.launcher.ui.PanelManager;
import eu.cakihorse.launcher.ui.panel.IPanel;
import eu.cakihorse.launcher.ui.panel.Panel;
import fr.litarvan.openauth.AuthPoints;
import fr.litarvan.openauth.AuthenticationException;
import fr.litarvan.openauth.Authenticator;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.model.AuthAgent;
import fr.litarvan.openauth.model.AuthProfile;
import fr.litarvan.openauth.model.response.AuthResponse;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;


public class Login extends Panel {
   // GridPane card = new GridPane();

    Saver saver = Launcher.getInstance().getSaver();
    AtomicBoolean offlineAuth = new AtomicBoolean(false);

    TextField userField = new TextField();
    PasswordField passwordField = new PasswordField();
    Label userErrorLabel = new Label();
    Label passwordErrorLabel = new Label();
    Button btnLogin = new Button("Connexion");
    Button msLoginBtn = new Button();
    CheckBox authModeChk = new CheckBox("Crack");

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getStylesheetPath() {
        return "css/login.css";
    }

    @Override
    public void init(PanelManager panelManager) {
        super.init(panelManager);

        // Background


        //ColumnConstraints columnConstraints = new ColumnConstraints();


        //this.layout.getColumnConstraints().add(columnConstraints);


        // Background image

        GridPane bgImage = new GridPane();
        setCanTakeAllSize(bgImage);
        bgImage.getStyleClass().add("bg-image");
        this.layout.add(bgImage, 0, 0);
        //this.layout.add(loginCard, 0, 0);
        // Login card
        setCanTakeAllSize(this.layout);
       // loginCard.getStyleClass().add("login-card");



//setLeft(loginCard);

        setCanTakeAllWidth(this.layout);





        /*
         * Login sidebar
         */
        Label title = new Label("Connexion");
        title.setFont(Font.font("Consolas", FontWeight.LIGHT, FontPosture.REGULAR, 30f));

        title.getStyleClass().add("login-title");
        setCenterH(title);
        setCanTakeAllSize(title);
        title.setTranslateY(-120);
        title.setTranslateX(520);
        title.setStyle("-fx-text-fill: white;");
        title.setTextAlignment(TextAlignment.CENTER);


        //Login Card
        /*
        Rectangle rectangle = new Rectangle();

        rectangle.setWidth(400);
        rectangle.setHeight(500);
        rectangle.setFill(Color.DARKSLATEGRAY);
        rectangle.setTranslateX(400);

        rectangle.setArcHeight(50);
        rectangle.setArcWidth(50);
**/

        // Username/E-Mail
        setCanTakeAllSize(userField);
        setCenterV(userField);
        setCenterH(userField);
        userField.setPromptText("Adresse E-Mail");
        userField.setMaxWidth(300);
        userField.setTranslateY(-70d);
        userField.setTranslateX(900);
        userField.getStyleClass().add("login-input");
        userField.textProperty().addListener((_a, oldValue, newValue) -> {
            this.updateLoginBtnState(userField, userErrorLabel);
        });

        // User error
        setCanTakeAllSize(userErrorLabel);
        setCenterV(userErrorLabel);
        setCenterH(userErrorLabel);
        userErrorLabel.getStyleClass().add("login-error");
        userErrorLabel.setTranslateY(-45d);

        userErrorLabel.setMaxWidth(280);
        userErrorLabel.setTextAlignment(TextAlignment.LEFT);

        // Password
        setCanTakeAllSize(passwordField);
        setCenterV(passwordField);
        setCenterH(passwordField);
        passwordField.setPromptText("Mot de passe");
        passwordField.setMaxWidth(300);
        //passwordField.setTranslateY(-15d);
        //passwordField.setTranslateX(900);
        passwordField.getStyleClass().add("login-input");
        passwordField.textProperty().addListener((_a, oldValue, newValue) -> {
            this.updateLoginBtnState(passwordField, passwordErrorLabel);
        });


        // User error
        setCanTakeAllSize(passwordErrorLabel);
        setCenterV(passwordErrorLabel);
        setCenterH(passwordErrorLabel);
        passwordErrorLabel.getStyleClass().add("login-error");
        passwordErrorLabel.setTranslateY(10d);
        passwordErrorLabel.setMaxWidth(280);
        passwordErrorLabel.setTextAlignment(TextAlignment.LEFT);

        // Login button
        setCanTakeAllSize(btnLogin);
        setCenterV(btnLogin);
        setCenterH(btnLogin);
        btnLogin.setDisable(true);
        btnLogin.setMaxWidth(300);
        btnLogin.setTranslateY(40d);
        btnLogin.setTranslateX(900);
        btnLogin.getStyleClass().add("login-log-btn");
        btnLogin.setOnMouseClicked(e -> this.authenticate(userField.getText(), passwordField.getText()));


        setCanTakeAllSize(authModeChk);
        setCenterV(authModeChk);
        setCenterH(authModeChk);
        authModeChk.setTranslateY(90d);
        authModeChk.setTranslateX(5000);

        authModeChk.getStyleClass().add("login-mode-chk");
        authModeChk.setMaxWidth(300);

        authModeChk.selectedProperty().addListener((e, old, newValue) -> {
            offlineAuth.set(newValue);
            passwordField.setDisable(newValue);
            if (newValue) {
                userField.setPromptText("Pseudo");
                passwordField.clear();
            } else {
                userField.setPromptText("Adresse E-Mail");
            }

            btnLogin.setDisable(!(userField.getText().length() > 0 && (offlineAuth.get() || passwordField.getText().length() > 0)));
        });

        //setCanTakeAllSize(authModeChk);
        //setCenterV(authModeChk);
        //setCenterH(authModeChk);

        //authModeChk.getStyleClass().add("login-mode-chk");
        //authModeChk.maxWidth(300);
        //authModeChk.setTranslateY(85d);
        //authModeChk.selectedProperty().addListener((e, old, newValue);

        Separator separator = new Separator();
        setCanTakeAllSize(separator);
        setCenterH(separator);
        setCenterV(separator);
        separator.getStyleClass().add("login-separator");
        separator.setMaxWidth(300);
        separator.setTranslateY(90d);



        // Microsoft login button
        ImageView view = new ImageView(new Image("images/ms-logo.png"));
        view.setPreserveRatio(true);
        view.setFitHeight(30d);
        view.getStyleClass().add("logo-ms");
        setCanTakeAllSize(msLoginBtn);
        setCenterH(msLoginBtn);
        setCenterV(msLoginBtn);
        msLoginBtn.getStyleClass().add("ms-login-btn");
        msLoginBtn.setMaxWidth(300);
        msLoginBtn.setTranslateY(145d);
        msLoginBtn.setGraphic(view);
        msLoginBtn.setOnMouseClicked(e -> this.authenticateMS());

        this.layout.getChildren().addAll(title,userField, userErrorLabel, passwordField, passwordErrorLabel, authModeChk, btnLogin, separator, msLoginBtn);
        setCanTakeAllSize(authModeChk);
        setCenterV(authModeChk);
        setCenterH(authModeChk);
        authModeChk.setTranslateY(90d);
        authModeChk.setTranslateX(90d);
        authModeChk.getStyleClass().add("login-mode-chk");
        authModeChk.setMaxWidth(300);
        authModeChk.setTranslateY(85d);
        authModeChk.selectedProperty().addListener((e, old, newValue) -> {
            offlineAuth.set(newValue);
            passwordField.setDisable(newValue);
            if (newValue) {
                userField.setPromptText("Pseudo");
                passwordField.clear();
            } else {
                userField.setPromptText("Adresse E-Mail");
            }

            btnLogin.setDisable(!(userField.getText().length() > 0 && (offlineAuth.get() || passwordField.getText().length() > 0)));
        });

    }


    public void updateLoginBtnState(TextField textField, Label erroLabel) {
        if (offlineAuth.get() && textField == passwordField) return;
        if (textField.getText().length() == 0) {
            erroLabel.setText("Le champ ne peut être vide !");
        } else {
            erroLabel.setText("");
        }

        btnLogin.setDisable(!(userField.getText().length() > 0 && passwordField.getText().length() > 0));
    }

    public void authenticate(String user, String password) {
        if (!offlineAuth.get()) {
            Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);

            try {
                AuthResponse response = authenticator.authenticate(AuthAgent.MINECRAFT, user, password, null);

                saver.set("accessToken", response.getAccessToken());
                saver.set("clientToken", response.getClientToken());
                saver.save();

                AuthInfos infos = new AuthInfos(response.getSelectedProfile().getName(),
                        response.getAccessToken(), response.getClientToken(), response.getSelectedProfile().getId());

                Launcher.getInstance().setAuthInfos(infos);

                this.logger.info("Hello " + infos.getUsername());

                panelManager.showPanel((IPanel) new App());
            } catch (AuthenticationException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Une erreur est survenu lors de la connexion");
                alert.setContentText(e.getMessage());
                alert.show();
            }
        } else {
            AuthInfos infos = new AuthInfos(userField.getText(), null, null);
            saver.set("offline-username", infos.getUsername());
            saver.save();
            Launcher.getInstance().setAuthInfos(infos);

            this.logger.info("Hello " + infos.getUsername());

            panelManager.showPanel((IPanel) new App());
        }
    }

    public void authenticateMS() {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        authenticator.loginWithAsyncWebview().whenComplete((response, error) -> {
            if (error != null) {
                Launcher.getInstance().getLogger().err(error.toString());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText(error.getMessage());
                alert.show();
                return;
            }

            saver.set("msAccessToken", response.getAccessToken());
            saver.set("msRefreshToken", response.getRefreshToken());
            saver.save();
            Launcher.getInstance().setAuthInfos(new AuthInfos(
                    response.getProfile().getName(),
                    response.getAccessToken(),
                    response.getProfile().getId()
            ));
            this.logger.info("Hello " + response.getProfile().getName());
        });
    }
}