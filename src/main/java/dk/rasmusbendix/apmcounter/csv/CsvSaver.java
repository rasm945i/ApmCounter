package dk.rasmusbendix.apmcounter.csv;

import dk.rasmusbendix.apmcounter.apm.EventWrapper;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CsvSaver {

    @Getter @Setter private static File csvSaveLocation;

    public static void saveToCsv(EventWrapper wrapper) {

        int actionsPerMinute = 0;
        ArrayList<Integer> listOfActions = new ArrayList<>();
        ArrayList<Long> timestamps = wrapper.getCounter().getActions();
        long firstStamp = timestamps.get(0);
        long minuteInMs = TimeUnit.MINUTES.toMillis(1);
        int minutesToOffset = 0;

        for (int i = 0; i < timestamps.size(); i++) {
            long stamp = timestamps.get(i);
            if (stamp <= firstStamp + (minuteInMs * minutesToOffset)) {
                actionsPerMinute++;
                continue;
            }

            listOfActions.add(actionsPerMinute);
            actionsPerMinute = 0;
            minutesToOffset++;

            if (i + 1 < timestamps.size() && timestamps.get(i + 1) <= firstStamp + (minuteInMs * minutesToOffset))
                actionsPerMinute++;

        }

        try {

            if(csvSaveLocation == null) {
                throw new IOException("Save location not defined!");
            }

            File csv = new File(csvSaveLocation.getPath() + File.separatorChar + wrapper.getCsvFileName());
            if (!csv.exists()) {
                if (!csv.createNewFile()) {
                    throw new IOException("Failed to create CSV file!");
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(csv));

            for(int actions : listOfActions) {
                writer.append(String.valueOf(actions)).append(",");
            }

            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // DD-MM-YYYY-HH-mm-SS
    public static String getFileName(EventWrapper wrapper) {
        Instant instant = Instant.now();
        ZonedDateTime time = instant.atZone(ZoneOffset.systemDefault());
        return wrapper.getId().replace(" ", "_") + "-" + time.getDayOfMonth() + "-" + time.getMonthValue() + "-" + time.getYear() + "-" + time.getHour() + "-" + time.getMinute() + "-" + time.getSecond() + ".csv";
    }

    public static void stampsToString(List<Long> list) {
        System.out.println();
        System.out.println(join(list, ","));
        System.out.println();
    }

    private static String join(List<?> list, String sep) {
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < list.size(); i++) {

            builder.append(list.get(i));
            if(i+1 < list.size())
                builder.append(sep);

        }

        return builder.toString();

    }

}
