package com.desbugando_backend.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/teste")
public class testController {

    @Value("ee8d567c8260133")
    private String imgurClientId;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.imgur.com/3/")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Client-ID " + imgurClientId)
            .build();

    // HashMap para armazenar URLs das imagens
    private final Map<String, String> imageStore = new HashMap<>();

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Converter o arquivo em Base64
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());

            // Criar os dados do formulário
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("image", base64Image);
            formData.add("type", "base64");
            formData.add("privacy", "private"); // Definindo a imagem como privada

            // Enviar a imagem para Imgur
            Mono<String> response = webClient.post()
                    .uri("image")
                    .bodyValue(formData)
                    .retrieve()
                    .bodyToMono(String.class);

            // Obter a resposta
            String responseBody = response.block();
            if (responseBody != null) {
                // Extrair a URL da resposta JSON
                String imageUrl = extractImageUrl(responseBody);

                // Gerar um ID único para a imagem e armazenar a URL
                String imageId = UUID.randomUUID().toString();
                imageStore.put(imageId, imageUrl);

                return ResponseEntity.ok("Imagem enviada com sucesso. ID: " + imageId);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erro ao enviar imagem para Imgur");
            }

        } catch (IOException | WebClientResponseException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar o arquivo");
        }
    }

    @GetMapping("/imagem/{imageId}")
    public ResponseEntity<String> getImage(@PathVariable String imageId) {
        String imageUrl = imageStore.get(imageId);
        if (imageUrl != null) {
            return ResponseEntity.ok(imageUrl);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Imagem não encontrada");
        }
    }

    // Método auxiliar para extrair a URL da imagem da resposta JSON
    private String extractImageUrl(String responseBody) {
        // Simples extração de URL do JSON (substitua por um parser JSON se necessário)
        String urlPrefix = "\"link\":\"";
        int startIndex = responseBody.indexOf(urlPrefix) + urlPrefix.length();
        int endIndex = responseBody.indexOf("\"", startIndex);
        return responseBody.substring(startIndex, endIndex).replace("\\/", "/");
    }
}
