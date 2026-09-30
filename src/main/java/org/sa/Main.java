package org.sa;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

public class Main {
  private static final Pattern TIME_TAG_PATTERN = Pattern.compile("<time>(.*?)</time>");

  public static void main(String[] args) throws IOException {
    FileService service = new FileService();
    String inputGpxString = service.readGpxFile();

    // calculate delta between first point and desired start
    Instant firstTimeInstant = Instant.parse(TIME_TAG_PATTERN.matcher(inputGpxString).results().map(r -> r.group(1)).findFirst().get());
    Instant desiredStartUTC = firstTimeInstant.plus(2, ChronoUnit.DAYS).plus(21, ChronoUnit.HOURS);
    //.plus(0, ChronoUnit.HOURS).plus(0, ChronoUnit.MINUTES).plus(0, ChronoUnit.SECONDS);
    Duration delta = Duration.between(firstTimeInstant, desiredStartUTC);

    // shift all timestamps by the same delta
    String outputGpxString = TIME_TAG_PATTERN.matcher(inputGpxString).replaceAll(m -> "<time>" + Instant.parse(m.group(1)).plus(delta) + "</time>");
    service.writeGpxFile(outputGpxString);
  }
}