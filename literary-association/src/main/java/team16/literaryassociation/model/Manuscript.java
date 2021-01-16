package team16.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Manuscript {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pdf; //putanja do pdf fajla gde je sacuvan, ona ce da se menja sa objavljivanjem novih verzija
    private boolean original;
    private boolean accepted;
    private String reasonForRejection;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_request_id")
    private BookRequest bookRequest;

}
