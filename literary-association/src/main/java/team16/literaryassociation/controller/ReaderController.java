package team16.literaryassociation.controller;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidationException;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team16.literaryassociation.dto.FormFieldsDTO;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.model.User;
import team16.literaryassociation.model.VerificationToken;
import team16.literaryassociation.services.UserService;
import team16.literaryassociation.services.VerificationTokenService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/reader")
public class ReaderController {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;

    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private UserService userService;


    @GetMapping(value = "/start-process-register")
    public FormFieldsDTO startReaderRegistration() {

        ProcessInstance pi = runtimeService.startProcessInstanceByKey("Process_Reader_Reg");
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
        task.setAssignee("user");
        taskService.saveTask(task);

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        for(FormField fp : properties) {
            System.out.println(fp.getId() + fp.getType());
        }

        return new FormFieldsDTO(task.getId(), properties, pi.getId());
    }

    private Map<String, Object> listFieldsToMap(List<FormSubmissionDTO> formData) {
        Map<String, Object> retVal = new HashMap<>();
        for(FormSubmissionDTO dto: formData) {
            if(dto.getFieldId() != null) {
                retVal.put(dto.getFieldId(), dto.getFieldValue());
            }
            else {
                if (dto.getFieldId().equals("genres")) {
                    retVal.put(dto.getFieldId(), dto.getGenres());
                }
                if (dto.getFieldId().equals("betaGenres")) {
                    retVal.put(dto.getFieldId(), dto.getBetaGenres());
                }

            }

        }
        return retVal;
    }

    @PostMapping(value = "/register/{taskId}", produces = "application/json")
    public ResponseEntity<?> registerUser(@Valid @RequestBody List<FormSubmissionDTO> formData, @PathVariable("taskId") String taskId) {
        Map<String, Object> fieldsMap = listFieldsToMap(formData);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        for(FormSubmissionDTO item: formData){
            if(!item.getFieldId().equals("genres") &&  !item.getFieldId().equals("betaGenres") && !item.getFieldId().equals("betaReader")){
                if(item.getFieldValue() == null || item.getFieldValue().equals("")){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }else if(item.getFieldId().equals("betaReader")){
                if(!item.getFieldValue().equals("true") && !item.getFieldValue().equals("false")){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }

            //ovaj deo je za zanrove
            /*else if(item.getFieldId().equals("genres") && item.getGenres().size() == 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else if(item.getFieldId().equals("betaGenres") && item.getBetaGenres().size() < 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }*/
        }

        try{
            runtimeService.setVariable(processInstanceId, "registrationData", formData);
            System.out.println("Dodje dovde1");
            formService.submitTaskForm(taskId, fieldsMap);
            System.out.println("Dodje dovde2");

        } catch(FormFieldValidationException e){

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        System.out.println("MIBIBIKISIŠ");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/activateAccount/{processId}/{token}")
    public ResponseEntity<?> activateAccount(@PathVariable String processId, @PathVariable String token){

        System.out.println(processId);
        System.out.println(token);

        VerificationToken vt = this.verificationTokenService.findToken(token);
        if(vt == null){
            System.out.println("Token is null");
            return ResponseEntity.badRequest().body("Account activation failed. Token expired.");
        }
        if(!vt.isValid()){
            return ResponseEntity.badRequest().body("Account activation failed. Token expired.");
        }

        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processId).list();
        Task activationTask = tasks.get(0);

        User user = vt.getUser();
        user.setVerified(true);
        this.userService.saveUser(user);

        HashMap<String, Object> map = new HashMap<>();
        formService.submitTaskForm(activationTask.getId(), map);

          return ResponseEntity.ok().body("Account activation successful");
    }


}
