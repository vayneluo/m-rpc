package me.lattice.mrpc.protocol.handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: MRpc 自定义线程池
 * @author: lattice
 * @date: 2022/12/12 20:46
 */
public class CustomThreadExecutor {

    private static volatile ThreadPoolExecutor executor;

    /**
     * MRpc 自定义线程池执行任务
     */
    public static void execute(Runnable task) {
        // double check
        if (null == executor) {
            synchronized (CustomThreadExecutor.class) {
                if (null == executor) {
                    executor = new ThreadPoolExecutor(16, 16, 60L,
                            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(Integer.MAX_VALUE));
                }
            }
        }
        executor.execute(task);
    }
}
