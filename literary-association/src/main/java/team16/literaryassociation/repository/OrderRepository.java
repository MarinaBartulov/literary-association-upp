package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team16.literaryassociation.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
