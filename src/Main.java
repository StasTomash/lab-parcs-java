import parcs.AMInfo;
import parcs.channel;
import parcs.point;
import parcs.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int WORKERS_COUNT = 4;
    private static final int ITERATIONS = 100000000;
    private static List<List<Integer>> splitList(List<Integer> src) {
        int partitionSize = (src.size() + WORKERS_COUNT - 1) / WORKERS_COUNT;
        List<Integer> curList = null;
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < src.size(); i++) {
            if (i % partitionSize == 0) {
                result.add(new ArrayList<>());
                curList = result.get(i / partitionSize);
            }
            curList.add(src.get(i));
        }
        return result;
    }
    public static void main(String[] args) throws Exception {
        task curTask = new task();
        curTask.addJarFile("Runner.jar");
        AMInfo info = new AMInfo(curTask, null);

        System.out.println("Loading testcase...");
        File testcase = new File(curTask.findFile("test.txt"));
        Scanner testReader = new Scanner(testcase);

        int n = testReader.nextInt();
        DataChunk.Figure[] figures = new DataChunk.Figure[n];
        for (int i = 0; i < n; i++) {
            figures[i].type = testReader.nextInt();
            figures[i].x = testReader.nextInt();
            figures[i].y = testReader.nextInt();
            figures[i].radius = testReader.nextInt();
        }


        List<point> points = new ArrayList<>();
        List<channel> channels = new ArrayList<>();
        for (int i = 0; i < WORKERS_COUNT; i++) {
            points.add(info.createPoint());
            channels.add(points.get(i).createChannel());
        }

        for (int i = 0; i < WORKERS_COUNT; i++) {
            channels.get(i).write(new DataChunk(figures, ITERATIONS / WORKERS_COUNT));
        }

        double totalResult = (double) 0;
        for (int i = 0; i < WORKERS_COUNT; i++) {
            double result = channels.get(i).readDouble();
            totalResult += result / WORKERS_COUNT;
        }

        System.out.printf("Done, result is %f\n", totalResult);

        curTask.end();
    }
}
