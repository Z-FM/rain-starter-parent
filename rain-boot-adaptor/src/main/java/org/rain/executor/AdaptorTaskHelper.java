package org.rain.executor;

import org.rain.domain.result.TaskResultBox;

import java.util.Objects;

/**
 * 适配器任务执行helper.
 *
 * @author ZFM.
 * @date 2023/1/7 21:51.
 */
public class AdaptorTaskHelper {

    public static TaskResultBox executeAdaptorTask(String taskCode, String tenantNum, Object inputObj) throws TaskNotExistException {
        //任务获取,完全依赖服务端推到Redis
        AdaptorTask adaptorTask = AdaptorHelperBridge.getAdaptorTask(taskCode, tenantNum);
        if (Objects.isNull(adaptorTask)) {
            throw new TaskNotExistException();
        }
        return AdaptorHelperBridge.innerExecuteAdaptorTask(taskCode, tenantNum, inputObj, adaptorTask, true);
    }

}
