package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.model.User;

import java.util.List;

@Service
public class SendEmailService implements JavaDelegate {

    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Uslo u EmailService");

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("registrationData");

        String username = "";
        for(FormSubmissionDTO f: formData){
            if(f.getFieldId().equals("username")){
                username = (String)f.getFieldValue();
            }
        }
        User user = this.userService.findByUsername(username);
        mailService.sendConfirmRegMail(user, execution.getProcessInstanceId());

    }
}
