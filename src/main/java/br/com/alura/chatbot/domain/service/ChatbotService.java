package br.com.alura.chatbot.domain.service;

import br.com.alura.chatbot.openai.DadosRequisicaoChatCompletion;
import br.com.alura.chatbot.openai.OpenAIClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatbotService {

    private final OpenAIClient openAIClient;

    public ChatbotService(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    public String responderPergunta(String pergunta) {
        var promptSistema = "Você é um chatbot de atendimento a clientes de um ecommerce e deve responder apenas perguntas relacionadas com o ecommerce";
        var dados = new DadosRequisicaoChatCompletion(promptSistema, pergunta);
        return openAIClient.enviarRequisicaoChatCompletion(dados);
    }

    public List<String> carregarHistorico() {
        return openAIClient.carregarHistoricoDeMensagens();
    }

    public void limparHistorico() {
        openAIClient.apagarThread();
    }

}
