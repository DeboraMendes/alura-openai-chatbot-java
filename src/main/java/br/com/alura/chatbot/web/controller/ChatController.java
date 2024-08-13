package br.com.alura.chatbot.web.controller;

import br.com.alura.chatbot.domain.service.ChatbotService;
import br.com.alura.chatbot.web.dto.PerguntaDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@Controller
@RequestMapping({"/", "chat"})
public class ChatController {

    private static final String PAGINA_CHAT = "chat";

    private ChatbotService chatbotService;

    public ChatController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @GetMapping
    public String carregarPaginaChatbot() {
        return PAGINA_CHAT;
    }

    @PostMapping
    @ResponseBody
    public ResponseBodyEmitter responderPergunta(@RequestBody PerguntaDto perguntaDto) {
        var fluxoResposta = chatbotService.responderPergunta(perguntaDto.pergunta());
        var emitter = new ResponseBodyEmitter();

        fluxoResposta.subscribe(chunk -> {
                    var token = chunk.getChoices().get(0).getMessage().getContent();
                    if (token != null) {
                        emitter.send(token);
                    }
                },
                emitter::completeWithError,
                emitter::complete
        );

        return emitter;
    }

    @GetMapping("limpar")
    public String limparConversa() {
        return PAGINA_CHAT;
    }

}
