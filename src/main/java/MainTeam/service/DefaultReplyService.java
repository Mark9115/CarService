package MainTeam.service;

import MainTeam.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultReplyService {

    private final SendMessageService sendMessageService;

    public void reply(Message message) {
        sendMessageService.sendMessage("Не знаю такой команды!", message.getChatId(), null);
    }

}
