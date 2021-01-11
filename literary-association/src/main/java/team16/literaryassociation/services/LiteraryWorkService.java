package team16.literaryassociation.services;

import org.springframework.web.multipart.MultipartFile;
import team16.literaryassociation.model.LiteraryWork;


public interface LiteraryWorkService {

    LiteraryWork save(LiteraryWork literaryWork);
    String store(MultipartFile file, String processId, String username);
}
