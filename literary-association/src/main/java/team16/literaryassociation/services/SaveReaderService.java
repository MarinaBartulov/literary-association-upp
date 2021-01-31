package team16.literaryassociation.services;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.model.Genre;
import team16.literaryassociation.model.Reader;
import team16.literaryassociation.model.Role;
import team16.literaryassociation.services.interfaces.GenreService;
import team16.literaryassociation.services.interfaces.ReaderService;
import team16.literaryassociation.services.interfaces.RoleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaveReaderService implements JavaDelegate {

    @Autowired
    private IdentityService identityService;
    @Autowired
    private ReaderService readerService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("Uslo u SaveReaderService");
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, Object> map = this.listFieldsToMap(formData);


        Reader newReader = new Reader();
        newReader.setFirstName((String)map.get("firstName"));
        newReader.setLastName((String)map.get("lastName"));
        newReader.setEmail((String) map.get("email"));
        System.out.println(passwordEncoder.encode((String) map.get("password")));
        newReader.setPassword(passwordEncoder.encode((String) map.get("password")));
        Role role = this.roleService.findByName("ROLE_READER");
        newReader.getRoles().add(role);
        newReader.setUsername((String) map.get("username"));
        newReader.setCity((String) map.get("city"));
        newReader.setCountry((String) map.get("country"));
        newReader.setBetaReader((boolean) map.get("betaReader"));

        List<String> genres = (List<String>) map.get("genres");
        for (String genre : genres) {
            Genre g = this.genreService.findByName(genre);
            newReader.getGenres().add(g);
        }

        newReader = readerService.saveReader(newReader);

        if(newReader != null){
            execution.setVariable("readerId", newReader.getId());
            org.camunda.bpm.engine.identity.User cmdUser = identityService.newUser(newReader.getUsername());
            cmdUser.setEmail(newReader.getEmail());
            cmdUser.setFirstName(newReader.getFirstName());
            cmdUser.setLastName(newReader.getLastName());
            cmdUser.setPassword(newReader.getPassword());
            try {
                identityService.saveUser(cmdUser);
            }catch(Exception e){
                throw new BpmnError("SAVE_CAMUNDA_USER_FAILED", "Saving camunda user failed.");
            }
        }


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
}
