package fr.alexpado.jda.services.tools;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeConverter {

    private final long hours;
    private       long minutes;
    private       long seconds;

    public TimeConverter(long duration) {

        this.hours   = TimeUnit.HOURS.convert(duration, TimeUnit.SECONDS);
        this.minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.SECONDS);
        this.seconds = duration;
        this.minutes -= TimeUnit.MINUTES.convert(this.hours, TimeUnit.HOURS);
        this.seconds -= TimeUnit.SECONDS.convert(this.hours, TimeUnit.HOURS) + TimeUnit.SECONDS.convert(this.minutes, TimeUnit.MINUTES);
    }

    public static long fromString(@NotNull CharSequence value) {

        String  regex   = "(?<days>\\d*d)?(?<hours>\\d*h)?(?<minutes>\\d+m)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        long    time    = 0L;
        if (matcher.find()) {
            String day    = matcher.group("days");
            String hour   = matcher.group("hours");
            String minute = matcher.group("minutes");

            if (day != null) {
                time += TimeUnit.SECONDS.convert(Long.parseLong(day.replace("d", "")), TimeUnit.DAYS);
            }

            if (hour != null) {
                time += TimeUnit.SECONDS.convert(Long.parseLong(hour.replace("h", "")), TimeUnit.HOURS);
            }

            if (minute != null) {
                time += TimeUnit.SECONDS.convert(Long.parseLong(minute.replace("m", "")), TimeUnit.MINUTES);
            }
        }
        return time;
    }

    @Override
    public String toString() {

        if (this.hours != 0) {
            if (this.seconds != 0) {
                return String.format("%sh%02dm%02ds", this.hours, this.minutes, this.seconds);
            } else if (this.minutes != 0) {
                return String.format("%sh%02dm", this.hours, this.minutes);
            } else {
                return String.format("%sh", this.hours);
            }
        } else if (this.minutes != 0) {
            if (this.seconds != 0) {
                return String.format("%02dm%02ds", this.minutes, this.seconds);
            }
            return String.format("%sm", this.minutes);
        }
        return String.format("%02ds", this.seconds);
    }

}
