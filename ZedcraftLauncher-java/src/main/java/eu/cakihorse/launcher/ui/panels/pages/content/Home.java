package eu.cakihorse.launcher.ui.panels.pages.content;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import eu.cakihorse.launcher.Launcher;
import eu.cakihorse.launcher.ui.PanelManager;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.DownloadList;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.flowupdater.versions.VersionType;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class Home extends ContentPanel{

    private final Saver saver = Launcher.getInstance().getSaver();
    GridPane boxPane = new GridPane();
    ProgressBar progressBar = new ProgressBar();
    Label stepLabel = new Label();
    Label fileLabel = new Label();
    public boolean isDownloading = false;

    @Override
    public String getName() {
        return "home";

    }

    @Override
    public String getStylesheetPath() {
        return "css/content/home.css";
    }

    @Override
    public void init(PanelManager panelManager) {
        super.init(panelManager);


        progressBar.getStyleClass().clear();
        progressBar.getStyleClass().add("progressBar");
        stepLabel.getStyleClass().add("download-status");
        fileLabel.getStyleClass().add("download-status");

        progressBar.setTranslateY(-15);
        setCenterH(progressBar);
        setCanTakeAllWidth(progressBar);

        stepLabel.setTranslateY(5);
        setCenterH(stepLabel);
        setCanTakeAllSize(stepLabel);

        fileLabel.setTranslateY(5);
        setCenterH(fileLabel);
        setCanTakeAllSize(fileLabel);

        this.showPlayButton();

    }

    private void showPlayButton() {
        Button playBtn = new Button("C'est Parti !");
        FontAwesomeIconView playIcon = new FontAwesomeIconView(FontAwesomeIcon.PLAY);
        setCanTakeAllSize(playBtn);
        setCenterH(playBtn);
        setCenterV(playBtn);
        playBtn.getStyleClass().add("play-btn");
        playBtn.setGraphic(playIcon);
        playBtn.setTranslateX(450);
        playBtn.setTranslateY(120);
        playBtn.setOnMouseClicked(e -> this.play());
        boxPane.getChildren().add(playBtn);
        this.layout.getChildren().add(boxPane);
    }

    private void play() {
        isDownloading = true;
        boxPane.getChildren().clear();
        setProgress(0, 0);
        this.layout.getChildren().addAll(progressBar, stepLabel, fileLabel);


        Platform.runLater(() -> new Thread(this::update).start());
    }

    public void update(){

        IProgressCallback callback = new IProgressCallback() {

            private final DecimalFormat decimalFormat = new DecimalFormat("#.#");
            private String stepTxt = "";
            private String percentTxt = "0.0%";
            @Override
            public void step(Step step) {
                Platform.runLater(() -> {
                    stepTxt = StepInfo.valueOf(step.name()).getDetails();
                    setStatus(String.format("%s, (%s)", stepTxt, percentTxt));
                });
            }

            //@Override
            public void update(long downloaded, long max) {
                Platform.runLater(() -> {
                    percentTxt = decimalFormat.format(downloaded + 100.d / max) + "%";
                    setStatus(String.format("%s, (%s)", stepTxt, percentTxt));
                    setProgress(downloaded, max);
                });
            }

            @Override
            public void onFileDownloaded(Path path) {
                Platform.runLater(() -> {
                    String p = path.toString();
                    fileLabel.setText("..." + p.replace(Launcher.getInstance().getLauncherDir().toFile().getAbsolutePath(), ""));
                });
            }
        };

        try {
            final VanillaVersion vanillaVersion = new VanillaVersion.VanillaVersionBuilder()
                    .withName("1.16.5")
                    .withVersionType(VersionType.VANILLA)
                    .build();

            final FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder()
                    .withVanillaVersion(vanillaVersion)
                    .withLogger(Launcher.getInstance().getLogger())
                    .withProgressCallback(callback)
                    .build();

            updater.update(Launcher.getInstance().getLauncherDir());
            this.startGame(updater.getVanillaVersion().getName());

        }catch(Exception exception) {
            Launcher.getInstance().getLogger().err(exception.toString());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Un probleme est survenu");
            alert.setContentText(exception.getMessage());
            alert.show();
        }

    }

    public void startGame(String gameVersion){
        File gameFolder = Launcher.getInstance().getLauncherDir().toFile();

        GameInfos infos = new GameInfos(
                "Launcher",
                gameFolder,
                new GameVersion(gameVersion, GameType.V1_13_HIGHER_VANILLA),
                new GameTweak[]{
                }
        );

        Thread t = new Thread();
        try {
            ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(infos, GameFolder.FLOW_UPDATER,Launcher.getInstance().getAuthInfos());
            profile.getVmArgs().add(this.getRamArgsFromSaver());
            ExternalLauncher launcher = new ExternalLauncher(profile);

            Process p = launcher.launch();
            p.waitFor();

            System.exit(0);
        }catch (Exception exception){
            exception.printStackTrace();
            Launcher.getInstance().getLogger().err(exception.toString());
            }
        t.start();

        Platform.exit();
    }


    public String getRamArgsFromSaver() {
            int val = 1024;
        try {
            if (saver.get("maxRam") != null) {
                val = Integer.parseInt(saver.get("maxRam"));
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException error) {
            saver.set("maxRam", String.valueOf(val));
            saver.save();
        }
        return "-Xmx" + val + "M";

    }

    public void setStatus(String status) {

            this.stepLabel.setText(status);

    }

    public void setProgress(double current, double max) {

        this.progressBar.setProgress(current / max);

    }



    public enum StepInfo {
        READ("Lecture du fichier json..."),
        DL_LIBS("Téléchargement des libraries..."),
        DL_ASSETS("Téléchargement des ressources..."),
        EXTRACT_NATIVES("Extraction des natives..."),
        FORGE("Installation de forge..."),
        FABRIC("Installation de fabric..."),
        MODS("Téléchargement des mods..."),
        EXTERNAL_FILES("Téléchargement des fichier externes..."),
        POST_EXECUTIONS("Exécution post-installation..."),
        END("Vous pouvez jouer !");
        String details;

        StepInfo(String details) {
            this.details = details;
        }

        public String getDetails() {
            return details;
        }
    }
}
