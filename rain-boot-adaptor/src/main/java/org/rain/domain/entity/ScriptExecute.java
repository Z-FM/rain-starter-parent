package org.rain.domain.entity;

import lombok.Data;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * .
 *
 * @author ZFM.
 * @date 2023/1/15 14:09.
 */
@Data
public class ScriptExecute implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "参数不能为空")
    private String rawInputJsonStr;

    private Long scriptId;

    @NotNull(message = "租户不能为空")
    private Long tenantId;

    @NotBlank(message = "请选择要执行的脚本")
    private String scriptCode;

    private String applicationId;

    /**
     * 运行前检测
     */
    public void checkBeforeRun(){

    }

}
