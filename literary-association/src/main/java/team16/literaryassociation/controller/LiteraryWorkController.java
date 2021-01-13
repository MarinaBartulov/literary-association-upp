package team16.literaryassociation.controller;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team16.literaryassociation.services.LiteraryWorkService;

@RestController
@RequestMapping(value = "/api/literaryWork")
public class LiteraryWorkController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private LiteraryWorkService literaryWorkService;

    @PostMapping(value = "/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("processId") String processId,
                                                    @RequestParam("counter") String counter) {
        System.out.println("**********************************************************");
        System.out.println("Usao u upload fajla");
        System.out.println("ProcessID" + processId);

        String username = (String) runtimeService.getVariable(processId,"currentUser");
        System.out.println("Username: " + username);

        if(username != null)
        {
            String fileDownloadUrl = "";
            try {
                System.out.println("Upload " + counter + ". fajla");
                fileDownloadUrl = literaryWorkService.store(file, processId, username);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return  new ResponseEntity<>("Failed to upload file", HttpStatus.EXPECTATION_FAILED);
            }

            runtimeService.setVariable(processId, "url"+counter, fileDownloadUrl);

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String path = "literary-work-dir/" + username + "/" + processId + "/" + fileName;
            runtimeService.setVariable(processId, "pdfFileLocation"+counter, path);

            return new ResponseEntity("File uploaded", HttpStatus.OK);
        }
        return new ResponseEntity("User not found", HttpStatus.BAD_REQUEST);

    }

}
