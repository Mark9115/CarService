package MainTeam.controller;

import MainTeam.dto.Message;
import MainTeam.service.RedirectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/experimental")
@RequiredArgsConstructor
public class TelegramApiController {

    private final RedirectionService redirectionService;

    @PostMapping
    public void takeBody(@RequestBody Message message) {
        redirectionService.redirection(message);
    }
}
