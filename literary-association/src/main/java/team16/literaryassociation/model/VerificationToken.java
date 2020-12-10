package team16.literaryassociation.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    private LocalDateTime timestamp;
    private String value;
    private double duration;
    @OneToOne
    private User user;
}
