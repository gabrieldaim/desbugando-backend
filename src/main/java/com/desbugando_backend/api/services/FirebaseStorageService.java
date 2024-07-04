package com.desbugando_backend.api.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FirebaseStorageService {

    private Storage storage;

    private String bucketName = "desbugando-abcf1.appspot.com";



    @PostConstruct
    public void initialize() throws IOException {

        // Obter o conteúdo JSON das credenciais do ambiente
        String jsonCredentials = System.getenv("FIREBASE_ADMIN_SDK_JSON");
        System.out.println(GoogleCredentials.fromStream(new ByteArrayInputStream(jsonCredentials.getBytes())));
        // Inicializa o SDK do Firebase com as credenciais da variável de ambiente
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(jsonCredentials.getBytes()));
        StorageOptions options = StorageOptions.newBuilder().setCredentials(credentials).build();
        this.storage = options.getService();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        // Nomeie o arquivo de forma única no Storage do Firebase
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Faz o upload do arquivo para o Firebase Storage
        Bucket bucket = storage.get(bucketName);
        bucket.create(fileName, file.getInputStream(), file.getContentType());

        // Obtém a URL pública do arquivo recém enviado
        Blob blob = storage.get(bucketName, fileName);

        return blob.getMediaLink();
    }

    public String uploadFile(MultipartFile file, String folder) throws IOException {
        // Nomeie o arquivo de forma única no Storage do Firebase
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = folder + "/" + fileName; // Inclua a pasta no caminho do arquivo

        // Faz o upload do arquivo para o Firebase Storage
        Bucket bucket = storage.get(bucketName);
        bucket.create(filePath, file.getInputStream(), file.getContentType());

        // Obtém a URL pública do arquivo recém enviado
        Blob blob = storage.get(bucketName, filePath);

        return blob.getMediaLink();
    }


    public InputStream downloadFile(String fileName) {

        // Faz o download do arquivo do Firebase Storage
        Blob blob = storage.get(bucketName).get(fileName);
        if (blob == null) {
            throw new IllegalArgumentException("Arquivo não encontrado: " + fileName);
        }

        // Obtém os bytes do arquivo
        byte[] fileBytes = blob.getContent();

        // Converte os bytes em um InputStream
        return new ByteArrayInputStream(fileBytes);
    }

}
