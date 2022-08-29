package MainTeam.repository;

import MainTeam.entity.ClientProblem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientProblemRepository extends JpaRepository<ClientProblem, Long> {

    Optional<ClientProblem> findClientProblemByChatIdAndStatus(Long chatId, Boolean status);

}
