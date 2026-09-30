package org.sa;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class FileService {
  private Path gpxFilePath;


  public Path findSingleGpxFile() {
    List<Path> gpxFiles = new ArrayList<>();

    try {
      Enumeration<URL> roots = Thread.currentThread().getContextClassLoader().getResources("");
      while (roots.hasMoreElements()) {
        URI rootUri = roots.nextElement().toURI();
        Path rootPath = Path.of(rootUri);
        if (!Files.isDirectory(rootPath)) {
          continue;
        }
        try (var stream = Files.list(rootPath)) {
          stream.filter(p -> p.getFileName().toString().endsWith(".gpx")).forEach(gpxFiles::add);
        }
      }
    }
    catch (Exception e) {}

    if (gpxFiles.size() != 1) {
      throw new IllegalStateException("Expected exactly one .gpx file in resources, found: " + gpxFiles.size());
    }
    this.gpxFilePath = gpxFiles.get(0);
    return this.gpxFilePath;
  }

  public Path buildOutputPath() {
    String originalName = this.gpxFilePath.getFileName().toString();
    String baseName = originalName.endsWith(".gpx")
        ? originalName.substring(0, originalName.length() - ".gpx".length())
        : originalName;
    return Path.of("src/main/java/org/sa/ouput", baseName + "_SHIFTED.gpx");
  }

  public String readGpxFile() {
    findSingleGpxFile();
    try {
      return Files.readString(this.gpxFilePath);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void writeGpxFile(String outputGpxString) {
    Path outputFile = buildOutputPath();
    try {
      Files.createDirectories(outputFile.getParent());
      Files.writeString(outputFile, outputGpxString);
    }
    catch (Exception e){}
  }
}
