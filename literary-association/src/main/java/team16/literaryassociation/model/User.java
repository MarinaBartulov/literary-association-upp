package team16.literaryassociation.model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType = DiscriminatorType.STRING)
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(nullable=false)
    private String firstName;
    @Column(nullable=false)
    private String lastName;
    @Column(nullable=false)
    private String city;
    @Column(nullable=false)
    private String country;
    @Column(nullable=false, unique = true)
    private String email;
    @Column(nullable=false, unique = true)
    private String username;
    @Column(nullable=false)
    private String password;
    @Column(nullable=false)
    private boolean verified;

}