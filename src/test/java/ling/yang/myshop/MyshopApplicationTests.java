package ling.yang.myshop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ling.yang.myshop.Vo.CartVo;
import ling.yang.myshop.Vo.ProductVo;
import ling.yang.myshop.Vo.UserVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static ling.yang.myshop.exceptions.MyShopExceptionAttributes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class MyshopApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataSource dataSource;

    private List<UserVo> listUsers(String userAPI, ObjectMapper mapper) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(userAPI))
                                     .andExpect(status().isOk())
                                     .andReturn();
        JavaType listType = mapper.getTypeFactory()
                                  .constructCollectionType(List.class, UserVo.class);
        List<UserVo> list = mapper.readValue(mvcResult.getResponse()
                                                      .getContentAsString(), listType);
        return list;
    }

    private UserVo register(String userAPI, ObjectMapper mapper, UserVo user) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(userAPI)
                                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                    .content(mapper.writeValueAsString(user)))
                                     .andExpect(status().isOk())
                                     .andReturn();
        return mapper.readValue(mvcResult.getResponse()
                                         .getContentAsString(), UserVo.class);
    }

    private boolean updateUser(String userAPI, ObjectMapper mapper, UserVo user) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(userAPI + "/" + user.getId())
                                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                    .content(mapper.writeValueAsString(user)))
                                     .andExpect(status().isOk())
                                     .andReturn();
        return mapper.readValue(mvcResult.getResponse()
                                         .getContentAsString(), Boolean.class);
    }

    private boolean removeUser(String userAPI, ObjectMapper mapper, int userId) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(userAPI + "/" + userId))
                                     .andExpect(status().isOk())
                                     .andReturn();
        return mapper.readValue(mvcResult.getResponse()
                                         .getContentAsString(), Boolean.class);
    }

    @Test
    public void testUserAPI() throws Exception {
        // List
        String userAPI = "/api/v1/user";
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        List<UserVo> list = listUsers(userAPI, mapper);
        assertEquals(0, list.size());

        // Get, 404
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(userAPI + "/1"))
                                     .andExpect(status().isNotFound())
                                     .andReturn();
        ErrorResponse err = mapper.readValue(mvcResult.getResponse()
                                                      .getContentAsString(), ErrorResponse.class);
        assertEquals(USER_NOT_FOUND.getErrMsg(), err.getMessage());

        //Register
        UserVo user = UserVo.builder()
                            .name("test one")
                            .cvcNo("123")
                            .cardNo("12345687")
                            .expiryDate(LocalDate.now()
                                                 .plusDays(10))
                            .build();
        UserVo register = register(userAPI, mapper, user);

        // Update
        UserVo testOneUpdated = register.withName("test one updated");
        assertTrue(updateUser(userAPI, mapper, testOneUpdated));
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(userAPI + "/" + testOneUpdated.getId()))
                           .andExpect(status().isOk())
                           .andReturn();
        assertEquals(testOneUpdated, mapper.readValue(mvcResult.getResponse()
                                                               .getContentAsString(), UserVo.class));

        // Delete
        assertTrue(removeUser(userAPI, mapper, register.getId()));
    }

    private List<ProductVo> listProduct(String productAPI, ObjectMapper mapper) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(productAPI))
                                     .andExpect(status().isOk())
                                     .andReturn();
        JavaType listType = mapper.getTypeFactory()
                                  .constructCollectionType(List.class, ProductVo.class);
        List<ProductVo> list = mapper.readValue(mvcResult.getResponse()
                                                         .getContentAsString(), listType);
        return list;
    }

    private ProductVo addProduct(String productAPI, ObjectMapper mapper, ProductVo productVo) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(productAPI)
                                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                    .content(mapper.writeValueAsString(productVo)))
                                     .andExpect(status().isOk())
                                     .andReturn();
        return mapper.readValue(mvcResult.getResponse()
                                         .getContentAsString(), ProductVo.class);
    }

    private ProductVo updateProduct(String productAPI, ObjectMapper mapper, ProductVo testOneUpdated) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(productAPI + "/" + testOneUpdated.getId())
                                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                    .content(mapper.writeValueAsString(testOneUpdated)))
                                     .andExpect(status().isOk())
                                     .andReturn();
        return mapper.readValue(mvcResult.getResponse()
                                         .getContentAsString(), ProductVo.class);
    }

    private boolean removeProduct(String productAPI, ObjectMapper mapper, int id) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(productAPI + "/" + id))
                                     .andExpect(status().isOk())
                                     .andReturn();
        return mapper.readValue(mvcResult.getResponse()
                                         .getContentAsString(), Boolean.class);
    }

    @Test
    public void testProductAPI() throws Exception {
        String productAPI = "/api/v1/product";
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        List<ProductVo> productVos = listProduct(productAPI, mapper);
        assertEquals(0, productVos.size());

        // Get, 404
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(productAPI + "/1"))
                                     .andExpect(status().isNotFound())
                                     .andReturn();
        ErrorResponse err = mapper.readValue(mvcResult.getResponse()
                                                      .getContentAsString(), ErrorResponse.class);
        assertEquals(PRODUCT_NOT_FOUND.getErrMsg(), err.getMessage());

        // Add
        ProductVo productVo = ProductVo.builder()
                                       .name("test product one")
                                       .price(new BigDecimal("10.25"))
                                       .amount(23)
                                       .build();
        productVo = addProduct(productAPI, mapper, productVo);

        // Update
        ProductVo testOneUpdated = productVo.withName("test one product updated");
        assertEquals(testOneUpdated, updateProduct(productAPI, mapper, testOneUpdated));
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(productAPI + "/" + testOneUpdated.getId()))
                           .andExpect(status().isOk())
                           .andReturn();
        assertEquals(testOneUpdated, mapper.readValue(mvcResult.getResponse()
                                                               .getContentAsString(), ProductVo.class));

        // Delete
        assertTrue(removeProduct(productAPI, mapper, testOneUpdated.getId()));
    }

    private List<CartVo> listCartItems(String cartAPI, ObjectMapper mapper, int userId) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(cartAPI + "/" + userId))
                                     .andExpect(status().isOk())
                                     .andReturn();
        JavaType listType = mapper.getTypeFactory()
                                  .constructCollectionType(List.class, CartVo.class);
        List<CartVo> list = mapper.readValue(mvcResult.getResponse()
                                                      .getContentAsString(), listType);
        return list;
    }

    private CartVo addCartItem(String cartAPI, ObjectMapper mapper, CartVo cartVo) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(cartAPI)
                                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                    .content(mapper.writeValueAsString(cartVo)))
                                     .andExpect(status().isOk())
                                     .andReturn();
        return mapper.readValue(mvcResult.getResponse()
                                         .getContentAsString(), CartVo.class);
    }

    private boolean updateCartItem(String cartAPI, ObjectMapper mapper, CartVo cartVo) throws Exception {
        String url = cartAPI + "/" + cartVo.getUserId() + "/" + cartVo.getId();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(url)
                                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                    .content(mapper.writeValueAsString(cartVo)))
                                     .andExpect(status().isOk())
                                     .andReturn();
        return mapper.readValue(mvcResult.getResponse()
                                         .getContentAsString(), Boolean.class);
    }

    private boolean removeCartItem(String cartAPI, ObjectMapper mapper, CartVo cartVo) throws Exception {
        String url = cartAPI + "/" + cartVo.getUserId() + "/" + cartVo.getId();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(url))
                                     .andExpect(status().isOk())
                                     .andReturn();
        return mapper.readValue(mvcResult.getResponse()
                                         .getContentAsString(), Boolean.class);
    }

    @Test
    public void testCart() throws Exception {
        String cartAPI = "/api/v1/cart";
        String productAPI = "/api/v1/product";
        String userAPI = "/api/v1/user";

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // Prepare cart data
        UserVo user = UserVo.builder()
                            .name("test one")
                            .cvcNo("123")
                            .cardNo("12345687")
                            .expiryDate(LocalDate.now()
                                                 .plusDays(10))
                            .build();
        user = register(userAPI, mapper, user);

        ProductVo productVo = ProductVo.builder()
                                       .name("test product one")
                                       .price(new BigDecimal("10.25"))
                                       .amount(23)
                                       .build();
        productVo = addProduct(productAPI, mapper, productVo);

        List<CartVo> cartItems = listCartItems(cartAPI, mapper, user.getId());
        assertEquals(0, cartItems.size());

        // Get, 404
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(cartAPI + "/" + user.getId() + "/" + 1))
                                     .andExpect(status().isNotFound())
                                     .andReturn();
        ErrorResponse err = mapper.readValue(mvcResult.getResponse()
                                                      .getContentAsString(), ErrorResponse.class);
        assertEquals(CART_ITEM_NOT_FOUND.getErrMsg(), err.getMessage());

        // Add, too many
        CartVo cartVo = CartVo.builder()
                              .productId(productVo.getId())
                              .userId(user.getId())
                              .amount(25)
                              .build();
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(cartAPI)
                                                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                          .content(mapper.writeValueAsString(cartVo)))
                           .andExpect(status().isUnprocessableEntity())
                           .andReturn();
        err = mapper.readValue(mvcResult.getResponse()
                                        .getContentAsString(), ErrorResponse.class);
        assertEquals(NOT_ENOUGH_PRODUCT.getErrMsg(), err.getMessage());
        // Add
        cartVo = cartVo.withAmount(20);
        cartVo = addCartItem(cartAPI, mapper, cartVo);

        // Update
        cartVo = cartVo.withAmount(10);
        assertTrue(updateCartItem(cartAPI, mapper, cartVo));

        // Delete
        assertTrue(removeCartItem(cartAPI, mapper, cartVo));
        assertTrue(removeUser(userAPI, mapper, user.getId()));
        assertTrue(removeProduct(productAPI, mapper, productVo.getId()));
    }

}

class ErrorResponse {
    private String httpStatus;
    private String message;
    private String httpStatusCode;

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(String httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
}
