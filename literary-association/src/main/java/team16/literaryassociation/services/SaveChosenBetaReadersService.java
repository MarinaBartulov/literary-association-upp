package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.BetaReaderDTO;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.mapper.BetaReaderMapper;
import team16.literaryassociation.model.Manuscript;
import team16.literaryassociation.model.Reader;
import team16.literaryassociation.services.interfaces.ManuscriptService;
import team16.literaryassociation.services.interfaces.ReaderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SaveChosenBetaReadersService implements JavaDelegate {

    @Autowired
    private ReaderService readerService;

    @Autowired
    private ManuscriptService manuscriptService;

    @Autowired
    private BetaReaderMapper betaReaderMapper = new BetaReaderMapper();

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("Usao u save chosen beta readers");

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) delegateExecution.getVariable("formData");
        Map<String, Object> map = this.listFieldsToMap(formData);

        Long manuscriptId = (Long) delegateExecution.getVariable("manuscriptId");
        Manuscript manuscript = manuscriptService.findById(manuscriptId);
        if(manuscript == null)
        {
            System.out.println("Nije nasao Manuscript");
            throw new BpmnError("BETA_READER_SAVING_FAILED", "Finding manuscript failed.");
        }

        List<String> betaReaders = (List<String>) map.get("betaReaders");
        for (String betaReader : betaReaders) {
            Reader r = this.readerService.findById(Long.parseLong(betaReader));
            if(r == null) {
                throw new BpmnError("BETA_READER_SAVING_FAILED", "Finding beta-reader failed.");
            }
            manuscript.getBetaReaders().add(r);
        }

        Manuscript saved = manuscriptService.save(manuscript);
        if(saved == null) {
            System.out.println("Nije sacuvao Manuscript");
            throw new BpmnError("BETA_READER_SAVING_FAILED", "Saving manuscript failed.");
        }

        List<BetaReaderDTO> betaReaderDTOs = saved.getBetaReaders().stream().map( br -> betaReaderMapper.toDto(br)).collect(Collectors.toList());

        delegateExecution.setVariable("chosenBetaReaders", betaReaderDTOs);
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
