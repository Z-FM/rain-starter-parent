package org.rain.executor;

import org.rain.service.AdaptorCacheService;
import org.rain.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

    static AdaptorTask getAdaptorTask(String taskCode, String tenantNum) {
        //查询数据库
        AdaptorTaskHeader adaptorTaskHeader = AdaptorHelperBridge.getAdaptorCacheService().selectTargetAdaptorTaskHeader(taskCode, tenantNum);
        //查到是启用状态，将数据存入redis并返回
        if (Objects.nonNull(adaptorTaskHeader)) {
            AdaptorTask adaptorTask = new AdaptorTask();
            AdaptorTaskVo adaptorTaskVo = AdaptorHelperBridge.constructFromDb(adaptorTaskHeader);
            AdaptorHelperBridge.getRedisHelper().strSet(AdaptorHelperBridge.TASK_RESOURCE_PATH + TASK_HASH_KEY(taskCode, tenantNum), AdaptorHelperBridge.getRedisHelper().toJson(adaptorTaskVo), 300, TimeUnit.SECONDS);
            BeanUtils.copyProperties(adaptorTaskVo, adaptorTask);
            return adaptorTask;
        } else {
            AdaptorHelperBridge.getRedisHelper().strSet(AdaptorHelperBridge.TASK_RESOURCE_PATH + TASK_HASH_KEY(taskCode, tenantNum), "{}", 300, TimeUnit.SECONDS);
        }
        return null;

    }
}
