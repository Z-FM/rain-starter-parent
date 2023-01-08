package org.rain.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description: .
 * @Author: Z-FM.
 */
@TableName("script_header")
@Data
public class ScriptHeader extends BaseDomain {
    /**
     * 脚本主键
     */
    @TableId
    private Long scriptHeaderId;

    /**
     * 租户id
     */
    @NotNull(message = "请选择租户")
    private Long tenantId;

    /**
     * 所属服务
     */
    @NotBlank(message = "请选择服务")
    private String runningService;

    /**
     * 脚本编码
     */
    @NotBlank(message = "请维护编码信息")
    private String scriptCode;

    /**
     * 启用
     */
    private Integer enableFlag;

    /**
     * 描述
     */
    private String description;
}
