package ling.yang.myshop.web;

import ling.yang.myshop.Vo.UserVo;
import ling.yang.myshop.entity.User;
import ling.yang.myshop.exceptions.MyShopException;
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
@RequestMapping(value = "/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping
    public List<UserVo> list() {
        return userService.list()
                          .stream()
                          .map(UserVo::of)
                          .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserVo oneUser(@PathVariable int userId) {
        return userService.getOptById(userId)
                          .map(UserVo::of)
                          .orElseThrow(() -> new MyShopException(USER_NOT_FOUND));
    }

    @PostMapping
    public int register(@RequestBody UserVo user) {
        User entity = User.builder()
                          .name(user.getName())
                          .cvcNo(user.getCvcNo())
                          .cardNo(user.getCardNo())
                          .expiryDate(user.getExpiryDate())
                          .build();
        userService.save(entity);
        return entity.getId();
    }

    @PutMapping("/{userId}")
    public boolean update(@PathVariable int userId, @RequestBody UserVo user) {
        Optional<User> optById = userService.getOptById(userId);
        if (optById.isEmpty()) {
            throw new MyShopException(USER_NOT_FOUND);
        }
        User entity = User.builder()
                          .id(userId)
                          .name(user.getName())
                          .cvcNo(user.getCvcNo())
                          .cardNo(user.getCardNo())
                          .expiryDate(user.getExpiryDate())
                          .build();
        return userService.updateById(entity);
    }

    @DeleteMapping("/{userId}")
    public boolean remove(@PathVariable int userId) {
        Optional<User> optById = userService.getOptById(userId);
        if (optById.isEmpty()) {
            return false;
        }
        return userService.removeById(userId);
    }
}
