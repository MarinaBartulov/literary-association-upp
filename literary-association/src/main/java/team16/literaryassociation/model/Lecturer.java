package team16.literaryassociation.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Lecturer")
public class Lecturer extends User {
}
