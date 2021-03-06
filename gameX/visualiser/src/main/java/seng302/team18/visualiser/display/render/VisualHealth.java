package seng302.team18.visualiser.display.render;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import seng302.team18.model.Boat;
import seng302.team18.visualiser.display.render.Renderable;

/**
 * Class to handle the health system client side.
 */
public class VisualHealth implements Renderable {

    private Image heartImage;
    private HBox heartBox;
    private ImageView heartView1;
    private ImageView heartView2;
    private ImageView heartView3;
    private Boat boat;


    /**
     * Construct a VisualHealth for displaying hearts to represent player health in certain game modes.
     *
     * @param pane Pane to draw hearts on.
     * @param boat Player's boat to represent lives for.
     */
    public VisualHealth(Pane pane, Boat boat) {
        this.boat = boat;
        heartImage = new Image("/images/race_view/heart.png");
        heartBox = new HBox();
        heartBox.setLayoutX(150);
        heartBox.setLayoutY(10);
        heartView1 = new ImageView(heartImage);
        heartView2 = new ImageView(heartImage);
        heartView3 = new ImageView(heartImage);
        pane.getChildren().add(heartBox);
    }


    /**
     * Display a number of hearts according to how many lives the player has.
     * Only re-draws the hearts if the current number displayed does not match the player's lives.
     */
    public void render() {
        if (boat.getLives() != heartBox.getChildren().size()) {
            heartBox.getChildren().clear();
            switch (boat.getLives()) {
                case 3:
                    heartBox.getChildren().add(heartView3);
                case 2:
                    heartBox.getChildren().add(heartView2);
                case 1:
                    heartBox.getChildren().add(heartView1);
            }
        }
    }


    public void refresh() {
        heartBox.getChildren().clear();
        heartView1 = new ImageView(heartImage);
        heartView2 = new ImageView(heartImage);
        heartView3 = new ImageView(heartImage);
        render();
    }
}
