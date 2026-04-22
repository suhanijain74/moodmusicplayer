public class Song {
    private int id;
    private String title;
    private String artist;
    private String mood;
    private String language;
    private String url;

    public Song(int id, String title, String artist, String mood, String language, String url) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.mood = mood;
        this.language = language;
        this.url = url;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getMood() { return mood; }
    public String getLanguage() { return language; }
    public String getUrl() { return url; }

    @Override
    public String toString() {
        return title + " - " + artist;
    }
}
