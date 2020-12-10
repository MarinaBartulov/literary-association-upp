package team16.literaryassociation.model;

import javax.persistence.*;

@Entity
@DiscriminatorValue("Writer")
public class Writer extends User {

    @OneToOne(mappedBy = "user",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private VerificationToken token;
}
