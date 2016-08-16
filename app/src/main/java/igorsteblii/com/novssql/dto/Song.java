package igorsteblii.com.novssql.dto;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author igorsteblii on 05.08.16.
 */
public class Song extends RealmObject {

    @PrimaryKey
    private int id;
    private String artist;
    private String title;
    private long duration;
    private int year;

    public Song() {
    }

    public Song(int id, String artist, String title, long duration, int year) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.duration = duration;
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
