package com.care.controller.result;


import com.care.controller.uiModels.base.BaseVoModel;
import com.care.customlization.flexjson.ApiReturnValueTransformer;
import com.care.customlization.flexjson.JsonNullValueTransformer;
import com.care.domain.base.BaseModel;
import flexjson.JSONSerializer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by nujian on 2015/5/28.
 */
public class ResultBean {
    private static final Logger logger = Logger.getLogger(ResultBean.class);
    private String code = "200";
    private String msg = "";
    private String result = "";
    private String token = "";

    public ResultBean() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ResultBean(int code) {
        this.code = String.valueOf(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String toJson() {
        return new JSONSerializer()
                .transform(new JsonNullValueTransformer(), Void.class)
                .transform(new ApiReturnValueTransformer(), "result")
                .exclude("*.class", "version").serialize(this);
    }

    public static ResultBean wrap(Object returnVal) {
        return ResultBean.wrap(returnVal, null, null);
    }

    public static ResultBean wrap(Object returnVal, String[] includes) {
        return ResultBean.wrap(returnVal, includes, null);
    }

    public static ResultBean wrap(Object returnVal, String[] includes, String[] excludes) {
        ResultBean resultBean = new ResultBean();
        if (returnVal != null) {
            if (returnVal instanceof BaseVoModel){
                resultBean.setResult(((BaseVoModel) returnVal).toJson(includes, excludes));
            } else if (returnVal instanceof BaseModel) {
                resultBean.setResult(((BaseModel) returnVal).toJson(includes, excludes));
            } else if (returnVal instanceof Collection) {
                if(returnVal instanceof List){
                    List list = (List)returnVal;
                    if(CollectionUtils.isNotEmpty(list)){
                        if(list.get(0) instanceof BaseVoModel){
                            Collection<BaseVoModel> collection = (Collection<BaseVoModel>)returnVal;
                            resultBean.setResult(BaseVoModel.toJsonArray(collection, includes, excludes));
                        }else{
                            Collection<BaseModel> collection = (Collection<BaseModel>)returnVal;
                            resultBean.setResult(BaseModel.toJsonArray(collection, includes, excludes));
                        }
                    }else{
                        resultBean.setResult(BaseModel.toJsonArray(list, includes, excludes));
                    }
                }
//                resultBean.setResult(BaseModel.toJsonArray((Collection<BaseModel>) returnVal, includes, excludes));
            } else if (returnVal instanceof Map) {
                JSONSerializer serializer = new JSONSerializer();
                if (includes != null) {
                    serializer.setIncludes(Arrays.asList(includes));
                }
                if (excludes != null) {
                    serializer.setExcludes(Arrays.asList(excludes));
                }
                resultBean.setResult(serializer.deepSerialize(returnVal));
            } else {
                resultBean.setResult(new JSONSerializer().serialize(returnVal));
            }
        }

        return resultBean;
    }


    public static ResultBean wrap(Object returnVal, String[] includes, String[] excludes, String className) {
        ResultBean resultBean = new ResultBean();
        if (returnVal != null) {
            if (returnVal instanceof BaseVoModel){
                resultBean.setResult(((BaseVoModel) returnVal).toJson(includes, excludes));
            } else if (returnVal instanceof BaseModel) {
                resultBean.setResult(((BaseModel) returnVal).toJson(includes, excludes));
            } else if (returnVal instanceof Collection) {
                resultBean.setResult(BaseModel.toJsonArray((Collection<BaseModel>) returnVal, includes, excludes, className));
            } else {
                resultBean.setResult(new JSONSerializer().serialize(returnVal));
            }
        }

        return resultBean;
    }


    //parrent
    public static ResultBean wrap(Object returnVal, String[] includes, String[] excludes, String className, String parrent) {
        ResultBean resultBean = new ResultBean();
        if (returnVal != null) {
            if (returnVal instanceof BaseVoModel){
                resultBean.setResult(((BaseVoModel) returnVal).toJson(includes, excludes));
            } else if (returnVal instanceof BaseModel) {
                if (StringUtils.isNotBlank(className) && StringUtils.isNotBlank(parrent)) {
                    resultBean.setResult(((BaseModel) returnVal).toJson(includes, excludes, className, parrent));
                } else {
                    resultBean.setResult(((BaseModel) returnVal).toJson(includes, excludes));
                }
            } else if (returnVal instanceof Collection) {
                resultBean.setResult(BaseModel.toJsonArray((Collection<BaseModel>) returnVal, includes, excludes, className, parrent));
            } else {
                resultBean.setResult(new JSONSerializer().serialize(returnVal));
            }
        }
        return resultBean;
    }

    private static ResultBean DEFAULT_RESULT = new ResultBean();

    public static final ResultBean UN_AUTHORIZED = new ResultBean(401);


    public static String OK() {
        if (DEFAULT_RESULT == null) {
            DEFAULT_RESULT = new ResultBean();
        }
        return DEFAULT_RESULT.toJson();
    }


    public static ResultBean interalError(Exception e) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode("500");
        resultBean.setMsg(e.getMessage());
        Logger.getRootLogger().error(e);
        return resultBean;
    }
}
