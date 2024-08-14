package br.com.alura.chatbot.web.controller;

import br.com.alura.chatbot.domain.service.ChatbotService;
import br.com.alura.chatbot.web.dto.PerguntaDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/", "chat"})
public class ChatController {

    private static final String PAGINA_CHAT = "chat";

    private final ChatbotService chatbotService;

    public ChatController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @GetMapping
    public String carregarPaginaChatbot(Model model) {
        var mensagens = chatbotService.carregarHistorico();

        model.addAttribute("historico", mensagens);

        return PAGINA_CHAT;
    }

    @PostMapping
    @ResponseBody
    public String responderPergunta(@RequestBody PerguntaDto perguntaDto) {
        return chatbotService.responderPergunta(perguntaDto.pergunta());
    }

    @GetMapping("limpar")
    public String limparConversa() {
        chatbotService.limparHistorico();
        return "redirect:/chat";
    }

}
