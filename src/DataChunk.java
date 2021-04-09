import javax.xml.crypto.Data;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;


public class DataChunk implements Serializable {
    public List<Figure> figures;
    Integer rounds;

    public DataChunk(Figure[] figures, Integer rounds) {
        this.figures = new ArrayList<Figure>(Arrays.asList(figures));
        this.rounds = rounds;
    }
}
