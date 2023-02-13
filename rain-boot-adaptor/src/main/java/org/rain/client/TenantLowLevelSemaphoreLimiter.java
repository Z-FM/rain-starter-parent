package org.srm.boot.common.concurrent;


import org.srm.boot.annotions.SrmExposed;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.function.Supplier;

/**
 * local semaphore limiter, with tenant slot control
 * @author xing.yang01@hand-china.com
 */
@SrmExposed(maintainer = "xing.yang01@hand-china.com")
public class TenantLowLevelSemaphoreLimiter {
    private final Map<String,Semaphore> GLOBAL_TENANT_SEMAPHORE_MAP;

    private final int tenantLimit;

    public TenantLowLevelSemaphoreLimiter(int globalLimit,int tenantLimit) {
        GLOBAL_TENANT_SEMAPHORE_MAP=new ConcurrentHashMap<String,Semaphore>(){{
            put("SRM",new Semaphore(globalLimit));
        }};
        this.tenantLimit=tenantLimit;
    }


    public Semaphore getSemaphore(String tenantNum){
        if(Objects.isNull(tenantNum)||Objects.equals("SRM",tenantNum)){
            return GLOBAL_TENANT_SEMAPHORE_MAP.get("SRM");
        }
        return GLOBAL_TENANT_SEMAPHORE_MAP.computeIfAbsent(tenantNum, (v) -> new Semaphore(tenantLimit));
    }

    public <T> T execute(String tenantNum, Supplier<T> supplier){
        Semaphore semaphore = getSemaphore(tenantNum);
        try {
            semaphore.acquireUninterruptibly();
            return supplier.get();
        }finally {
            semaphore.release();
        }
    }

}
