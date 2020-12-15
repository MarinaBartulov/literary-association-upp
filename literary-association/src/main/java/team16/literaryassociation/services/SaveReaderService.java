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
                 newReader.setFirstName((String)f.getFieldValue());
                 cmdUser.setFirstName((String)f.getFieldValue());
             }
             if(f.getFieldId().equals("lastName")){
                 newReader.setLastName((String)f.getFieldValue());
                 cmdUser.setLastName((String)f.getFieldValue());
             }
             if(f.getFieldId().equals("email")){
                 newReader.setEmail((String)f.getFieldValue());
                 cmdUser.setEmail((String)f.getFieldValue());
             }
             if(f.getFieldId().equals("password")){
//                 String pass = "";
//                 String salt = BCrypt.gensalt();
//                 pass = BCrypt.hashpw(f.getFieldValue(), salt);
                 newReader.setPassword((String)f.getFieldValue());
                 cmdUser.setPassword((String)f.getFieldValue());
             }
             if(f.getFieldId().equals("username")){
                 newReader.setUsername((String)f.getFieldValue());
                 cmdUser.setId((String)f.getFieldValue());
             }
             if(f.getFieldId().equals("city")){
                 newReader.setCity((String)f.getFieldValue());
             }
             if(f.getFieldId().equals("country")){
                 newReader.setCountry((String)f.getFieldValue());
             }
             if(f.getFieldId().equals("betaReader")){
                 newReader.setBetaReader((boolean)f.getFieldValue());
             }
             //ovde treba dodati deo za zanrove

        }

        identityService.saveUser(cmdUser);
        readerService.saveReader(newReader);


    }
}
