import parcs.AM;
import parcs.AMInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class Runner implements AM {
    @Override
    public void run(AMInfo info) {
        DataChunk dataChunk = (DataChunk) info.parent.readObject();
	System.out.println(dataChunk.diagonal.size());
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < dataChunk.diagonal.size(); i++) {
            int valLeft = dataChunk.prevDiagonal.get(i);
            int valUp = 1000000000;
            if (i + 1 < dataChunk.prevDiagonal.size()) {
                valUp = dataChunk.prevDiagonal.get(i + 1);
            }
            if (valUp == 1000000000 && valLeft == 1000000000) {
                valUp = 0;
            }
            result.add(dataChunk.diagonal.get(i) + min(valUp, valLeft));
        }
	dataChunk.diagonal = result;
        info.parent.write((Serializable)dataChunk);
    }
}
