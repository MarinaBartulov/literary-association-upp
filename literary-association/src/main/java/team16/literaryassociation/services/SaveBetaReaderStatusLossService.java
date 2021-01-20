package team16.literaryassociation.services;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Reader;
import team16.literaryassociation.services.interfaces.ReaderService;
import team16.literaryassociation.services.interfaces.UserService;

@Service
public class SaveBetaReaderStatusLossService implements JavaDelegate {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReaderService readerService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("Usao u GivePenaltyPointService");

        String username = this.identityService.getCurrentAuthentication().getUserId();
        Reader betaReader = (Reader) userService.findByUsername(username);
        if(betaReader == null)
        {
            System.out.println("Nije nasao Reader-a");
            return;
        }

        betaReader.setBetaReader(false);
        readerService.saveReader(betaReader);
    }
}
