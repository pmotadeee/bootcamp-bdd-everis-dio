package com.everis.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utils provides common utility methods for handling files, dates,
 * and simple text operations within the automation framework.
 */
public class Utils {

    /**
     * Waits until at least one file exists in the specified directory,
     * or until the timeout is reached.
     *
     * @param dir              directory path to monitor
     * @param timeOutInSeconds timeout in seconds
     */
    public static void waitForFileExistsInPath(String dir, int timeOutInSeconds) {
        File folder = new File(dir);
        FileFilter filter = File::isFile;

        File[] files = folder.listFiles(filter);
        int counter = 0;
        boolean timeoutReached = false;

        while ((files == null || files.length == 0) && !timeoutReached) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }

            counter++;
            timeoutReached = timeOutInSeconds == counter / 5;
            files = folder.listFiles(filter);
        }

        if (timeoutReached) {
            System.err.println("No file was generated in path - " + dir +
                               " after " + timeOutInSeconds + " seconds.");
        }
    }

    /**
     * Returns the current date/time formatted according to the given pattern.
     *
     * @param format date format (e.g. "dd/MM/yyyy HH:mm:ss")
     * @return formatted date/time as String
     */
    public static String getDateTime(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date());
    }

    /**
     * Returns the date/time with a day offset, formatted according to the given pattern.
     *
     * @param format           date format (e.g. "dd/MM/yyyy")
     * @param daysToIncrement  number of days to add (can be negative)
     * @return formatted date/time as String
     */
    public static String getDateTime(String format, int daysToIncrement) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.DAY_OF_MONTH, daysToIncrement);
        return dateFormat.format(currentDate.getTime());
    }

    /**
     * Returns the current date formatted as dd/MM/yyyy.
     *
     * @return current date string
     */
    public static String getDate() {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    }

    /**
     * Returns the current hour formatted as HH:mm:ss.
     *
     * @return current time string
     */
    public static String getHour() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    /**
     * Returns the current date and hour formatted as dd/MM/yyyy HH:mm:ss.
     *
     * @return current date and time string
     */
    public static String getDateHour() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }

    /**
     * Reads a text file into a string using the given charset encoding.
     *
     * @param path     the file path
     * @param encoding the character set to use
     * @return file content as string, or empty string if an error occurs
     */
    public static String readFileToString(String path, Charset encoding) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, encoding);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
