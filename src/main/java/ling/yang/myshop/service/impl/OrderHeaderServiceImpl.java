package ling.yang.myshop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ling.yang.myshop.entity.Cart;
import ling.yang.myshop.entity.OrderDetail;
import ling.yang.myshop.entity.OrderHeader;
import ling.yang.myshop.entity.Product;
import ling.yang.myshop.entity.User;
import ling.yang.myshop.entity.enums.OrderStatus;
import ling.yang.myshop.exceptions.MyShopException;
import ling.yang.myshop.mapper.OrderHeaderMapper;
import ling.yang.myshop.service.ICartService;
import ling.yang.myshop.service.IOrderDetailService;
import ling.yang.myshop.service.IOrderHeaderService;
import ling.yang.myshop.service.IProductService;
import ling.yang.myshop.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ling.yang.myshop.exceptions.MyShopExceptionAttributes.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Ling Yang
 * @since 2023-08-13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderHeaderServiceImpl extends ServiceImpl<OrderHeaderMapper, OrderHeader> implements IOrderHeaderService {
    private final ICartService cartService;
    private final IProductService productService;
    private final IUserService userService;
    private final IOrderDetailService detailService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String validateSave(int userId, String orderNo, List<Integer> cartIds) {
        Optional<OrderHeader> oneOpt = this.lambdaQuery()
                                           .eq(OrderHeader::getOrderNo, orderNo)
                                           .oneOpt();
        if (oneOpt.isPresent()) {
            log.warn("Order {} already exists.", orderNo);
            return orderNo;
        }
        List<Cart> carts = cartService.listByIds(cartIds);
        List<Integer> productIds = carts.stream()
                                        .map(Cart::getProductId)
                                        .collect(Collectors.toList());
        List<Product> products = productService.listByIds(productIds);
        HashMap<Integer, Product> productMap = products.stream()
                                                       .collect(Collectors.toMap(Product::getId, p -> p, (p1, p2) -> p1,
                                                           HashMap::new));
        for (Cart c : carts) {
            if (c.getAmount() > productMap.get(c.getProductId())
                                          .getAmount()) {
                throw new MyShopException(NOT_ENOUGH_PRODUCT);
            }
        }
        Optional<User> optById = userService.getOptById(userId);
        if (optById.isEmpty()) {
            throw new MyShopException(USER_NOT_FOUND);
        }
        User user = optById.get();
        if (user.getExpiryDate()
                .isBefore(LocalDate.now())) {
            throw new MyShopException(USER_CREDIT_CARD_EXPIRED);
        }
        OrderHeader header = OrderHeader.builder()
                                        .orderNo(orderNo)
                                        .userId(userId)
                                        .expiryDate(user.getExpiryDate())
                                        .cvcNo(user.getCvcNo())
                                        .status(OrderStatus.UNPAID)
                                        .build();

        this.save(header);
        List<OrderDetail> details = carts.stream()
                                         .map(c -> OrderDetail.builder()
                                                              .orderId(header.getId())
                                                              .cartId(c.getId())
                                                              .productId(c.getProductId())
                                                              .amount(c.getAmount())
                                                              .price(productMap.get(c.getProductId())
                                                                               .getPrice())
                                                              .build())
                                         .collect(Collectors.toList());
        detailService.saveBatch(details);
        return orderNo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean validatePay(int userId, String orderNo) {
        User user = userService.getById(userId);
        if (user.getExpiryDate()
                .isBefore(LocalDate.now())) {
            throw new MyShopException(USER_CREDIT_CARD_EXPIRED);
        }
        OrderHeader header = this.lambdaQuery()
                                 .eq(OrderHeader::getUserId, userId)
                                 .eq(OrderHeader::getOrderNo, orderNo)
                                 .one();
        List<OrderDetail> list = detailService.lambdaQuery()
                                              .eq(OrderDetail::getOrderId, header.getId())
                                              .list();
        List<Integer> productIds = list.stream()
                                       .map(OrderDetail::getProductId)
                                       .collect(Collectors.toList());
        List<Integer> cartIds = list.stream()
                                    .map(OrderDetail::getCartId)
                                    .collect(Collectors.toList());
        List<Product> products = productService.listByIds(productIds);
        HashMap<Integer, Product> productMap = products.stream()
                                                       .collect(Collectors.toMap(Product::getId, p -> p, (p1, p2) -> p1,
                                                           HashMap::new));
        List<Product> newProducts = new ArrayList<>();
        for (OrderDetail d : list) {
            Product product = productMap.get(d.getProductId());
            if (d.getAmount() > product.getAmount()) {
                throw new MyShopException(NOT_ENOUGH_PRODUCT);
            }
            newProducts.add(product.withAmount(product.getAmount() - d.getAmount()));
        }
        productService.updateBatchById(newProducts);
        cartService.removeBatchByIds(cartIds);
        header = header.withStatus(OrderStatus.PAID);
        return this.updateById(header);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(int userId, String orderNo) {
        Optional<OrderHeader> header = this.lambdaQuery()
                                           .eq(OrderHeader::getUserId, userId)
                                           .eq(OrderHeader::getOrderNo, orderNo)
                                           .oneOpt();
        if (header.isEmpty()
            || header.get()
                     .getStatus() == OrderStatus.PAID) {
            throw new MyShopException(ORDER_ALREADY_PAID);
        }
        List<OrderDetail> list = detailService.lambdaQuery()
                                              .eq(OrderDetail::getOrderId, header.get()
                                                                                 .getId())
                                              .list();
        detailService.removeBatchByIds(list);
        this.removeById(header.get());
    }
}
