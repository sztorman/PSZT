import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Created by mateusz on 19.01.2014.
 */
public class ConnectingLine extends Line {

    Square square1;
    Square square2;

    public ConnectingLine(Square square1, Square square2) {

        super(square1.getPhysicalCenterX(), square1.getPhysicalCenterY(),
                square2.getPhysicalCenterX(), square2.getPhysicalCenterY());
        this.square1 = square1;
        this.square2 = square2;
        setStrokeWidth(3);
        setStroke(Color.GREEN);
    }
}
