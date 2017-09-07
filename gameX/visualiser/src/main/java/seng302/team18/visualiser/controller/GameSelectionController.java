package seng302.team18.visualiser.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import seng302.team18.messageparsing.AC35MessageParserFactory;
import seng302.team18.messageparsing.Receiver;
import seng302.team18.model.RaceMode;
import seng302.team18.send.ControllerMessageFactory;
import seng302.team18.send.Sender;
import seng302.team18.visualiser.ClientRace;
import seng302.team18.visualiser.util.ConfigReader;

import javax.net.SocketFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Controller For the game type selection screen
 */
public class GameSelectionController {

    @FXML private Pane outerPane;
    @FXML private Pane innerPane;
    private Stage stage;


    private Label raceLabel;
    private Image raceButtonImage;
    private RaceMode mode;

    private int colourIndex = 0;
    private List<Color> boatColours = Arrays.asList(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
            Color.CYAN, Color.BLUE, Color.PURPLE, Color.MAGENTA);


    @FXML
    public void initialize() {
        initialiseRaceButton();
        initialiseArcadeButton();
        initialiseSpyroButton();
        initialiseBumperBoatsButton();
        initialiseBackButton();
        registerListeners();
    }


    /**
     * Register any necessary listeners.
     */
    private void registerListeners() {
        outerPane.widthProperty().addListener((observableValue, oldWidth, newWidth) -> reDraw());
        outerPane.heightProperty().addListener((observableValue, oldHeight, newHeight) -> reDraw());
    }


    /**
     * Re draw the the pane which holds elements of the play interface to be in the middle of the window.
     */
    public void reDraw() {
        if (!(innerPane.getWidth() == 0)) {
            innerPane.setLayoutX((outerPane.getScene().getWidth() / 2) - (innerPane.getWidth() / 2));
            innerPane.setLayoutY((innerPane.getScene().getHeight() / 2) - (innerPane.getHeight() / 2));
        }

        else if (stage != null) {
            innerPane.setLayoutX((stage.getWidth() / 2) - (innerPane.getPrefWidth()/2));
            innerPane.setLayoutY((stage.getHeight() / 2) - (innerPane.getPrefHeight()/2));
        }
    }


    /**
     * Set up the button for starting a race.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseRaceButton() {
        raceLabel = new Label();
        raceLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        raceLabel.getStyleClass().add("raceImage");
        innerPane.getChildren().add(raceLabel);

        raceButtonImage = new Image("/images/RaceWhite.png");
        raceLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) raceButtonImage.getWidth(), 2)));
        raceLabel.setLayoutY((600 / 2));
        raceLabel.setOnMouseClicked(event -> hostRace());
    }


    /**
     * Set up the button for starting an arcade race.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseArcadeButton() {
        raceLabel = new Label();
        raceLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        raceLabel.getStyleClass().add("arcadeImage");
        innerPane.getChildren().add(raceLabel);

        raceButtonImage = new Image("/images/Arcade_Race_White.png");
        raceLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) raceButtonImage.getWidth(), 2)));
        raceLabel.setLayoutY((600 / 2) + 50);
        raceLabel.setOnMouseClicked(event -> startArcade());
    }


    /**
     * Set up the button for starting a challenge mode game.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseSpyroButton() {
        raceLabel = new Label();
        raceLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        raceLabel.getStyleClass().add("spyroImage");
        innerPane.getChildren().add(raceLabel);

        raceButtonImage = new Image("/images/SpyroWhite.png");
        raceLabel.setLayoutX((600 / 2) - 150);
        raceLabel.setLayoutY((600 / 2) + 100);
        raceLabel.setOnMouseClicked(event -> startBumperBoats());
    }


    /**
     * Set up the button for starting a bumper boats game.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseBumperBoatsButton() {
        raceLabel = new Label();
        raceLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        raceLabel.getStyleClass().add("bumperBoatsImage");
        innerPane.getChildren().add(raceLabel);

        raceButtonImage = new Image("/images/bumper-boatsWhite.png");
        raceLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) raceButtonImage.getWidth(), 2)));
        raceLabel.setLayoutY((600 / 2) + 150);
        raceLabel.setOnMouseClicked(event -> startBumperBoats());
    }


    /**
     * Set up the button for getting back to the title screen.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseBackButton() {
        raceLabel = new Label();
        raceLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        raceLabel.getStyleClass().add("backButtonImage");
        innerPane.getChildren().add(raceLabel);

        raceButtonImage = new Image("/images/back_button.gif");
        raceLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) raceButtonImage.getWidth(), 2)));
        raceLabel.setLayoutY((600 / 2) + 200);
        raceLabel.setOnMouseClicked(event -> backButtonAction());
    }


    /**
     * Creates a controller manager object and begins an instance of the program.
     *
     * @throws Exception A connection error
     */
    @SuppressWarnings("Duplicates")
    private void startConnection(Receiver receiver, Sender sender) throws Exception {
        Stage stage = (Stage) innerPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("PreRace.fxml"));
        Parent root = loader.load();
        PreRaceController controller = loader.getController();
        stage.setTitle("High Seas");
        outerPane.getScene().setRoot(root);
        stage.show();

