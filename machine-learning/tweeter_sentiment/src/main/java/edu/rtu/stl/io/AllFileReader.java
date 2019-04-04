package edu.rtu.stl.io;

import static java.util.Arrays.asList;

import java.nio.file.Path;
import java.util.List;

public class AllFileReader implements FileReader {

    private final List<FileReader> readers = asList(new ZipFileReader(), new BasicFileReader());

    @Override
    public List<String> readLines(Path path) {
        return readers.stream().filter(x -> x.matches(path)).findAny()
                .orElseThrow(() -> new IllegalArgumentException("Not a readable file path")).readLines(path);
    }

    @Override
    public boolean matches(Path path) {
        return readers.stream().anyMatch(x -> x.matches(path));
    }
}
