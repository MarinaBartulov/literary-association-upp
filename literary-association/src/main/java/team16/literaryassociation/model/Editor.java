package team16.literaryassociation.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@DiscriminatorValue("Editor")
public class Editor extends User {

    @OneToMany(mappedBy = "editor")
    private Set<BookRequest> bookRequests;
}
