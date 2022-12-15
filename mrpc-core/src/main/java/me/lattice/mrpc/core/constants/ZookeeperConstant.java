package me.lattice.mrpc.core.constants;

/**
 * @description: zookeeper常量类
 * @author: lattice
 * @date: 2022/12/15 9:53 AM
 */
public class ZookeeperConstant {

    /**
     * zookeeper根节点
     */
    public static final String ROOT_PATH = "/m-rpc";

    /**
     * 最大重试次数
     */
    public static final int MAX_RETRIES = 3;

    /**
     * 会话超时时间
     */
    public static final int SLEEP_TIME = 1000;
}
