package MainTeam.dto;

import MainTeam.json.MessageDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonDeserialize(using = MessageDeserializer.class)
public class Message {

    private Long chatId;
    private String text;
    private Long timestamp;
}
