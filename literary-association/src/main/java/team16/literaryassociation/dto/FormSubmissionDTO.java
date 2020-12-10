package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormSubmissionDTO implements Serializable {

    @NotNull(message = "Form field id cannot be null.")
    private String fieldId;
    private String fieldValue;
    private List<String> genres = new ArrayList<String>();
    private List<String> betaGenres = new ArrayList<String>();

}
