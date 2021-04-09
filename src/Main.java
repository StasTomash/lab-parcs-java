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
        int[][] table = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                table[i][j] = testReader.nextInt();
            }
        }

        List<List<Integer>> diagonals = new ArrayList<>();

        for (int i = 0; i < 2 * n - 1; i++) {
            diagonals.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                diagonals.get(i + j).add(table[j][i]);
            }
        }

	List<point> points = new ArrayList<>();
	List<channel> channels = new ArrayList<>();
	for (int i = 0; i < WORKERS_COUNT; i++) {
	    points.add(info.createPoint());
	    channels.add(points.get(i).createChannel());
	}

        List<Integer> prevDiagonal = new ArrayList<>();
        for (int iteration = 0; iteration < 2 * n - 1; iteration++) {
	    if (iteration != 0) {
		for (int i = 0; i < WORKERS_COUNT; i++) {
		    points.set(i, info.createPoint());
		    channels.set(i, points.get(i).createChannel());
		}
	    }	    
            if (iteration > 0) {
		prevDiagonal.add(100000000);
	    } else {
		prevDiagonal.add(0);
	    }
            List<List<Integer>> diagonalChunks = splitList(diagonals.get(iteration));
            List<Integer> diagonal = diagonals.get(iteration);
            int prevPos = 0;
            for (int i = 0; i < WORKERS_COUNT; i++) {
                List<Integer> chunk, prevChunk;
                try {
                    chunk = diagonalChunks.get(i);
                } catch (IndexOutOfBoundsException exception) {
                    chunk = new ArrayList<>();
                }

                if (diagonal.size() >= prevDiagonal.size()) {
                    if (i == 0) {
                        prevChunk = new ArrayList<>();
			prevChunk.add(1000000000);
                        prevChunk.addAll(prevDiagonal.subList(prevPos, prevPos + chunk.size()));
                    } else {
                        prevChunk = new ArrayList<>(prevDiagonal.subList(prevPos - 1, prevPos + chunk.size()));
                    }
                } else {
                    prevChunk = new ArrayList<>(prevDiagonal.subList(prevPos, prevPos + chunk.size() + 1));
                }
		prevPos += chunk.size();
		points.get(i).execute("Runner");
                channels.get(i).write(new DataChunk(chunk, prevChunk));
		//System.out.println("wrote" + i);
            }
	    prevDiagonal.clear();
            for (parcs.channel channel : channels) {
                DataChunk resChunk = (DataChunk)channel.readObject();
                prevDiagonal.addAll(resChunk.diagonal);
		//System.out.println("read");
            }
        }

        System.out.printf("Done, result is %d\n", prevDiagonal.get(0));

        curTask.end();
    }
}
