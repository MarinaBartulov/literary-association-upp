package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team16.literaryassociation.model.Reader;

public interface ReaderRepository extends JpaRepository<Reader, Long> {
}
