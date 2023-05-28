import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileArchiver {

    private static final String OUTPUT_FILE = "./src/main/resources/archive.zip";

    public static File archiveFiles(List<String> filePaths) throws IOException {
        File outputFile = new File(OUTPUT_FILE);

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(outputFile))) {
            byte[] buffer = new byte[1024];

            for (String filePath : filePaths) {
                File file = new File(filePath);
                if (file.exists() && file.isFile()) {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zipOutputStream.putNextEntry(zipEntry);

                    int length;
                    while ((length = fileInputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, length);
                    }

                    fileInputStream.close();
                    zipOutputStream.closeEntry();
                }
            }
        }

        return outputFile;
    }
}
