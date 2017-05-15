package seng302.team18.visualiser.controller;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import seng302.team18.messageparsing.SocketMessageReceiver;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;
import seng302.team18.visualiser.display.RaceLoop;
import seng302.team18.visualiser.display.*;
import seng302.team18.visualiser.messageinterpreting.MessageInterpreter;


/**
 * The Controller class for the Main Window view.
 */
public class MainWindowController {
    @FXML private Group group;
    @FXML private Label timerLabel;
    @FXML private ToggleButton fpsToggler;
    @FXML private Label fpsLabel;
    @FXML private TableView tableView;
    @FXML private TableColumn<Boat, Integer> boatPositionColumn;
    @FXML private TableColumn<Boat, String> boatNameColumn;
    @FXML private TableColumn<Boat, Double> boatSpeedColumn;
    @FXML private Pane raceViewPane;
    @FXML private Polygon arrow;
    @FXML private WebView map;

    private Boolean onImportant;
    private Boolean boatNameImportant;
    private Boolean boatSpeedImportant;
    private Boolean estimatedTimeImportant;
    private Boolean timeSinceLastMarkImportant;

    private Race race;
    private RaceLoop raceLoop;
    private RaceRenderer raceRenderer;
    private CourseRenderer courseRenderer;
    private BackgroundRenderer backgroundRenderer;
    private RaceClock raceClock;
    private WindDirection windDirection;


    @FXML
    public void initialize() {
        onImportant = true;
        boatNameImportant = true;
        boatSpeedImportant = false;
        estimatedTimeImportant = false;
        timeSinceLastMarkImportant = false;
    }


    @FXML
    public void toggleFPS() {
        fpsLabel.setVisible(!fpsToggler.isSelected());
    }


    @FXML
    public void setFullAnnotationLevel() {
        onImportant = false;
        for (AnnotationType type : AnnotationType.values()) {
            raceRenderer.setVisibleAnnotations(type, true);
        }
    }


    @FXML
    public void setNoneAnnotationLevel() {
        onImportant = false;
        for (AnnotationType type : AnnotationType.values()) {
            raceRenderer.setVisibleAnnotations(type, false);
        }
    }


    @FXML
    public void setImportantAnnotationLevel() {
        onImportant = true;
        raceRenderer.setVisibleAnnotations(AnnotationType.NAME, boatNameImportant);
        raceRenderer.setVisibleAnnotations(AnnotationType.SPEED, boatSpeedImportant);
        raceRenderer.setVisibleAnnotations(AnnotationType.ESTIMATED_TIME_NEXT_MARK, estimatedTimeImportant);
        raceRenderer.setVisibleAnnotations(AnnotationType.TIME_SINCE_LAST_MARK, timeSinceLastMarkImportant);
    }


    public void toggleBoatName() {
        boatNameImportant = !boatNameImportant;
        if (onImportant) {
            setImportantAnnotationLevel();
        }
    }


    public void toggleBoatSpeed() {
        boatSpeedImportant = !boatSpeedImportant;
        if (onImportant) {
            setImportantAnnotationLevel();
        }
    }


    public void toggleEstimatedTime() {
        estimatedTimeImportant = !estimatedTimeImportant;
        if (onImportant) {
            setImportantAnnotationLevel();
        }
    }

    public void toggleTimeSinceLastMark() {
        timeSinceLastMarkImportant = !timeSinceLastMarkImportant;
        if (onImportant) {
            setImportantAnnotationLevel();
        }
    }


    /**
     * Sets the cell values for the race table, these are place, boat name and boat speed.
     */
    private void setUpTable() {
        Callback<Boat, Observable[]> callback =(Boat boat) -> new Observable[]{
                boat.placeProperty(),
        };
        ObservableList<Boat> observableList = FXCollections.observableArrayList(callback);
        observableList.addAll(race.getStartingList());

        SortedList<Boat> sortedList = new SortedList<>(observableList,
                (Boat boat1, Boat boat2) -> {
                    if( boat1.getPlace() < boat2.getPlace() ) {
                        return -1;
                    } else if( boat1.getPlace() > boat2.getPlace() ) {
                        return 1;
                    } else {
                        return 0;
                    }
                });
        tableView.setItems(sortedList);
        boatPositionColumn.setCellValueFactory(new PropertyValueFactory<>("place"));
        boatNameColumn.setCellValueFactory(new PropertyValueFactory<>("boatName"));
        boatSpeedColumn.setCellValueFactory(new PropertyValueFactory<>("speed"));
        boatSpeedColumn.setCellFactory(col -> new TableCell<Boat, Double>() {
            @Override
            public void updateItem(Double speed, boolean empty) {
                super.updateItem(speed, empty) ;
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.3f", speed));
                }
            }
        });
        tableView.getColumns().setAll(boatPositionColumn, boatNameColumn, boatSpeedColumn);
    }

    private void startWindDirection() {
        windDirection = new WindDirection(race, arrow, race.getCourse().getWindDirection());
        windDirection.start();
    }


//    public void startRace(long secondsDelay) {
//        final double KMPH_TO_MPS = 1000.0 / 3600.0;
////        double timeScaleFactor = race.getCourse().getCourseDistance()
////                / (race.getStartingList().get(0).getSpeed() * KMPH_TO_MPS) / race.getDuration();
////        secondsDelay /= (double) timeScaleFactor;
//        raceClock.start();
//        Timeline showLive = new Timeline(new KeyFrame(
//                Duration.seconds(secondsDelay),
//                event -> {
//                    raceClock = new RaceClock(timerLabel, race, 0d);
//                    raceClock.start();
//                    raceLoop.start();
//                }));
//        showLive.setCycleCount(1);
//        showLive.play();
//    }


    /**
     * initialises race variables and begins the race loop. Adds listers to the race view to listen for when the window
     * has been re-sized.
     * @param race The race which is going to be displayed.
     * @param interpreter A message interpreter.
     * @param receiver A socket message receiver.
     */
    public void setUp(Race race, MessageInterpreter interpreter, SocketMessageReceiver receiver) {
        this.race = race;

        raceRenderer = new RaceRenderer(race, group, raceViewPane);
        raceRenderer.renderBoats();
        courseRenderer =  new CourseRenderer(race.getCourse(), group, raceViewPane);
        backgroundRenderer = new BackgroundRenderer(group, race.getCourse(), map.getEngine());


        raceClock = new RaceClock(timerLabel);
        raceClock.start();

        raceLoop = new RaceLoop(race, raceRenderer, courseRenderer, new FPSReporter(fpsLabel), backgroundRenderer);
        startWindDirection();

        for (Boat boat : race.getStartingList()) {
            boat.setPlace(race.getStartingList().size());
        }

        raceLoop.start();

        raceViewPane.widthProperty().addListener((observableValue, oldWidth, newWidth) -> {
            courseRenderer.renderCourse();
            raceRenderer.renderBoats();
            raceRenderer.reDrawTrails(race.getStartingList());
            backgroundRenderer.renderBackground();
        });
        raceViewPane.heightProperty().addListener((observableValue, oldHeight, newHeight) -> {
            courseRenderer.renderCourse();
            raceRenderer.renderBoats();
            raceRenderer.reDrawTrails(race.getStartingList());
            backgroundRenderer.renderBackground();
        });
        setUpTable();
    }

    public RaceClock getRaceClock() {
        return raceClock;
    }

}
