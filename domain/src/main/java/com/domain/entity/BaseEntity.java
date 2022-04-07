package com.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author carl
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public abstract class BaseEntity<T extends Model<?>> extends Model<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1406417548874537595L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    @Version
    private Long version;
    @TableLogic
    private Integer deleted;

    @Override
    public String toString() {
        return "id=" + id;
    }
}
