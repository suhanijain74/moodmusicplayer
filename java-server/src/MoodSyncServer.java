import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class MoodSyncServer {
    private static final int PORT = 9090;

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // API Route: Get songs based on mood and language
        server.createContext("/api/songs", new SongApiHandler());
        
        // Static Files Route: Serve the React app (from the 'dist' folder)
        server.createContext("/", new StaticFileHandler());
        
        server.setExecutor(null);
        System.out.println("=================================================");
        System.out.println("🧠 MoodSync Java Backend logic active!");
        System.out.println("🔗 Access the UI at: http://localhost:" + PORT);
        System.out.println("=================================================");
        server.start();
    }

    static class SongApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Enable CORS so the React app can talk to us
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            
            Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
            String mood = params.getOrDefault("mood", "Happy");
            String lang = params.getOrDefault("lang", "All");

            List<Song> filteredSongs = DataService.getSongsByMood(mood, lang);
            String jsonResponse = songsToJson(filteredSongs);

            byte[] bytes = jsonResponse.getBytes("UTF-8");
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

        private String songsToJson(List<Song> songs) {
            return "[" + songs.stream()
                .map(s -> String.format("{\"id\":%d, \"title\":\"%s\", \"artist\":\"%s\", \"mood\":\"%s\", \"language\":\"%s\", \"url\":\"%s\"}",
                    s.getId(), s.getTitle(), s.getArtist(), s.getMood(), s.getLanguage(), s.getUrl()))
                .collect(Collectors.joining(",")) + "]";
        }

        private Map<String, String> queryToMap(String query) {
            if (query == null) return new HashMap<>();
            Map<String, String> result = new HashMap<>();
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) result.put(entry[0], entry[1]);
            }
            return result;
        }
    }

    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/")) path = "/index.html";

            // Serve from the 'dist' folder (Vite build output)
            File file = new File("../dist" + path);
            if (!file.exists()) file = new File("dist" + path);
            if (!file.exists()) file = new File("public" + path); // Fallback for raw public files

            if (!file.exists() || file.isDirectory()) {
                exchange.sendResponseHeaders(404, 0);
                exchange.close();
                return;
            }

            String contentType = getContentType(file.getName());
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(200, file.length());
            try (OutputStream os = exchange.getResponseBody(); FileInputStream fs = new FileInputStream(file)) {
                fs.transferTo(os);
            }
        }

        private String getContentType(String filename) {
            if (filename.endsWith(".html")) return "text/html";
            if (filename.endsWith(".css")) return "text/css";
            if (filename.endsWith(".js")) return "application/javascript";
            if (filename.endsWith(".mp3")) return "audio/mpeg";
            return "application/octet-stream";
        }
    }
}
