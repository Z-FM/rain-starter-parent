package org.rain.service;

import org.rain.domain.entity.ScriptHeader;

/**
 * .
 *
 * @author ZFM.
 * @date 2023/1/7 22:24.
 */
public interface AdaptorCacheService {

    ScriptHeader selectTargetScriptHeader(String scriptCode, Long tenantId);

}
