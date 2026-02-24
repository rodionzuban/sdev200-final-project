package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Playlist extends TrackCollection {
    private String playlistName;
    private String playlistDescription;
    private List<Integer> notPlayedIndices;
    private boolean onShuffle;

    // constructors
    public Playlist() {
        super();
        notPlayedIndices = new ArrayList<Integer>();
        playlistName = "";
        playlistDescription = "";
        onShuffle = false;
    }

    public Playlist(List<WavFile> tracks) {
        super(tracks);
        notPlayedIndices = IntStream.range(0, tracks.size()).boxed().collect(Collectors.toList());
        playlistName = "";
        playlistDescription = "";
        onShuffle = false;
    }

    public Playlist(String playlistName, String playlistDescription) {
        super();
        notPlayedIndices = new ArrayList<Integer>();
        this.playlistName = playlistName;
        this.playlistDescription = playlistDescription;
        onShuffle = false;
    }

    public Playlist(List<WavFile> tracks, String playlistName, String playlistDescription) {
        super(tracks);
        notPlayedIndices = IntStream.range(0, tracks.size()).boxed().collect(Collectors.toList());
        this.playlistName = playlistName;
        this.playlistDescription = playlistDescription;
        onShuffle = false;
    }

    // getter methods
    public String getPlaylistName() {
        return playlistName;
    }

    public String getPlaylistDescription() {
        return playlistDescription;
    }

    public boolean getOnShuffle() {
        return onShuffle;
    }

    // get the next track, either sequentially or by randomly selecting from
    // notPlayedIndices in this shuffle iteration
    @Override
    public WavFile nextTrack() {
        if (size() <= 0) {
            return null;
        }

        if (onShuffle) {
            if (notPlayedIndices.size() == 0) {
                notPlayedIndices = IntStream.range(0, tracks.size()).boxed().collect(Collectors.toList());
            }
            int index = (int) (Math.random() * (notPlayedIndices.size()));

            int trackIndex = notPlayedIndices.remove(index);
            return setCurrentIndex(trackIndex);
        } else {
            return super.nextTrack();
        }
    }

    // toggle shuffled mode, resetting notPlayedIndices
    public void toggleShuffle() {
        if (onShuffle) {
            notPlayedIndices = IntStream.range(0, tracks.size()).boxed().collect(Collectors.toList());
        } else {
            notPlayedIndices.remove(currentIndex);
        }

        onShuffle = !onShuffle;
    }

    // update notPlayedIndices when a track is added for shuffling
    @Override
    public boolean addTrack(WavFile track) {
        boolean added = super.addTrack(track);

        if (added) {
            notPlayedIndices.add(size() - 1);
        }

        return added;
    }

    // update notPlayedIndices by removing deleted index and subtracting 1 from all
    // indices higher than the one that was removed
    @Override
    public boolean removeTrack(int index) {
        boolean removed = super.removeTrack(index);

        if (removed) {
            notPlayedIndices.remove((Integer) index);

            for (int i = 0; i < notPlayedIndices.size(); i++) {
                if (notPlayedIndices.get(i) > index) {
                    notPlayedIndices.set(i, notPlayedIndices.get(i) - 1);
                }
            }
        }

        return removed;
    }
}