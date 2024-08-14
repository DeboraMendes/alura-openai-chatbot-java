package br.com.alura.chatbot.domain.service;

import br.com.alura.chatbot.openai.DadosRequisicaoChatCompletion;
import br.com.alura.chatbot.openai.OpenAIClient;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import io.reactivex.Flowable;
import org.springframework.stereotype.Service;

@Service
public class ChatbotService {

    private OpenAIClient openAIClient;

    public ChatbotService(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    public Flowable<ChatCompletionChunk> responderPergunta(String pergunta) {
        var promptSistema = "Você é um chatbot de atendimento a clientes de um ecommerce e deve responder apenas perguntas relacionadas com o ecommerce";
        var dados = new DadosRequisicaoChatCompletion(promptSistema, pergunta);
        return openAIClient.enviarRequisicaoChatCompletion(dados);
    }

}
