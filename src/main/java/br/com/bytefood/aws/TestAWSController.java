package br.com.bytefood.aws;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class TestAWSController {

    private final AWSS3Service awss3Service;

    @PostMapping
    public ResponseEntity<String> upload(@RequestParam("file")MultipartFile file,
                                         @RequestParam("keyName")String keyName){

        URL savedFile = awss3Service.uploadFile(keyName, file);
        return ResponseEntity.ok(savedFile.toString());
    }
}
