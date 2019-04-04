package edu.rtu.stl.io;

import static java.util.Arrays.asList;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicFileReader implements FileReader {

    private static final Logger LOG = LoggerFactory.getLogger(BasicFileReader.class);

    @Override
    public List<String> readLines(Path path) {
        try {
            return IOUtils.readLines(new FileInputStream(path.toString()));
        } catch (IOException e) {
            LOG.error("Failed to read file {}. {}", path, e);
            return Collections.emptyList();
        }
    }

    @Override
    public boolean matches(Path path) {
        String name = path.toFile().getName();
        for (String ext : asList(".txt", ".csv", ".pos", ".neg")) {
            if (name.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

}
