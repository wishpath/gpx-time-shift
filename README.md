# GPX Timestamp Shifter

Shifts all `<time>` timestamps in a `.gpx` file by a fixed offset, relative to the
activity's original start time.

## Run

1. Drop exactly one `.gpx` file into `src/main/resources/`.
2. In `Main.java`, set the desired shift on the `desiredStartUTC` line, e.g.:
   ```java
   Instant desiredStartUTC = firstTimeInstant.plus(2, ChronoUnit.DAYS).plus(21, ChronoUnit.HOURS);
   ```
   Chain `.plus(n, ChronoUnit.X)` calls for any combination of `DAYS`, `HOURS`,
   `MINUTES`, or `SECONDS`. To shift *earlier*, use negative values, e.g.
   `.plus(-30, ChronoUnit.MINUTES)`.
3. Run `Main.main()`.

Output is written to `src/main/java/org/sa/ouput/<originalName>_SHIFTED.gpx`.