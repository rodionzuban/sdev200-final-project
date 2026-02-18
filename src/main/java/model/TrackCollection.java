package model;

import java.util.ArrayList;
import java.util.List;

public class TrackCollection {
    protected List<WavFile> tracks;
    protected int currentIndex;

    // constructors

    public TrackCollection() {
        tracks = new ArrayList<WavFile>();
        currentIndex = -1;
    }

    public TrackCollection(List<WavFile> tracks) {
        this.tracks = new ArrayList<>(tracks);
        currentIndex = 0;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    // set a new index and return the track at that position
    public WavFile setCurrentIndex(int index) throws IllegalArgumentException {
        if (index >= 0 && index < tracks.size()) {
            currentIndex = index;
            return tracks.get(currentIndex);
        } else {
            throw new IllegalArgumentException("Index out of TrackCollection bounds");
        }
    }

    public boolean contains(WavFile track) {
        return tracks.contains(track);
    }

    public int indexOf(WavFile track) {
        return tracks.indexOf(track);
    }

    public int size() {
        return tracks.size();
    }

    // add a track if it isn't already in the list (prevent duplicates)
    public boolean addTrack(WavFile track) {
        if (!contains(track)) {
            tracks.add(track);
            return true;
        }
        return false;
    }

    // remove a track and change currentIndex accordingly
    public boolean removeTrack(int index) {
        if (index >= 0 && index < tracks.size()) {
            tracks.remove(index);
            if (currentIndex > index) {
                currentIndex--;
            } else if (index == currentIndex) {
                if (tracks.size() == 0) {
                    currentIndex = -1;
                } else if (currentIndex > 0) {
                    currentIndex--;
                } else {
                    currentIndex++;
                }
            }

            return true;
        }

        return false;
    }

}