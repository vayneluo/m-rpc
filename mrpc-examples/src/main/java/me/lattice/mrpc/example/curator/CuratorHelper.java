package me.lattice.mrpc.example.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.Watcher;

import java.util.List;
import java.util.function.Function;

/**
 * @description:
 * @author: lattice
 * @date: 2022/12/22 9:12 AM
 */
public class CuratorHelper {

    private static final String ZK_ADDRESS = "localhost:2181";

    private static final int ZK_SESSION_TIMEOUT = 5000;

    private static final int ZK_CONNECTION_TIMEOUT = 5000;

    private static final CuratorFramework client = start(ZK_SESSION_TIMEOUT, ZK_CONNECTION_TIMEOUT);

    public static CuratorFramework start(int connectionTimeoutMs, int sessionTimeoutMs) {
        return newClient(connectionTimeoutMs, sessionTimeoutMs, null);
    }


    public static CuratorFramework newClient(int connectionTimeoutMs, int sessionTimeoutMs, RetryPolicy retryPolicy) {
        if (retryPolicy == null) {
            // 默认指数回溯重试策略
            // baseSleepTimeMs: 重试间隔时间
            // maxRetries: 最大重试次数
            // eg: 重试间隔时间为1s, 最大重试次数为3, 则重试时间为: 1s, 2s, 4s
            retryPolicy = new ExponentialBackoffRetry(1000, 3);
        }
        CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_ADDRESS, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
        client.start();
        return client;
    }

    public static void createNode(String path, String data) {
        try {
            client.create().creatingParentsIfNeeded().forPath(path, data.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static byte[] getNodeData(String path) {
        byte[] bytes;
        try {
            bytes = client.getData().forPath(path);
            System.out.println(new String(bytes));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    public static void delete(String path) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void close() {
        CloseableUtils.closeQuietly(client);
    }

    public static List<String> chlidrenList(String path) throws Exception {
        return client.getChildren().forPath(path);
    }

    public static void addListener(String path) throws Exception {
        client.getData().watched().inBackground().forPath(path);
    }

    public static List<String> watchedGetChildren(String path, Watcher watcher) throws Exception
    {
        /**
         * Get children and set the given watcher on the node.
         */
        return client.getChildren().usingWatcher(watcher).forPath(path);
    }
}
