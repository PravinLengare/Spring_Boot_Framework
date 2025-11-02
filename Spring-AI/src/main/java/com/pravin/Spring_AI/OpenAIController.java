package com.pravin.Spring_AI;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenAIController {

    // using the chat model
    /*
    private OpenAiChatModel chatModel;

    public OpenAIController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }
    @GetMapping("/api/{message}")
    public String getResponse(@PathVariable String message){

        String response = chatModel.call(message);

        return response;
    }

     */

    // using the chat Client
    /*
    private ChatClient chatClient;

    public OpenAIController(OpenAiChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }
    @GetMapping("/api/{message}")
    public ResponseEntity<String> getResponse(@PathVariable String message){

        String response = chatClient
                            .prompt(message)
                            .call()
                            .content();

        return ResponseEntity.ok(response);
    }

     */

    // using metadata

    private ChatClient chatClient;

//    public OpenAIController(OpenAiChatModel chatModel) {
//        this.chatClient = ChatClient.create(chatModel);
//    }

    ChatMemory chatMemory = MessageWindowChatMemory.builder().build();

    public OpenAIController(ChatClient.Builder builder){
        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();

    }

    @GetMapping("/api/{message}")
    public ResponseEntity<String> getResponse(@PathVariable String message){

       ChatResponse chatResponse = chatClient
                .prompt(message)
                .call()
                .chatResponse();

        System.out.println(chatResponse.getMetadata().getModel());

       String response = chatResponse
               .getResult()
               .getOutput()
               .getText();

        return ResponseEntity.ok(response);
    }
}
