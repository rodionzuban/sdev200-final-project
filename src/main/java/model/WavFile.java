package model;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class WavFile {
    private String title;
    private long duration;
    private String filePath;
    private int numListens;

    // initialize WavFile if a given file does exist
    public WavFile(File file) throws IllegalArgumentException {
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist");
        }

        if (!file.getAbsolutePath().toLowerCase().endsWith(".wav")) {
            throw new IllegalArgumentException("File is not a WAV file");
        }

        title = extractTitle(file);
        duration = extractDuration(file);
        filePath = file.getAbsolutePath();
        numListens = 0;
    }

    // getter methods
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return duration;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getNumListens() {
        return numListens;
    }

    public void addListen() {
        numListens++;
    }

    // get properties of title and duration from wav file
    public static String extractTitle(File file) {
        try {
            String fileName = file.getName();
            int lastDotIndex = fileName.lastIndexOf(".");
            return (lastDotIndex > 0) ? fileName.substring(0, lastDotIndex) : fileName;
        } catch (Exception e) {
            System.out.println("Error extracting audio file name: " + e.getMessage());
            return null;
        }
    }

    public static long extractDuration(File file) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioStream.getFormat();

            long frames = audioStream.getFrameLength();
            float frameRate = format.getFrameRate();

            double durationSeconds = frames / frameRate;

            audioStream.close();

            return (long) (durationSeconds * 1000);
        } catch (Exception e) {
            System.out.println("Error reading audio file: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        return this.filePath.equals(((WavFile) obj).filePath);
    }
}
