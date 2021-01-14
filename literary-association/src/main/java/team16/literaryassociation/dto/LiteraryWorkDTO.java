package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team16.literaryassociation.model.LiteraryWork;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LiteraryWorkDTO {

    protected Long id;

    private String title;

    private String path;

    private String downloadUrl;

    public LiteraryWorkDTO(LiteraryWork literaryWork)
    {
        this.id = literaryWork.getId();
        this.title = literaryWork.getTitle();
        this.path = literaryWork.getPath();
        this.downloadUrl = literaryWork.getDownloadUrl();
    }
}
