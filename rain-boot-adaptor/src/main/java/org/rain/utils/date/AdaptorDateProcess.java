package org.rain.utils.date;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

/**
 * 用于自动处理日期三种类型之间序列化与反序列化关系的集合。
 */
public class AdaptorDateProcess {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateSerializer.class);

    private static TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }

    public static class DateDeserializer extends JsonDeserializer<Date> {

        @SneakyThrows
        public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String value = jsonParser.getValueAsString();
            value = value.trim();
            if (StringUtils.isEmpty(value)) {
                return null;
            }
            SimpleDateFormat dateFormatGmt;
            if (value.length() == 10) {
                dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd");
            } else if (value.length() == 19) {
                dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            } else {
                throw new Exception("date value not supported: " + value);
            }

            try {
                dateFormatGmt.setTimeZone(getTimeZone());
                return dateFormatGmt.parse(value);
            } catch (Exception var5) {
                LOGGER.warn("DateDeserializer format error", var5);
                return null;
            }
        }
    }

    public static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
        JsonDeserializer<LocalDate> YYYY_MM_DD_HH_MM_SS = new com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        JsonDeserializer<LocalDate> YYYY_MM_DD = new com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        @SneakyThrows
        public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String value = jsonParser.getValueAsString();
            value = value.trim();
            if (StringUtils.isEmpty(value)) {
                return null;
            }
            if (value.length() == 10) {
                return YYYY_MM_DD.deserialize(jsonParser, deserializationContext);
            } else if (value.length() == 19) {
                return YYYY_MM_DD_HH_MM_SS.deserialize(jsonParser, deserializationContext);
            } else {
                throw new Exception("date value not supported: " + value);
            }

        }

        @SneakyThrows
        @Override
        public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext, LocalDate intoValue) throws IOException {
            String value = jsonParser.getValueAsString();
            value = value.trim();
            if (StringUtils.isEmpty(value)) {
                return null;
            }
            if (value.length() == 10) {
                return YYYY_MM_DD.deserialize(jsonParser, deserializationContext, intoValue);
            } else if (value.length() == 19) {
                return YYYY_MM_DD_HH_MM_SS.deserialize(jsonParser, deserializationContext, intoValue);
            } else {
                throw new Exception("date value not supported: " + value);
            }
        }
    }

    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        JsonDeserializer<LocalDateTime> YYYY_MM_DD_HH_MM_SS = new com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        JsonDeserializer<LocalDateTime> YYYY_MM_DD = new com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        @SneakyThrows
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String value = jsonParser.getValueAsString();
            value = value.trim();
            if (StringUtils.isEmpty(value)) {
                return null;
            }
            if (value.length() == 10) {
                return YYYY_MM_DD.deserialize(jsonParser, deserializationContext);
            } else if (value.length() == 19) {
                return YYYY_MM_DD_HH_MM_SS.deserialize(jsonParser, deserializationContext);
            } else {
                throw new Exception("date value not supported: " + value);
            }

        }

        @SneakyThrows
        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext, LocalDateTime intoValue) throws IOException {
            String value = jsonParser.getValueAsString();
            value = value.trim();
            if (StringUtils.isEmpty(value)) {
                return null;
            }
            if (value.length() == 10) {
                return YYYY_MM_DD.deserialize(jsonParser, deserializationContext, intoValue);
            } else if (value.length() == 19) {
                return YYYY_MM_DD_HH_MM_SS.deserialize(jsonParser, deserializationContext, intoValue);
            } else {
                throw new Exception("date value not supported: " + value);
            }
        }
    }

}
