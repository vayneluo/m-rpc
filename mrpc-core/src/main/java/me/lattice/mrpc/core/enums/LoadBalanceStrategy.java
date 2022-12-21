package me.lattice.mrpc.core.enums;

/**
 * @description: 负载均衡策略枚举类
 * @author: lattice
 * @date: 2022/12/21 4:55 PM
 */
public enum LoadBalanceStrategy {

    /**
     * 随机
     */
    RANDOM,

    /**
     * 轮询
     */
    ROUND_ROBIN,

    /**
     * 加权轮询
     */
    WEIGHT_ROUND_ROBIN,

    /**
     * 最小活跃数
     */
    LEAST_ACTIVE,

    /**
     * 加权最小活跃数
     */
    WEIGHT_LEAST_ACTIVE,

    /**
     * 一致性哈希
     */
    CONSISTENT_HASH


}
