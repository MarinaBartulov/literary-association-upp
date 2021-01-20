package team16.literaryassociation.services;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Reader;
import team16.literaryassociation.services.impl.EmailService;
import team16.literaryassociation.services.interfaces.UserService;

@Service
public class SendEmailForPenaltyPointService implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("Usao u slanje mejla zbog isteka roka za davanje komentara od strane beta-reader-a.");

        String username = this.identityService.getCurrentAuthentication().getUserId();
        Reader betaReader = (Reader) userService.findByUsername(username);
        if(betaReader == null)
        {
            System.out.println("Nije nasao Reader-a");
            return;
        }

        String subject = "Got penalty point";
        String text = "Hello " + betaReader.getFirstName() + " " + betaReader.getLastName() + ",\n\nYou" +
                "'ve got one more penalty point, because you haven't commented the manuscript in time." +
                "If you reach five penalty points, you'll lose your beta-reader status." +
                "\n\nBest regards,\nLiterary association";

        emailService.sendEmail(betaReader.getEmail(), subject, text);
    }
}
