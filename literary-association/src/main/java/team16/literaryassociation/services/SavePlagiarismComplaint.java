package team16.literaryassociation.services;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.*;
import team16.literaryassociation.services.interfaces.BookService;
import team16.literaryassociation.services.interfaces.PlagiarismComplaintService;
import team16.literaryassociation.services.interfaces.UserService;

@Service
public class SavePlagiarismComplaint implements JavaDelegate {

    @Autowired
    private BookService bookService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private UserService userService;

    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        String username = identityService.getCurrentAuthentication().getUserId();
        User user = userService.findByUsername(username);

        if (user == null) {
            System.out.println("Nije nasao writera");
            return;
            // throw new BpmnError("BOOK_SAVING_FAILED", "Invalid writer.");
        }

        Long myBookTitleId = (Long) delegateExecution.getVariable("myBookId");

        Book myBook = bookService.findOne(myBookTitleId);

        if (myBook == null) {
            System.out.println("Nije nasao my book");
            return;
            // throw new BpmnError("BOOK_SAVING_FAILED", "Invalid writer.");
        }

        String plagiatBookTitle = (String) delegateExecution.getVariable("plagiatBookTitle");
        String writerFirstName = (String) delegateExecution.getVariable("writerFirstName");
        String writerLastName = (String) delegateExecution.getVariable("writerLastName");

        Book plagiat = bookService.findBookByTitleAndWritersName(plagiatBookTitle, writerFirstName, writerLastName);

        if (plagiat == null) {
            System.out.println("Nije nasao book plagiat");
            return;
            // throw new BpmnError("BOOK_SAVING_FAILED", "Invalid writer.");
        }

        PlagiarismComplaint plagiarismComplaint = new PlagiarismComplaint();
        plagiarismComplaint.setMyBook(myBook);
        plagiarismComplaint.setBookPlagiat(plagiat);
        PlagiarismComplaint newPC = plagiarismComplaintService.save(plagiarismComplaint);

        delegateExecution.setVariable("plagiarismComplaintId", newPC.getId());

        Editor mainEditor = myBook.getEditor();
        delegateExecution.setVariable("mainEditor", mainEditor.getUsername());
        Writer writer = myBook.getWriter();
        delegateExecution.setVariable("email", mainEditor.getEmail());
        String emailText = "Dear " + mainEditor.getFirstName() + " " + mainEditor.getLastName() + ", \n\n " +
                "Writer " + writer.getFirstName() + " " + writer.getLastName() + " has reported plagiarism for his book: " +
                myBook.getTitle() + ". Please, choose editors for the plagiarism decision. \n\nBest regards,\nLiterary association";
        String subject = "Plagiarism detection";
        delegateExecution.setVariable("emailText", emailText);
        delegateExecution.setVariable("emailSubject", subject);
    }
}
