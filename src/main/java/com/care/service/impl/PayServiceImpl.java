package com.care.service.impl;

import com.care.domain.Order;
import com.care.domain.TradingRecord;
import com.care.domain.User;
import com.care.domain.enums.OrderStatus;
import com.care.domain.enums.PayChannel;
import com.care.domain.enums.TradingStatus;
import com.care.exception.EntityNotFoundException;
import com.care.exception.OrderStatusErrorException;
import com.care.service.OrderService;
import com.care.service.PayService;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.portable.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nujian on 16/2/23.
 */
@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private OrderService orderService;

    @Override
    @Transactional
    public Map<String, Object> send(User user,HttpServletRequest request, HttpServletResponse response, Integer orderId, PayChannel payChannel,String memo) throws EntityNotFoundException, OrderStatusErrorException {
        Map<String,Object> result = null;

        Order order = orderService.findOrder(orderId);
        if(order == null){
            throw new EntityNotFoundException();
        }
        if(!(order.getStatus().equals(OrderStatus.WAIT_PAY) || order.getStatus().equals(OrderStatus.INIT))){
            throw new OrderStatusErrorException();
        }
        String ip = request.getRemoteAddr();
        TradingRecord record = makeOrLoadTradingRecordByOrder(ip,memo,user, payChannel, order);

        result = send(request, response, order, record);
     return result;
    }

    private Map<String, Object> send(HttpServletRequest request, HttpServletResponse response, Order order, TradingRecord record) {
        Map<String,Object> result = null;
        switch (record.getChannel()){
            case WEIXIN:
                result = sendPay4Weixin(request,response,order,record);
                break;
            case ALIPAY:
                result = sendPay4Alipay(request,response,order,record);
                break;
        }
        return result;
    }

    private Map<String, Object> sendPay4Alipay(HttpServletRequest request, HttpServletResponse response, Order order, TradingRecord record) {
        Map<String,Object> result = new HashMap<String, Object>();

        return result;
    }

    private Map<String, Object> sendPay4Weixin(HttpServletRequest request, HttpServletResponse response, Order order, TradingRecord record) {
        Map<String,Object> result = new HashMap<String, Object>();

        return result;
    }

    private TradingRecord makeOrLoadTradingRecordByOrder(String payIp,String memo,User user, PayChannel payChannel, Order order) {
        TradingRecord record = TradingRecord.getTradingRecordByOrder(order);
        if(record == null){
            record = new TradingRecord();
            record.setPayer(user);
            record.setCost(order.getCost());
            record.setChannel(payChannel);
            record.setOrder(order);
            record.setMemo("");
            record.setStatus(TradingStatus.WAIT_PAY);
            record.setThirdPartsTradingNum("12213213");
            record.setPayIp(payIp);
            record.setMemo(memo);
            record.setThirdPartsTradingNum(makeThirdPartsTradingNum());

            record.persist();
        }
        return record;
    }


    private String makeThirdPartsTradingNum(){
        StringBuffer number = new StringBuffer();
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String currTime = outFormat.format(now);
        number.append(currTime.substring(8, currTime.length()));
        number.append(buildRandom(4));
        return number.toString();
    }

    private int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    public boolean notifyAliPay(HttpServletRequest request) throws EntityNotFoundException {
        boolean success = false;
        String outTradeNo = "";

        Map requestParams = request.getParameterMap();
        Map<String, String> params = new HashMap<String, String>();
        try{
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                params.put(name, valueStr);
            }
            String notifyData = params.get("notify_data");
            outTradeNo = getOutTradeNoByNotifyData(notifyData);
            if(StringUtils.isBlank(outTradeNo)){
                outTradeNo = params.get("out_trade_no");
            }
            //todo 处理后续业务


        }catch(Exception e){
            e.printStackTrace();
        }
        return success;
    }

    /**
     *
     * @param notifyData
     * @return
     */
    private String getOutTradeNoByNotifyData(String notifyData){
        String outTradeNo = null;
//        if(StringUtils.isNotBlank(notifyData)){
//            try {
//                Map notifyMap = XMLUtil.doXMLParse(notifyData);
//                outTradeNo = String.valueOf(notifyMap.get("out_trade_no"));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return outTradeNo;
    }

    @Override
    public boolean notifyWeixin(ResponseHandler responseHandler) throws Exception {
        return false;
    }
}
