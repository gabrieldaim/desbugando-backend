package com.desbugando_backend.api.controller;

<<<<<<< HEAD
import com.desbugando_backend.api.services.FirebaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
=======
>>>>>>> parent of 8b1aff2 (adicionando funções de post e get de imagem)
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
<<<<<<< HEAD
import java.io.InputStream;
=======
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
>>>>>>> parent of 8b1aff2 (adicionando funções de post e get de imagem)

@RestController
@RequestMapping("/api/files")
public class testController {

<<<<<<< HEAD
    @Autowired
    private FirebaseStorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = storageService.uploadFile(file);
            return ResponseEntity.ok("Arquivo carregado com sucesso. URL: " + fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Falha ao carregar o arquivo: " + e.getMessage());
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        try {
            // Faz o download do arquivo do Firebase Storage
            InputStream fileStream = storageService.downloadFile(fileName);

            // Lê o conteúdo do InputStream em bytes
            byte[] fileBytes = fileStream.readAllBytes();

            // Configura os headers da resposta HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

            // Retorna a resposta com o conteúdo do arquivo e headers
            return ResponseEntity.ok().headers(headers).body(fileBytes);
        } catch (Exception e) {
            // Trata exceções caso o arquivo não seja encontrado ou ocorra algum erro
=======
    @GetMapping
    public ResponseEntity testar(){
        String test = "funcionando!!!";
        return ResponseEntity.ok(test);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        // Obter o caminho do diretório src/main/resources
        ClassLoader classLoader = getClass().getClassLoader();
        Path resourcePath;
        try {
            resourcePath = Paths.get(classLoader.getResource("").toURI()).resolve("imagens");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o diretório de recursos");
        }

        // Verifique se o diretório existe, caso contrário, crie-o
        if (!Files.exists(resourcePath)) {
            try {
                Files.createDirectories(resourcePath);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível criar o diretório");
            }
        }

        // Obtenha o nome original do arquivo
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome do arquivo inválido");
        }

        // Crie um novo arquivo no diretório especificado
        Path destinationPath = resourcePath.resolve(fileName);
        try {
            // Transfira o conteúdo do arquivo enviado para o novo arquivo
            file.transferTo(destinationPath.toFile());
            return ResponseEntity.status(HttpStatus.OK).body("Arquivo salvo com sucesso em: " + destinationPath.toAbsolutePath().toString());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar o arquivo");
        }
    }

    @GetMapping("/imagem/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        // Obter o caminho do diretório src/main/resources/imagens
        ClassLoader classLoader = getClass().getClassLoader();
        Path resourcePath;
        try {
            resourcePath = Paths.get(classLoader.getResource("").toURI()).resolve("imagens");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        // Caminho completo do arquivo
        Path filePath = resourcePath.resolve(fileName);
        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        try {
            // Ler o conteúdo do arquivo
            byte[] imageBytes = Files.readAllBytes(filePath);

            // Definir os headers apropriados para a resposta
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", Files.probeContentType(filePath));
            headers.add("Content-Disposition", "inline; filename=\"" + fileName + "\"");

            // Retornar a imagem no corpo da resposta
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
>>>>>>> parent of 8b1aff2 (adicionando funções de post e get de imagem)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
