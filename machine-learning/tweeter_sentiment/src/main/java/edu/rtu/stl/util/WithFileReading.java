package edu.rtu.stl.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

import edu.rtu.stl.io.AllFileReader;
import edu.rtu.stl.io.FileReader;

public interface WithFileReading extends WithDefaultProperties {

    Path BASE_PATH = Paths.get("src", "main", "resources");
    FileReader FILE_READER = new AllFileReader();

    default Set<String> readStopWords() {
        return FILE_READER.readLines(BASE_PATH.resolve(getProperty("stop.word.path"))).stream().map(x ->
                x.replaceAll("[^a-zA-Z ]", "").toLowerCase()).collect(Collectors.toSet());
    }
}
