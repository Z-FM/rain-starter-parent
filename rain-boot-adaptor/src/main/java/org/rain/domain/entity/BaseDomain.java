package org.rain.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description: .
 * @Author: Z-FM.
 */
@Data
public abstract class BaseDomain implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
    public static final String FIELD_STATUS = "status";

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime creationDate;
    @TableField(fill = FieldFill.INSERT)
    private Long createdBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastUpdateDate;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long lastUpdatedBy;

    private Long objectVersionNumber;

    /**
     * 状态（1:正常；0:删除）
     */
    @TableLogic
    private int status;

}
