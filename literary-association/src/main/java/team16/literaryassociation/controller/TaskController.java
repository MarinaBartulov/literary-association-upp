package team16.literaryassociation.controller;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.literaryassociation.dto.FormFieldsDTO;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.dto.TaskDTO;

import javax.validation.Valid;
import java.util.ArrayList;
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
    @Autowired
    private IdentityService identityService;

    @GetMapping(value="/get-form-fields/{taskId}")
    public FormFieldsDTO getFormFields(@PathVariable("taskId") String taskId){

        TaskFormData tfd = formService.getTaskFormData(taskId);
        List<FormField> properties = tfd.getFormFields();
        return new FormFieldsDTO(properties);
    }

    @PostMapping(value = "/submit-form/{taskId}", produces = "application/json")
    public ResponseEntity<?> submitForm(@Valid @RequestBody List<FormSubmissionDTO> formData, @PathVariable("taskId") String taskId) {

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

        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

        if(pi != null) {
            System.out.println("Process instance nije null");
            Object globalErrorObj = runtimeService.getVariable(processInstanceId, "globalError");
            if(globalErrorObj != null){
                System.out.println("Global error object nije null");
                boolean globalError = (boolean) globalErrorObj;
                System.out.println("Global error je " + globalError);
                if(globalError){
                    runtimeService.setVariable(processInstanceId, "globalError", false);
                    return ResponseEntity.badRequest().body("Invalid data.");
                }
            }
        }

        System.out.println("Doslo do kraja");
        return ResponseEntity.ok().body("Form successfully submitted.");
    }

    @GetMapping
    public ResponseEntity getAllMyTasks(){

        String username = this.identityService.getCurrentAuthentication().getUserId();
        System.out.println(username);
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(username).active().list();
        List<Group> groups = identityService.createGroupQuery().groupMember(username).list();
        for(Group g : groups){
            List<Task> tasks2 = taskService.createTaskQuery().taskCandidateGroup(g.getId()).active().list();
            for(Task t : tasks2){
                tasks.add(t);
            }
        }

        List<TaskDTO> tasksDTOs = new ArrayList<>();
        for(Task t : tasks){
            TaskFormData tfd = formService.getTaskFormData(t.getId());
            tasksDTOs.add(new TaskDTO(t.getId(),t.getProcessInstanceId(),t.getName(),t.getAssignee(),tfd.getFormFields()));
        }

        return new ResponseEntity(tasksDTOs, HttpStatus.OK);

    }

    @GetMapping(value = "/{taskId}")
    public ResponseEntity getTaskData(@PathVariable("taskId") String taskId){

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        TaskFormData tfd = formService.getTaskFormData(taskId);

        TaskDTO taskDTO = new TaskDTO(task.getId(),task.getProcessInstanceId(),task.getName(),task.getAssignee(),tfd.getFormFields());

        return new ResponseEntity(taskDTO, HttpStatus.OK);

    }

    @GetMapping(value = "/process/{processId}")
    public ResponseEntity getActiveTaskForProcess(@PathVariable("processId") String processId){

        String username = identityService.getCurrentAuthentication().getUserId();
        Task task = taskService.createTaskQuery().taskAssignee(username).active()
                .processInstanceId(processId).singleResult();

        if (task != null) {
            TaskFormData tfd = formService.getTaskFormData(task.getId());
            TaskDTO taskDTO = new TaskDTO(task.getId(),task.getProcessInstanceId(),task.getName(),task.getAssignee(),tfd.getFormFields());
            return new ResponseEntity(taskDTO, HttpStatus.OK);
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping(value = "/taskId/{processId}")
    public ResponseEntity getActiveTaskIdForProcess(@PathVariable("processId") String processId){

        String username = identityService.getCurrentAuthentication().getUserId();
        Task task = taskService.createTaskQuery().taskAssignee(username).active()
                .processInstanceId(processId).singleResult();

        if (task != null) {
            return new ResponseEntity(task.getId(), HttpStatus.OK);
        } else {
            return ResponseEntity.ok().build();
        }
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
