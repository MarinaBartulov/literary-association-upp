package team16.literaryassociation.controller;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.dto.StartProcessDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/reader")
public class ReaderController {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;


    @GetMapping(value = "/start-process-register")
    public ResponseEntity<?> startReaderRegistration() {

        ProcessInstance pi = runtimeService.startProcessInstanceByKey("Process_Reader_Reg");
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
        task.setAssignee("user");
        taskService.saveTask(task);
        StartProcessDTO sp = new StartProcessDTO(pi.getId(), task.getId());
        return new ResponseEntity(sp, HttpStatus.OK);
    }

    @PutMapping(value = "/activateAccount/{processId}/{token}")
    public ResponseEntity<?> activateAccount(@PathVariable String processId, @PathVariable String token){

        System.out.println(processId);
        System.out.println(token);

        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processId).list();
        Task activationTask = null;
        try {
            activationTask = tasks.get(0);
        }catch(Exception e){//ako se proces vec zavrsio, sto znaci da je isteklo vreme za aktivaciju
            return ResponseEntity.badRequest().body("Account activation failed. Token expired.");
        }
        runtimeService.setVariable(processId, "token", token);
        HashMap<String, Object> map = new HashMap<>();
        formService.submitTaskForm(activationTask.getId(), map);

        return ResponseEntity.ok().body("Account activation successful");
    }

//    @GetMapping(value="/get-form-fields/{taskId}")
//    public FormFieldsDTO getFormFields(@PathVariable("taskId") String taskId){
//
//        TaskFormData tfd = formService.getTaskFormData(taskId);
//        List<FormField> properties = tfd.getFormFields();
//        return new FormFieldsDTO(properties);
//    }
//
//    @PostMapping(value = "/register/{taskId}", produces = "application/json")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody List<FormSubmissionDTO> formData, @PathVariable("taskId") String taskId) {
//        Map<String, Object> fieldsMap = listFieldsToMap(formData);
//        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
//        String processInstanceId = task.getProcessInstanceId();
//        System.out.println("Dodje dovde1");
//
//        if(!fieldsMap.containsKey("firstName") || !fieldsMap.containsKey("lastName") || !fieldsMap.containsKey("city")
//        ||!fieldsMap.containsKey("country") || !fieldsMap.containsKey("username") || !fieldsMap.containsKey("password") ||
//                !fieldsMap.containsKey("confirmPassword") || !fieldsMap.containsKey("email") || !fieldsMap.containsKey("betaReader")
//        || !fieldsMap.containsKey("genres")
//        ){
//            return ResponseEntity.badRequest().body("Validation failed. All fields are required.");
//        }
//        System.out.println("Dodje dovde1");
//        System.out.println(fieldsMap.get("firstName"));
//        System.out.println(fieldsMap.get("lastName"));
//        System.out.println(fieldsMap.get("city"));
//        System.out.println(fieldsMap.get("country"));
//        System.out.println(fieldsMap.get("email"));
//        System.out.println(fieldsMap.get("username"));
//        System.out.println(fieldsMap.get("password"));
//        System.out.println(fieldsMap.get("confirmPassword"));
//        System.out.println(fieldsMap.get("betaReader"));
//
//
//        boolean v1 = fieldsMap.get("firstName") != null && (!fieldsMap.get("firstName").equals(""));
//        boolean v2 = fieldsMap.get("lastName") != null && (!fieldsMap.get("lastName").equals(""));
//        boolean v3 = fieldsMap.get("city") != null && (!fieldsMap.get("city").equals(""));
//        boolean v4 = fieldsMap.get("country") != null && (!fieldsMap.get("country").equals(""));
//        boolean v5 = fieldsMap.get("email") != null && (!fieldsMap.get("email").equals(""));
//        boolean v6 = fieldsMap.get("username") != null && (!fieldsMap.get("username").equals(""));
//        boolean v7 = fieldsMap.get("password") != null && (!fieldsMap.get("password").equals("")) && ((String)fieldsMap.get("password")).length() >=8;
//        boolean v8 = fieldsMap.get("confirmPassword") != null && (!fieldsMap.get("confirmPassword").equals("")) && ((String)fieldsMap.get("confirmPassword")).length() >=8;
//
//
//        System.out.println("Dodje dovde1");
//
//
//        if(!(v1 && v2 && v3 && v4 && v5 && v6 && v7 && v7 && v8)){
//            return ResponseEntity.badRequest().body("Validation failed. All fields are required.");
//        }
//        runtimeService.setVariable(processInstanceId, "registrationData", formData);
//
//        //obrisem da ne bi puklo u camundi jer prihvata samo jednu vrednost koja je id
//        fieldsMap.remove("genres");
//        fieldsMap.remove("betaGenres");
//
//        try{
//            System.out.println("Dodje dovde1");
//            formService.submitTaskForm(taskId, fieldsMap);
//            System.out.println("Dodje dovde2");
//
//        } catch(Exception e){
//            return new ResponseEntity("Camunda validation failed.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        System.out.println("Doslo do kraja");
//        return ResponseEntity.ok().body("Registration successful! Email confirmation required.");
//    }



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
