package team16.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiteraryWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    private String title;

    @Column
    private String pdf;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private MembershipApplication membershipApplication;

    public LiteraryWork(String title, String pdf)
    {
        this.title = title;
        this.pdf = pdf;
    }
}
