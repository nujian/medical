package com.care.customlization.jsondoc;

import com.care.domain.base.BaseModel;
import com.care.service.JSONdocService;
import org.jsondoc.core.pojo.ApiDoc;
import org.jsondoc.core.pojo.ApiMethodDoc;
import org.jsondoc.core.pojo.ApiResponseObjectDoc;
import org.jsondoc.core.pojo.JSONDoc;
import org.jsondoc.springmvc.controller.JSONDocController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by nujian on 2015/5/28.
 */
@RequestMapping(value = "/caredoc")
public class CareDocController extends JSONDocController {

    @Autowired
    private JSONdocService jsondocService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public
    @ResponseBody
    JSONDoc getApi() {
        JSONDoc doc = super.getApi();
        try {
            for (ApiDoc apiDoc : doc.getApis()) {
                for (ApiMethodDoc methodDoc : apiDoc.getMethods()) {
                    ApiResponseObjectDoc responseObjectDoc = methodDoc.getResponse();
                    if (responseObjectDoc != null && responseObjectDoc.getObjectClazz() != null) {
                        String[] includes = responseObjectDoc.getIncludes();
                        String[] excludes = responseObjectDoc.getExcludes();
                        if (responseObjectDoc.getObjectClazz().getSuperclass() == BaseModel.class ) {
                            try {
                                if (responseObjectDoc.getMultiple().equalsIgnoreCase("true")) {
                                    responseObjectDoc.setSample(jsondocService.generateSample(responseObjectDoc.getObjectClazz(), true, includes, excludes));
                                } else {
                                    responseObjectDoc.setSample(jsondocService.generateSample(responseObjectDoc.getObjectClazz(), false, includes, excludes));
                                }
                            } catch (Exception e) {
//                                e.printStackTrace();
//                                System.out.println(methodDoc.getPath());
                            }

                        } else {
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }
}
