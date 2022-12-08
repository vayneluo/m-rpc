package me.lattice.mrpc.core.enums;

/**
 * @description: 注册中心类型枚举类
 * @author: lattice
 * @date: 2022/12/8 5:48 PM
 */
public enum RegistryTypeEnum {

    ZK("zookeeper"),

    NACOS("nacos"),

    EUREKA("eureka"),
    ;


    String type;

    RegistryTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static RegistryTypeEnum lookUp(String type) {
        for (RegistryTypeEnum registryTypeEnum : RegistryTypeEnum.values()) {
            if (registryTypeEnum.getType().equals(type)) {
                return registryTypeEnum;
            }
        }
        return null;
    }
}
