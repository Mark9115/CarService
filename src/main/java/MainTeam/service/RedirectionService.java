package MainTeam.service;

import MainTeam.entity.Client;
import MainTeam.dto.Message;
import MainTeam.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RedirectionService {

    private final ClientRepository clientRepository;
    private final ConsultationService consultationService;
    private final OrderSparePartsService orderSparePartsService;
    private final InitialService initialService;
    private final DefaultReplyService defaultReplyService;

    public void redirection(Message message) {
        String messageText = message.getText();
        Client client = clientRepository
            .findClientByChatId(message.getChatId())
            .orElse(Client.builder().chatId(message.getChatId()).build());

        if (client.getPartStateMachine() != null || messageText.equalsIgnoreCase("Заказать запчасть")) {
            orderSparePartsService.stateMachineMovement(client, message);
        } else if (client.getConsultationStateMachine() != null || messageText.equalsIgnoreCase(
            "Получить консультацию")) {
            consultationService.stateMachineMovement(client, message);
        } else if (messageText.equals("/start")) {
            initialService.initial(message);
        } else {
            defaultReplyService.reply(message);
        }
    }

}
