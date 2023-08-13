package ling.yang.myshop.Vo;

import ling.yang.myshop.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserVo {

    private Integer id;

    private String name;

    private String cardNo;

    private LocalDate expiryDate;

    private String cvcNo;

    public static UserVo of(User entity) {
        return UserVo.builder()
                     .id(entity.getId())
                     .name(entity.getName())
                     .cardNo(entity.getCardNo())
                     .expiryDate(entity.getExpiryDate())
                     .cvcNo(entity.getCvcNo())
                     .build();
    }
}
