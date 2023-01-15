package org.rain.executor;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.rain.domain.entity.ScriptExecute;
import org.rain.domain.entity.ScriptHeader;
import org.rain.domain.result.TaskResultBox;
import org.rain.service.AdaptorCacheService;
import org.rain.utils.ObjectMapperCollection;
import org.rain.utils.ScriptUtils;
import org.rain.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * .
 *
 * @author ZFM.
 * @date 2023/1/7 22:20.
 */
public class AdaptorHelperBridge {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdaptorHelperBridge.class);

    private AdaptorHelperBridge() {
    }

    private static AdaptorCacheService adaptorCacheService;

    private static void init() {
        if (adaptorCacheService == null) {
            AdaptorCacheService context = SpringUtils.getBean(AdaptorCacheService.class);
        }
    }

    /**
     * 获取执行参数，目前只将参数、脚本编码和租户id作为参数，获取具体脚本编码交给脚本执行服务。后续可以考虑将脚本代码获取功能迁移到这里，脚本执行服务只完成执行功能。
     *
     * @param taskCode 脚本编码.
     * @param tenantId 租户id.
     * @return 执行所需参数.
     */
    static ScriptExecute getAdaptorTask(String taskCode, Long tenantId) {
        //查询数据库
        ScriptHeader adaptorTaskHeader = AdaptorHelperBridge.getAdaptorCacheService().selectTargetScriptHeader(taskCode, tenantId);
        //查到是启用状态，将数据存入redis并返回
        if (Objects.nonNull(adaptorTaskHeader)) {
            ScriptExecute scriptExecute = new ScriptExecute();
            scriptExecute.setScriptId(adaptorTaskHeader.getScriptHeaderId());
            scriptExecute.setTenantId(tenantId);
            scriptExecute.setScriptCode(adaptorTaskHeader.getScriptCode());
            //todo 优化：存入redis缓存
            return scriptExecute;
        } else {
            //todo 优化：redis缓存存空对象
            LOGGER.info("Script not exist.Scriptcode:{},tenantId:{}", taskCode, tenantId);
        }
        return null;

    }

    static TaskResultBox innerExecuteAdaptorTask(String taskCode, Long tenantId, Object inputObj, ScriptExecute scriptExecute) {
        Assert.notNull(taskCode, "task under this tenant not found,it may not enabled");
        //执行前校验,确保可执行
        scriptExecute.checkBeforeRun();

        Map<String, Object> input = ScriptUtils.obj2AdaptorMap(inputObj);

        Map<String, Object> result = doScriptProcess(input, scriptExecute, tenantId, "Adaptor Task start running " + taskCode + " with TenantId " + tenantId))
        ;

        List<String> resultJson = resultList.stream().map(x -> obj2Json(x, 2)).collect(Collectors.toList());
        return new TaskResultBox(resultJson, taskCode, adaptorTask.getScriptVersion());
    }

    @SneakyThrows
    private static Map<String, Object> doScriptProcess(Map<String, Object> param, ScriptExecute adaptorTask, Long tenantId, String loggerInfo) {
        ScriptAndParamDTO scriptEngineInvokeDTO = new ScriptAndParamDTO();
        scriptEngineInvokeDTO.setScript(script);
        scriptEngineInvokeDTO.setParam(param);
        scriptEngineInvokeDTO.setTenantNum(tenantNum);
        scriptEngineInvokeDTO.setLoggingInfo(loggerInfo);
        Long scriptVersion = adaptorTask.getScriptVersion();
        String str = MarmotScriptClient.executeWithContentV3(scriptEngineInvokeDTO);

        Map<String, Object> result;
        if (StringUtils.hasText(str)) {
            try {
                result = ObjectMapperCollection.MARMOT_SCRIPT_OBJECT_MAPPER.readValue(str, new TypeReference<Map<String, Object>>() {
                });

            } catch (IOException e) {
                e.printStackTrace();
                throw new Exception("result must be a json object result: " + str);
            }
        } else {
            throw new Exception("result must be a json object result: " + str);
        }
        if (Objects.isNull(result)) {
            return new HashMap<>();
        }
        //js执行错误要体现出来
        exceptionCheck(result, adaptorTask.getScriptCode(), adaptorTask.getTenantId());
        return result;
    }

    public static AdaptorCacheService getAdaptorCacheService() {
        return adaptorCacheService;
    }

    public static void setAdaptorCacheService(AdaptorCacheService adaptorCacheService) {
        AdaptorHelperBridge.adaptorCacheService = adaptorCacheService;
    }
}
