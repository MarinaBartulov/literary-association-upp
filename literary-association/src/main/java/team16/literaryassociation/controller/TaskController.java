package team16.literaryassociation.controller;


import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import team16.literaryassociation.dto.FormFieldsDTO;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.dto.StartProcessDTO;
import team16.literaryassociation.model.User;
import team16.literaryassociation.security.auth.JwtBasedAuthentication;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/task")
public class TaskController {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;

    @GetMapping(value="/get-form-fields/{taskId}")
    public FormFieldsDTO getFormFields(@PathVariable("taskId") String taskId){
        System.out.println("Usao u get form fields");
        TaskFormData tfd = formService.getTaskFormData(taskId);
        System.out.println("Dobavio form fields");
        List<FormField> properties = tfd.getFormFields();
        return new FormFieldsDTO(properties);
    }

    @PostMapping(value = "/submit-form/{taskId}", produces = "application/json")
    public ResponseEntity<?> submitForm(@Valid @RequestBody List<FormSubmissionDTO> formData, @PathVariable("taskId") String taskId) {
        System.out.println("Usao u submit form");
        Map<String, Object> fieldsMap = listFieldsToMap(formData);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        runtimeService.setVariable(processInstanceId, "formData", formData);

        try{
            System.out.println("Dodje dovde1");
            formService.submitTaskForm(taskId, fieldsMap);
            System.out.println("Dodje dovde2");

        } catch(Exception e){
            return new ResponseEntity("Camunda validation failed.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        System.out.println("Doslo do kraja");
        return ResponseEntity.ok().body("Registration successful! Email confirmation required.");
    }

    @GetMapping(value = "/get-assignees-task-id/{processId}")
    public ResponseEntity<?> getAssigneesTaskId(@PathVariable String processId) {
        System.out.println("Usao u get assignees task id");
        System.out.println(processId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JwtBasedAuthentication jwtBasedAuthentication = (JwtBasedAuthentication) auth;
        User user = (User) jwtBasedAuthentication.getPrincipal();

        Task task = taskService.createTaskQuery().taskAssignee(user.getUsername()).active().processInstanceId(processId).singleResult();
        System.out.println(task.getName());
        System.out.println("Task Id: " + task.getId());
        return new ResponseEntity(task.getId(), HttpStatus.OK);
    }

    private Map<String, Object> listFieldsToMap(List<FormSubmissionDTO> formData) {
        Map<String, Object> retVal = new HashMap<>();
        for(FormSubmissionDTO dto: formData) {
            if(!(dto.getFieldValue() instanceof List)) { //zbog enuma
                retVal.put(dto.getFieldId(), dto.getFieldValue());
            }
        }
        return retVal;
    }
}
