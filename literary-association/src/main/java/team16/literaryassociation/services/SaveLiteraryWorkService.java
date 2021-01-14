package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.model.LiteraryWork;
import team16.literaryassociation.model.MembershipApplication;

import java.util.List;

@Service
public class SaveLiteraryWorkService implements JavaDelegate {

    @Autowired
    private MembershipApplicationService membershipApplicationService;

    @Autowired
    private LiteraryWorkService literaryWorkService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Usao u save literary work service");
        execution.setVariable("negativeOpinionCounter", 0); // daju misljenje ispocetka
        execution.setVariable("moreMaterialNeeded", 0); // daju misljenje ispocetka

        Long membershipApplicationId = (Long) execution.getVariable("membership_application_id");
        System.out.println("membership application id: " + membershipApplicationId);
        MembershipApplication membershipApplication = membershipApplicationService.getOne(membershipApplicationId);
        if(membershipApplication == null)
        {
            System.out.println("Nije nasao MembershipApplication");
            return;
        }

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        System.out.println("Velicina formData: "+ formData.size() );

        int i = 0;
        for(FormSubmissionDTO dto : formData)
        {
            System.out.println("Pravi Literary Work, brojac:" + i);
            String title = (String)dto.getFieldValue();
            String path = (String)execution.getVariable("pdfFileLocation"+i);
            String url = (String)execution.getVariable("url"+i);

            LiteraryWork literaryWork = new LiteraryWork(title, path, url);
            literaryWork.setMembershipApplication(membershipApplication);
            literaryWorkService.save(literaryWork);
            i++;
        }
    }
}
