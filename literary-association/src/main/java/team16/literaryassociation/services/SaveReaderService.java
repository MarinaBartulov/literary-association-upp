package team16.literaryassociation.services;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.model.Reader;

import java.util.List;

@Service
public class SaveReaderService implements JavaDelegate {

    @Autowired
    IdentityService identityService;

    @Autowired
    ReaderService readerService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("Uslo u SaveReaderService");
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("registrationData");

        org.camunda.bpm.engine.identity.User cmdUser = identityService.newUser("newUser");
        Reader newReader = new Reader();

        for(FormSubmissionDTO f : formData) {
            System.out.println(f.getFieldId());
            System.out.println(f.getFieldValue());
        }

        for(FormSubmissionDTO f : formData){

             if(f.getFieldId().equals("firstName")){
                 newReader.setFirstName(f.getFieldValue());
                 cmdUser.setFirstName(f.getFieldValue());
             }
             if(f.getFieldId().equals("lastName")){
                 newReader.setLastName(f.getFieldValue());
                 cmdUser.setLastName(f.getFieldValue());
             }
             if(f.getFieldId().equals("email")){
                 newReader.setEmail(f.getFieldValue());
                 cmdUser.setEmail(f.getFieldValue());
             }
             if(f.getFieldId().equals("password")){
//                 String pass = "";
//                 String salt = BCrypt.gensalt();
//                 pass = BCrypt.hashpw(f.getFieldValue(), salt);
                 newReader.setPassword(f.getFieldValue());
                 cmdUser.setPassword(f.getFieldValue());
             }
             if(f.getFieldId().equals("username")){
                 newReader.setUsername(f.getFieldValue());
                 cmdUser.setId(f.getFieldValue());
             }
             if(f.getFieldId().equals("city")){
                 newReader.setCity(f.getFieldValue());
             }
             if(f.getFieldId().equals("country")){
                 newReader.setCountry(f.getFieldValue());
             }
             if(f.getFieldId().equals("betaReader")){
                 newReader.setBetaReader(Boolean.parseBoolean(f.getFieldValue()));
             }
             //ovde treba dodati deo za zanrove

        }

        identityService.saveUser(cmdUser);
        readerService.saveReader(newReader);


    }
}
