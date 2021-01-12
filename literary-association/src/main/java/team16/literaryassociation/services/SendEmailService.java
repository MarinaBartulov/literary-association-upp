package team16.literaryassociation.services;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.model.User;
import team16.literaryassociation.model.Writer;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.UUID;

@Service
public class SendEmailService implements JavaDelegate {

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;
    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("Uslo u EmailService");

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");

        String username = "";
        for(FormSubmissionDTO f: formData){
            if(f.getFieldId().equals("username")){
                username = (String)f.getFieldValue();
            }
        }
        User user = this.userService.findByUsername(username);

        String token = UUID.randomUUID().toString();
        this.verificationTokenService.createVerificationToken(user, token);

        String confirmationUrl = "";

        if (user instanceof Writer){
            confirmationUrl = "https://localhost:3000/registrationConfirmation/writer/" + execution.getProcessInstanceId() + "/"  + token;
        }
        else {
             confirmationUrl = "https://localhost:3000/registrationConfirmation/" + execution.getProcessInstanceId() + "/" + token;
        }
        String text = "Hello " + user.getFirstName() + " " + user.getLastName() + ",\n\nPlease confirm your registration by clicking on the link below: " +
                " \n" + confirmationUrl + "\n\nBest regards,\nLiterary association";

        String subject = "Literary association account activation.";

        emailService.sendEmail(user.getEmail(), subject, text);

    }
}
