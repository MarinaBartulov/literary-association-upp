package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Book;
import team16.literaryassociation.model.PlagiarismComplaint;
import team16.literaryassociation.services.interfaces.BookService;
import team16.literaryassociation.services.interfaces.PlagiarismComplaintService;

@Service
public class SavePlagiarismDecisionService implements JavaDelegate {

    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;

    @Autowired
    private BookService bookService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        Long plagiarismComplaintId = (Long) execution.getVariable("plagiarismComplaintId");

        PlagiarismComplaint plagiarismComplaint = plagiarismComplaintService.findById(plagiarismComplaintId);
        if(plagiarismComplaint == null){
            System.out.println("Nije nasao zalbu na plagijarizam");
            return;
            ///error
        }

        Integer plagiat = (Integer)execution.getVariable("plagiat");
        Integer notPlagiat = (Integer)execution.getVariable("notPlagiat");

        System.out.println("Plagiat: " + plagiat);
        System.out.println("Not Plagiat: " + notPlagiat);

        if(plagiat>0 && notPlagiat==0)
        {
            Book bookPlagiat = plagiarismComplaint.getBookPlagiat();
            bookPlagiat.setPlagiarism(true);
            bookService.save(bookPlagiat);
        }

    }
}
