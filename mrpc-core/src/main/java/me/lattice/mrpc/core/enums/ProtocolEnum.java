package me.lattice.mrpc.core.enums;

/**
 * @description: MRpc Protocol Enum
 * @author lattice
 */
public enum ProtocolEnum {

    HTTP("http"),
    HTTPS("https"),
    TCP("tcp"),
    UDP("udp"),
    MQ("mq"),
    RPC("rpc"),
    ;

    private String protocol;

    ProtocolEnum(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
