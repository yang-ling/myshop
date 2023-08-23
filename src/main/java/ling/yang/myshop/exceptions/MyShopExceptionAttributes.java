package ling.yang.myshop.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MyShopExceptionAttributes {
    USER_NOT_FOUND("No User Found!", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND("No Product Found!", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND("No Cart Item Found!", HttpStatus.NOT_FOUND),
    ORDER_ITEM_NOT_FOUND("No Order Item Found!", HttpStatus.NOT_FOUND),
    NOT_ENOUGH_PRODUCT("Not Enough Products!", HttpStatus.UNPROCESSABLE_ENTITY),
    ORDER_ALREADY_PAID("This order has already been paid!", HttpStatus.UNPROCESSABLE_ENTITY),
    USER_CREDIT_CARD_EXPIRED("Your credit card is expired!", HttpStatus.UNPROCESSABLE_ENTITY);

    private final String errMsg;
    private final HttpStatus httpStatus;
}
