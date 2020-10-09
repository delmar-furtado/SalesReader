/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providers;

import com.mycompany.salesreader.SalesReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author delmar
 */
public class HandleFiles {

    // Data output path
    public static final String PATH_IN = "/HOMEPATH/data/in/";
    // Data entry path
    public static final String PATH_FILE_OUT = "/HOMEPATH/data/out/result";
    public Reader reader;

    public HandleFiles() {
        this.reader = new Reader();
    }

    /**
     * Process all files and write in the output file
     */
    public void processExistingFiles() {
        Stream<String> contentOfAllFiles = Stream.of(new File(PATH_IN).listFiles())
                .flatMap(file -> {
                    try {
                        return Files.lines(Paths.get(file.toURI()));
                    } catch (IOException ex) {
                        Logger.getLogger(HandleFiles.class.getName()).log(Level.SEVERE, "Fail reading file", ex);
                    }
                    return null;
                });

        String result = reader.getOutputResult(contentOfAllFiles);
        updateOutput(result);
    }

    /**
     * keep watching new files to process
     */
    public void keepWatching() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();

            Path path = Paths.get(PATH_IN);
            path.register(watchService, ENTRY_MODIFY);
            boolean poll = true;
            while (poll) {
                WatchKey key = watchService.take();
                key.pollEvents().forEach(event -> {
                    System.out.println("Modified file found!");

                    processExistingFiles();
                });
                poll = key.reset();
            }
        } catch (IOException ex) {
            Logger.getLogger(SalesReader.class.getName()).log(Level.SEVERE, "Fail instantiating WatchService", ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(SalesReader.class.getName()).log(Level.SEVERE, "Interrupted while waiting", ex);
        } catch (ClosedWatchServiceException ex) {
            Logger.getLogger(SalesReader.class.getName()).log(Level.SEVERE, "This watch service is closed, or it is closed while waiting for the next key", ex);
        } catch (Exception ex) {
            Logger.getLogger(SalesReader.class.getName()).log(Level.SEVERE, "Fail whatching folder", ex);
        }
    }

    /**
     * update output file
     */
    private void updateOutput(String result) {
        try {
            try (FileOutputStream outputStream = new FileOutputStream(PATH_FILE_OUT)) {
                byte[] strToBytes = result.getBytes();
                outputStream.write(strToBytes);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HandleFiles.class.getName()).log(Level.SEVERE, "Fail creating output", ex);
        } catch (IOException ex) {
            Logger.getLogger(HandleFiles.class.getName()).log(Level.SEVERE, "Fail writing", ex);
        }
    }
}
