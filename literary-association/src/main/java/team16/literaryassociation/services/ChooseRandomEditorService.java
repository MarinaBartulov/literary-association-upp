package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.BookRequest;
import team16.literaryassociation.model.Editor;
import team16.literaryassociation.services.interfaces.BookRequestService;
import team16.literaryassociation.services.interfaces.EditorService;

@Service
public class ChooseRandomEditorService implements JavaDelegate {

    @Autowired
    private BookRequestService bookRequestService;
    @Autowired
    private EditorService editorService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        Long bookRequestId = (Long) execution.getVariable("bookRequestId");
        BookRequest br = this.bookRequestService.findById(bookRequestId);
        Editor editor = this.editorService.findRandomEditor();
        br.setEditor(editor);
        this.bookRequestService.save(br);
        execution.setVariable("chosenEditor", editor.getUsername());

        //slanje email-a editoru
        String text = "Hello " + editor.getFirstName() + " " + editor.getLastName() + ",\n\nA new book has been requested for publishing and assigned to you. Please review it and give your opinion." +
                "\n\nBest regards,\nLiterary association";

        String subject = "Literary association - New book request.";
        execution.setVariable("email", editor.getEmail());
        execution.setVariable("emailSubject", subject);
        execution.setVariable("emailText", text);
    }
}
