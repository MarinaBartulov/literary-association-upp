package team16.literaryassociation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.User;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private Environment env;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Async
    public void sendConfirmRegMail(User user, String processInstanceId) throws MailException, InterruptedException, MessagingException, MessagingException {
        String token = UUID.randomUUID().toString();
        this.verificationTokenService.createVerificationToken(user, token);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String confirmationUrl
                =  "https://localhost:3000/registrationConfirmation/" + processInstanceId + "/" + token;
        String link = "<a href='" + confirmationUrl + "'>" + confirmationUrl + "</a>";
        String htmlMsg = "Hello, \n " + user.getFirstName() + " " + user.getLastName() + ",\n\n Please confirm your registration by clicking on the link below: " +
                " \n " + link;
        helper.setText(htmlMsg, true);
        helper.setTo(user.getEmail());
        helper.setSubject("Literary association account activation.");
        helper.setFrom(env.getProperty("spring.mail.username"));
        javaMailSender.send(mimeMessage);
    }
}
