package seng302.team18.visualiser.controller;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import seng302.team18.model.RaceMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Controller For the game type selection screen
 */
public class GameSelectionController {

    @FXML
    private Pane buttonBox;
    @FXML
    private Pane optionsBox;
    @FXML
    private Pane outerPane;
    @FXML
    private Label errorLabel;
    @FXML
    private Pane boatView;
    @FXML
    private Label arrowLeft;
    @FXML
    private Label arrowRight;

    private Stage stage;

    private StringProperty ipStrProp;
    private StringProperty portStrProp;

    private RaceMode mode;
    private Boolean isHosting;

    private final static double Y_POS_BOAT_VIEW = -200;
    private final static double Y_POS_SELECTION_BOX = -250;
    private final static double Y_POS_ERROR_TEXT = -150;
    private final static double Y_POS_BUTTON_BOX = -40;
    private final static double OPTION_BUTTONS_HEIGHT = 50;
    private final static double ARROW_Y_GAP = 5;
    private final static double BOAT_SIZE = 200;

    private ImageView selectModeView;
    private Image selectModeImage;
    private ImageView customiseBoatView;
    private Image customiseImage;

    private Polyline boat;
    private int colourIndex = 0;
    private List<Color> boatColours = Arrays.asList(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
            Color.CYAN, Color.BLUE, Color.PURPLE, Color.MAGENTA);

    @FXML
    public void initialize() {
        initialiseHeaders();
        initialiseBoatPicker();
        setUpModeSelection();

        final double height = Y_POS_BUTTON_BOX - Y_POS_ERROR_TEXT;
        errorLabel.setMaxHeight(height);
        errorLabel.setMinHeight(height);
        errorLabel.setPrefHeight(height);

        registerListeners();
    }


    /**
     * Register any necessary listeners.
     */
    private void registerListeners() {
        outerPane.widthProperty().addListener(((observable, oldValue, newValue) -> reDraw()));
        outerPane.heightProperty().addListener(((observable, oldValue, newValue) -> reDraw()));
    }


    /**
     * Re draw the the pane which holds elements of the play interface to be in the middle of the window.
     */
    public void reDraw() {


        // button box
        //noinspection Duplicates
        if (buttonBox.getWidth() != 0) {
            buttonBox.setLayoutX((outerPane.getScene().getWidth() / 3) - (buttonBox.getWidth() / 2));
            buttonBox.setLayoutY((outerPane.getScene().getHeight() / 2) + Y_POS_BUTTON_BOX);
            selectModeView.setLayoutX(buttonBox.getLayoutX());
            selectModeView.setLayoutY(buttonBox.getLayoutY() - selectModeImage.getHeight() * 2);
        } else if (stage != null) {
            buttonBox.setLayoutX((stage.getWidth() / 3) - (buttonBox.getPrefWidth() / 2));
            selectModeView.setLayoutX(buttonBox.getLayoutX());
            buttonBox.setLayoutY((stage.getHeight() / 2) + Y_POS_BUTTON_BOX);
        }

        // error text
        //noinspection Duplicates
        if (errorLabel.getWidth() != 0) {
            errorLabel.setLayoutX((outerPane.getScene().getWidth() / 3) - (errorLabel.getWidth() / 2));
            errorLabel.setLayoutY((outerPane.getScene().getHeight() / 2) + Y_POS_ERROR_TEXT);
        } else if (stage != null) {
            errorLabel.setLayoutX((stage.getWidth() / 3) - (errorLabel.getPrefWidth() / 2));
            errorLabel.setLayoutY((stage.getHeight() / 2) + Y_POS_ERROR_TEXT);
        }

        if (boatView.getWidth() != 0) {
            boatView.setLayoutX((outerPane.getScene().getWidth() / 2) + buttonBox.getWidth() - (0.75 * BOAT_SIZE));
            customiseBoatView.setLayoutX((outerPane.getScene().getWidth() / 2) + buttonBox.getWidth() - (0.75 * BOAT_SIZE));
            boatView.setLayoutY((outerPane.getScene().getHeight() / 2) + Y_POS_BUTTON_BOX);
        } else if (stage != null) {
            boatView.setLayoutX((stage.getWidth() / 2) + (300 * 1.75));
            boatView.setLayoutY((stage.getHeight() / 2) + Y_POS_BUTTON_BOX);
        }

        final double arrowWidth = 55;
        arrowLeft.setLayoutX(boatView.getLayoutX());
        arrowLeft.setLayoutY(boatView.getLayoutY() + boatView.getHeight() + ARROW_Y_GAP);
        arrowRight.setLayoutX(boatView.getLayoutX() + boatView.getWidth() - arrowWidth);
        arrowRight.setLayoutY(boatView.getLayoutY() + boatView.getHeight() + ARROW_Y_GAP);
    }


