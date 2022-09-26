package com.odonto.uploads;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service
public class FireBaseStorageService {
    final static String urlStorage = "ssds";

    @PostConstruct
    public void init() throws IOException {
        //ler arquivo de configuracao
        if(FirebaseApp.getApps().isEmpty()){
            InputStream serviceAccount = FireBaseStorageService.class
                    .getResourceAsStream("/odonto-ds3t-374b0-firebase-adminsdk-5iv5c-1a4e626af4.json");


//            FileInputStream serviceAccount =
//                    new FileInputStream("path/to/serviceAccountKey.json");

            //definir os dados necessarios para acessar o storage
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("odonto-ds3t-374b0.appspot.com")
                    .build();

            //inicializar o servico cliente Firebase
            FirebaseApp.initializeApp(options);
        }
    }

    public String upLoad(FileUpload arquivo, String name){
        Bucket bucket = StorageClient.getInstance().bucket();

        //pegar arquivo base 64 e converter novamente para bytes

//        arquivo.getBase64().getBytes();
//        byte[] bytes = arquivo.getBase64().getBytes();
        byte[] arquivoEmBytes = Base64.getDecoder().decode(arquivo.getBase64());

        //criar o arquivo com os dados fornecidos
        Blob blob = bucket.create(
                name,
                arquivoEmBytes,
                arquivo.getMimeType()
        );
        // configurar regra para o arquivo
        blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        System.out.println(bucket.getSelfLink());
        return "https:storage.googleapis.com/" + bucket.getName()+"/"+ name ;
    }

}
