import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class FileSearcher extends RecursiveTask<List<String>> {

    private static final int THRESHOLD = 10;

    private File[] files;
    private String searchString;

    public FileSearcher(File[] files, String searchString) {
        this.files = files;
        this.searchString = searchString;
    }

    @Override
    protected List<String> compute() {
        if (files != null)
        {
            int size = files.length;
            if (size > THRESHOLD) {
                int mid = size / 2;
                FileSearcher leftSearcher =
                    new FileSearcher(Arrays.copyOfRange(files, 0, mid), searchString);
                leftSearcher.fork();

                FileSearcher rightSearcher =
                    new FileSearcher(Arrays.copyOfRange(files, mid, size), searchString);

                List<String> rightResult = rightSearcher.compute();
                List<String> leftResult = leftSearcher.join();

                rightResult.addAll(leftResult);
                return rightResult;
            } else {
                return searchFiles(files);
            }
        }

        return Collections.emptyList();
    }

    private List<String> searchFiles(File[] files) {
        List<String> filePaths = new ArrayList<>();
        List<FileSearcher> tasks = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                FileSearcher searcher = new FileSearcher(file.listFiles(), searchString);
                tasks.add(searcher);
            } else if (file.getName().toLowerCase().matches(searchString)) {
                filePaths.add(file.getAbsolutePath());
            }
        }
        if (!tasks.isEmpty()) {
            filePaths.addAll(ForkJoinTask.invokeAll(tasks)
                                         .stream()
                                         .flatMap(fjt -> fjt.join().stream())
                                         .toList());
        }
        return filePaths;
    }
}
