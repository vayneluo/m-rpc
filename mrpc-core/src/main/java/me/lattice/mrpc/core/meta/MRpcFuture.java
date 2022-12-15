package me.lattice.mrpc.core.meta;

import io.netty.util.concurrent.Promise;
import lombok.Data;

/**
 * @description: 回调Future
 * @author: lattice
 * @date: 2022/12/15 5:00 PM
 */
@Data
public class MRpcFuture<T> {

    private Promise<T> promise;

    private long timeout;

    public MRpcFuture(Promise<T> promise, long timeout) {
        this.promise = promise;
        this.timeout = timeout;
    }
}
