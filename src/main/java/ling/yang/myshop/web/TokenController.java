package ling.yang.myshop.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/token")
public class TokenController {
    @GetMapping(value = "", produces = MediaType.TEXT_PLAIN_VALUE)
    public String generateToken() {
        return UUID.randomUUID()
                   .toString()
                   .replace("-", "");
    }
}
