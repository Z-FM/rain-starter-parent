package org.rain.domain.result;


import com.fasterxml.jackson.core.type.TypeReference;
import io.choerodon.core.exception.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.srm.boot.adaptor.client.util.ObjectMapperCollection;

import java.io.IOException;
import java.util.List;

/**
 * 我们仍然提供返回值,但给它包装起来
 */
public class TaskResultBox {
    private static final Logger logger = LoggerFactory.getLogger(TaskResultBox.class);
    private static final Long TASK_SCRIPT_VERSION_V3 = 3L;
    List<String> resultList;
    String taskCode;
    Long scriptVersion;
    public int getSize(){
        return resultList.size();
    }


    public TaskResultBox(List<String> resultList, String taskCode, Long scriptVersion) {
        Assert.notNull(resultList,"result not null");
        this.resultList = resultList;
        this.taskCode=taskCode;
        this.scriptVersion=scriptVersion;
    }

    @Deprecated
    public <T> T get(int index, Class<T> tClass){
        Assert.isTrue(index<resultList.size(),taskCode+"has only "+resultList.size()+" result");
        String jsonStr = resultList.get(index);
        if(StringUtils.hasText(jsonStr)){
            try {
                if(TASK_SCRIPT_VERSION_V3.equals(scriptVersion)){
                    return ObjectMapperCollection.MARMOT_SCRIPT_OBJECT_MAPPER.readValue(jsonStr,tClass);
                }else {
                    return ObjectMapperCollection.ADAPTOR_OBJECT_MAPPER.readValue(jsonStr,tClass);
                }
            } catch (IOException e) {
                logger.error("taskBox get exception ",e);
                throw new CommonException("invalid result json");
            }
        }
        return null;
    }

    @Deprecated
    public <T> T get(int index, TypeReference<T> tRef){
        Assert.isTrue(index<resultList.size(),taskCode+"has only "+resultList.size()+" result");
        String jsonStr = resultList.get(index);
        if(StringUtils.hasText(jsonStr)){
            try {
                if(TASK_SCRIPT_VERSION_V3.equals(scriptVersion)){
                    return ObjectMapperCollection.MARMOT_SCRIPT_OBJECT_MAPPER.readValue(jsonStr,tRef);
                }else {
                    return ObjectMapperCollection.ADAPTOR_OBJECT_MAPPER.readValue(jsonStr,tRef);
                }
            } catch (IOException e) {
                logger.error("taskBox get exception ",e);
                throw new CommonException("invalid result json");
            }
        }
        return null;
    }

    public <T> T getAs(Class<T> tClass){
        Assert.isTrue(0<resultList.size(),taskCode+"has only "+resultList.size()+" result");
        String jsonStr = resultList.get(0);
        if(StringUtils.hasText(jsonStr)){
            try {
                if(TASK_SCRIPT_VERSION_V3.equals(scriptVersion)){
                    return ObjectMapperCollection.MARMOT_SCRIPT_OBJECT_MAPPER.readValue(jsonStr,tClass);
                }else {
                    return ObjectMapperCollection.ADAPTOR_OBJECT_MAPPER.readValue(jsonStr,tClass);
                }
            } catch (IOException e) {
                logger.error("taskBox get exception ",e);
                throw new CommonException("invalid result json");
            }
        }
        return null;
    }

    public <T> T getAs(TypeReference<T> tRef){
        Assert.isTrue(0<resultList.size(),taskCode+"has only "+resultList.size()+" result");
        String jsonStr = resultList.get(0);
        if(StringUtils.hasText(jsonStr)){
            try {
                if(TASK_SCRIPT_VERSION_V3.equals(scriptVersion)){
                    return ObjectMapperCollection.MARMOT_SCRIPT_OBJECT_MAPPER.readValue(jsonStr,tRef);
                }else {
                    return ObjectMapperCollection.ADAPTOR_OBJECT_MAPPER.readValue(jsonStr,tRef);
                }
            } catch (IOException e) {
                logger.error("taskBox get exception ",e);
                throw new CommonException("invalid result json");
            }
        }
        return null;
    }

    @Deprecated
    public List<String> rowResult(){
        return resultList;
    }

    public List<String> rawResult(){
        return resultList;
    }
}
