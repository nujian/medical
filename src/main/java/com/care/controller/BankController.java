package com.care.controller;

import com.care.controller.result.ResponseEntityUtils;
import com.care.controller.result.ResultBean;
import com.care.exception.base.CareException;
import com.care.service.BankService;
import org.jsondoc.core.annotation.*;
import org.jsondoc.core.pojo.ApiVerb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by nujian on 16/3/6.
 */
@Controller
@RequestMapping(value = "/ws/bank")
@Api(name = "银行",description = "银行相关接口")
public class BankController {

    @Autowired
    private BankService bankService;

    @ApiMethod(
            path = "/bank/list",
            verb = ApiVerb.GET,
            description = "银行列表",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponseObject(sample = "")
    public ResponseEntity<String> match() throws CareException {
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(bankService.getBankList()).toJson());
    }

}
