package com.care.service;

import com.care.controller.uiModels.UserProfileVo;
import com.care.domain.*;
import com.care.domain.embeddables.Location;
import com.care.domain.enums.ManageType;
import com.care.domain.enums.SmsType;
import com.care.exception.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by nujian on 16/2/18.
 */
public interface UserService {

    /**
     *
     * @param user
     * @return
     * @throws BadVerifyCodeException
     */
    @Deprecated
    User reg(User user) throws BadVerifyCodeException;

    User edit(User current, User user);

    NurseInfo attest(User current, NurseInfo nurse);

    void initUserWallet(User user);

    User load(Integer userId);

    User findUserByMobile(String mobile);

    void login(HttpServletRequest request, HttpServletResponse response, String mobile, String password, Location location) throws BadVerifyCodeException;

    SmsVerifyLog requestSms(String mobile, SmsType type);

    UserAddress manageAddress(User current, UserAddress userAddress, ManageType type) throws NoPermissionsException, EntityNotFoundException;

    List<UserAddress> loadAddress(User current, Integer page, Integer count);

    UserCashLog cash(User user, UserCashLog log, String walletPassword) throws LackOfBalanceException, EntityNotFoundException, BadWalletPasswordException;

    UserWalletBankInfo manageBankInfo(User user, UserWalletBankInfo bankInfo, ManageType type, String password) throws EntityNotFoundException, BadWalletPasswordException;

    List<UserWalletBankInfo> loadUserBankInfos(User user, Integer page, Integer count);

    List<UserWalletChangeLog> walletChangeLogs(User user, Integer page, Integer count);

    boolean caretWalletPassword(User user, String password, String verifyCode) throws EntityNotFoundException, BadVerifyCodeException;

    boolean reSetWalletPassword(User user, String password, String verifCode) throws EntityNotFoundException, BadVerifyCodeException;

    List<Comment> getUserComments(Integer userId, Integer page, Integer count);

    /**
     * 用户主页
     * @param id
     * @return
     */
    UserProfileVo wrapUserProfile(Integer id);

    /**
     * 后台管理相关接口
     */
    List<User> findUsers(Integer page, Integer count);

    List<UserCashLog> findUnhandelUserCashLog(Integer page, Integer count);
}
