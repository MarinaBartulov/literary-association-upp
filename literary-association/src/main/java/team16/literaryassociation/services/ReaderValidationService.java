package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;

import java.util.List;

@Service
public class ReaderValidationService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Uslo u ReaderValidationService");

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("registrationData");
        boolean isValid = true;
        boolean isBeta = false;

        for(FormSubmissionDTO item: formData){
            System.out.println(item.getFieldId() + " : " + item.getFieldValue());

        }
        isValid = true;
        execution.setVariable("isValid", isValid);
        System.out.println("postavio isValid na " + isValid);
    }
}
