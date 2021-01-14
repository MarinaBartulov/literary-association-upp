package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.MembershipApplication;
import team16.literaryassociation.model.Writer;
import team16.literaryassociation.services.interfaces.MembershipApplicationService;
import team16.literaryassociation.services.interfaces.UserService;

@Service
public class CreateMembershipApplicationService implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Autowired
    private MembershipApplicationService membershipApplicationService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        Long userId = (Long) execution.getVariable("user_id");
        Writer writer = (Writer)userService.getOne(userId);

        String processId = execution.getProcessInstanceId();
        MembershipApplication membershipApplication = membershipApplicationService.save(new MembershipApplication(writer, processId));
        execution.setVariable("membership_application_id", membershipApplication.getId());
    }
}