    public void initialiseHeaders() {
        customiseImage = new Image("/images/game_selection/customise_boat.gif");
        customiseBoatView = new ImageView(customiseImage);
        outerPane.getChildren().add(customiseBoatView);

        selectModeImage = new Image("/images/game_selection/select_game_mode.gif");
        selectModeView = new ImageView(selectModeImage);
        outerPane.getChildren().add(selectModeView);
    }


    private void setUpModeSelection() {
        setOptionButtons(Arrays.asList(
                createRaceButton(),
                createArcadeButton(),
                createChallengeButton(),
                createBumperBoatsButton(),
                createSpectatorButton()
        ));

        reDraw();
    }


    private void setUpConnectionType(RaceMode mode) {
        this.mode = mode;

        setOptionButtons(Arrays.asList(
                createJoinButton(),
                createCreateButton()
        ));

        reDraw();
    }


    private void setUpConnectionOptions(boolean isHosting) {
        this.isHosting = isHosting;

        setOptionButtons(Arrays.asList(
                createHostEntry(),
                createPlayButton()
        ));

        reDraw();
    }


    private ImageView getTrailGameMode(RaceMode mode) {
        String url = "/images/game_selection/game_mode.png";
        switch (mode) {
            case RACE:
                url = "images/game_selection/race_white.png";
                break;
            case ARCADE:
                url = "images/game_selection/arcade_white.png";
                break;
            case CHALLENGE_MODE:
                url = "images/game_selection/challenge_white.png";
                break;
            case BUMPER_BOATS:
                url = "images/game_selection/bumper_white.png";
                break;
            case SPECTATION:
                url = "images/game_selection/spectator_white.png";
                break;
        }

        return new ImageView(url);
    }


    private ImageView getTrailConnectionMode(boolean isHosting) {
        String url = (isHosting) ? "images/game_selection/create_game_white.png" : "images/game_selection/join_game_white.png";
        return new ImageView(url);
    }


    /**
     * Set up the button for starting a race.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private Labeled createRaceButton() {
        Label label = new Label();
        label.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        label.getStyleClass().add("raceImage");
        label.setOnMouseClicked(event -> setUpConnectionType(RaceMode.RACE));
        return label;
    }


    /**
     * Set up the button for starting an arcade race.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private Labeled createArcadeButton() {
        Label label = new Label();
        label.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        label.getStyleClass().add("arcadeImage");
        label.setOnMouseClicked(event -> setUpConnectionType(RaceMode.ARCADE));
        return label;
    }


    /**
     * Set up the button for starting a challenge mode game.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private Labeled createChallengeButton() {
        Label label = new Label();
        label.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        label.getStyleClass().add("challengeImage");
        label.setOnMouseClicked(event -> setUpConnectionType(RaceMode.CHALLENGE_MODE));
        return label;
    }


    /**
     * Set up the button for starting a bumper boats game.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private Labeled createBumperBoatsButton() {
        Label label = new Label();
        label.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        label.getStyleClass().add("bumperBoatsImage");
        label.setOnMouseClicked(event -> setUpConnectionType(RaceMode.BUMPER_BOATS));
        return label;
    }


    /**
     * Sets up the button for spectating an existing game,
     * Image used will changed when hovered over as defined in the playInterface css.
     */
    private Labeled createSpectatorButton() {
        Label label = new Label();
        label.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        label.getStyleClass().add("spectatorImage");
        label.setOnMouseClicked(event -> setUpConnectionType(RaceMode.SPECTATION));
        return label;
    }


    /**
     * Sets up the button for spectating an existing game,
     * Image used will changed when hovered over as defined in the playInterface css.
     */
    private Labeled createCreateButton() {
        Label label = new Label();
        label.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        label.getStyleClass().add("createImage");
        label.setOnMouseClicked(event -> setUpConnectionOptions(true));
        return label;
    }


    /**
     * Sets up the button for spectating an existing game,
     * Image used will changed when hovered over as defined in the playInterface css.
     */
    private Labeled createJoinButton() {
        Label label = new Label();
        label.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        label.getStyleClass().add("joinImage");
        label.setOnMouseClicked(event -> setUpConnectionOptions(false));
        return label;
    }


