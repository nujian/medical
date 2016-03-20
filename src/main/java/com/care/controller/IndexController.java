package com.care.controller;

import com.care.controller.result.ResponseEntityUtils;
import com.care.controller.result.ResultBean;
import com.care.controller.uiModels.IndexVo;
import com.care.domain.User;
import com.care.exception.base.CareException;
import com.care.service.IndexService;
import com.care.service.SecurityService;
import org.jsondoc.core.annotation.*;
import org.jsondoc.core.pojo.ApiVerb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by nujian on 16/2/26.
 */
@Controller
@RequestMapping(value = "/ws/index")
@Api(name = "首页",description = "首页相关接口")
public class IndexController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private IndexService indexService;

    @ApiMethod(
            path = "/index/",
            verb = ApiVerb.GET,
            description = "首页",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponseObject(sample = "")
    public ResponseEntity<String> match(HttpServletRequest request) throws CareException {
        User current = securityService.getCurrentLoginUser(request);
        IndexVo index = indexService.wrapIndex(current);
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(index,new String[]{"user.mobile"}).toJson());
    }

}
