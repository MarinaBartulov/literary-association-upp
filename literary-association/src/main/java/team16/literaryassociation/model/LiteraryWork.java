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
    private String path;

    @Column
    private String downloadUrl;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MembershipApplication membershipApplication;

    public LiteraryWork(String title, String path, String downloadUrl)
    {
        this.title = title;
        this.path = path;
        this.downloadUrl = downloadUrl;
    }
}
