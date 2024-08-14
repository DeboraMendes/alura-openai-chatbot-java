package br.com.alura.chatbot.openai;

import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;

@Component
public class OpenAIClient {

    private final String opeanAIApiKey;
    private final OpenAiService openAiService;

    public OpenAIClient(@Value("${app.openai.api.key}") String opeanAIApiKey) {
        this.opeanAIApiKey = opeanAIApiKey;
        this.openAiService = new OpenAiService(opeanAIApiKey, Duration.ofSeconds(60));
    }

    /**
     * O streaming de mensagens da API da OpenAI é uma característica avançada que permite a comunicação bidirecional e
     * contínua entre o cliente e o servidor.
     * Essa funcionalidade é especialmente útil para aplicações que requerem troca de dados em tempo real ou
     * para a manutenção de uma conversa persistente em cenários que envolvem modelos de linguagem,
     * como chatbots ou assistentes virtuais.
     *
     * @param dados prompts
     * @return resposta
     */
    public Flowable<ChatCompletionChunk> enviarRequisicaoChatCompletion(DadosRequisicaoChatCompletion dados) {
        var request = ChatCompletionRequest
                .builder()
                .model("gpt-4-1106-preview")
                .messages(Arrays.asList(
                        new ChatMessage(
                                ChatMessageRole.SYSTEM.value(),
                                dados.promptSistema()),
                        new ChatMessage(
                                ChatMessageRole.USER.value(),
                                dados.promptUsuario())))
                .stream(true)
                .build();

        var segundosParaProximaTentiva = 5;
        var tentativas = 0;
        while (tentativas++ != 5) {
            try {
                return openAiService
                        .streamChatCompletion(request);
            } catch (OpenAiHttpException ex) {
                var errorCode = ex.statusCode;
                switch (errorCode) {
                    case 401 -> throw new RuntimeException("Erro com a chave da API!", ex);
                    case 429, 500, 503 -> {
                        try {
                            Thread.sleep(1000 * segundosParaProximaTentiva);
                            segundosParaProximaTentiva *= 2;
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        throw new RuntimeException("API Fora do ar! Tentativas finalizadas sem sucesso!");
    }

}
