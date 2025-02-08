package com.github.milomarten.flagguesser.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;

public class MultiEnumSetDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {
    private JavaType javaType;
    private JavaType javaTypeWithWrapper;

    public MultiEnumSetDeserializer() {}

    private MultiEnumSetDeserializer(JavaType javaType, JavaType javaTypeWithWrapper) {
        this.javaType = javaType;
        this.javaTypeWithWrapper = javaTypeWithWrapper;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) throws JsonMappingException {
        var wrapperType = beanProperty.getType().containedType(0);
        if (wrapperType.isEnumType()) {
            var itemCountType = deserializationContext.getTypeFactory()
                    .constructParametricType(ItemCount.class, wrapperType);
            return new MultiEnumSetDeserializer(wrapperType, itemCountType);
        }
        throw deserializationContext.invalidTypeIdException(wrapperType, "enum", null);
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        var set = new MultiEnumSet<>((Class<Enum>)this.javaType.getRawClass());
        var token = jsonParser.getCurrentToken();
        while (token != JsonToken.END_ARRAY) {
            if (token == JsonToken.VALUE_STRING) {
                set.add(deserializationContext.readValue(jsonParser, javaType));
            } else if (token == JsonToken.START_OBJECT) {
                ItemCount<Enum> ic = deserializationContext.readValue(jsonParser, javaTypeWithWrapper);
                set.add(ic.item(), ic.count());
            }
            token = jsonParser.nextToken();
        }
        return set;
    }

}
