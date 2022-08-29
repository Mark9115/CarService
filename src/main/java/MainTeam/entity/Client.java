package MainTeam.entity;

import MainTeam.entity.enumeration.ConsultationStateMachine;
import MainTeam.entity.enumeration.PartStateMachine;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "client")
public class Client {
    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "consultation_state")
    private ConsultationStateMachine consultationStateMachine;

    @Column(name = "part_state")
    private PartStateMachine partStateMachine;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "client_problem_id")
    private List<ClientProblem> clientProblems;

}
