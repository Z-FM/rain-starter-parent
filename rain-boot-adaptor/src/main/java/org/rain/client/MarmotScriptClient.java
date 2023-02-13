package org.srm.boot.adaptor.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.choerodon.core.convertor.ApplicationContextHelper;
import io.choerodon.core.exception.CommonException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.srm.boot.adaptor.client.feign.ScriptSpecialFeignClient;
import org.srm.boot.adaptor.client.feign.ScriptContainerFeign;
import org.srm.boot.adaptor.client.feign.vo.ScriptAndParamDTO;
import org.srm.boot.adaptor.faketx.client.FakeTxScriptClient;
import org.srm.boot.common.concurrent.TenantLowLevelSemaphoreLimiter;

/**
 * 土拨鼠脚本执行客户端
 * @author xing.yang01@hand-china.com
 */
public class MarmotScriptClient {

    private static ScriptContainerFeign SCRIPT_REMOTE_CONTAINER_SERVICE;
    private static ScriptSpecialFeignClient SCRIPT_FEIGN_ACCESS_TOOL = new ScriptSpecialFeignClient();


    private static void init(){
        if(SCRIPT_REMOTE_CONTAINER_SERVICE ==null){
            ApplicationContext context = ApplicationContextHelper.getContext();
            SCRIPT_REMOTE_CONTAINER_SERVICE =context.getBean(ScriptContainerFeign.class);
        }
    }

    /**
     * 控制客户端并发执行量
     */
    private static final TenantLowLevelSemaphoreLimiter MARMOT_SCRIPT_CLIENT_LIMITER=new TenantLowLevelSemaphoreLimiter(16,8);


    public static String executeWithContentV2(ScriptAndParamDTO scriptAndParamDTO){
        init();
        String responseEntity =MARMOT_SCRIPT_CLIENT_LIMITER.execute(scriptAndParamDTO.getTenantNum(),
                ()->SCRIPT_FEIGN_ACCESS_TOOL.selectClient().executeWithContent(scriptAndParamDTO)
//                ()->SCRIPT_REMOTE_CONTAINER_SERVICE.executeWithContent(scriptAndParamDTO)
        );
        if(responseEntity!=null){
            Assert.isTrue(StringUtils.hasText(responseEntity)," execute result must has body content");
            return responseEntity;
        }
        throw new CommonException("not possible");
    }

    public static String executeWithContentV3(ScriptAndParamDTO scriptAndParamDTO){
        init();
        return MARMOT_SCRIPT_CLIENT_LIMITER.execute(scriptAndParamDTO.getTenantNum(),()->{
            try {
                return FakeTxScriptClient.executeScript(scriptAndParamDTO);
            } catch (JsonProcessingException e) {
                throw new CommonException("not possible");
            }
        });

    }

    /**
     * v3测试用
     * @param scriptAndParamDTO
     * @return
     */

    public static String executeWithContentV3WithLog(ScriptAndParamDTO scriptAndParamDTO){
        init();
        return MARMOT_SCRIPT_CLIENT_LIMITER.execute(scriptAndParamDTO.getTenantNum(),()->{
            try {
                return FakeTxScriptClient.executeScriptWithLog(scriptAndParamDTO);
            } catch (JsonProcessingException e) {
                throw new CommonException("not possible");
            }
        });

    }


}
