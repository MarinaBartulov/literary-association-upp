package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.dto.GenreDTO;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReaderValidationService implements JavaDelegate {

    @Autowired
    private UserService userService;
    @Autowired
    private GenreService genreService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Uslo u ReaderValidationService");

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, Object> map = this.listFieldsToMap(formData);
        boolean isValid = true;
        boolean isBeta = false;

        for(FormSubmissionDTO item: formData){
            System.out.println(item.getFieldId() + " : " + item.getFieldValue());
        }

        if(userService.findByUsername((String)map.get("username")) != null){
            isValid = false;
        }
        if(!isValidEmailAddress((String) map.get("email"))) {
            isValid = false;
        }
        if(!map.get("password").equals(map.get("confirmPassword"))){
            isValid = false;
        }

        List<Map<String,String>> genres = (List<Map<String,String>>) map.get("genres");
        if(genres.size() < 1){
            isValid = false;
        }
        for(Map<String,String> genre : genres){
            if(this.genreService.findById(Long.parseLong(genre.get("id"))) == null){
                isValid = false;
                break;
            }
        }

        isBeta = (boolean)map.get("betaReader");
        if(isBeta){
            List<Map<String,String>> betaGenres = (List<Map<String,String>>) map.get("betaGenres");
            if(betaGenres.size() < 1){
                isValid = false;
            }
            for(Map<String,String> genre : betaGenres){
                if(this.genreService.findById(Long.parseLong(genre.get("id"))) == null){
                    isValid = false;
                    break;
                }
            }
        }

        execution.setVariable("isValid", isValid);
        System.out.println("postavio isValid na " + isValid);
    }


    private Map<String, Object> listFieldsToMap(List<FormSubmissionDTO> formData) {
        Map<String, Object> retVal = new HashMap<>();
        for(FormSubmissionDTO dto: formData) {
            if(dto.getFieldId() != null) {
                retVal.put(dto.getFieldId(), dto.getFieldValue());
            }
        }
        return retVal;
    }


    private boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}