    private Region createHostEntry() {
        Control ipEntry = createIpField();
        Control portEntry = createPortField();
        AnchorPane pane = new AnchorPane();

        final double width = 300;
        final double height = 45;
        pane.setMinWidth(width);
        pane.setPrefWidth(width);
        pane.setMaxWidth(width);
        pane.setMinHeight(height);
        pane.setPrefHeight(height);
        pane.setMaxHeight(height);

        AnchorPane.setBottomAnchor(ipEntry, 0D);
        AnchorPane.setTopAnchor(ipEntry, 0D);
        AnchorPane.setLeftAnchor(ipEntry, 0D);

        AnchorPane.setBottomAnchor(portEntry, 0D);
        AnchorPane.setTopAnchor(portEntry, 0D);
        AnchorPane.setRightAnchor(portEntry, 0D);

        pane.getChildren().add(ipEntry);
        pane.getChildren().add(portEntry);

        return pane;
    }


    private boolean isIpFirstFocus = true;

    private Control createIpField() {
        TextField field = new TextField();
        field.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        field.getStyleClass().add("addressInput");
        field.setPromptText("host address");

        final int height = 45;
        final int width = 200;
        field.setMaxHeight(height);
        field.setPrefHeight(height);
        field.setMinHeight(height);
        field.setMaxWidth(width);
        field.setPrefWidth(width);
        field.setMinWidth(width);

        if (isHosting) {
            field.setText("127.0.0.1");
            field.setDisable(true);
        }

        ipStrProp = field.textProperty();

        isIpFirstFocus = true;
        field.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (isIpFirstFocus && newValue) {
                outerPane.requestFocus();
                isIpFirstFocus = false;
            }
        });

        return field;
    }


    private Control createPortField() {
        final TextField field = new TextField("5005");
        field.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        field.getStyleClass().add("addressInput");
        field.setPromptText("port");

        final int height = 45;
        final int width = 95;
        field.setMaxHeight(height);
        field.setPrefHeight(height);
        field.setMinHeight(height);
        field.setMaxWidth(width);
        field.setPrefWidth(width);
        field.setMinWidth(width);

        portStrProp = field.textProperty();

        return field;
    }


    private Labeled createPlayButton() {
        Label label = new Label();
        label.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        label.getStyleClass().add("playImage");
        label.setOnMouseClicked(event ->
                new GameConnection(
                        errorLabel.textProperty(),
                        outerPane,
                        mode,
                        boatColours.get(colourIndex)
                ).startGame(
                        ipStrProp.get(),
                        portStrProp.get(),
                        isHosting
                )
        );
        return label;
    }


    /**
     * returns the player to the playInterface
     */
    @FXML
    private void backButtonAction() {
        if (mode == null) {
            exitSelectionScreen();

        } else if (isHosting == null) {
            // Go back to mode selection
            mode = null;
            setUpModeSelection();
        } else {
            // go back to hosting selection
            isHosting = null;
            setUpConnectionType(mode);
        }
    }


    private void exitSelectionScreen() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("StartupInterface.fxml"));
        Parent root = null; // throws IOException
        try {
            root = loader.load();
        } catch (IOException e) {
            System.err.println("Error occurred loading title screen.");
        }
        TitleScreenController controller = loader.getController();
        controller.setStage(stage);
        outerPane.getScene().setRoot(root);
        stage.setMaximized(true);
        stage.show();
    }


    /**
     * Creates buttons for left and right selection and sets up a preview of the boat with the current colour.
     */
    private void initialiseBoatPicker() {
        Double[] boatShape = new Double[]{
                0.0, BOAT_SIZE / -2,
                BOAT_SIZE / -2, BOAT_SIZE / 2,
                BOAT_SIZE / 2, BOAT_SIZE / 2,
                0.0, BOAT_SIZE / -2
        };

        boat = new Polyline();
        boat.getPoints().addAll(boatShape);
        boat.setRotate(90);
        boat.setFill(boatColours.get(colourIndex));
        boat.setStrokeWidth(BOAT_SIZE / 40);
        boatView.getChildren().add(boat);
        boatView.setPadding(new Insets(BOAT_SIZE / 8));
        boat.toFront();
    }


    /**
     * Display the next boat colour option.
     */
    @FXML
    private void rightButtonAction() {
        colourIndex = (colourIndex + 1) % boatColours.size();
        boat.setFill(boatColours.get(colourIndex));
    }


    /**
     * Display the previous boat colour option.
     */
    @FXML
    private void leftButtonAction() {
        colourIndex = Math.floorMod((colourIndex - 1), boatColours.size());
        boat.setFill(boatColours.get(colourIndex));
    }


    void setStage(Stage stage) {
        this.stage = stage;
    }


    private void setOptionButtons(List<Region> buttons) {
        double x = optionsBox.getLayoutX();
        double y = optionsBox.getLayoutY();

        optionsBox.getChildren().setAll(buttons);

        for (Region button : buttons) {
            button.setLayoutX(x);
            button.setLayoutY(y);

            y += OPTION_BUTTONS_HEIGHT;
        }
    }
}
