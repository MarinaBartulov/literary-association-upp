package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import team16.literaryassociation.model.LiteraryWork;
import team16.literaryassociation.repository.LiteraryWorkRepository;
import team16.literaryassociation.services.LiteraryWorkService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class LiteraryWorkServiceImpl implements LiteraryWorkService {

    @Autowired
    private LiteraryWorkRepository literaryWorkRepository;

    @Override
    public LiteraryWork save(LiteraryWork literaryWork) {
        return literaryWorkRepository.save(literaryWork);
    }

    @Override
    public String store(MultipartFile file, String processId, String username) {

        System.out.println("Usao u upload SERVICE");
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        if(!Files.exists(Paths.get("literary-works-dir/"+username))) {
            try {
                Files.createDirectory(Paths.get("literary-works-dir/"+username));
                System.out.println("Kreira prvi direktorijum ");
            } catch (IOException e1) {
                System.out.println("Fejluje da Kreira prvi direktorijum ");
                e1.printStackTrace();
            }
        }

        if(!Files.exists(Paths.get("literary-works-dir/"+username+"/" + processId))) {
            try {
                Files.createDirectory(Paths.get("literary-works-dir/"+username+"/" + processId));
                System.out.println("Kreira drugi direktorijum ");
            } catch (IOException e1) {
                System.out.println("Fejluje da Kreira drugi direktorijum ");
                e1.printStackTrace();
            }
        }

        try {
            Files.copy(file.getInputStream(), Paths.get("literary-works-dir/"+username+"/" + processId).resolve(file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Kreira fajl");
        } catch (Exception e) {
            System.out.println("Ne Kreira fajl");
            throw new RuntimeException("FAIL!");
        }

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/literaryWork/download/")
                .path(username + "/" + processId+"/"+fileName).toUriString();
        System.out.println("U store servisu - download URL:");
        System.out.println(fileDownloadUri);
        return fileDownloadUri;
    }
}
