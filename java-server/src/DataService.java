import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataService {
    private static final List<Song> ALL_SONGS = new ArrayList<>();
    private static final List<User> USERS = new ArrayList<>();

    static {
        // Ported from App.jsx
        ALL_SONGS.add(new Song(37, "One Dance", "Drake", "Happy", "english", "/songs/one_dance.mp3"));
        ALL_SONGS.add(new Song(38, "Baby", "Justin Bieber", "Happy", "english", "/songs/baby.mp3"));
        ALL_SONGS.add(new Song(39, "Last Friday Night", "Katy Perry", "Happy", "english", "/songs/last_friday_night.mp3"));
        ALL_SONGS.add(new Song(40, "Obsessed", "Mariah Carey", "Happy", "english", "/songs/obsessed.mp3"));
        ALL_SONGS.add(new Song(41, "What Makes You Beautiful", "One Direction", "Happy", "english", "/songs/what_makes_you_beautiful.mp3"));
        ALL_SONGS.add(new Song(42, "Levitating", "Dua Lipa", "Happy", "english", "/songs/levitating.mp3"));
        ALL_SONGS.add(new Song(43, "Gasolina", "Daddy Yankee", "Happy", "english", "/songs/gasolina.mp3"));
        ALL_SONGS.add(new Song(44, "One Kiss", "Calvin Harris & Dua Lipa", "Happy", "english", "/songs/one_kiss.mp3"));
        ALL_SONGS.add(new Song(21, "Dope Shope", "Honey Singh", "Happy", "hindi", "/songs/dope_shope.mp3"));
        ALL_SONGS.add(new Song(22, "Angreji Beat", "Honey Singh", "Happy", "hindi", "/songs/angreji_beat.mp3"));
        ALL_SONGS.add(new Song(23, "Party All Night", "Honey Singh", "Happy", "hindi", "/songs/party_all_night.mp3"));
        ALL_SONGS.add(new Song(24, "Chaar Botal Vodka", "Honey Singh", "Happy", "hindi", "/songs/chaar_botal_vodka.mp3"));
        ALL_SONGS.add(new Song(25, "Vele", "Vishal-Shekhar", "Happy", "hindi", "/songs/vele.mp3"));
        ALL_SONGS.add(new Song(26, "Blue Eyes", "Honey Singh", "Happy", "hindi", "/songs/blue_eyes.mp3"));
        ALL_SONGS.add(new Song(27, "Brown Rang", "Honey Singh", "Happy", "hindi", "/songs/brown_rang.mp3"));
        ALL_SONGS.add(new Song(28, "Desi Kalakaar", "Honey Singh", "Happy", "hindi", "/songs/desi_kalakaar.mp3"));
        ALL_SONGS.add(new Song(50, "white ferrari", "Frank Ocean", "Calm", "english", "/songs/white_ferrari.mp3"));
        ALL_SONGS.add(new Song(51, "thinking bout you", "Frank Ocean", "Calm", "english", "/songs/thinking_bout_you.mp3"));
        ALL_SONGS.add(new Song(29, "Hosanna", "A.R. Rahman", "Calm", "hindi", "/songs/hosanna.mp3"));
        ALL_SONGS.add(new Song(30, "Subhanallah", "Sreerama Chandra", "Calm", "hindi", "/songs/subhanallah.mp3"));
        ALL_SONGS.add(new Song(31, "Tu Hi Hai", "Arijit Singh", "Calm", "hindi", "/songs/tu_hi_hai.mp3"));
        ALL_SONGS.add(new Song(32, "Katiya Karun", "Harshdeep Kaur", "Calm", "hindi", "/songs/katiya_karun.mp3"));
        ALL_SONGS.add(new Song(33, "Sauda Hai Dil Ka", "Anupam Amod", "Calm", "hindi", "/songs/sauda_hai_dil_ka.mp3"));
        ALL_SONGS.add(new Song(34, "Mere Liye Tum Kaafi Ho", "Ayushmann Khurrana", "Calm", "hindi", "/songs/mere_liye_tum_kaafi_ho.mp3"));
        ALL_SONGS.add(new Song(35, "Titli", "Chinmayi Sripaada", "Calm", "hindi", "/songs/titli.mp3"));
        ALL_SONGS.add(new Song(36, "Jab Tak", "Armaan Malik", "Calm", "hindi", "/songs/jab_tak.mp3"));
        ALL_SONGS.add(new Song(9, "cigarettes out the window", "TV Girl", "Sad", "english", "/songs/cigarettes_out_the_window.mp3"));
        ALL_SONGS.add(new Song(10, "daddy issues", "The Neighbourhood", "Sad", "english", "/songs/daddy_issues.mp3"));
        ALL_SONGS.add(new Song(11, "blue hair", "TV Girl", "Sad", "english", "/songs/blue_hair.mp3"));
        ALL_SONGS.add(new Song(12, "sad", "XXXTENTACION", "Sad", "english", "/songs/sad.mp3"));
        ALL_SONGS.add(new Song(45, "american wedding", "Frank Ocean", "Sad", "english", "/songs/american_wedding.mp3"));
        ALL_SONGS.add(new Song(46, "505", "Arctic Monkeys", "Sad", "english", "/songs/505.mp3"));
        ALL_SONGS.add(new Song(47, "dancing with your ghost", "Sasha Alex Sloan", "Sad", "english", "/songs/dancing_with_your_ghost.mp3"));
        ALL_SONGS.add(new Song(49, "moonlight", "XXXTENTACION", "Sad", "english", "/songs/moonlight.mp3"));
        ALL_SONGS.add(new Song(13, "Paaro", "Aditya Rikhari", "Sad", "hindi", "/songs/paaro.mp3"));
        ALL_SONGS.add(new Song(14, "Dooron Dooron", "Vishal Mishra", "Sad", "hindi", "/songs/dooron_dooron.mp3"));
        ALL_SONGS.add(new Song(15, "Kun Faya Kun", "A.R. Rahman", "Sad", "hindi", "/songs/kun_faya_kun.mp3"));
        ALL_SONGS.add(new Song(16, "Ye Tune Kya Kiya", "Javed Bashir", "Sad", "hindi", "/songs/ye_tune_kya_kiya.mp3"));
        ALL_SONGS.add(new Song(17, "Husn", "Anuv Jain", "Sad", "hindi", "/songs/husn.mp3"));
        ALL_SONGS.add(new Song(18, "Oh Meri Laila", "Atif Aslam", "Sad", "hindi", "/songs/oh_meri_laila.mp3"));
        ALL_SONGS.add(new Song(19, "Tu Jaane Na", "Atif Aslam", "Sad", "hindi", "/songs/tu_jaane_na.mp3"));
        ALL_SONGS.add(new Song(20, "Abhi Na Jao Chhod Kar", "Mohammed Rafi & Asha Bhosle", "Sad", "hindi", "/songs/abhi_na_jao.mp3"));
    }

    public static List<Song> getSongsByMood(String mood, String language) {
        return ALL_SONGS.stream()
                .filter(s -> s.getMood().equalsIgnoreCase(mood))
                .filter(s -> language.equalsIgnoreCase("All") || s.getLanguage().equalsIgnoreCase(language))
                .collect(Collectors.toList());
    }

    public static boolean login(String email, String password) {
        return USERS.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password));
    }

    public static boolean signup(String email, String password) {
        if (USERS.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email))) {
            return false;
        }
        USERS.add(new User(email, password));
        return true;
    }
}
