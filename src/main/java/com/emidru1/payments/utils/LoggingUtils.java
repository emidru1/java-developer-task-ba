package com.emidru1.payments.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class LoggingUtils {

    private static final RestTemplate restTemplate = new RestTemplate();

    private LoggingUtils() {

    }

    private static String getClientIp(HttpServletRequest request) {
        String header = request.getHeader("X-Forwarded-For");
        if (header == null || header.isEmpty()) {
            return request.getRemoteAddr();
        } else {
            return header.split(",")[0];
        }
    }

    private static String getCountryFromIp(String ip) {
        try {
            String url = "http://ip-api.com/json/" + ip;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && "success".equals(response.get("status"))) {
                return (String) response.get("country");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    public static void logIpAndCountry(HttpServletRequest request) {
        String ipAddress = getClientIp(request);
        String country = getCountryFromIp(ipAddress);

        System.out.println("IP: " + ipAddress);
        System.out.println("Country: " + country);
    }

}
