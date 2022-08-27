package MainTeam.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDependency {

    private Long chatId;
    private Long timestamp;
    private Consultation consultation;
    private ConsultationStateMachine consultationStateMachine;
}
