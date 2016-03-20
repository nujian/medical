package com.care.domain.base;

import com.care.Constants;
import com.care.customlization.DbNamingStrategy;
import com.care.customlization.flexjson.BigDecimalFromatTransformer;
import com.care.customlization.flexjson.JsonNullValueTransformer;
import com.care.domain.User;
import flexjson.JSON;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Index;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;

/**
 * Created by nujian on 16/2/17.
 */
@Configurable
@MappedSuperclass
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "version", "currentUser", "isDeleted"})
public abstract class BaseModel {

    public static final DbNamingStrategy dbNamingStrategy = new DbNamingStrategy();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @JSON(include = false)
    @Version
    @Column(name = "version")
    private Integer version;

//    @org.boon.json.annotations.JsonIgnore
//    @JsonIgnore
//    @Transient
//    @JSON(include = false)
//    private String webBasePath = Constants.WEB_BASE_PATH;

    @org.boon.json.annotations.JsonIgnore
    @JsonIgnore
    @JSON(include = false)
    @NotNull
    @Basic(fetch = FetchType.LAZY)
    @DateTimeFormat(style = "MM")
    @Temporal(TemporalType.TIMESTAMP)
    @Index(name = "create_time")
    protected Date createTime;

    @org.boon.json.annotations.JsonIgnore
    @JsonIgnore
    @JSON(include = false)
    @NotNull
    @DateTimeFormat(style = "MM")
    @Temporal(TemporalType.TIMESTAMP)
    @Index(name = "update_time")
    protected Date updateTime;

    @org.boon.json.annotations.JsonIgnore
    @JsonIgnore
    @JSON(include = false)
    @NotNull
    protected boolean isDeleted = false;

