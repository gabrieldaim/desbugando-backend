package com.desbugando_backend.api.controller;

import com.desbugando_backend.api.domain.usuarios.Usuarios;
import com.desbugando_backend.api.infra.security.CustomUserDetailsService;
import com.desbugando_backend.api.infra.security.TokenService;
import com.desbugando_backend.api.repositories.UsuariosRepository;
import com.desbugando_backend.api.services.FirebaseStorageService;
import com.desbugando_backend.api.util.InformacoesToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
public class testController {

    @Autowired
    private FirebaseStorageService storageService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        Usuarios usuarioToken = new InformacoesToken(tokenService,customUserDetailsService).getCurrentUser();
        try {
            String folder = "fotosPerfil"; // Caminho da pasta onde você deseja salvar os arquivos
            String fileUrl = storageService.uploadFile(file, folder);
            usuarioToken.setUrlFoto(fileUrl);
            usuariosRepository.save(usuarioToken);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Falha ao carregar o arquivo: " + e.getMessage());
        }
    }

    @GetMapping("/files/{fileName}")
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
