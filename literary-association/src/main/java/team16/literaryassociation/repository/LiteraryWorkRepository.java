package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team16.literaryassociation.model.LiteraryWork;

@Repository
public interface LiteraryWorkRepository extends JpaRepository<LiteraryWork, Long> {
}