    @org.boon.json.annotations.JsonIgnore
    @JsonIgnore
    @JSON(include = false)
    public User getCurrentUser() {
        try {
            User cachedUser = Constants.CURRENT_USER_THREAD_CACHE.get();
            if (cachedUser != null) {
                return cachedUser;
            } else {
                org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal != null) {
                    User curr = User.findUserByMobile(principal.getUsername());
                    if (curr != null) {
                        Constants.CURRENT_USER_THREAD_CACHE.set(curr);
                    }
                    return curr;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 标示在后台管理页面该对象列表里checkbox是否选中
     */
    @com.fasterxml.jackson.annotation.JsonIgnore
    @JsonIgnore
    @JSON(include = false)
    @Transient
    private boolean selected = false;

//    @Transient
//    protected Map<String,Object> expands;// = new HashMap<String,Object>();
//
//    public Map<String, Object> getExpands() {
//        return expands;
//    }
//
//    public void setExpands(Map<String, Object> expands) {
//        this.expands = expands;
//    }



    public String getTableName(Class<? extends BaseModel> clazz){
        try {
            return clazz.getAnnotation((Table.class)).name();
        }catch (Exception e){
            return dbNamingStrategy.classToTableName(clazz.getSimpleName());
        }
    }


    public static List<BaseModel> findBaseModelEntries(int firstResult, int maxResults, Class clazz) {
        return entityManager().createQuery("SELECT o FROM " + clazz.getSimpleName() + " o", clazz).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static BaseModel findEntity(Integer id, Class<? extends BaseModel> entityClass) {
        return entityManager().find(entityClass, id);
    }

    @PrePersist
    public void prePersist() {
        if (createTime == null) createTime = new Date();
        if (updateTime == null) updateTime = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        updateTime = new Date();
    }

    @PreRemove
    public void preDelete() {
        updateTime = new Date();
    }

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
                .exclude("*.class", "*.handler", "*.hibernateLazyInitializer");
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
        if(StringUtils.isNotBlank(parrent)){
            dateType = parrent;
        }
        JSONSerializer serializer = new JSONSerializer()
                .transform(new JsonNullValueTransformer(), void.class)
                .transform(new DateTransformer(dateType), Date.class)
                .transform(new BigDecimalFromatTransformer(), BigDecimal.class)
                .exclude("*.class",  "*.handler", "*.hibernateLazyInitializer");
        if (fields != null && fields.length > 0) {
            serializer.include(fields);
        }
        if (excludeFeilds != null && excludeFeilds.length > 0) {
            serializer.exclude(excludeFeilds);
        }
        return serializer.serialize(this);
    }


    public static String toJsonArray(Collection<? extends BaseModel> collection) {
        return toJsonArray(collection, null, null);
    }

    public static String toJsonArray(Collection<? extends BaseModel> collection, String[] fields) {
        return toJsonArray(collection, fields, null);
    }

    public static String toJsonArray(Collection<? extends BaseModel> collection, String[] fields, String[] excludeFeilds) {
        JSONSerializer serializer = new JSONSerializer()
                .transform(new JsonNullValueTransformer(), void.class)
                .transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"), Date.class)
                .transform(new BigDecimalFromatTransformer(), BigDecimal.class)
                .exclude("*.class",  "*.handler", "*.hibernateLazyInitializer");
        if (fields != null && fields.length > 0) {
            serializer.include(fields);
        }
        if (excludeFeilds != null && excludeFeilds.length > 0) {
            serializer.exclude(excludeFeilds);
        }
        return serializer.serialize(collection);
    }


    public static String toJsonArray(Collection<? extends BaseModel> collection, String[] fields, String[] excludeFeilds, String className) {

        String dateType = "yyyy-MM-dd HH:mm:ss";
        JSONSerializer serializer = new JSONSerializer()
                .transform(new JsonNullValueTransformer(), void.class)
                .transform(new DateTransformer(dateType), Date.class)
                .transform(new BigDecimalFromatTransformer(), BigDecimal.class)
                .exclude("*.class", "*.handler", "*.hibernateLazyInitializer");
        if (fields != null && fields.length > 0) {
            serializer.include(fields);
        }
        if (excludeFeilds != null && excludeFeilds.length > 0) {
            serializer.exclude(excludeFeilds);
        }
        return serializer.serialize(collection);
    }


    public static String toJsonArray(Collection<? extends BaseModel> collection, String[] fields, String[] excludeFeilds, String className, String parrent) {

        String dateType = "yyyy-MM-dd HH:mm:ss";
        JSONSerializer serializer = new JSONSerializer()
                .transform(new JsonNullValueTransformer(), void.class)
                .transform(new DateTransformer(dateType), Date.class)
                .transform(new BigDecimalFromatTransformer(), BigDecimal.class)
                .exclude("*.class", "*.handler", "*.hibernateLazyInitializer");
        if (fields != null && fields.length > 0) {
            serializer.include(fields);
        }
        if (excludeFeilds != null && excludeFeilds.length > 0) {
            serializer.exclude(excludeFeilds);
        }
        return serializer.serialize(collection);
    }


    public static <T extends BaseModel> T fromJson(String json, Class<? extends BaseModel> clazz) {
        return new JSONDeserializer<T>()
                .use(null, clazz).use(Date.class, new DateTransformer("yyyy-MM-dd")).deserialize(json);
    }

    public static List fromJsonArray(String json, Class<? extends BaseModel> clazz) {
        return new JSONDeserializer<List>()
                .use("values", clazz).deserialize(json);
    }


    public static BaseModel getRandomEntity(Class<? extends BaseModel> clazz) {
        Long count = entityManager().createQuery("select count(o) from " + clazz.getSimpleName() + " o", Long.class).getSingleResult();
        int r = new SecureRandom().nextInt(count.intValue());
        return entityManager().createQuery("select o from " + clazz.getSimpleName() + " o", clazz).setFirstResult(r).setMaxResults(1).getSingleResult();
    }

    public static BaseModel getRandomEntity(Class<? extends BaseModel> clazz, int upperbound) {
        Long count = entityManager().createQuery("select count(o) from " + clazz.getSimpleName() + " o", Long.class).getSingleResult();
        upperbound = upperbound >= count.intValue() ? count.intValue() : upperbound;
        int r = new SecureRandom().nextInt(upperbound);
        return entityManager().createQuery("select o from " + clazz.getSimpleName() + " o", clazz).setFirstResult(r).setMaxResults(1).getSingleResult();
    }



    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @PersistenceContext
    protected transient EntityManager entityManager;

    public static final EntityManager entityManager() {
        EntityManager em = new BaseModel() {
        }.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static BaseModel findBaseModel(Integer id) {
        if (id == null) return null;
        return entityManager().find(BaseModel.class, id);
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            BaseModel attached = BaseModel.findBaseModel(this.id);
            this.entityManager.remove(attached);
        }
    }

    @Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

    @Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

    @Transactional
    public BaseModel merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BaseModel merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}