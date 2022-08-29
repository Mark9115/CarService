package MainTeam.service;

import MainTeam.entity.Client;
import MainTeam.dto.Message;
import MainTeam.entity.enumeration.PartStateMachine;
import MainTeam.repository.ClientRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSparePartsService {
    private final SendMessageService sendMessageService;
    private final ClientRepository clientRepository;

    public void stateMachineMovement(Client client, Message message) {
        if(client.getPartStateMachine() == null){
            Map<String, String> map = new HashMap<>();
            map.put("reply_markup", "{\"remove_keyboard\": true}");
            sendMessageService.sendMessage("Введите номер запчасти:", message.getChatId(), map);
            //TODO =>
            //client.setPartStateMachine(PartStateMachine.SEARCHING);
            //clientRepository.save(client);
        }
    }
}
