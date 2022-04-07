package com.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @author carl
 */
@Getter
@Setter
@EqualsAndHashCode(of = {""}, callSuper = true)
@NoArgsConstructor
@TableName("COMPANY")
public class Company extends BaseEntity<Company> {
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String name;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String address;

    @Builder
    public Company(String name, String address, Long id) {
        this.setId(id);
        this.name = name;
        this.address = address;
    }

    @Override
    public String toString() {
        return "{" + super.toString() +
                ", name=" + name +
                ", address=" + address +
                '}';
    }
}
