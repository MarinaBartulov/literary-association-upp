package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team16.literaryassociation.dto.*;
import team16.literaryassociation.enums.OrderStatus;
import team16.literaryassociation.exception.BadRequestException;
import team16.literaryassociation.exception.NotFoundException;
import team16.literaryassociation.model.*;
import team16.literaryassociation.repository.OrderRepository;
import team16.literaryassociation.services.interfaces.BookService;
import team16.literaryassociation.services.interfaces.OrderService;
import team16.literaryassociation.services.interfaces.ReaderService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BookService bookService;
    @Autowired
    private ReaderService readerService;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO dto, Merchant merchant) {

        Authentication currentReader = SecurityContextHolder.getContext().getAuthentication();
        String username = currentReader.getName();
        Reader reader = this.readerService.findByUsername(username);

        for(OrderBookDTO ob: dto.getBooks()){
            Book book = this.bookService.findById(ob.getBookId());
            if(book == null){
                throw new NotFoundException("Book with id " + ob.getBookId() + " doesn't exits.");
            }
        }

        Order order = new Order();
        order.setTotal(dto.getTotal());
        order.setDateCreated(LocalDateTime.now());
        order.setReader(reader);
        order.setOrderStatus(OrderStatus.PENDING);

        try {
            this.orderRepository.save(order);
        }catch(Exception e){
            throw new BadRequestException("Error occurred while saving a new order.");
        }

        Set<OrderBook> books = new HashSet<>();
        for(OrderBookDTO ob: dto.getBooks()){
            Book book = this.bookService.findById(ob.getBookId());
            OrderBook orderBook = new OrderBook();
            orderBook.setBook(book);
            orderBook.setAmount(ob.getAmount());
            orderBook.setOrder(order);
            books.add(orderBook);
        }

        order.setBooks(books);

        try {
            this.orderRepository.save(order);
        }catch(Exception e){
            throw new BadRequestException("Error occurred while saving a new order.");
        }

        ResponseEntity<OrderResponseDTO> response;
        try {
            response = restTemplate.postForEntity("https://localhost:8083/psp-service/api/order",
                    new OrderDTO(order.getId(), merchant.getMerchantEmail(),
                            "USD", dto.getTotal(), merchant.getMerchantSuccessUrl(), merchant.getMerchantFailedUrl(),
                            merchant.getMerchantErrorUrl()), OrderResponseDTO.class);
            System.out.println(response.getBody().getOrderId());
        }catch(Exception e){
            e.printStackTrace();
            throw new BadRequestException("Error occurred while sending order on payment concentrator.");
        }
        return response.getBody();
    }

    @Override
    public List<OrderHistoryDTO> getOrders() {

        Authentication currentReader = SecurityContextHolder.getContext().getAuthentication();
        String username = currentReader.getName();
        List<Order> orders = this.orderRepository.getOrders(username);
        List<OrderHistoryDTO> ordersDTO = new ArrayList<>();
        for(Order o : orders){
           OrderHistoryDTO orderDTO = new OrderHistoryDTO();
           orderDTO.setId(o.getId());
           orderDTO.setTotal(o.getTotal());
           orderDTO.setOrderStatus(o.getOrderStatus().toString());
           orderDTO.setDateCreated(o.getDateCreated());
           orderDTO.setBooks(o.getBooks().stream().map(or -> new OrderBookHistoryDTO(or)).collect(Collectors.toList()));
           orderDTO.setMerchant(o.getBooks().stream().collect(Collectors.toList()).get(0).getBook().getPublisher().getMerchantName());
           ordersDTO.add(orderDTO);
        }
        return ordersDTO;
    }
}
