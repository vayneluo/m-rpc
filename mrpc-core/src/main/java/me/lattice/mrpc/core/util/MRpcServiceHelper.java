package me.lattice.mrpc.core.util;

import me.lattice.mrpc.core.constants.SymbolConstant;

/**
 * @description:
 * @author: lattice
 * @date: 2022/12/15 10:32 AM
 */
public class MRpcServiceHelper {

    public static String buildServiceName(String serviceName, String version) {
        return serviceName + SymbolConstant.DASH + version;
    }
}
