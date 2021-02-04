package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.EditorPlagiarismNote;
import team16.literaryassociation.model.PlagiarismComplaint;
import team16.literaryassociation.services.interfaces.PlagiarismComplaintService;

@Service
public class GetAllEditorsNotesService implements JavaDelegate {

    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long plagiarismComplaintId = (Long) delegateExecution.getVariable("plagiarismComplaintId");

        PlagiarismComplaint plagiarismComplaint = plagiarismComplaintService.findById(plagiarismComplaintId);
        if(plagiarismComplaint == null) {
            System.out.println("Nije nasao plagiarism complaint");
            return;
            //throw new BpmnError("BETA_READER_SAVING_FAILED", "Finding beta-reader failed.");
        }

        String notes = "";
        int i = 0;
        for (EditorPlagiarismNote epn: plagiarismComplaint.getEditorPlagiarismNotes()) {
            i++;
            notes += "Note " + i + ": \n" + epn.getNotes() + "\n\n";
        }

        delegateExecution.setVariable("allEditorsNotes", notes);
        //za novi krug brojanja misljenja
        delegateExecution.setVariable("plagiat", 0);
        delegateExecution.setVariable("notPlagiat",0);
    }
}
