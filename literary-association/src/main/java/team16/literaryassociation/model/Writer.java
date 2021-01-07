package team16.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Writer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Writer extends User {

    @OneToOne(mappedBy = "user",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private VerificationToken token;

    @OneToOne(mappedBy = "writer", cascade = CascadeType.ALL)
    private MembershipApplication membershipApplication;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "writer_genre",
            joinColumns = @JoinColumn(name = "writer_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private Set<Genre> genres = new HashSet<>();
}
