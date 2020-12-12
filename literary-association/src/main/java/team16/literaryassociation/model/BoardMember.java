package team16.literaryassociation.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("BoardMember")
public class BoardMember extends User {
}