        ClientRace race = new ClientRace();
        race.setMode(mode);
        controller.setUp(race, receiver, sender);
        System.out.println(boatColours.get(colourIndex));
        controller.initConnection(boatColours.get(colourIndex));
    }

    /**
     * Opens a socket and connection on the given host and port number
     * @param host The host IP address for the socket
     * @param port The port number used for the socket
     */
    private void openStream(String host, int port) {
        try {
            Socket socket = SocketFactory.getDefault().createSocket(host, port);
            startConnection(new Receiver(socket, new AC35MessageParserFactory()), new Sender(socket, new ControllerMessageFactory()));
        } catch (Exception e) {
            System.out.println();        }
    }



    /**
     * Creates the model in a new process.
     * Reads in the file path to the model jar from the config file "visualiser-config.txt" (from the same directory).
     */
    @SuppressWarnings("Duplicates")
    private void createModel() {
        final String CONFIG_FILE_NAME = "visualiser-config.txt";
        final List<String> tokens = Collections.singletonList("MODEL_PATH");
        ConfigReader reader = new ConfigReader(tokens);
        InputStream configStream = null;
        try {
            configStream = new FileInputStream(CONFIG_FILE_NAME);
            String filePath = reader.parseConfig(configStream).get("MODEL_PATH");
            Runtime.getRuntime().exec("java -jar " + filePath);
        } catch (IOException e) {
            if (null == configStream) {
                System.out.println("You don't have a config file"); // TODO August 12 csl62 have to show error but title screen incomplete
            } else {
                e.printStackTrace();
            }
        }
    }


    /**
     * returns the player to the playInterface
     */
    @SuppressWarnings("Duplicates")
    public void backButtonAction(){
        Stage stage = (Stage) innerPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("PlayInterface.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.err.println("Error occurred loading play interface screen");
        }
        PlayInterfaceController controller = loader.getController();
        controller.setStage(stage);
        stage.setTitle("High Seas");
        outerPane.getScene().setRoot(root);
        stage.setMaximized(true);
        stage.show();
    }


    /**
     * Act on user pressing the host new game button.
     * Starts a bumper boats game
     */
    private void startBumperBoats(){
        System.out.println("BumperBoats");
    }


    /**
     * Act on user pressing the host new game button.
     * Starts game mode
     */
    public void hostRace(){
        createModel();
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mode = RaceMode.RACE;
        openStream("127.0.0.1", 5005);
    }


    /**
     * Act on user pressing the host new game button.
     * Starts and arcade game
     */
    private void startArcade() {
        createModel();
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mode = RaceMode.ARCADE;
        openStream("127.0.0.1", 5005);
    }


    public Stage getStage() {
        return stage;
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public int getColourIndex() {
        return colourIndex;
    }

    public void setColourIndex(int colourIndex) {
        this.colourIndex = colourIndex;
    }
}
