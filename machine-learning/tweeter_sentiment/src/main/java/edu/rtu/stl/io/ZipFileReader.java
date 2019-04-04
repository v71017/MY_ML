package edu.rtu.stl.io;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public class ZipFileReader implements FileReader {

    private static final Logger LOG = LoggerFactory.getLogger(ZipFileReader.class);

    public List<String> readLines(Path path) {
        try {
            ArrayList<String> result = new ArrayList<>();
            ZipFile zipFile = new ZipFile(path.toFile());
            for (Object fileHeader : zipFile.getFileHeaders()) {
                LOG.info("reading file header: " + ((FileHeader) fileHeader).getFileName());
                try (Scanner scanner = new Scanner(zipFile.getInputStream((FileHeader) fileHeader))) {
                    while (scanner.hasNext()) {
                        result.add(scanner.nextLine());
                    }
                }
            }
            return result;
        } catch (ZipException e) {
            LOG.error("Failed to unzip file {}. {}", path, e);
            return Collections.emptyList();
        }
    }

    @Override
    public boolean matches(Path path) {
        String name = path.toFile().getName();
        return name.endsWith(".zip");
    }
}
