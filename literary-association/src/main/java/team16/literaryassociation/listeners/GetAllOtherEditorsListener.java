package team16.literaryassociation.listeners;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;

@Service
public class GetAllOtherEditorsListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("USAO U GET all other Editors");
    }
}
