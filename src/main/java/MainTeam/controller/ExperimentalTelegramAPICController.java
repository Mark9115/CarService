package MainTeam.controller;

import MainTeam.entity.Message;
import MainTeam.service.TelegramAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/experimental")
@RequiredArgsConstructor
public class ExperimentalTelegramAPICController {

    private final TelegramAPIService telegramAPIService;

    @PostMapping
    public void takeBody(@RequestBody Message message) {
        telegramAPIService.takeMessageFromClient(message);
    }
}
