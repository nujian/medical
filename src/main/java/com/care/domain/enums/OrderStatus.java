package com.care.domain.enums;

/**
 * Created by nujian on 16/2/19.
 * 订单状态
 */
public enum OrderStatus {
    /**
     * 初始化
     */
    INIT,
    /**
     * 以匹配
     */
    MATCHED,
    /**
     * 等待支付
     */
    WAIT_PAY,
    /**
     * 已支付
     */
    PAIED,
    /**
     * 已完成
     */
    FINISH,
    /**
     * 已取消
     */
    CANCEL
}
