import parcs.AM;
import parcs.AMInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.min;

public class Runner implements AM {
    private static final double GRID_SIZE = 1000000000;
    private boolean belongs(double x, double y, DataChunk.Figure figure) {
        if (figure.type == 1) {
            return Math.pow(figure.radius, 2) <= Math.pow(figure.x - x, 2) + Math.pow(figure.y - y, 2);
        } else {
            return Math.abs(figure.x - x) <= figure.radius && Math.abs(figure.y - y) <= figure.radius;
        }
    }

    @Override
    public void run(AMInfo info) {
        DataChunk dataChunk = (DataChunk) info.parent.readObject();
        Random rand = new Random();
        int positives = 0;
        for (int it = 0; it < dataChunk.rounds; it++) {
            double x = rand.nextDouble() * GRID_SIZE;
            double y = rand.nextDouble() * GRID_SIZE;
            for (DataChunk.Figure figure : dataChunk.figures) {
                if (belongs(x, y, figure)) {
                    positives++;
                    break;
                }
            }
        }

        info.parent.write((double)positives/(double)dataChunk.rounds);
    }
}
