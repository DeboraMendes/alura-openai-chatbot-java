package br.com.alura.chatbot.openai;

import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.messages.Message;
import com.theokanning.openai.messages.MessageRequest;
import com.theokanning.openai.runs.RunCreateRequest;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.threads.ThreadRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;

@Component
public class OpenAIClient {

    private final String opeanAIApiKey;
    private final String assistantId;
    private final OpenAiService openAiService;

    private String threadId;

    public OpenAIClient(@Value("${app.openai.api.key}") String opeanAIApiKey,
                        @Value("${app.openai.assistant.id}") String assistantId) {
        this.opeanAIApiKey = opeanAIApiKey;
        this.openAiService = new OpenAiService(opeanAIApiKey, Duration.ofSeconds(60));
        this.assistantId = assistantId;
    }

    /**
     * O modo Assistant da OpenAI, embora represente um avanço significativo em termos de inteligência artificial e ofereça múltiplas aplicações, enfrenta algumas limitações. Com a ambição de melhorar continuamente o serviço, a OpenAI está ciente das restrições atuais e planeja expandir as capacidades do Assistant com atualizações futuras.
     * Abaixo, detalhamos algumas das limitações conhecidas na versão beta do modo Assistant da OpenAI:
     * 1. Suporte para saída em tempo real (streaming output): atualmente, o Assistant não oferece suporte para saída contínua de dados, o que inclui mensagens e etapas de execução em tempo real, comumente conhecidas como "streaming". Essa funcionalidade é crucial para aplicações que necessitam de interações constantes e atualizações imediatas.
     * 2. Suporte para notificações: outra restrição é a inexistência de um mecanismo de notificações que permita compartilhar atualizações sobre o status de objetos sem recorrer a pesquisas frequentes ou polling. Isso significa que, no momento, para obter informações atualizadas, é necessário fazer novas solicitações repetidamente, o que pode ser ineficiente.
     * 3. Integração com DALL·E ou funcionalidades de navegação (browsing): o Assistant ainda não tem funcionalidades para integrar o DALL·E, o sistema da OpenAI que gera imagens a partir de descrições textuais, nem para realizar atividades de navegação pela internet. Isso limita as possibilidades de interação com conteúdos visuais e de busca de informações.
     * 4. Criação de mensagens de usuário com imagens: a interação atual não permite que usuários criem mensagens integradas com imagens, o que restringe a capacidade de comunicação a textos puros, sem a riqueza e o contexto que as imagens podem proporcionar.
     *
     * @param dados prompts
     * @return resposta
     */
    public String enviarRequisicaoChatCompletion(DadosRequisicaoChatCompletion dados) {
        var messageRequest = MessageRequest
                .builder()
                .role(ChatMessageRole.USER.value())
                .content(dados.promptUsuario())
                .build();

        if (this.threadId == null) {
            var threadRequest = ThreadRequest
                    .builder()
                    .messages(Arrays.asList(messageRequest))
                    .build();

            var thread = openAiService.createThread(threadRequest);
            this.threadId = thread.getId();
        } else {
            openAiService.createMessage(this.threadId, messageRequest);
        }

        //código omitido

        var runRequest = RunCreateRequest
                .builder()
                .assistantId(assistantId)
                .build();
        var run = openAiService.createRun(threadId, runRequest);

        try {
            while (!run.getStatus().equalsIgnoreCase("completed")) {
                Thread.sleep(1000 * 10); //10 segundos
                run = openAiService.retrieveRun(threadId, run.getId());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var mensagens = openAiService.listMessages(threadId);

        return mensagens
                .getData()
                .stream()
                .max(Comparator.comparingInt(Message::getCreatedAt))
                .orElseThrow()
                .getContent().get(0).getText().getValue();
    }

}
