package com.care.controller;

import com.care.controller.result.ResponseEntityUtils;
import com.care.controller.result.ResultBean;
import com.care.domain.*;
import com.care.domain.embeddables.Location;
import com.care.domain.enums.ManageType;
import com.care.domain.enums.SmsType;
import com.care.exception.BadVerifyCodeException;
import com.care.exception.base.CareException;
import com.care.service.SecurityService;
import com.care.service.UserService;
import com.care.utils.PageCountUtils;
import org.jsondoc.core.annotation.*;
import org.jsondoc.core.pojo.ApiParamType;
import org.jsondoc.core.pojo.ApiVerb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by nujian on 16/2/17.
 */
@RequestMapping("/ws/users")
@Controller
@Api(name = "用户", description = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;


    @ApiMethod(
            path = "/users/login",
            verb = ApiVerb.POST,
            description = "登陆",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ApiResponseObject(sample = "")
    void login(HttpServletRequest request,HttpServletResponse response,
               @ApiParam(paramType = ApiParamType.QUERY,name = "mobile",description = "手机号",required = true)
               @RequestParam(value = "mobile",required = false) String mobile,
               @ApiParam(paramType = ApiParamType.QUERY,name = "password",description = "密码",required = true)
               @RequestParam(value = "password",required = true)String password,
               Location location,
               @ApiParam(paramType = ApiParamType.QUERY,name = "longitude",description = "经度",required = false)
               @RequestParam(value = "longitude",required = false) String longitude,
               @ApiParam(paramType = ApiParamType.QUERY,name = "latitude",description = "纬度",required = false)
               @RequestParam(value = "latitude",required = false) String latitude) throws BadVerifyCodeException {
        userService.login(request, response, mobile, password, location);
    }

    @ApiMethod(
            path = "/users/edit",
            verb = ApiVerb.POST,
            description = "编辑用户信息",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ApiResponseObject(sample = "")
    ResponseEntity<String> edit(
            HttpServletRequest request,
            User user,
            @ApiParam(paramType = ApiParamType.QUERY,name = "username",description = "用户名",required = false)
            @RequestParam(value = "username",required = false)String useranem,
            @ApiParam(paramType = ApiParamType.QUERY,name = "personalizedSign",description = "个性签名",required = false)
            @RequestParam(value = "personalizedSign",required = false)String personalizedSign,
            @ApiParam(paramType = ApiParamType.QUERY,name = "gender",description = "性别",allowedvalues = {"FEMALE","MALE"},required = false)
            @RequestParam(value = "gender",required = false)String gender,
            @ApiParam(paramType = ApiParamType.QUERY,name = "birthDate",description = "生日",required = false)
            @RequestParam(value = "birthDate",required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")String birthDate,
            @ApiParam(paramType = ApiParamType.QUERY,name = "portrait.file",description = "用户头像",required = false)
            @RequestParam(value = "portrait.file",required = false) MultipartFile portrait,
            @ApiParam(paramType = ApiParamType.QUERY,name = "defaultAddress.id",description = "默认地址",required = false)
            @RequestParam(value = "defaultAddress.id",required = false)Integer defaultAddress,
            @ApiParam(paramType = ApiParamType.QUERY,name = "nurseInfo.workDate",description = "加入工作时间",required = false)
            @RequestParam(value = "nurseInfo.workDate",required = false)@DateTimeFormat(pattern = "yyyy-MM-dd") String workDate,
            @ApiParam(paramType = ApiParamType.QUERY,name = "nurseInfo.workPhoto.file",description = "护士工作照",required = false)
            @RequestParam(value = "nurseInfo.workPhoto.file",required = false) MultipartFile workPhoto,
            @ApiParam(paramType = ApiParamType.QUERY,name = "nurseInfo.certification.file",description = "护士资格证图片",required = false)
            @RequestParam(value = "nurseInfo.certification.file",required = false) MultipartFile certification,
            @ApiParam(paramType = ApiParamType.QUERY,name = "nurseInfo.hospital.name",description = "护士所在医院名称",required = false)
            @RequestParam(value = "nurseInfo.hospital.name",required = false) String hospitalName,
            @ApiParam(paramType = ApiParamType.QUERY,name = "nurseInfo.hospital.level",description = "护士所在医院级别",required = false)
            @RequestParam(value = "nurseInfo.hospital.level",required = false) String hospitalLevel,
            @ApiParam(paramType = ApiParamType.QUERY,name = "nurseInfo.hospital.address",description = "护士所在医院地址",required = false)
            @RequestParam(value = "nurseInfo.hospital.address",required = false) String hospitalAddress) throws CareException {
        User current = securityService.getCurrentLoginUser(request);
        User target = userService.edit(current,user);
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(target,new String[]{"mobile","defaultAddress"},null,"User","yyyy-MM-dd").toJson());
    }


    @ApiMethod(
            path = "/users/profile/{id}",
            verb = ApiVerb.GET,
            description = "个人主页",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @RequestMapping(value = "/profile/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ApiResponseObject(sample = "")
    ResponseEntity<String> profile(@PathVariable("id") Integer id) throws CareException {
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(userService.wrapUserProfile(id), null,null,"User","yyyy-MM-dd").toJson());
    }

    @ApiMethod(
            path = "/users/nurse/attest",
            verb = ApiVerb.POST,
            description = "护士认证",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/nurse/attest", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ApiResponseObject(sample = "")
    ResponseEntity<String> nurseAttest(
            HttpServletRequest request,
            NurseInfo nurse,
            @ApiParam(paramType = ApiParamType.QUERY,name = "workDate",description = "加入工作时间",required = false)
            @RequestParam(value = "workDate",required = false)@DateTimeFormat(pattern = "yyyy-MM-dd") String workDate,
            @ApiParam(paramType = ApiParamType.QUERY,name = "title",description = "护士职称",required = false)
            @RequestParam(value = "title",required = false) String title,
            @ApiParam(paramType = ApiParamType.QUERY,name = "workPhoto.file",description = "护士工作照",required = false)
            @RequestParam(value = "workPhoto.file",required = false) MultipartFile workPhoto,
            @ApiParam(paramType = ApiParamType.QUERY,name = "certification.file",description = "护士资格证图片",required = false)
            @RequestParam(value = "certification.file",required = false) MultipartFile certification,
            @ApiParam(paramType = ApiParamType.QUERY,name = "hospital.name",description = "护士所在医院名称",required = false)
            @RequestParam(value = "hospital.name",required = false) String hospitalName,
            @ApiParam(paramType = ApiParamType.QUERY,name = "hospital.level",description = "护士所在医院级别",required = false)
            @RequestParam(value = "hospital.level",required = false) String hospitalLevel,
            @ApiParam(paramType = ApiParamType.QUERY,name = "hospital.address",description = "护士所在医院地址",required = false)
            @RequestParam(value = "hospital.address",required = false) String hospitalAddress) throws CareException {
        User current = securityService.getCurrentLoginUser(request);
        NurseInfo result = userService.attest(current, nurse);
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(result).toJson());
    }

    @ApiMethod(
            path = "/users/requestSms",
            verb = ApiVerb.POST,
            description = "获取验证码",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @RequestMapping(value = "/requestSms", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ApiResponseObject(sample = "")
    ResponseEntity<String> requestSms(@ApiParam(paramType = ApiParamType.QUERY,name = "mobile",description = "手机号",required = true)
               @RequestParam(value = "mobile",required = false) String mobile,
               @ApiParam(paramType = ApiParamType.QUERY,name = "type",description = "验证码类型",required = true,allowedvalues = {"USER_REG","USER_LOGIN","FORGET_PASSWORD","CREATE_WALLET_PASSWORD","FORGET_WALLET_PASSWORD"})
               @RequestParam(value = "type",required = true)SmsType type) {
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(userService.requestSms(mobile, type).getVerifyCode()).toJson());
    }

    @ApiMethod(
            path = "/users/mine",
            verb = ApiVerb.GET,
            description = "当前用户信息",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/mine", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponseObject(sample = "")
    public ResponseEntity<String> mine(HttpServletRequest request) throws CareException {
        User current = securityService.getCurrentLoginUser(request);
        String[] inclends = new String[]{"wallet","mobile"};
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(current,inclends).toJson());
    }

    @ApiMethod(
            path = "/users/wallet/index",
            verb = ApiVerb.GET,
            description = "钱包变动记录",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/wallet/index", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponseObject(sample = "")
    public ResponseEntity<String> walletChangeLogs(HttpServletRequest request) throws CareException {
        User current = securityService.getCurrentLoginUser(request);
        UserWallet wallet = current.getWallet();
        if(wallet == null){
            userService.initUserWallet(current);
        }
        wallet.setWalletChangeLogs(userService.walletChangeLogs(current,1,10));
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(wallet,new String[]{"walletChangeLogs"},new String[]{"user.*"}).toJson());
    }

    @ApiMethod(
            path = "/users/wallet/password/manage",
            verb = ApiVerb.POST,
            description = "管理钱包密码",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/wallet/password/manage", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponseObject(sample = "")
    public ResponseEntity<String> manageWalletPassword(
            HttpServletRequest request,
            @ApiParam(paramType = ApiParamType.QUERY, name = "password", description = "钱包密码", required = true)
            @RequestParam(value = "password",required = true) String password,
            @ApiParam(paramType = ApiParamType.QUERY, name = "verifyCode", description = "验证码", required = true)
            @RequestParam(value = "verifyCode",required = true) String verifyCode,
            @ApiParam(paramType = ApiParamType.QUERY, name = "type", description = "管理类型", required = true ,allowedvalues = {"CREATE","FORGET"})
            @RequestParam(value = "type",required = true) ManageType type
            ) throws CareException {
        User current = securityService.getCurrentLoginUser(request);
        boolean success = false;
        switch (type){
            case CREATE:
                success = userService.caretWalletPassword(current,password,verifyCode);
                break;
            case FORGET:
                success = userService.reSetWalletPassword(current,password,verifyCode);
                break;
            default:
                break;
        }
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(success).toJson());
    }

    @ApiMethod(
            path = "/users/wallet/changelogs",
            verb = ApiVerb.GET,
            description = "钱包变动记录",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/wallet/changelogs", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponseObject(sample = "")
    public ResponseEntity<String> walletChangeLogs(
            HttpServletRequest request,
            @ApiParam(paramType = ApiParamType.QUERY, name = "page", description = "分页页码", required = false) @RequestParam(value = "page",required = false) Integer page,
            @ApiParam(paramType = ApiParamType.QUERY, name = "count", description = "单页返回的记录条数", required = false)  @RequestParam(value = "count",required = false) Integer count
    ) throws CareException {
        User current = securityService.getCurrentLoginUser(request);
        List<UserWalletChangeLog> logs = userService.walletChangeLogs(current,page,count);
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(logs,new String[]{"createTime"}).toJson());
    }

    @ApiMethod(
            path = "/users/address/manage",
            verb = ApiVerb.POST,
            description = "管理地址",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/address/manage", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ApiResponseObject(sample = "")
    ResponseEntity<String> manageAddress(
            HttpServletRequest request,
            UserAddress userAddress,
            @ApiParam(paramType = ApiParamType.QUERY,name = "id",description = "地址id",required = false)
            @RequestParam(value = "id",required = false) String id,
            @ApiParam(paramType = ApiParamType.QUERY,name = "location.longitude",description = "经度",required = false)
            @RequestParam(value = "location.longitude",required = false) String longitude,
            @ApiParam(paramType = ApiParamType.QUERY,name = "location.latitude",description = "纬度",required = false)
            @RequestParam(value = "location.latitude",required = false) String latitude,
            @ApiParam(paramType = ApiParamType.QUERY,name = "gender",description = "性别",required = false,allowedvalues = {"FEMALE", "MALE"})
            @RequestParam(value = "gender",required = false) String gender,
            @ApiParam(paramType = ApiParamType.QUERY,name = "addressBase",description = "基本地址",required = false)
            @RequestParam(value = "addressBase",required = false) String addressBase,
            @ApiParam(paramType = ApiParamType.QUERY,name = "addressDetail",description = "地址详情",required = false)
            @RequestParam(value = "addressDetail",required = false) String addressDetail,
            @ApiParam(paramType = ApiParamType.QUERY,name = "contactName",description = "基本地址",required = false)
            @RequestParam(value = "contactName",required = false) String contactName,
            @ApiParam(paramType = ApiParamType.QUERY,name = "contactMobile",description = "基本地址",required = false)
            @RequestParam(value = "contactMobile",required = false) String contactMobile,
            @ApiParam(paramType = ApiParamType.QUERY,name = "type",description = "管理类型",required = true,allowedvalues = {"CREATE","DELETE","UPDATE"})
            @RequestParam(value = "type",required = false) ManageType type
    ) throws CareException {
        User current = securityService.getCurrentLoginUser(request);
        UserAddress result = userService.manageAddress(current, userAddress, type);
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(result).toJson());
    }

    @ApiMethod(
            path = "/users/address/load",
            verb = ApiVerb.GET,
            description = "加载用户地址",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/address/load", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ApiResponseObject(sample = "")
    ResponseEntity<String> loadAddress(
            HttpServletRequest request,
            @ApiParam(paramType = ApiParamType.QUERY, name = "page", description = "分页页码", required = false) @RequestParam(value = "page",required = false) Integer page,
            @ApiParam(paramType = ApiParamType.QUERY, name = "count", description = "单页返回的记录条数", required = false)  @RequestParam(value = "count",required = false) Integer count
            ) throws CareException {
        User current = securityService.getCurrentLoginUser(request);
        List<UserAddress> addresses = userService.loadAddress(current, PageCountUtils.processPage(page), PageCountUtils.processCount(count));
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(addresses).toJson());
    }

    @ApiMethod(
            path = "/users/cash",
            verb = ApiVerb.POST,
            description = "用户提现",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/cash", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ApiResponseObject(sample = "")
    ResponseEntity<String> cash(
            HttpServletRequest request,
            UserCashLog cashLog,
            @ApiParam(paramType = ApiParamType.QUERY, name = "cost", description = "提现金额", required = true)
            @RequestParam(value = "cost",required = true) BigDecimal cost,
            @ApiParam(paramType = ApiParamType.QUERY, name = "memo", description = "提现说明", required = false)
            @RequestParam(value = "memo",required = false) String memo,
            @ApiParam(paramType = ApiParamType.QUERY, name = "bankInfo.id", description = "", required = true)
            @RequestParam(value = "bankInfo.id",required = false) String bank,
            @ApiParam(paramType = ApiParamType.QUERY, name = "walletPassword", description = "钱包密码", required = true)
            @RequestParam(value = "walletPassword",required = false) String walletPassword
    ) throws CareException {
        User current = securityService.getCurrentLoginUser(request);
        UserCashLog log = userService.cash(current, cashLog, walletPassword);
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(log).toJson());
    }

    @ApiMethod(
            path = "/users/bankinfo/manage",
            verb = ApiVerb.POST,
            description = "管理用户银行卡信息",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/bankinfo/manage", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ApiResponseObject(sample = "")
    ResponseEntity<String> manageBankInfo(
            HttpServletRequest request,
            UserWalletBankInfo bankInfo,
            @ApiParam(paramType = ApiParamType.QUERY, name = "id", description = "用户银行信息ID", required = false)
            @RequestParam(value = "id",required = false) Integer id,
            @ApiParam(paramType = ApiParamType.QUERY, name = "cardNum", description = "银行卡号", required = true)
            @RequestParam(value = "cardNum",required = true) String cost,
            @ApiParam(paramType = ApiParamType.QUERY, name = "bank.id", description = "银行Id", required = false)
            @RequestParam(value = "bank.id",required = false) String bankId,
            @ApiParam(paramType = ApiParamType.QUERY, name = "bankName", description = "银行名称", required = false)
            @RequestParam(value = "bankName",required = false) String bankName,
            @ApiParam(paramType = ApiParamType.QUERY,name = "type",description = "管理类型",required = true,allowedvalues = {"CREATE","DELETE","UPDATE"})
            @RequestParam(value = "type",required = false) ManageType type,
            @ApiParam(paramType = ApiParamType.QUERY,name = "password",description = "钱包密码",required = true)
            @RequestParam(value = "password",required = false) String password
    ) throws CareException {
        User current = securityService.getCurrentLoginUser(request);
        UserWalletBankInfo result = userService.manageBankInfo(current, bankInfo, type, password);
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(result).toJson());
    }

    @ApiMethod(
            path = "/users/bankinfo/list",
            verb = ApiVerb.GET,
            description = "加载用户绑定银行卡信息",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/bankinfo/list", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ApiResponseObject(sample = "")
    ResponseEntity<String> loadUserBankInfos(
            HttpServletRequest request,
            @ApiParam(paramType = ApiParamType.QUERY, name = "page", description = "分页页码", required = false) @RequestParam(value = "page",required = false) Integer page,
            @ApiParam(paramType = ApiParamType.QUERY, name = "count", description = "单页返回的记录条数", required = false)  @RequestParam(value = "count",required = false) Integer count
    ) throws CareException {
        User current = securityService.getCurrentLoginUser(request);
        List<UserWalletBankInfo> bankInfos = userService.loadUserBankInfos(current, PageCountUtils.processPage(page), PageCountUtils.processCount(count));
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(bankInfos).toJson());
    }


    @ApiMethod(
            path = "/users/comment/list",
            verb = ApiVerb.GET,
            description = "根据用Id加载评论",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @RequestMapping(value = "/comment/list", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ApiResponseObject(sample = "")
    ResponseEntity<String> getUserCommets(
            HttpServletRequest request,
            @ApiParam(paramType = ApiParamType.QUERY, name = "userId", description = "用户Id", required = true) @RequestParam(value = "userId",required = true) Integer userId,
            @ApiParam(paramType = ApiParamType.QUERY, name = "page", description = "分页页码", required = false) @RequestParam(value = "page",required = false) Integer page,
            @ApiParam(paramType = ApiParamType.QUERY, name = "count", description = "单页返回的记录条数", required = false)  @RequestParam(value = "count",required = false) Integer count
    ) throws CareException {
        List<Comment> comments = userService.getUserComments(userId,PageCountUtils.processPage(page), PageCountUtils.processCount(count));
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(comments, null,new String[]{"*.order","*.nurseInfo"},"User","yyyy-MM-dd").toJson());
    }


}
