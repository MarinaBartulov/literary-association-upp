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
        }
        return retVal;
    }

    @PostMapping(value = "/register/{taskId}", produces = "application/json")
    public ResponseEntity<?> registerUser(@Valid @RequestBody List<FormSubmissionDTO> formData, @PathVariable("taskId") String taskId) {
        Map<String, Object> fieldsMap = listFieldsToMap(formData);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        System.out.println("Dodje dovde1");

        if(!fieldsMap.containsKey("firstName") || !fieldsMap.containsKey("lastName") || !fieldsMap.containsKey("city")
        ||!fieldsMap.containsKey("country") || !fieldsMap.containsKey("username") || !fieldsMap.containsKey("password") ||
                !fieldsMap.containsKey("confirmPassword") || !fieldsMap.containsKey("email") || !fieldsMap.containsKey("betaReader")
        ){
            return ResponseEntity.badRequest().body("Validation failed. All fields are required.");
        }
        System.out.println("Dodje dovde1");
        System.out.println(fieldsMap.get("firstName"));
        System.out.println(fieldsMap.get("lastName"));
        System.out.println(fieldsMap.get("city"));
        System.out.println(fieldsMap.get("country"));
        System.out.println(fieldsMap.get("email"));
        System.out.println(fieldsMap.get("username"));
        System.out.println(fieldsMap.get("password"));
        System.out.println(fieldsMap.get("confirmPassword"));

        boolean v1 = fieldsMap.get("firstName") != null && (!fieldsMap.get("firstName").equals(""));
        boolean v2 = fieldsMap.get("lastName") != null && (!fieldsMap.get("lastName").equals(""));
        boolean v3 = fieldsMap.get("city") != null && (!fieldsMap.get("city").equals(""));
        boolean v4 = fieldsMap.get("country") != null && (!fieldsMap.get("country").equals(""));
        boolean v5 = fieldsMap.get("email") != null && (!fieldsMap.get("email").equals(""));
        boolean v6 = fieldsMap.get("username") != null && (!fieldsMap.get("username").equals(""));
        boolean v7 = fieldsMap.get("password") != null && (!fieldsMap.get("password").equals("")) && ((String)fieldsMap.get("password")).length() >=8;
        boolean v8 = fieldsMap.get("confirmPassword") != null && (!fieldsMap.get("confirmPassword").equals("")) && ((String)fieldsMap.get("confirmPassword")).length() >=8;

        System.out.println("Dodje dovde1");


        if(!(v1 && v2 && v3 && v4 && v5 && v6 && v7 && v7 && v8)){
            return ResponseEntity.badRequest().body("Validation failed. All fields are required.");
        }
        runtimeService.setVariable(processInstanceId, "registrationData", formData);

        try{
            System.out.println("Dodje dovde1");
            formService.submitTaskForm(taskId, fieldsMap);
            System.out.println("Dodje dovde2");

        } catch(FormFieldValidationException e){

            return ResponseEntity.badRequest().body("Validation failed");
        }

        System.out.println("Doslo do kraja");
        return ResponseEntity.ok().build();
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
