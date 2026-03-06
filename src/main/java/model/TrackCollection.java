package model;

import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TrackCollection {
    protected ObservableList<WavFile> tracks;
    protected int currentIndex;
    protected IntegerProperty size = new SimpleIntegerProperty(0);

    // constructors

    public TrackCollection() {
        tracks = FXCollections.observableArrayList();
        currentIndex = -1;
    }

    public TrackCollection(List<WavFile> tracks) {
        this.tracks = FXCollections.observableArrayList(tracks);
        currentIndex = 0;
        size.set(tracks.size());
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

    public IntegerProperty sizeProperty() {
        return size;
    }

    // add a track if it isn't already in the list (prevent duplicates)
    public boolean addTrack(WavFile track) {
        if (!contains(track)) {
            tracks.add(track);
            if (currentIndex == -1) {
                currentIndex = 0;
            }
            size.set(size());
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
            size.set(size());
            return true;
        }

        return false;
    }

    // load next track in collection
    public WavFile nextTrack() {
        if (size() <= 0) {
            return null;
        }
        int index = currentIndex == size() - 1 ? 0 : currentIndex + 1;
        return setCurrentIndex(index);
    }

    public WavFile previousTrack() {
        if (size() <= 0) {
            return null;
        }
        int index = currentIndex == 0 ? size() - 1 : currentIndex - 1;
        return setCurrentIndex(index);
    }

    public ObservableList<WavFile> getTracks() {
        return tracks;
    }

    public WavFile getTrack() {
        return tracks.get(currentIndex);
    }

    public WavFile getAtIndex(int index) {
        if (index >= 0 && index < size()) {
            return tracks.get(index);
        }
        return null;
    }

}