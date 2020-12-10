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


    @GetMapping(value = "/start-process-register")
    public FormFieldsDTO registerReader() {

        ProcessInstance pi = runtimeService.startProcessInstanceByKey("Process_Reader_Reg");
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0); // Task_All_Genres_Retrieval
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
            retVal.put(dto.getFieldId(), dto.getFieldValue());
        }
        return retVal;
    }

    @PostMapping(value = "/register/{taskId}", produces = "application/json")
    public ResponseEntity<?> registerUser(@Valid @RequestBody List<FormSubmissionDTO> formData, @PathVariable("taskId") String taskId) {
        Map<String, Object> fieldsMap = listFieldsToMap(formData);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        for(FormSubmissionDTO item: formData){
            String fieldId = item.getFieldId();
            if(!fieldId.equals("genres") &&  !fieldId.equals("betaGenres")){
                if(item.getFieldValue() == null || item.getFieldValue().equals("")){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } else if(fieldId.equals("genres") && item.getGenres().size() == 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else if(fieldId.equals("betaGenres") && item.getBetaGenres().size() == 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        try{
            formService.submitTaskForm(taskId, fieldsMap);
        } catch(FormFieldValidationException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        System.out.println("MIBIBIKISIŠ");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
