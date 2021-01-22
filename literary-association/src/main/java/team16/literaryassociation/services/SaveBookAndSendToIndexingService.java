package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.model.*;
import team16.literaryassociation.services.interfaces.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaveBookAndSendToIndexingService implements JavaDelegate {

    @Autowired
    private GenreService genreService;

    @Autowired
    private ManuscriptService manuscriptService;

    @Autowired
    private UserService userService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private BookService bookService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("Usao u SaveBookAndSendToIndexingService");

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) delegateExecution.getVariable("formData");
        Map<String, Object> map = this.listFieldsToMap(formData);

        Book book = new Book();

        book.setTitle((String) map.get("title"));
        book.setSynopsis((String) map.get("synopsis"));
        book.setISBN((String) map.get("ISBN"));
        book.setYear((String) map.get("year"));
        try {
            book.setNumOfPages(Integer.parseInt((String) map.get("numberOfPages")));
        } catch (Exception e) {
            throw new BpmnError("BOOK_SAVING_FAILED", "Invalid number of pages.");
        }

        String genreName = (String) map.get("genre");
        Genre genre = this.genreService.findByName(genreName);
        if (genre == null) {
            System.out.println("Nije nasao Genre");
            throw new BpmnError("BOOK_SAVING_FAILED", "Invalid genre.");
        }

        Long manuscriptId = (Long) delegateExecution.getVariable("manuscriptId");
        Manuscript manuscript = manuscriptService.findById(manuscriptId);
        if (manuscript == null) {
            System.out.println("Nije nasao manuscript");
            throw new BpmnError("BOOK_SAVING_FAILED", "Invalid manuscript.");
        }

        book.setGenre(genre);
        book.setManuscript(manuscript);
        book.setPdf(manuscript.getPdf());

        String writerUsername = (String) delegateExecution.getVariable("writer");
        Writer writer = (Writer) userService.findByUsername(writerUsername);
        if (writer == null) {
            System.out.println("Nije nasao writer");
            throw new BpmnError("BOOK_SAVING_FAILED", "Invalid writer.");
        }
        book.setWriter(writer);

        String editorUsername = (String) delegateExecution.getVariable("chosenEditor");
        Editor editor = (Editor) userService.findByUsername(editorUsername);
        if (editor == null) {
            System.out.println("Nije nasao editor");
            throw new BpmnError("BOOK_SAVING_FAILED", "Invalid editor.");
        }
        book.setEditor(editor);

        String lecturerUsername = (String) delegateExecution.getVariable("lecturer");
        Lecturer lecturer = (Lecturer) userService.findByUsername(lecturerUsername);
        if (lecturer == null) {
            System.out.println("Nije nasao lecturer");
            throw new BpmnError("BOOK_SAVING_FAILED", "Invalid lecturer.");
        }
        book.setLecturer(lecturer);

        String publisher = (String) map.get("publisher");
        Merchant merchant = merchantService.findByEmail(publisher);
        if(merchant == null) {
            System.out.println("Nije nasao merchant");
            throw new BpmnError("BOOK_SAVING_FAILED", "Invalid merchant.");
        }
        book.setPublisher(merchant);
        book.setPublishersAddress("Trg Nikole Pasica 4, Beograd");

        bookService.save(book);
    }

    private Map<String, Object> listFieldsToMap(List<FormSubmissionDTO> formData) {
        Map<String, Object> retVal = new HashMap<>();
        for(FormSubmissionDTO dto: formData) {
            if(dto.getFieldId() != null) {
                retVal.put(dto.getFieldId(), dto.getFieldValue());
            }
        }
        return retVal;
    }
}
