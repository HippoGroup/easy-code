package com.hippocp.easy.code.util.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author ZhouYifan
 * @date 2021/12/30
 */
public class IpAddressUtil {

    /**
     * 日志对象
     */
    private static final Logger log = LoggerFactory.getLogger(IpAddressUtil.class);

    /**
     * 获取ip地址
     *
     * @param request {@link HttpServletRequest}
     * @return {@link String} ip地址
     */
    public static String getIpAddress(HttpServletRequest request) {

        String ip;
        ip = request.getHeader("x-real-ip");

        if (isNull(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }

        if (isNull(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (isNull(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        final String localHostIp = "127.0.0.1";

        if (isNull(ip)) {
            // 获取发送请求的客户端或最后一个代理的 Internet 协议 (IP) 地址。
            // 对于 HTTP servlet，与 CGI 变量REMOTE_ADDR的值相同。
            ip = request.getRemoteAddr();

            if (localHostIp.equals(ip)) {

                InetAddress address = null;
                try {
                    // 获取本地主机的地址
                    address = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    log.error("无法确定主机的 IP 地址。", e);
                }

                if (address == null) {
                    return "错误信息如下：InetAddress为Null，无法继续读取ip地址";
                }
                // 获取ip地址字符串
                ip = address.getHostAddress();

            }

        }

        final String comma = ",";
        final int ipAddressLength = 15;

        if (ip != null && ip.length() > ipAddressLength) {
            int indexOf = ip.indexOf(comma);
            if (indexOf > 0) {
                ip = ip.substring(0, indexOf);
            }
        }

        return ip;
    }

    /**
     * 判断ip地址是否为空或者为unknown
     *
     * @param ip ip地址
     * @return ip为空或者为unknown返回ture，不为空返回false
     */
    public static boolean isNull(String ip) {
        return ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip);
    }

    private IpAddressUtil() {
    }
}
