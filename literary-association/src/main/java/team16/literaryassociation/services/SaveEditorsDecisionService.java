package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.BookRequest;
import team16.literaryassociation.services.interfaces.BookRequestService;

@Service
public class SaveEditorsDecisionService implements JavaDelegate {

    @Autowired
    private BookRequestService bookRequestService;


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Uslo u save editor's decision service");

        boolean accept = (boolean) execution.getVariable("accept");
        String explanation = (String) execution.getVariable("explanation");
        Long bookRequestId = (Long) execution.getVariable("bookRequestId");
        BookRequest br = this.bookRequestService.findById(bookRequestId);
        br.setAccepted(accept);
        if(!accept){
            br.setRejectExplanation(explanation);
        }
        this.bookRequestService.save(br);

        execution.setVariable("email", br.getWriter().getEmail());
        String subject = "";
        String text = "";
        if(accept){
            System.out.println("Salje se mejl da je prihvatio");
            subject = "Book request accepted.";
            text = "Hello " + br.getWriter().getFirstName() + " " + br.getWriter().getLastName() + ",\n\nYour request for book publishing has been accepted. Please upload the manuscript." +
                    "\n\nBest regards,\nLiterary association";
        }else{
            System.out.println("Salje se mejl da je odbio");
            subject = "Book request rejected.";
            text = "Hello " + br.getWriter().getFirstName() + " " + br.getWriter().getLastName() + ",\n\nYour request for book publishing has been rejected. Reason: " + explanation +
                    "\n\nBest regards,\nLiterary association";
        }

        execution.setVariable("emailText", text);
        execution.setVariable("emailSubject", subject);

    }
}
