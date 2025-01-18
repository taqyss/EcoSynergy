package com.example.ecosynergy;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Utility {

    public static String formatTimestamp(String timestampString) {
        try {
            // Check if the string is a valid numerical timestamp
            if (timestampString != null && !timestampString.equals("Just now")) {
                long timestamp = Long.parseLong(timestampString);

                long currentTime = System.currentTimeMillis();
                long diff = currentTime - timestamp;

                if (diff < TimeUnit.MINUTES.toMillis(1)) {
                    return "Just now";
                } else if (diff < TimeUnit.HOURS.toMillis(1)) {
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                    return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
                } else if (diff < TimeUnit.DAYS.toMillis(1)) {
                    long hours = TimeUnit.MILLISECONDS.toHours(diff);
                    return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
                } else if (diff < TimeUnit.DAYS.toMillis(30)) {
                    long days = TimeUnit.MILLISECONDS.toDays(diff);
                    return days + " day" + (days > 1 ? "s" : "") + " ago";
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                    return sdf.format(new Date(timestamp));
                }
            } else {
                return "Just now"; // For when the timestamp is the string "Just now"
            }
        } catch (NumberFormatException e) {
            Log.e("Utility", "Invalid timestamp format", e);
            return "Unknown time";
        }
    }


    public static String getCurrentTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }
}
