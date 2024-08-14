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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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

    public List<String> carregarHistoricoDeMensagens() {
        var mensagens = new ArrayList<String>();

        if (this.threadId != null) {
            mensagens.addAll(
                    openAiService
                            .listMessages(this.threadId)
                            .getData()
                            .stream()
                            .sorted(Comparator.comparingInt(Message::getCreatedAt))
                            .map(m -> m.getContent().get(0).getText().getValue())
                            .toList()
            );
        }

        return mensagens;
    }

    /**
     * Embora não seja necessário excluir a thread para limpar o histórico, essa é uma prática recomendada ao utilizar o modo Assistant da OpenAI. A gestão eficiente das threads pode ter um impacto significativo tanto na otimização dos recursos quanto na redução de custos associados à plataforma. Abaixo, listamos alguns benefícios dessa recomendação:
     * 1. Proteção de dados e privacidade: threads antigas podem conter informações sensíveis ou confidenciais que, se não forem mais necessárias, devem ser excluídas para minimizar riscos de segurança e proteger a privacidade dos dados.
     * 2. Limpeza e organização de dados: acumular threads sem utilidade pode levar à desordem e dificuldade de navegação. Ao remover dados irrelevantes, mantém-se um ambiente de trabalho organizado, facilitando a localização de informações importantes e a gestão geral da plataforma.
     * 3. Otimização de recursos: recursos computacionais são alocados para manter e processar as threads existentes. Eliminando threads inativas, pode-se reduzir a carga sobre os servidores e otimizar o uso de recursos, o que é crucial para o desempenho do sistema como um todo.
     * 4. Economia de custos: dependendo do modelo de cobrança, armazenar e gerenciar grandes volumes de dados pode acarretar custos adicionais. Removendo dados desnecessários, usuários e organizações podem economizar financeiramente ao diminuir o uso de armazenamento e processamento.
     */
    public void apagarThread() {
        if (this.threadId != null) {
            openAiService.deleteThread(this.threadId);
            this.threadId = null;
        }
    }

}
