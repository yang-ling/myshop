package ling.yang.myshop.web;

import io.swagger.v3.oas.annotations.Operation;
import ling.yang.myshop.Vo.OrderDetailVo;
import ling.yang.myshop.Vo.OrderVo;
import ling.yang.myshop.entity.OrderDetail;
import ling.yang.myshop.entity.OrderHeader;
import ling.yang.myshop.entity.Product;
import ling.yang.myshop.entity.User;
import ling.yang.myshop.exceptions.MyShopException;
import ling.yang.myshop.service.IOrderDetailService;
import ling.yang.myshop.service.IOrderHeaderService;
import ling.yang.myshop.service.IProductService;
import ling.yang.myshop.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ling.yang.myshop.exceptions.MyShopExceptionAttributes.*;

@RestController
@RequestMapping(value = "/api/v1/order", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OrderController {
    private final IOrderHeaderService headerService;
    private final IOrderDetailService detailService;
    private final IProductService productService;
    private final IUserService userService;

    @GetMapping("/{userId}")
    public List<OrderVo> listUserOrders(@PathVariable int userId) {
        List<OrderHeader> headers = headerService.lambdaQuery()
                                                 .eq(OrderHeader::getUserId, userId)
                                                 .list();
        return headers.stream()
                      .map(h -> {
                          List<OrderDetail> details = detailService.lambdaQuery()
                                                                   .eq(OrderDetail::getOrderId, h.getId())
                                                                   .list();
                          List<OrderDetailVo> detailVos = details.stream()
                                                                 .map(d -> {
                                                                     Product product = productService.getById(
                                                                         d.getProductId());
                                                                     return OrderDetailVo.of(d, h, product);
                                                                 })
                                                                 .collect(Collectors.toList());
                          User user = userService.getById(h.getUserId());
                          return OrderVo.of(h, user, detailVos);
                      })
                      .collect(Collectors.toList());
    }

    @GetMapping("/{userId}/{orderNo}")
    public OrderVo oneOrder(@PathVariable int userId, @PathVariable String orderNo) {
        Optional<OrderHeader> orderHeader = headerService.lambdaQuery()
                                                         .eq(OrderHeader::getUserId, userId)
                                                         .eq(OrderHeader::getOrderNo, orderNo)
                                                         .oneOpt();
        if (orderHeader.isEmpty()) {
            throw new MyShopException(ORDER_ITEM_NOT_FOUND);
        }
        List<OrderDetail> orderDetails = detailService.lambdaQuery()
                                                      .eq(OrderDetail::getOrderId, orderHeader.get()
                                                                                              .getId())
                                                      .list();
        List<OrderDetailVo> detailVos = orderDetails.stream()
                                                    .map(d -> {
                                                        Product product = productService.getById(d.getProductId());
                                                        return OrderDetailVo.of(d, orderHeader.get(), product);
                                                    })
                                                    .collect(Collectors.toList());
        User user = userService.getById(orderHeader.get()
                                                   .getUserId());
        return OrderVo.of(orderHeader.get(), user, detailVos);
    }

    /**
     * @param userId
     * @param orderNo Call `/api/v1/token` to generate orderNo
     * @param cartId
     * @return
     */
    @Operation(summary = "Place an order", description = "Place an order")
    @PostMapping("/{userId}/{orderNo}")
    public OrderVo placeOrder(@PathVariable int userId, @PathVariable String orderNo,
        @RequestBody List<Integer> cartId) {
        headerService.validateSave(userId, orderNo, cartId);
        return this.oneOrder(userId, orderNo);
    }

    @Operation(summary = "Pay an order", description = "Pay an order. Its status will be PAID")
    @PutMapping("/{userId}/{orderNo}")
    public OrderVo payOrder(@PathVariable int userId, @PathVariable String orderNo) {
        headerService.validatePay(userId, orderNo);
        return this.oneOrder(userId, orderNo);
    }

    @Operation(summary = "Cancel an order", description = "Cancel an order")
    @DeleteMapping("/{userId}/{orderNo}")
    public OrderVo cancelOrder(@PathVariable int userId, @PathVariable String orderNo) {
        headerService.cancelOrder(userId, orderNo);
        return this.oneOrder(userId, orderNo);
    }
}
