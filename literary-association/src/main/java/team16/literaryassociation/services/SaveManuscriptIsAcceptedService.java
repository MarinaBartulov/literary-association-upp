package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Manuscript;
import team16.literaryassociation.model.Writer;
import team16.literaryassociation.services.interfaces.ManuscriptService;

@Service
public class SaveManuscriptIsAcceptedService implements JavaDelegate {

    @Autowired
    private ManuscriptService manuscriptService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("Usao u save manuscript is accepted");

        Long manuscriptId = (Long) execution.getVariable("manuscriptId");
        Manuscript manuscript = this.manuscriptService.findById(manuscriptId);
        boolean accept = (boolean) execution.getVariable("accept");
        manuscript.setAccepted(accept);

        String reasonForRejection = "";
        if(!accept){
            reasonForRejection = (String) execution.getVariable("reasonForRejection");
            manuscript.setReasonForRejection(reasonForRejection);
        }

        this.manuscriptService.save(manuscript);

        if(!accept){
            System.out.println("Salje se mejl da je odbijen manuscript");
            Writer writer = manuscript.getBookRequest().getWriter();
            String email = writer.getEmail();
            String subject = "Manuscript rejected.";
            String text = "Hello " + writer.getFirstName() + " " + writer.getLastName() + ",\n\nYour manuscript has been rejected. Reason for rejection: " + reasonForRejection +
                    "\n\nBest regards,\nLiterary association";
            execution.setVariable("email", email);
            execution.setVariable("emailText", text);
            execution.setVariable("emailSubject", subject);
        }
    }
}
