package com.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;

/**
 * @author carl
 */
@Getter
@Setter
@EqualsAndHashCode(of = {""}, callSuper = true)
@NoArgsConstructor
public class User extends BaseEntity<User> {
    private Integer age;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String gender;

    @Builder
    public User(Long id, Integer age, String gender) {
        this.setId(id);
        this.age = age;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "{" + super.toString() +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}
