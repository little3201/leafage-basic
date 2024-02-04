package io.leafage.basic.hypervisor.dto;

import java.io.Serializable;

/**
 * dto class for access log.
 *
 * @author liwenqinag 2022/4/15 13:39
 **/
public class AccessLogDTO implements Serializable {

    /**
     * IP地址
     */
    private String ip;

    /**
     * 地理位置
     */
    private String location;

    /**
     * 用户代理信息
     */
    private String userAgent;

    /**
     * HTTP方法
     */
    private String httpMethod;

    /**
     * 请求URL
     */
    private String url;

    /**
     * HTTP状态码
     */
    private Integer statusCode;

    /**
     * 响应时间
     */
    private Long responseTime;

    /**
     * 来源页面
     */
    private String referer;

    /**
     * 会话标识符
     */
    private String sessionId;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 浏览器
     */
    private String browser;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }
}
