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
@TableName("script_line")
@Data
public class ScriptLine extends BaseDomain {
    /**
     * 主键
     */
    @TableId
    private Long scriptLineId;

    /**
     * 租户id
     */
    @NotNull(message = "请选择租户")
    private Long tenantId;

    /**
     * 脚本头id
     */
    @NotNull(message = "请维护基本信息")
    private Long scriptHeaderId;

    /**
     * 脚本内容
     */
    @NotBlank(message = "请维护脚本信息")
    private String scriptContent;

    /**
     * 脚本类型
     */
    private String scriptType;
}
