package MainTeam.service;

import MainTeam.entity.Client;
import MainTeam.entity.ClientProblem;
import MainTeam.dto.Message;
import MainTeam.entity.enumeration.ConsultationStateMachine;
import MainTeam.repository.ClientProblemRepository;
import MainTeam.repository.ClientRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final SendMessageService sendMessageService;
    private final ClientRepository clientRepository;
    private final ClientProblemRepository clientProblemRepository;

    public void stateMachineMovement(Client client, Message message) {

        if(client.getConsultationStateMachine() == null){
            Map<String, String> map = new HashMap<>();
            map.put("reply_markup", "{\"remove_keyboard\": true}");

            sendMessageService.sendMessage("Ваше имя:", message.getChatId(), map);
            client.setConsultationStateMachine(ConsultationStateMachine.NAME);
            clientRepository.save(client);
            return;
        }

        ClientProblem clientProblem = clientProblemRepository
            .findClientProblemByChatIdAndStatus(client.getChatId(), Boolean.FALSE)
            .orElse(ClientProblem.builder().chatId(client.getChatId()).status(Boolean.FALSE).build());

        switch (client.getConsultationStateMachine()) {
            case NAME -> {
                clientProblem.setName(message.getText());
                sendMessageService.sendMessage("Модель:", message.getChatId(), null);
                client.setConsultationStateMachine(ConsultationStateMachine.MODEL);
            }
            case MODEL -> {
                clientProblem.setModel(message.getText());
                sendMessageService.sendMessage("Описание проблемы:", message.getChatId(), null);
                client.setConsultationStateMachine(ConsultationStateMachine.DESCRIPTION);
            }
            case DESCRIPTION -> {
                clientProblem.setDescription(message.getText());
                sendMessageService.sendMessage("Адрес:", message.getChatId(), null);
                client.setConsultationStateMachine(ConsultationStateMachine.ADDRESS);
            }
            case ADDRESS -> {
                clientProblem.setAddress(message.getText());
                clientProblem.setStatus(Boolean.TRUE);
                client.getClientProblems().add(clientProblem);
                client.setConsultationStateMachine(null);
                sendMessageService.sendMessage("Звонок заказан!", message.getChatId(), null);

            }
        }
        clientProblemRepository.save(clientProblem);
        clientRepository.save(client);
    }
}
