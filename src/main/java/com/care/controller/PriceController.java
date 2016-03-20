package com.care.controller;

import com.care.controller.result.ResponseEntityUtils;
import com.care.controller.result.ResultBean;
import com.care.domain.User;
import com.care.exception.base.CareException;
import com.care.service.PriceService;
import com.care.service.SecurityService;
import org.jsondoc.core.annotation.*;
import org.jsondoc.core.pojo.ApiParamType;
import org.jsondoc.core.pojo.ApiVerb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * Created by nujian on 16/2/24.
 */
@Controller
@RequestMapping(value = "/ws/price")
@Api(name = "价格",description = "价格相关接口")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @Autowired
    private SecurityService securityService;


    @ApiMethod(
            path = "/price/init",
            verb = ApiVerb.POST,
            description = "价格初始化",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/init", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponseObject(sample = "")
    public ResponseEntity<String> init(
            HttpServletRequest request,
            @ApiParam(paramType = ApiParamType.QUERY, name = "price", description = "体检价格(人／次)", required = true)
            @RequestParam(value = "price",required = true) BigDecimal cost,
            @ApiParam(paramType = ApiParamType.QUERY, name = "memo", description = "价格说明", required = false)
            @RequestParam(value = "memo",required = false) String memo
    ) throws CareException {
        User currentUser = securityService.getCurrentLoginUser(request);
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(priceService.add(currentUser,cost,memo)).toJson());
    }
}
