package team16.literaryassociation.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Editor")
public class Editor extends User {
}
