package org.srm.boot.adaptor.client.exception;

import io.choerodon.core.exception.CommonException;

public class ScriptContainerExecutionException extends CommonException {

    public ScriptContainerExecutionException(String code, Object... parameters) {
        super(code, parameters);
    }
}
