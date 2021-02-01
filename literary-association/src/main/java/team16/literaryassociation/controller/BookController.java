package team16.literaryassociation.controller;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team16.literaryassociation.dto.StartProcessDTO;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @GetMapping(value = "/start-process-plagiarism-detection")
    public ResponseEntity startPlagiarismDetectionProcess(){

        ProcessInstance pi = runtimeService.startProcessInstanceByKey("Process_Plagiarism_Detection");
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
        taskService.saveTask(task);
        StartProcessDTO sp = new StartProcessDTO(pi.getId(), task.getId());
        return new ResponseEntity(sp, HttpStatus.OK);
    }
}
