/*
 *  Copyright 2018-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.leafage.basic.hypervisor.domain;

import io.leafage.basic.hypervisor.audit.AuditMetadata;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * model class for access log
 *
 * @author liwenqiang 2022-03-18 21:09
 */
@Table(name = "access_logs")
public class AccessLog extends AuditMetadata {

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
    @Column(value = "user_agent")
    private String userAgent;

    /**
     * HTTP方法
     */
    @Column(value = "http_method")
    private String httpMethod;

    /**
     * 请求URL
     */
    private String url;

    /**
     * HTTP状态码
     */
    @Column(value = "status_code")
    private Integer statusCode;

    /**
     * 响应时间
     */
    @Column(value = "response_time")
    private Long responseTime;

    /**
     * 来源页面
     */
    private String referer;

    /**
     * 会话标识符
     */
    @Column(value = "session_id")
    private String sessionId;

    /**
     * 设备类型
     */
    @Column(value = "device_type")
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
