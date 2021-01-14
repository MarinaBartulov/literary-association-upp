package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.User;
import team16.literaryassociation.services.impl.EmailService;
import team16.literaryassociation.services.interfaces.UserService;

@Service
public class SendRejectedEmailToWriterService implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Uslo u SendRejectedEmailToWriterService");
        String username = (String) execution.getVariable("username");
        User user = this.userService.findByUsername(username);

        String text = "Hello " + user.getFirstName() + " " + user.getLastName() + ",\n\nWe are sorry to inform you that your membership application has been rejected" +
                " by our board members."
                + "\n\nBest regards,\nLiterary association";

        String subject = "Literary association membership application.";

        emailService.sendEmail(user.getEmail(), subject, text);

    }
}
