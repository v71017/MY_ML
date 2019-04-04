package edu.rtu.stl.io;

import java.nio.file.Path;
import java.util.List;

public interface FileReader {
    List<String> readLines(Path path);
    boolean matches(Path path);
}
