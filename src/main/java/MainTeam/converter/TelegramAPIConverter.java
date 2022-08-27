package MainTeam.converter;

import MainTeam.entity.Client;
import MainTeam.entity.Consultation;
import org.springframework.stereotype.Component;

@Component
public class TelegramAPIConverter {

    public Client convertFromConsultationToClient(Consultation consultation){

        return Client
            .builder()
            .name(consultation.getName())
            .model(consultation.getModel())
            .description(consultation.getDescription())
            .address(consultation.getAddress())
            .build();
    }
}
