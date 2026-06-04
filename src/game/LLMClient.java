package game;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class LLMClient {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-4o-mini";

    /**
     * Sends a prompt to the LLM and returns whichever string from validMoves
     * appears in the response. Returns null if the API key is missing, the
     * network call fails, or no valid move is found in the reply.
     */
    public static String askMove(String systemPrompt, String userPrompt, String[] validMoves) {
        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            return null;
        }

        String body = buildRequestBody(systemPrompt, userPrompt);
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .timeout(Duration.ofSeconds(8))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body().toLowerCase();
            for (String move : validMoves) {
                if (responseBody.contains(move.toLowerCase())) {
                    return move;
                }
            }
        } catch (Exception e) {
            // Network/API failure — caller handles fallback
        }
        return null;
    }

    private static String buildRequestBody(String systemPrompt, String userPrompt) {
        return "{"
             + "\"model\":" + jsonString(MODEL) + ","
             + "\"messages\":["
             +   "{\"role\":\"system\",\"content\":" + jsonString(systemPrompt) + "},"
             +   "{\"role\":\"user\",\"content\":" + jsonString(userPrompt) + "}"
             + "],"
             + "\"max_tokens\":10,"
             + "\"temperature\":0.3"
             + "}";
    }

    private static String jsonString(String s) {
        return "\""
             + s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
             + "\"";
    }
}
