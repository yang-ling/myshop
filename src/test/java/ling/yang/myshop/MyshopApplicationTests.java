package ling.yang.myshop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ling.yang.myshop.Vo.UserVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;
import java.awt.*;
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

        // Delete
        assertTrue(removeUser(userAPI, mapper, register.getId()));
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
