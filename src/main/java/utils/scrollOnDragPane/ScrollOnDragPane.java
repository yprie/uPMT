package utils.scrollOnDragPane;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ScrollOnDragPane extends ScrollPane implements Initializable {

    private double scrollThreshold = 0.15;
    private double pixelScrolledPerSeconds = 250;
    private Animation scrollXAnimation = null;
    private Animation scrollYAnimation = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setOnDragOver(dragEvent -> { onDragOver(dragEvent.getX(), dragEvent.getY()); });
        setOnMouseExited(mouseEvent-> { stopXAndYScroll(); });
        setOnDragExited(dragEvent -> { stopXAndYScroll(); });
        setOnDragDropped(dragEvent -> { stopXAutoScroll(); });
    }

    public void setScrollingSpeed(double pixelPerSeconds) { pixelScrolledPerSeconds = pixelPerSeconds; }




    private void onDragOver(double mouseX, double mouseY) {
        //Horizontal detection
        double xPercent = mouseX/getWidth();
        if(xPercent <= scrollThreshold || xPercent >= 1 - scrollThreshold){
            if(scrollXAnimation == null) {
                scrollXAnimation = horizontalProgressiveScroll(this, xPercent > 0.5, pixelScrolledPerSeconds);
                scrollXAnimation.play();
            }
        }
        else
            stopXAutoScroll();

        //Vertical detection
        double yPercent = mouseY/getHeight();
        if(yPercent <= scrollThreshold || yPercent >= 1 - scrollThreshold){
            if(scrollYAnimation == null) {
                scrollYAnimation = verticalProgressiveScroll(this, yPercent > 0.5, pixelScrolledPerSeconds);
                scrollYAnimation.play();
            }
        }
        else
            stopYAutoScroll();
    }

    private void stopXAutoScroll() {
        if (scrollXAnimation != null){
            scrollXAnimation.stop();
            scrollXAnimation = null;
        }
    }

    private void stopYAutoScroll() {
        if (scrollYAnimation != null){
            scrollYAnimation.stop();
            scrollYAnimation = null;
        }
    }

    private void stopXAndYScroll() { stopXAutoScroll(); stopYAutoScroll();}

    private static Animation horizontalProgressiveScroll(ScrollPane pane, boolean rightScroll, double scrollingSpeed) {
        double dist = computeScrollDistance(pane.getHvalue(), rightScroll, pane.getContent().getBoundsInLocal().getWidth(), pane.getViewportBounds().getWidth());
        return new Timeline(
                new KeyFrame(Duration.seconds(dist / scrollingSpeed), new KeyValue(pane.hvalueProperty(), rightScroll ? 1 : 0))
        );
    }

    private static Animation verticalProgressiveScroll(ScrollPane pane, boolean bottomScroll, double scrollingSpeed) {
        double dist = computeScrollDistance(pane.getVvalue(), bottomScroll, pane.getContent().getBoundsInLocal().getHeight(), pane.getViewportBounds().getHeight());
        return new Timeline(
                new KeyFrame(Duration.seconds(dist / scrollingSpeed), new KeyValue(pane.vvalueProperty(), bottomScroll ? 1 : 0))
        );
    }

    private static double computeScrollDistance(double paneCursorValue, boolean positiveScroll, double innerLength, double viewPortLength) {
        double target = positiveScroll ? innerLength - viewPortLength/2 : viewPortLength / 2;
        double center = (innerLength - viewPortLength) * paneCursorValue + viewPortLength/2; //affine function for mapping cursor value to content center
        return Math.abs(target - center);
    }

}
