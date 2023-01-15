package org.rain.utils;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.rain.utils.date.AdaptorDateProcess;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

/**
 * 特殊ObjectMapper集合，都是final的不要改
 */
public class ObjectMapperCollection {

    /**
     * ADAPTOR_MAPPING_OBJECT_MAPPER
     * 这是映射模板用的
     */
    public static final ObjectMapper ADAPTOR_MAPPING_OBJECT_MAPPER = new ObjectMapper() {{
        disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //LocalDatetime序列化,自定义逻辑
        JavaTimeModule timeModule = new JavaTimeModule();

        /*
        序列化方法将格式限定
         */
        timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        timeModule.addSerializer(Date.class, new DateSerializer());

        /*
        重写了反序列化方法
         */
        timeModule.addDeserializer(LocalDate.class, new AdaptorDateProcess.LocalDateDeserializer());
        timeModule.addDeserializer(LocalDateTime.class, new AdaptorDateProcess.LocalDateTimeDeserializer());
        timeModule.addDeserializer(Date.class, new AdaptorDateProcess.DateDeserializer());

        registerModule(timeModule);

        setTimeZone(TimeZone.getDefault());
    }};

    /**
     * 对于序列化，所有的时间，都变成 yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss 长度分别对应10,19
     */

    /**
     * Adaptor专用objectMapper
     * 这是MarmotScript v2用的
     */
    public static final ObjectMapper ADAPTOR_OBJECT_MAPPER = new ObjectMapper() {{
        disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        disable(MapperFeature.USE_ANNOTATIONS);
        //LocalDatetime序列化
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        timeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        //Date会变成时间戳
        registerModule(timeModule);
        setTimeZone(TimeZone.getDefault());
    }};


    /**
     * 选择性启用注解
     */
    public static class MarmotScriptAnnotationIntrospector extends JacksonAnnotationIntrospector {
        private static final Set<Class<?>> allowedAnnotations = new HashSet<Class<?>>() {{
            add(JsonAnySetter.class);
            add(JsonAnyGetter.class);
        }};

        @Override
        public boolean isAnnotationBundle(Annotation ann) {
            if (allowedAnnotations.contains(ann.annotationType())) {
                return super.isAnnotationBundle(ann);
            }
            return false;
        }
    }

    /**
     * 专门用于marmotScript的序列化与反序列化方案
     * 日期特殊处理+数字特殊处理
     * 这是MarmotScript v3用的
     */
    public static final ObjectMapper MARMOT_SCRIPT_OBJECT_MAPPER = new ObjectMapper() {{
        disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        disable(MapperFeature.USE_ANNOTATIONS);
        setAnnotationIntrospector(new MarmotScriptAnnotationIntrospector());
        setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //LocalDatetime序列化,自定义逻辑
        JavaTimeModule timeModule = new JavaTimeModule();

        /*
        序列化方法将格式限定
         */
        timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        timeModule.addSerializer(Date.class, new DateSerializer());

        /*
        重写了反序列化方法
         */
        timeModule.addDeserializer(LocalDate.class, new AdaptorDateProcess.LocalDateDeserializer());
        timeModule.addDeserializer(LocalDateTime.class, new AdaptorDateProcess.LocalDateTimeDeserializer());
        timeModule.addDeserializer(Date.class, new AdaptorDateProcess.DateDeserializer());

        registerModule(timeModule);
        //数字全部字符串化
        SimpleModule numberModule = new SimpleModule();
        numberModule.addSerializer(Number.class, new NumberToStringSerializer());
        registerModule(numberModule);
        setTimeZone(TimeZone.getDefault());
    }};

    public static class NumberToStringSerializer extends ToStringSerializer {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (value instanceof Number) {
                String format = NUMBER_FORMAT.format(value);
                gen.writeString(format);
            } else {
                super.serialize(value, gen, provider);
            }
        }
    }

    /**
     * 防止科学输入法的格式化
     */
    public static final NumberFormat NUMBER_FORMAT = GET_NUMBER_FORMAT();

    private static NumberFormat GET_NUMBER_FORMAT() {
        NumberFormat instance = NumberFormat.getInstance();
        instance.setMinimumFractionDigits(0);
        instance.setMaximumFractionDigits(Integer.MAX_VALUE);
        instance.setMaximumIntegerDigits(Integer.MAX_VALUE);
        instance.setRoundingMode(RoundingMode.HALF_UP);
        instance.setGroupingUsed(false);
        return instance;
    }
}
