import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String HELP = """
    Usage: searcher [options] <rootDirectory> <fileName>
    <fileName>      case insensitive, enter full name with extension or part of name with *
                    examples:
                        searcher C:\\Windows fileName.ext
                        searcher C:\\Windows file*
                        searcher C:\\Windows *name*
                        
    where options include:
        -tc         number of threads, the default is the number of processors on your PC              
    'searcher -help'  print this help message  
    """;

    private static FileSearcherManager fileSearcherManager;
    private static String fileName;
    private static List<String> paths;

    public static void main(String[] args) {
        switch (args.length) {
            case 0 -> showHelp();
            case 1 -> {
                if ("-help".equals(args[0])) {
                    showHelp();
                } else {
                    System.out.println("Unrecognized arguments. Use 'searcher -help'" +
                                       " for getting a help message");
                }
            }
            case 2 -> {
                fileSearcherManager = new FileSearcherManager(args[0]);
                fileName = args[1];
            }
            case 4 -> {
                if ("-tc".equals(args[0])) {
                    if (args[1].matches("\\d+")) {
                        int tc = Integer.parseInt(args[1]);
                        fileSearcherManager = new FileSearcherManager(args[2], tc);
                        fileName = args[3];
                    } else {
                        System.out.println("Wrong value '-tc' option");
                    }
                } else {
                    System.out.println("Unrecognized option. Use 'searcher /help'" +
                                       "for getting a help message");
                }
            }
            default -> {
                System.out.println("Check arguments");
                showHelp();
            }
        }

        if (fileSearcherManager != null) {
            System.out.println("Searching is started...");
            paths = fileSearcherManager.search(fileName);
            System.out.println("Found " + paths.size() + " occurrences:");
            paths.forEach(System.out::println);
            if (paths.size() > 0)
            {
                sendFiles();
            }
        }
    }

    private static void sendFiles() {
        System.out.println("\nDo you want to archive files?");
        System.out.print("y/n? Or press 'Enter' for yes): ");
        Scanner scn = new Scanner(System.in);
        String answer = scn.nextLine();
        if ("y".equals(answer) || "".equals(answer)) {
            try {
                FileArchiver.archiveFiles(paths);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void showHelp() {
        System.out.println(HELP);
    }
}
