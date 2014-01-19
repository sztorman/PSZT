import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Created by mateusz on 19.01.2014.
 */
public class ConnectingLine extends Line {

    Square square1;
    Square square2;
    static final int offset = 0;

    public ConnectingLine(Square square1, Square square2) {

        super(square1.getLocation().getKey(), square1.getLocation().getValue(),
                square2.getLocation().getKey(), square2.getLocation().getValue());
        this.square1 = square1;
        this.square2 = square2;
        setStrokeWidth(3);
        setStroke(Color.GREEN);
    }
}
