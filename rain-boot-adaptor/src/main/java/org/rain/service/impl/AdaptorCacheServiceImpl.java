package org.rain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.rain.domain.entity.ScriptHeader;
import org.rain.service.AdaptorCacheService;
import org.rain.service.ScriptHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * .
 *
 * @author ZFM.
 * @date 2023/1/7 22:24.
 */
@Service
public class AdaptorCacheServiceImpl implements AdaptorCacheService {
    @Autowired
    private ScriptHeaderService scriptHeaderService;

    @Override
    public ScriptHeader selectTargetScriptHeader(String scriptCode, Long tenantId) {
        return this.scriptHeaderService.getOne(new QueryWrapper<ScriptHeader>().lambda().eq(ScriptHeader::getTenantId, tenantId)
                .eq(ScriptHeader::getScriptCode, scriptCode));
    }
}
