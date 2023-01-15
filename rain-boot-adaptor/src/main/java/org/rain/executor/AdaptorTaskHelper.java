package org.rain.executor;

import org.rain.database.exception.TaskNotExistException;
import org.rain.domain.entity.ScriptExecute;
import org.rain.domain.result.TaskResultBox;

import java.util.Objects;

/**
 * 适配器任务执行helper.
 *
 * @author ZFM.
 * @date 2023/1/7 21:51.
 */
public class AdaptorTaskHelper {

    public static TaskResultBox executeAdaptorTask(String taskCode, Long tenantId, Object inputObj) throws TaskNotExistException {
        ScriptExecute adaptorTask = AdaptorHelperBridge.getAdaptorTask(taskCode, tenantId);
        if (Objects.isNull(adaptorTask)) {
            throw new TaskNotExistException();
        }
        return AdaptorHelperBridge.innerExecuteAdaptorTask(taskCode, tenantId, inputObj, adaptorTask, true);
    }

}
