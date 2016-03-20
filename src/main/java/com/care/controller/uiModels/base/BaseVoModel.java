package com.care.controller.uiModels.base;


import com.care.customlization.flexjson.BigDecimalFromatTransformer;
import com.care.customlization.flexjson.JsonNullValueTransformer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

/**
 * Created by nujian on 15/12/14.
 * 前端VO基类
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "version", "currentUser", "isDeleted"})
public class BaseVoModel {

    public String toJson() {
        return toJson(null, null);
    }

    public String toJson(String[] fields) {
        return toJson(fields, null);
    }

    public String toJson(String[] fields, String[] excludeFeilds) {
        JSONSerializer serializer = new JSONSerializer()
                .transform(new JsonNullValueTransformer(), void.class)
                .transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"), Date.class)
                .transform(new BigDecimalFromatTransformer(), BigDecimal.class)
                .exclude("*.class", "*.handler", "*.hibernateLazyInitializer");
        if (fields != null && fields.length > 0) {
            serializer.include(fields);
        }
        if (excludeFeilds != null && excludeFeilds.length > 0) {
            serializer.exclude(excludeFeilds);
        }
        return serializer.serialize(this);
    }


    public String toJson(String[] fields, String[] excludeFeilds, String className) {

        String dateType = "yyyy-MM-dd HH:mm:ss";

        JSONSerializer serializer = new JSONSerializer()
                .transform(new JsonNullValueTransformer(), void.class)
                .transform(new DateTransformer(dateType), Date.class)
                .transform(new BigDecimalFromatTransformer(), BigDecimal.class)
                .exclude("*.class", "*.version", "*.handler", "*.hibernateLazyInitializer");
        if (fields != null && fields.length > 0) {
            serializer.include(fields);
        }
        if (excludeFeilds != null && excludeFeilds.length > 0) {
            serializer.exclude(excludeFeilds);
        }
        return serializer.serialize(this);
    }

    public String toJson(String[] fields, String[] excludeFeilds, String className, String parrent) {

        String dateType = "yyyy-MM-dd HH:mm:ss";
        JSONSerializer serializer = new JSONSerializer()
                .transform(new JsonNullValueTransformer(), void.class)
                .transform(new DateTransformer(dateType), Date.class)
                .transform(new BigDecimalFromatTransformer(), BigDecimal.class)
                .exclude("*.class", "*.version", "*.handler", "*.hibernateLazyInitializer");
        if (fields != null && fields.length > 0) {
            serializer.include(fields);
        }
        if (excludeFeilds != null && excludeFeilds.length > 0) {
            serializer.exclude(excludeFeilds);
        }
        return serializer.serialize(this);
    }


    public static String toJsonArray(Collection<? extends BaseVoModel> collection) {
        return toJsonArray(collection, null, null);
    }

    public static String toJsonArray(Collection<? extends BaseVoModel> collection, String[] fields) {
        return toJsonArray(collection, fields, null);
    }

    public static String toJsonArray(Collection<? extends BaseVoModel> collection, String[] fields, String[] excludeFeilds) {
        JSONSerializer serializer = new JSONSerializer()
                .transform(new JsonNullValueTransformer(), void.class)
                .transform(new DateTransformer("yyyy-MM-dd HH:mm"), Date.class)
                .transform(new BigDecimalFromatTransformer(), BigDecimal.class)
                .exclude("*.class", "*.version", "*.handler", "*.hibernateLazyInitializer");
        if (fields != null && fields.length > 0) {
            serializer.include(fields);
        }
        if (excludeFeilds != null && excludeFeilds.length > 0) {
            serializer.exclude(excludeFeilds);
        }
        return serializer.serialize(collection);
    }


    public static String toJsonArray(Collection<? extends BaseVoModel> collection, String[] fields, String[] excludeFeilds, String className) {

        String dateType = "yyyy-MM-dd HH:mm:ss";
        JSONSerializer serializer = new JSONSerializer()
                .transform(new JsonNullValueTransformer(), void.class)
                .transform(new DateTransformer(dateType), Date.class)
                .transform(new BigDecimalFromatTransformer(), BigDecimal.class)
                .exclude("*.class", "*.version", "*.handler", "*.hibernateLazyInitializer");
        if (fields != null && fields.length > 0) {
            serializer.include(fields);
        }
        if (excludeFeilds != null && excludeFeilds.length > 0) {
            serializer.exclude(excludeFeilds);
        }
        return serializer.serialize(collection);
    }


    public static String toJsonArray(Collection<? extends BaseVoModel> collection, String[] fields, String[] excludeFeilds, String className, String parrent) {

        String dateType = "yyyy-MM-dd HH:mm:ss";
        JSONSerializer serializer = new JSONSerializer()
                .transform(new JsonNullValueTransformer(), void.class)
                .transform(new DateTransformer(dateType), Date.class)
                .transform(new BigDecimalFromatTransformer(), BigDecimal.class)
                .exclude("*.class", "*.version", "*.handler", "*.hibernateLazyInitializer");
        if (fields != null && fields.length > 0) {
            serializer.include(fields);
        }
        if (excludeFeilds != null && excludeFeilds.length > 0) {
            serializer.exclude(excludeFeilds);
        }
        return serializer.serialize(collection);
    }

}
