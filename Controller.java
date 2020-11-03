package fractalTreeDraw;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class Controller extends Canvas implements Initializable {

    @FXML
    private Canvas CANVAS = new Canvas();

    @FXML
    private AnchorPane ANCHORPANE = new AnchorPane();

    @FXML
    private Slider ANGLE_SLIDER = new Slider();

    @FXML
    private Slider LENGTH_SLIDER = new Slider();

    @FXML
    private TextField TEXTFIELD = new TextField();

    @FXML
    private CheckBox CHECKBOX_RANDOM = new CheckBox();

    private GraphicsContext GRAPHICSCONTEXT;
    private double ANGLE = 25;
    private double TRUNK_LENGTH = 165;
    private double TRUNK_WIDTH = 15;
    private double DEPTH = 10;
    private double RIGHT_BRANCH_RANDOM_NUMBER = 1;
    private double LEFT_BRANCH_RANDOM_NUMBER = 1;
    //private double GOLDEN_RATIO = (1 - Math.sqrt(5)) / 2;

    //  Clears canvas, moves to drawing position, calls branch(), moves back to start
    private void drawFractalTree(double TRUNK_LENGTH, double TRUNK_WIDTH, double DEPTH) {

        // set center translation
        double width = CANVAS.getWidth();
        double height = CANVAS.getHeight();

        //  Clear Canvas
        GRAPHICSCONTEXT.clearRect(0, 0, width, height);

        // move to Start position
        GRAPHICSCONTEXT.translate(width / 2, height);

        // create tree
        GRAPHICSCONTEXT.setLineWidth(1);
        branch(TRUNK_LENGTH, TRUNK_WIDTH, DEPTH);

        // return to Start
        GRAPHICSCONTEXT.translate(0, TRUNK_LENGTH);

        // return to 0,0
        GRAPHICSCONTEXT.translate(-(width / 2), -height);
    }

    //  draws branches
    private void branch(double TRUNK_LENGTH, double TRUNK_WIDTH, double DEPTH) {

        //  Set trunk width
        GRAPHICSCONTEXT.setLineWidth(TRUNK_WIDTH);

        GRAPHICSCONTEXT.setLineCap(StrokeLineCap.ROUND);

        //  Set color based on trunkSize
        if (DEPTH <= 2) {
            GRAPHICSCONTEXT.setStroke(Color.rgb(0, 50, 0));
        } else {
            GRAPHICSCONTEXT.setStroke(Color.rgb(50, 25, 0));
        }

        // draw line
        GRAPHICSCONTEXT.strokeLine(0, 0, 0, -TRUNK_LENGTH);

        // translate to end of line drawn
        GRAPHICSCONTEXT.translate(0, -TRUNK_LENGTH);

        //  return if at end of depth
        if (DEPTH < 1) {
            GRAPHICSCONTEXT.setStroke(Color.rgb(50, 150, 0));
            GRAPHICSCONTEXT.strokeOval(-TRUNK_WIDTH, -TRUNK_WIDTH, 5, 10);
            return;
        }

        // Right Branch
        GRAPHICSCONTEXT.rotate(ANGLE * RIGHT_BRANCH_RANDOM_NUMBER);
        branch(TRUNK_LENGTH * 2/3, TRUNK_WIDTH * 2/3, DEPTH - 1);
        GRAPHICSCONTEXT.translate(0, TRUNK_LENGTH * 2/3);
        GRAPHICSCONTEXT.rotate(-ANGLE * RIGHT_BRANCH_RANDOM_NUMBER);

        // Left Branch
        GRAPHICSCONTEXT.rotate(-ANGLE * LEFT_BRANCH_RANDOM_NUMBER);
        branch(TRUNK_LENGTH * 2/3, TRUNK_WIDTH * 2/3, DEPTH - 1);
        GRAPHICSCONTEXT.translate(0, TRUNK_LENGTH * 2/3);
        GRAPHICSCONTEXT.rotate(ANGLE * LEFT_BRANCH_RANDOM_NUMBER);

    }

    private void sleep(int i) {
        try {
            TimeUnit.MICROSECONDS.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        //System.out.println(keyEvent.getCode());
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {

            try {
                double MESSAGE = Integer.parseInt(TEXTFIELD.getText());
                DEPTH = MESSAGE;
                drawFractalTree(TRUNK_LENGTH, TRUNK_WIDTH, DEPTH);
            } catch (NumberFormatException nfe) {
                TEXTFIELD.setText("# please");
                System.out.println("NumberFormatException: " + nfe.getMessage());
            }
        }
    }

    public void handleMouseClicked(MouseEvent mouseEvent) {
        TEXTFIELD.setText("Children: " + DEPTH);

        drawFractalTree(TRUNK_LENGTH, TRUNK_WIDTH, DEPTH);
        System.out.println("Mouse click");
    }

    public void handleSliderDrag(MouseEvent mouseEvent) {
        ANGLE = ANGLE_SLIDER.getValue();
        TRUNK_LENGTH = LENGTH_SLIDER.getValue();
        drawFractalTree(TRUNK_LENGTH, TRUNK_WIDTH, DEPTH);
        System.out.println("DRAGGED. Angle :" + ANGLE + "| LENGTH: " + TRUNK_LENGTH);
    }

    public void handleCheckBox(ActionEvent event) {

        //  If box checked
        if (CHECKBOX_RANDOM.isSelected()) {
            CHECKBOX_RANDOM.setText("Asymmetrical");
            randomNumber();
            System.out.println("Asymmetrical: " + RIGHT_BRANCH_RANDOM_NUMBER + "|" + LEFT_BRANCH_RANDOM_NUMBER);
        } else {
            CHECKBOX_RANDOM.setText("Symmetrical");
            RIGHT_BRANCH_RANDOM_NUMBER = 1;
            LEFT_BRANCH_RANDOM_NUMBER = 1;
            ANGLE = ANGLE_SLIDER.getValue();
            System.out.println("Binary tree selected");
        }
        drawFractalTree(TRUNK_LENGTH, TRUNK_WIDTH, DEPTH);
    }

    private void randomNumber() {
        // define the range
        double min = 1;
        double max = Math.PI;
        double range = max - min + 1;

        //  generate random number within range
        RIGHT_BRANCH_RANDOM_NUMBER = (Math.random() * range) + min;
        LEFT_BRANCH_RANDOM_NUMBER = (Math.random() * range) + min;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set angle slider Defaults
        ANGLE_SLIDER.setMax(90);
        ANGLE_SLIDER.setValue(25);
        ANGLE_SLIDER.setTooltip(new Tooltip("0-180"));

        // Set length slider Defaults
        LENGTH_SLIDER.setMax(1000);
        LENGTH_SLIDER.setValue(165);
        LENGTH_SLIDER.setTooltip(new Tooltip("0-1000"));

        // Tooltip for depth
        Tooltip tip = new Tooltip("Careful... this is recursive...");
        TEXTFIELD.setTooltip(tip);

        // Resizable Canvas
        CANVAS.widthProperty().bind(ANCHORPANE.widthProperty());
        CANVAS.heightProperty().bind(ANCHORPANE.heightProperty());
        GRAPHICSCONTEXT = CANVAS.getGraphicsContext2D();
        System.out.println("INIT");
    }
}