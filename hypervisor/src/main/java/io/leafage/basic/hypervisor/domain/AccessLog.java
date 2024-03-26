/*
 *  Copyright 2018-2024 little3201.
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
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * model class for access log.
 *
 * @author wq li 2022/1/26 15:20
 **/
@Entity
@Table(name = "access_logs")
public class AccessLog extends AuditMetadata {

    /**
     * IP地址
     */
    private String ip;

    /**
     * 地理位置
     */
    @Column(name = "location", length = 50)
    private String location;

    /**
     * 内容
     */
    @Column(name = "context", length = 1000)
    private String context;

    /**
     * 用户代理信息
     */
    @Column(name = "user_agent")
    private String userAgent;

    /**
     * HTTP方法
     */
    @Column(name = "http_method", length = 10)
    private String httpMethod;

    /**
     * 请求URL
     */
    private String url;

    /**
     * HTTP状态码
     */
    @Column(name = "status_code")
    private Integer statusCode;

    /**
     * 响应时间
     */
    @Column(name = "response_time")
    private Long responseTime;

    /**
     * 来源页面
     */
    private String referer;

    /**
     * 会话标识符
     */
    @Column(name = "session_id", length = 50)
    private String sessionId;

    /**
     * 设备类型
     */
    @Column(name = "device_type", length = 20)
    private String deviceType;

    /**
     * 操作系统
     */
    @Column(name = "os", length = 50)
    private String os;

    /**
     * 浏览器
     */
    @Column(name = "browser", length = 50)
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

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
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
