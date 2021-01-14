package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team16.literaryassociation.model.MembershipApplication;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MembershipApplicationDTO {

    protected Long id;

    private Integer moreMaterialRequested;

    private String processId;

    //zbog fronta

    private String writerFirstName;

    private String writerLastName;

    //private LocalDateTime deadline;

    //private Double price;

    //private boolean paid;

    //private LocalDateTime paymentDate;

    public MembershipApplicationDTO(MembershipApplication membershipApplication)
    {
        this.id = membershipApplication.getId();
        this.processId = membershipApplication.getProcessId();
        this.moreMaterialRequested = membershipApplication.getMoreMaterialRequested();
        this.writerFirstName = membershipApplication.getWriter().getFirstName();
        this.writerLastName = membershipApplication.getWriter().getLastName();
    }
}
