package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.model.Genre;
import team16.literaryassociation.services.interfaces.GenreService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookRequestValidationService implements JavaDelegate {

    @Autowired
    private GenreService genreService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, Object> map = this.listFieldsToMap(formData);
        boolean bookIsValid = true;

        if(((String)map.get("title")).equals("")){
            bookIsValid = false;
        }
        if(((String)map.get("title")).equals("")){
            bookIsValid = false;
        }

        Map<String, String> genre = ((List<Map<String,String>>) map.get("genre")).get(0);
        if(this.genreService.findById((Long.parseLong(genre.get("id")))) == null){
            bookIsValid = false;
        }

        execution.setVariable("bookIsValid", bookIsValid);
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
