package team16.literaryassociation.services.interfaces;

import team16.literaryassociation.dto.OrderRequestDTO;
import team16.literaryassociation.dto.OrderResponseDTO;
import team16.literaryassociation.model.Merchant;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO, Merchant merchant);
}
