# Curso GPT e Java: Desenvolvimento de um Chatbot com IA

## Descrição

Este curso aborda o desenvolvimento de um chatbot em Java e Spring Boot, integrado com a OpenAI, incluindo o uso de funcionalidades avançadas como o modo Assistant, armazenamento de histórico, recuperação de conhecimento via arquivos e criação de funções dinâmicas.

### Tópicos Abordados

- Desenvolva um chatbot em Java e Spring Boot integrado com a OpenAI.
- Utilize o modo Assistant da OpenAI para manter o histórico de mensagens.
- Armazene as mensagens em Threads e recupere o histórico de conversas.
- Faça upload de arquivos com a ferramenta de Knowledge Retrieval (File search) da OpenAI.
- Crie funções dinâmicas com a ferramenta de Function Calling da OpenAI.

### Aula 1: Stream de Mensagens

- Baixe e execute a aplicação Chatbot.
- Integre a aplicação com a API da OpenAI.
- Utilize o recurso de streaming da API da OpenAI.
- Adapte o código da aplicação para suportar o streaming de mensagens.

### Aula 2: Modo Assistant

- Entenda que o modo de chat padrão da OpenAI não armazena o histórico de mensagens.
- Descubra o modo Assistant da OpenAI, que mantém o histórico de mensagens.
- Compreenda o funcionamento do modo Assistant, seus objetos e fluxo de trabalho.
- Configure e teste o modo Assistant via Playground.
- Adapte o código do Chatbot para utilizar o modo Assistant, considerando o histórico de conversas.

### Aula 3: Histórico de Conversas

- Saiba que uma Thread e todas as suas mensagens ficam armazenadas na API da OpenAI.
- Escreva código Java para recuperar mensagens de uma Thread.
- Carregue e exiba mensagens da Thread quando o usuário acessar o chatbot.
- Limpe o histórico de conversa, apagando a Thread atual.

### Aula 4: Knowledge Retrieval (File Search)

- Aprenda que assistentes na OpenAI podem consultar arquivos para formar uma base de conhecimento.
- Habilite a ferramenta de Knowledge Retrieval (File search) e faça upload de documentos.
- Entenda como as respostas geradas referenciam os arquivos consultados.
- Adapte o código da aplicação para remover referências nas mensagens.

### Aula 5: Function Calling

- Compreenda as limitações da ferramenta de Knowledge Retrieval devido ao conteúdo estático dos arquivos.
- Explore a ferramenta de Function Calling para gerar respostas dinâmicas via chamada de funções.
- Declare e teste funções no Playground do modo Assistant da OpenAI.
- Adapte o código do chatbot para permitir a chamada de uma função, como o cálculo de frete.

## Requirements

- [JDK 17](https://www.oracle.com/br/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://maven.apache.org)

## Build

```shell
cd alura-openai-chatbot-java
mvn install
```

## Run

`br.com.alura.chatbot.ChatbotApplication`

## Environment variables

 Name                | Value               
---------------------|---------------------
 OPENAI_API_KEY      | OpenAI API Key      
 OPENAI_ASSISTANT_ID | OpenAI Assistant ID 

## Chatbot

http://localhost:8080/

## Important links

* [Alura | Curso de GPT e Java: desenvolva um Chatbot com IA](https://cursos.alura.com.br/course/gpt-java-desenvolva-chatbot-ia)
* [OpenAI | Streaming](https://platform.openai.com/docs/api-reference/streaming)
* [OpenAI | Assistants](https://platform.openai.com/docs/assistants/overview)
* [OpenAI | Assistant Tools](https://platform.openai.com/docs/assistants/tools)
* [OpenAI | Supported files](https://platform.openai.com/docs/assistants/tools/file-search)
* [OpenAI | Playground](https://platform.openai.com/playground/assistants)
* [OpenAI | Function calling](https://platform.openai.com/docs/assistants/tools/function-calling)