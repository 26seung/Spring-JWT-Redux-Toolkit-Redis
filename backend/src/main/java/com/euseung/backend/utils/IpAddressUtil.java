package com.euseung.backend.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public class IpAddressUtil {

    private static final List<String> HEADER_LIST = Arrays.asList(
            "X-Forwarded-For",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_VIA",
            "IPV6_ADR"
    );

    //  클라이언트 IP 주소 확인
    public static String getClientIp(HttpServletRequest request) {
        String clientIp = null;

        for (String header : HEADER_LIST) {
            clientIp = request.getHeader(header);
            if (StringUtils.hasText(clientIp) && !clientIp.equalsIgnoreCase("unknown")) {
                return clientIp;
            }
        }

        return request.getRemoteAddr();
    }
}
