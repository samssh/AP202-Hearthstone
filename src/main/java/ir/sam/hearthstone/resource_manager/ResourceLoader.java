package ir.sam.hearthstone.resource_manager;

import net.lingala.zip4j.ZipFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class ResourceLoader {
    private static String[] args;
    private final static String defaultAddress = "./src/main/resources/configurations/MainConfig.properties";
    private final static String defaultUrl="http://8upload.ir/uploads/f126514325.zip";

    private final static ResourceLoader instance = new ResourceLoader();
    private final String INPUT_ZIP_FILE = "./temp.zip";
    private final String OUTPUT_FOLDER = "./src/main/resources";

    public static ResourceLoader getInstance() {
        return instance;
    }

    public void checkResources() {
        File file = new File(ResourceLoader.getConfigAddress());
        if (!file.exists()) {
            downloadFile();
            unZipIt();
            ResourceLoader.setArgs(new String[]{OUTPUT_FOLDER + "/configurations/MainConfig.properties"});
        }
    }

    private void downloadFile() {
        try {
            System.out.println("download resources");
            long startTime = System.currentTimeMillis();

            URL url = new URL(ResourceLoader.getUrl());

            url.openConnection();
            InputStream reader = url.openStream();

            FileOutputStream writer = new FileOutputStream(this.INPUT_ZIP_FILE);
            byte[] buffer = new byte[102400];
            int totalBytesRead = 0;
            int bytesRead;

            System.out.println("Reading ZIP file 20KB blocks at a time.\n");

            while ((bytesRead = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, bytesRead);
                buffer = new byte[102400];
                totalBytesRead += bytesRead;
            }

            long endTime = System.currentTimeMillis();

            System.out.println("Done. " + totalBytesRead + " bytes read (" + (endTime - startTime) + " millseconds).\n");
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void unZipIt() {
        try {
            System.out.println("unzipping");
            ZipFile zipFile = new ZipFile(INPUT_ZIP_FILE);

            if (zipFile.isEncrypted()) {
                System.out.println("enter password");
                zipFile.setPassword(new Scanner(System.in).nextLine().toCharArray());
            }
            File file = new File(OUTPUT_FOLDER);
            if (file.exists() || (!file.exists() && file.mkdirs()))
                zipFile.extractAll(OUTPUT_FOLDER);
            else {
                System.out.println("build failed");
                System.exit(-1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getConfigAddress() {
        return args.length > 0 ? args[0] : defaultAddress;
    }

    public static String getUrl(){
        return args.length > 1 ? args[1] : defaultUrl;
    }

    public static void setArgs(String[] args) {
        ResourceLoader.args = args;
    }
}
