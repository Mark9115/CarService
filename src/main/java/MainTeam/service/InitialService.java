package MainTeam.service;

import MainTeam.dto.Message;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InitialService {

    private final SendMessageService sendMessageService;

    public void initial(Message message){
        Map<String, String> map = new HashMap<>();
        map.put("reply_markup",
            "{\"keyboard\": [ [{\"text\": \"Получить консультацию\"}], [{ \"text\": \"Заказать запчасть\"}] ]}");
        sendMessageService.sendMessage("Что бы вы хотели?", message.getChatId(), map);
    }
}
