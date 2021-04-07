import javax.xml.crypto.Data;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;

public class DataChunk implements Serializable {
    public List<Integer> diagonal;
    public List<Integer> prevDiagonal;

    public DataChunk(List<Integer> diagonal, List<Integer> prevDiagonal) {
        this.diagonal = diagonal;
        this.prevDiagonal = prevDiagonal;
    }
}
