package org.rain.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * .
 *
 * @author ZFM.
 * @date 2023/1/15 15:28.
 */
public class ScriptUtils {
    private static final Logger logger = LoggerFactory.getLogger(ScriptUtils.class);

    /**
     * 注意，这个东西整出来的所有value都会变成字符串
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> obj2AdaptorMap(Object obj) {
        try {
            String str = obj2Str(obj);
            Map<String, Object> result = ObjectMapperCollection.MARMOT_SCRIPT_OBJECT_MAPPER.readValue(str,
                    new TypeReference<Map<String, Object>>() {
                    });
            return result;
        } catch (IOException e) {
            logger.debug("marmotScript 3 input exception ", e);
            throw new CommonException("marmotScript 3 input exception");
        }
    }


    private static String obj2Str(Object obj) throws JsonProcessingException {
        /*
         * page特殊处理
         */
        if (obj instanceof Page<?>) {
            SimplePage<?> simplePage = new SimplePage<>((Page<?>) obj);
            return ObjectMapperCollection.MARMOT_SCRIPT_OBJECT_MAPPER.writeValueAsString(simplePage);
        }
        return ObjectMapperCollection.MARMOT_SCRIPT_OBJECT_MAPPER.writeValueAsString(obj);
    }
}
