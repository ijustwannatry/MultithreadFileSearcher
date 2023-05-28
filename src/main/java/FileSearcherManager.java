import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class FileSearcherManager {

    private List<String> paths;
    private ForkJoinPool forkJoinPool;
    private File root;

    public FileSearcherManager(String rootPath) {
        root = new File(rootPath);
        forkJoinPool = new ForkJoinPool();
    }

    public FileSearcherManager(String rootPath, int threadCount) {
        root = new File(rootPath);
        forkJoinPool = new ForkJoinPool(threadCount);
    }

    public List<String> search(String fileName) {
        if (!root.exists() || !root.isDirectory()) {
            System.out.println("Root directory doesn't exist or isn't directory");
        }

        String searchString = getSearchString(fileName);

        if (root.isDirectory()) {
            FileSearcher searcher = new FileSearcher(root.listFiles(), searchString);
            paths = forkJoinPool.invoke(searcher);
        }
        return paths == null ? Collections.emptyList() : paths;
    }

    private String getSearchString(String str) {
        if (str == null || str.isEmpty())
        {
            return "";
        }

        return str.replaceAll("\\*", ".*").toLowerCase();
    }
}

