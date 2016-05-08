package com.care.service.impl;

import com.care.Constants;
import com.care.controller.result.ResultBean;
import com.care.controller.uiModels.UserProfileVo;
import com.care.domain.*;
import com.care.domain.embeddables.Location;
import com.care.domain.enums.*;
import com.care.exception.*;
import com.care.service.PictureService;
import com.care.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by nujian on 16/2/18.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PictureService pictureService;

    @Override
    public User findUser(Integer userId) {
        return User.entityManager().find(User.class,userId);
    }

    @Override
    @Transactional
    public User reg(User user) throws BadVerifyCodeException {
        User target = null;
        User last = findUserByMobile(user.getMobile());
        if(last != null){
            target = last;
        }else {

            SmsVerifyLog log = SmsVerifyLog.getLastUnVerifyLogByMobileAndType(user.getMobile(), SmsType.USER_REG);
            if(log != null && log.getVerifyCode().equals(user.getVerifyCode())){
                processSmsVerifyLog(log);
                target = new User();
                target.setUserType(user.getUserType() == null ? UserType.USER : user.getUserType());
                target.setUsername(user.getUsername());
                target.setMobile(user.getMobile());
                target.setGender(user.getGender() == null ? Gender.FEMALE : user.getGender());
                target.setBirthDate(user.getBirthDate());
                target.setPassword(DigestUtils.sha256Hex(user.getVerifyCode()));
                target.setPersonalizedSign(StringUtils.isBlank(user.getPersonalizedSign())?null:user.getPersonalizedSign());
                target.persist();
                //todo 用户头像，护士证书处理
                target = processPortrait(target, user);
                if(user.getUserType().equals(UserType.NURSE)){
                    NurseInfo nurseInfo = user.getNurseInfo();
                    if(nurseInfo != null){
                        processNurseInfo(target,nurseInfo);
                    }
                }
                initUserWallet(target);
            }else {
                throw new BadVerifyCodeException();
            }
        }
        return target;
    }

    @Transactional
    public User edit(User current, User user){
        boolean changed = false;
        if(StringUtils.isNotBlank(user.getPersonalizedSign())){
            current.setPersonalizedSign(user.getPersonalizedSign());
            changed = true;
        }
        if(StringUtils.isNotBlank(user.getUsername())){
            current.setUsername(user.getUsername());
            changed = true;
        }
        if(user.getPortrait() != null && user.getPortrait().getFile() != null){
            current = processPortrait(current,user);
            changed = true;
        }
        if(user.getBirthDate() != null){
            current.setBirthDate(user.getBirthDate());
            changed = true;
        }
        if(user.getGender() != null){
            current.setGender(user.getGender());
            changed = true;
        }
        if(user.getDefaultAddress() != null){
            current.setDefaultAddress(settingDefaultAddress(current,user.getDefaultAddress()));
        }
        if(user.getNurseInfo() != null && current.getUserType().equals(UserType.NURSE)){
            NurseInfo nurse = current.getNurseInfo();
            NurseInfo sourse = user.getNurseInfo();
            if(sourse.getHospital()!=null){
                Hospital hospital = nurse.getHospital();
                Hospital hospitalSourse = sourse.getHospital();
                if(hospital == null){
                    hospital = new Hospital();
                }
                if(StringUtils.isNotBlank(hospitalSourse.getAddress())){
                    hospital.setAddress(hospitalSourse.getAddress());
                    changed = true;
                }
                if(StringUtils.isNotBlank(hospitalSourse.getName())){
                    hospital.setName(hospitalSourse.getName());
                    changed = true;
                }
                if(StringUtils.isNotBlank(hospitalSourse.getLevel())){
                    hospital.setLevel(hospitalSourse.getLevel());
                    changed = true;
                }
                nurse.setHospital(hospital);
            }
        }
        if(changed){
            current.merge();
        }
        current.setDefaultAddress(UserAddress.findUserDefaultAddress(current));
        return current;
    }

    private UserAddress settingDefaultAddress(User current, UserAddress address) {
        UserAddress defaultAddress = UserAddress.findUserDefaultAddress(current);
        if(defaultAddress != null){
            defaultAddress.setIsDefault(false);
            defaultAddress.merge();
        }
        UserAddress targetAddress = UserAddress.find(address.getId());
        if(targetAddress != null){
            targetAddress.setIsDefault(true);
            targetAddress.merge();
        }
        return targetAddress;
    }

    @Transactional
    public NurseInfo attest(User current, NurseInfo nurse){
        NurseInfo nurseInfo = NurseInfo.findNurseByUser(current);
        if(nurseInfo == null && (!current.getUserType().equals(UserType.NURSE))){
            current.setUserType(UserType.NURSE);
            nurseInfo = processNurseInfo(current,nurse);
        }
        return nurseInfo;
    }

    private void processSmsVerifyLog(SmsVerifyLog log) {
        log.setIsVerified(true);
        log.setVerifiedTime(new Date());
        log.merge();
    }

    private NurseInfo processNurseInfo(User target, NurseInfo nurseInfo) {
        NurseInfo nurse = new NurseInfo();
        Hospital resource = nurseInfo.getHospital();
        Hospital hospital = null;
        if(resource != null){
            hospital = new Hospital();
            hospital.setAddress(resource.getAddress());
            hospital.setLevel(resource.getLevel());
            hospital.setName(resource.getName());
            hospital.persist();
        }
        if(hospital != null){
            nurse.setHospital(hospital);
        }
        nurse.setTitle(nurseInfo.getTitle());
        nurse.setWorkDate(nurseInfo.getWorkDate());
        nurse.setUser(target);
        nurse.persist();

        Picture workPhoto = nurseInfo.getWorkPhoto();
        if(workPhoto !=null && workPhoto.getFile() != null){
            try{
                nurse.setWorkPhoto(pictureService.associate(nurse,workPhoto.getFile().getBytes(),PictureType.NURSE_WORK_PHOTO));
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        Picture certification = nurseInfo.getCertification();
        if(certification != null && certification.getFile() != null){
            try{
                nurse.setCertification(pictureService.associate(nurse,certification.getFile().getBytes(),PictureType.NURSE_CERTIFICATION));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return nurse;
    }

    private User processPortrait(User target, User user) {
        Picture image = user.getPortrait();
        if(image != null && image.getFile() != null){
            try {
                if(target.getPortrait()!=null){
                    target.getPortrait().remove();
                }
                target.setPortrait(pictureService.associate(target, image.getFile().getBytes(), PictureType.USER_PORTRAIT));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return target;
    }

    public void initUserWallet(User user){
        if(user.getWallet() == null){
            UserWallet wallet = new UserWallet();
            wallet.setBalance(Constants.INIT_COST);
            wallet.setUser(user);
            wallet.persist();
            logWalletChange(wallet, "钱包初始化", wallet.getBalance(), wallet.getBalance());
        }
    }

    private UserWallet getUserWallet(User user){
        if(user.getWallet() == null){
            initUserWallet(user);
        }
        return user.getWallet();
    }

    private void logWalletChange(UserWallet wallet, String memo, BigDecimal before, BigDecimal after) {
        UserWalletChangeLog log = new UserWalletChangeLog();
        log.setMemo(memo);
        log.setWallet(wallet);
        log.setBalanceBefore(before);
        log.setBalanceAfter(after);
        log.persist();
    }


    @Override
    public User load(Integer userId) {
        return User.findUser(userId);
    }

    @Override
    public User findUserByMobile(String mobile) {
        return User.findUserByMobile(mobile);
    }

    @Override
    @Transactional
    public void login(HttpServletRequest request,HttpServletResponse response, String mobile, String password, Location location) throws  BadVerifyCodeException {
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        User currUser = User.findUserByMobile(mobile);
        boolean loginByVerifyCode = false;
        SmsVerifyLog log = SmsVerifyLog.getLastUnVerifyLogByMobileAndType(mobile,SmsType.USER_LOGIN);
        if(log != null && StringUtils.equalsIgnoreCase(log.getVerifyCode(),password)){
            loginByVerifyCode = true;
        }
        if(currUser == null){
            if(log != null && StringUtils.equalsIgnoreCase(password,log.getVerifyCode())){
                currUser = new User();
                currUser.setMobile(mobile);
                currUser.setPassword(DigestUtils.sha256Hex(password));
                currUser.setUserType(UserType.USER);
                currUser.setGender(Gender.MALE);
                currUser.persist();
            }else{
                throw new BadVerifyCodeException();
            }
        }
        if(!loginByVerifyCode){
            if((!StringUtils.equalsIgnoreCase(currUser.getPassword(), DigestUtils.sha256Hex(password)))){
                throw new BadVerifyCodeException();
            }
        }

        String userLoginToken = DigestUtils.sha256Hex(String.valueOf(System.currentTimeMillis())+(UUID.randomUUID().toString())).toUpperCase();
        ResultBean resultBean = ResultBean.wrap(currUser,new String[]{"wallet.balance"},new String[]{"wallet.*"});
        logUserLogin(request, currUser, location, userLoginToken);
        try {
            response.setHeader(Constants.USER_LOGIN_TOKEN, userLoginToken);
            resultBean.setToken(userLoginToken);
            response.getWriter().print(resultBean.toJson());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Transactional
    private void logUserLogin(HttpServletRequest request,User currUser, Location location,String userLoginToken) {
        UserLoginLog last = UserLoginLog.getLastActiveLogByUserId(currUser.getId());
        if(last != null){
            last.setIsActive(false);
            last.merge();
        }
        UserLoginLog log = new UserLoginLog();
        log.setIsActive(true);
        log.setLocation(location);
        log.setUserLoginToken(userLoginToken);
        log.setUser(currUser);
        log.setUserAgent(request.getHeader("user-agent"));
        log.persist();
    }

    @Override
    public SmsVerifyLog requestSms(String mobile, SmsType type) {
        SmsVerifyLog log = new SmsVerifyLog();
        log.setMobile(mobile);
        log.setIsVerified(false);
        log.setVerifyCode(String.valueOf((int) (Math.random() * 9000) + 1000));
        log.setType(type);
        log.persist();
        //todo 调用发送短信接口

        return log;
    }

    @Override
    @Transactional
    public UserAddress manageAddress(User current, UserAddress userAddress, ManageType type) throws NoPermissionsException, EntityNotFoundException {
        UserAddress result = null;
        switch (type){
            case CREATE:
                result = createAddress(current,userAddress);
                break;
            case DELETE:
                deleteAddress(current,userAddress);
                break;
            case UPDATE:
                result = updateAddress(current,userAddress);
                break;
            default:
                break;
        }
        return result;
    }



    private UserAddress updateAddress(User current, UserAddress userAddress) throws EntityNotFoundException, NoPermissionsException {
        if(userAddress == null || userAddress.getId() == null){
            throw new EntityNotFoundException();
        }
        UserAddress target = UserAddress.find(userAddress.getId());
        if(!target.getUser().equals(current)){
            throw new NoPermissionsException();
        }
        boolean needUpdate = false;
        if(StringUtils.isNotBlank(userAddress.getAddressBase())){
            needUpdate = true;
            target.setAddressBase(userAddress.getAddressBase());
        }
        if(StringUtils.isNotBlank(userAddress.getAddressDetail())){
            needUpdate = true;
            target.setAddressDetail(userAddress.getAddressDetail());
        }
        if(userAddress.getGender() != null){
            needUpdate = true;
            target.setGender(userAddress.getGender());
        }
        if(StringUtils.isNotBlank(userAddress.getContactName())){
            needUpdate = true;
            target.setContactName(userAddress.getContactName());
        }
        if(StringUtils.isNotBlank(userAddress.getContactMobile())){
            needUpdate = true;
            target.setContactMobile(userAddress.getContactMobile());
        }
        if(userAddress.getLocation() != null){
            needUpdate = true;
            target.setLocation(userAddress.getLocation());
        }
        if(needUpdate){
            target.merge();
        }
        return target;
    }

    private void deleteAddress(User current, UserAddress userAddress) throws EntityNotFoundException, NoPermissionsException {
        if(userAddress == null || userAddress.getId() == null){
            throw new EntityNotFoundException();
        }
        UserAddress target = UserAddress.find(userAddress.getId());
        if(!target.getUser().equals(current)){
            throw new NoPermissionsException();
        }
        target.remove();
    }

    private UserAddress createAddress(User current, UserAddress userAddress) {
        userAddress.setUser(current);
        userAddress.persist();
        return userAddress;
    }


    @Override
    public List<UserAddress> loadAddress(User current, Integer page, Integer count) {
        String query = "from UserAddress ua where ua.user.id=:user_id order by id desc";
        return UserAddress.entityManager().createQuery(query,UserAddress.class)
                .setParameter("user_id",current.getId()).setFirstResult((page -1) * count)
                .setMaxResults(count).getResultList();
    }

    @Transactional
    public UserCashLog cash(User user, UserCashLog log, String walletPassword) throws LackOfBalanceException, EntityNotFoundException, BadWalletPasswordException {
        UserWallet wallet = getUserWallet(user);
        BigDecimal before = wallet.getBalance();
        if(before.compareTo(log.getCost()) == -1){
            throw new LackOfBalanceException();
        }
        if(log.getBankInfo() == null || log.getBankInfo().getId() == null){
            throw new EntityNotFoundException();
        }
        if(!wallet.getPassword().equals(DigestUtils.sha256Hex(walletPassword))){
            throw new BadWalletPasswordException();
        }
        UserWalletBankInfo bankInfo = UserWalletBankInfo.find(log.getBankInfo().getId());
        UserCashLog target = new UserCashLog();
        target.setBankInfo(bankInfo);
        target.setCardNum(bankInfo.getCardNum());
        target.setUser(user);
        target.setCost(log.getCost());
        target.setStatus(CashStatus.INIT);
        target.setMemo(StringUtils.isNotBlank(log.getMemo()) ? log.getMemo() : "用户提现");
        target.persist();

        wallet.setBalance(wallet.getBalance().subtract(log.getCost()));
        wallet.merge();
        logWalletChange(wallet, "用户提现", before, wallet.getBalance());

        //todo 通知后台管理员

        return log;
    }

    @Transactional
    public UserWalletBankInfo manageBankInfo(User user, UserWalletBankInfo bankInfo, ManageType type, String password) throws EntityNotFoundException, BadWalletPasswordException {
        UserWallet wallet = getUserWallet(user);
        if(!type.equals(ManageType.CREATE)){
            checkWalletPassword(wallet,password);
        }
        UserWalletBankInfo result = null;
        switch (type){
            case CREATE:
                result = createBankInfo(user,bankInfo);
                break;
            case UPDATE:
                result = updateBankInfo(user, bankInfo);
                break;
            case DELETE:
                deleteBaknInfo(user, bankInfo);
                break;
        }
        return result;
    }

    private boolean checkWalletPassword(UserWallet wallet,String password){
        return StringUtils.equals(wallet.getPassword(),DigestUtils.sha256Hex(password));
    }

    private void deleteBaknInfo(User user, UserWalletBankInfo bankInfo) {
        if(bankInfo != null && bankInfo.getId() != null){
            UserWalletBankInfo target = UserWalletBankInfo.find(bankInfo.getId());
            target.remove();
        }
    }

    private UserWalletBankInfo updateBankInfo(User user, UserWalletBankInfo bankInfo) throws EntityNotFoundException {
        boolean changed = false;
        UserWalletBankInfo target = null;
        if(bankInfo != null && bankInfo.getId() != null){
            target = UserWalletBankInfo.find(bankInfo.getId());
            if(target == null){
                throw new EntityNotFoundException();
            }
            if(StringUtils.isNotBlank(bankInfo.getCardNum())){
                changed = true;
                target.setCardNum(bankInfo.getCardNum());
            }
            if(StringUtils.isNotBlank(bankInfo.getBankName())){
                changed = true;
                target.setBankName(bankInfo.getBankName());
            }
            if(bankInfo.getBank() != null){
                Bank bank = Bank.find(bankInfo.getBank().getId());
                if(bank != null){
                    target.setBank(bank);
                }
                changed = true;
            }
            if(changed){
                target.merge();
            }
        }
        return target;
    }

    private UserWalletBankInfo createBankInfo(User user, UserWalletBankInfo bankInfo) {
        UserWallet wallet = user.getWallet();
        if(wallet == null){
            initUserWallet(user);
            wallet = user.getWallet();
        }
        UserWalletBankInfo target = UserWalletBankInfo.findBankInfoByCardNum(bankInfo.getCardNum());
        if(target == null){
            Bank bank = null;
            if(bankInfo.getBank() != null){
                bank = Bank.find(bankInfo.getBank().getId());
            }
            target = new UserWalletBankInfo();
            target.setBankName(bank == null ? StringUtils.isNotBlank(bankInfo.getBankName())?bankInfo.getBankName():null:bank.getBankName());
            target.setCardNum(bankInfo.getCardNum());
            target.setWallet(wallet);
            target.setBank(bank);
            target.persist();
        }
        return target;
    }

    public List<UserWalletBankInfo> loadUserBankInfos(User user, Integer page, Integer count){
        String query = "from UserWalletBankInfo ubi where ubi.wallet.user.id=:user_id order by ubi.createTime desc";
        List<UserWalletBankInfo> bankInfos = UserWalletBankInfo.entityManager().createQuery(query,UserWalletBankInfo.class)
                .setParameter("user_id",user.getId()).setFirstResult((page - 1)*count).setMaxResults(count).getResultList();
        return bankInfos;
    }

    public List<UserWalletChangeLog> walletChangeLogs(User user,Integer page,Integer count){
        UserWallet wallet = user.getWallet();
        if(wallet == null){
            initUserWallet(user);
        }
        return UserWalletChangeLog.getWalletChangelogsByUser(user.getWallet(),page,count);
    }

    @Transactional
    public boolean caretWalletPassword(User user,String password,String verifyCode) throws EntityNotFoundException, BadVerifyCodeException {
        boolean success = false;
        SmsVerifyLog log = SmsVerifyLog.getLastUnVerifyLogByMobileAndType(user.getMobile(),SmsType.CREATE_WALLET_PASSWORD);
        if(log == null || !log.getVerifyCode().equals(verifyCode)){
            throw new BadVerifyCodeException();
        }
        UserWallet wallet = getUserWallet(user);
        wallet.setPassword(DigestUtils.sha256Hex(password));
        wallet.merge();
        success = true;
        return success;
    }

    @Transactional
    public boolean reSetWalletPassword(User user,String password,String verifCode) throws EntityNotFoundException, BadVerifyCodeException {
        boolean success = false;
        SmsVerifyLog log = SmsVerifyLog.getLastUnVerifyLogByMobileAndType(user.getMobile(),SmsType.FORGET_WALLET_PASSWORD);
        if(log == null || !log.getVerifyCode().equals(verifCode)){
            throw new BadVerifyCodeException();
        }
        UserWallet wallet = getUserWallet(user);
        wallet.setPassword(DigestUtils.sha256Hex(wallet.getPassword()));
        wallet.merge();
        success = true;
        return success;
    }

    public List<Comment> getUserComments(Integer userId,Integer page,Integer count){
        return Comment.getUserComment(userId,page,count);
    }


    public UserProfileVo wrapUserProfile(Integer id){
        UserProfileVo profile = null;
        User user = User.findUser(id);
        if(user != null){
            profile = new UserProfileVo();
            profile.setUser(user);
            profile.setComments(Comment.getUserComment(id,1,10));
        }
        return profile;
    }

    public List<User> findUsers(Integer page,Integer count){
        String query = "from User u order by u.id desc";
        return User.entityManager().createQuery(query,User.class)
                .setFirstResult((page - 1) * count).setMaxResults(count).getResultList();
    }

    public List<UserCashLog> findUnhandelUserCashLog(Integer page,Integer count){
        String query = "from UserCashLog log where log.status =:init_status order by log.createTime desc";
        return UserCashLog.entityManager().createQuery(query,UserCashLog.class).setParameter("init_status",CashStatus.INIT)
                .setFirstResult((page - 1)*count).setMaxResults(count).getResultList();
    }

    @Override
    public Integer findUserTotalPage(Integer initCount) {
        String query = "select count(u) from User u ";
        Integer count = User.entityManager().createQuery(query,Long.class).getSingleResult().intValue();
        return count%initCount != 0?(count/initCount) + 1: count/initCount;
    }

    @Override
    public Integer findCashTotalPage(Integer initCount) {
        String query = "select count(ucl) from UserCashLog ucl";
        Integer count = UserCashLog.entityManager().createQuery(query,Long.class).getSingleResult().intValue();
        return count%initCount != 0?(count/initCount) + 1: count/initCount;
    }

    @Override
    @Transactional
    public void stopUser(Integer userId) {
        User user = findUser(userId);
        if(user != null){
            user.setStatus(UserStatus.STOP);
            user.merge();
        }
    }

    @Override
    public void unStopUser(Integer userId) {
        User user = findUser(userId);
        if(user != null && user.getStatus().equals(UserStatus.STOP)){
            user.setStatus(UserStatus.NORMAL);
            user.merge();
        }
    }

    @Override
    public Integer getStatisticsNum4UserByUnit(StatisticsUnit statisticsUnit) {
        String query = "select count(u) from User u where u.createTime >:time";
        return User.entityManager().createQuery(query,Long.class)
                .setParameter("time",statisticsUnit.getQueryDate())
                .getSingleResult().intValue();
    }

    @Override
    public Integer getStatisticsNum4CashByUnit(StatisticsUnit statisticsUnit) {
        String query = "select count(ul) from UserCashLog ul where ul.createTime >:time";
        return UserCashLog.entityManager().createQuery(query,Long.class)
                .setParameter("time", statisticsUnit.getQueryDate())
                .getSingleResult().intValue();
    }
}
