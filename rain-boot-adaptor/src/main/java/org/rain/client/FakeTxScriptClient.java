package org.srm.boot.adaptor.faketx.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.choerodon.core.convertor.ApplicationContextHelper;
import io.choerodon.core.exception.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.srm.boot.adaptor.client.feign.ScriptSpecialFeignClient;
import org.srm.boot.adaptor.client.feign.ScriptContainerFeign;
import org.srm.boot.adaptor.client.feign.vo.ScriptAndParamDTO;
import org.srm.boot.adaptor.client.util.ObjectMapperCollection;
import org.srm.boot.adaptor.faketx.InvokeUtils;
import org.srm.boot.adaptor.faketx.controller.service.AdaptorCommonLambdaSupport;
import org.srm.boot.adaptor.faketx.debug.DebugUtils;

/**
 * 带有fake-tx功能的MarmotScript客户端
 * 判断是否启用fake-tx进行脚本执行的逻辑放在这里
 * @author xing.yang01@hand-china.com
 */
public class FakeTxScriptClient {
    private static ScriptContainerFeign scriptRemoteContainerService;
    private static AdaptorCommonLambdaSupport adaptorCommonLambdaSupport;
    private static ScriptSpecialFeignClient scriptSpecialFeignClient = new ScriptSpecialFeignClient();
    private static final Logger logger = LoggerFactory.getLogger(FakeTxScriptClient.class);

    private static void init(){
        if(adaptorCommonLambdaSupport==null){
            ApplicationContext context = ApplicationContextHelper.getContext();
            adaptorCommonLambdaSupport=context.getBean(AdaptorCommonLambdaSupport.class);
        }
    }

    /**
     * fake-tx 额外准备
     * 检查条件是否充足，充足则开启fake-tx，否则进行普通执行
     * @param scriptAndParamDTO 调用参数
     */
    private static void prepareInvoke(ScriptAndParamDTO scriptAndParamDTO){
        if(scriptRemoteContainerService ==null){
            ApplicationContext context = ApplicationContextHelper.getContext();
            scriptRemoteContainerService =context.getBean(ScriptContainerFeign.class);

        }
        boolean fakeTxEnabled = FakeTxClientCentre.sqlSessionPreparedAndServiceOk();
        //实际操作上我们忽略SAGA,不管它
        if(fakeTxEnabled){
            if(logger.isDebugEnabled()){
                logger.debug("@Transactional found, execute with fake-tx");
            }
            if("srm-oauth".equals(DebugUtils.getApplicationId())||"hzero-oauth".equals(DebugUtils.getApplicationId())){
                //oauth禁用fake-tx
                scriptAndParamDTO.setFakeTxEnabled(false);
            }else {
                scriptAndParamDTO.setFakeTxEnabled(true);
            }
            scriptAndParamDTO.fillFakeTxInfo(FakeTxClientCentre.currentFakeTxInfo());

        }else {
            throw new CommonException("no @Transactional found ,this must be bug ,do contract R&D Support");
        }
    }

    public static String executeScript(ScriptAndParamDTO scriptAndParamDTO) throws JsonProcessingException {
        init();
        return adaptorCommonLambdaSupport.doWithTransactionalAndException(()->{
            try{
                prepareInvoke(scriptAndParamDTO);
                String bodyStr = ObjectMapperCollection.MARMOT_SCRIPT_OBJECT_MAPPER.writeValueAsString(scriptAndParamDTO);
                return InvokeUtils.processException(scriptSpecialFeignClient.selectClient().executeWithContentV3(bodyStr));
            }finally {
                FakeTxClientCentre.clearKeyForEveryExecution();
            }
        });


    }
    public static String executeScriptWithLog(ScriptAndParamDTO scriptAndParamDTO) throws JsonProcessingException {
        init();
        return adaptorCommonLambdaSupport.doWithTransactionalAndException(()->{
            try{
                prepareInvoke(scriptAndParamDTO);
                String bodyStr = ObjectMapperCollection.MARMOT_SCRIPT_OBJECT_MAPPER.writeValueAsString(scriptAndParamDTO);
                return InvokeUtils.processException(scriptSpecialFeignClient.selectClient().executeWithContentV3WithLog(bodyStr));
            }finally {
                FakeTxClientCentre.clearKeyForEveryExecution();
            }
        });

    }



}
