package MainTeam.json;

import MainTeam.entity.Message;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

public class MessageDeserializer extends JsonDeserializer<Message> {

    @Override
    public Message deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException {

        ObjectCodec codec = parser.getCodec();
        JsonNode jsonNode = codec.readTree(parser);

        long chatId = jsonNode.path("message").path("chat").path("id").asLong();
        String text = jsonNode.path("message").path("text").asText();
        Long timestamp = Long.parseLong(jsonNode.path("message").path("date").asText());

        return new Message(chatId, text, timestamp);
    }
}
