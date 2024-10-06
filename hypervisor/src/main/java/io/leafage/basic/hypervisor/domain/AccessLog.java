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


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import top.leafage.common.servlet.audit.AuditMetadata;

/**
 * model class for access log.
 *
 * @author wq li
 */
@Entity
@Table(name = "access_logs")
public class AccessLog extends AuditMetadata {

    private String url;

    @Column(name = "http_method", length = 10)
    private String httpMethod;

    private String ip;

    @Column(name = "location", length = 50)
    private String location;

    private String params;

    private String body;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "response_times")
    private Long responseTimes;

    @Column(name = "response_message")
    private String responseMessage;
    

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String method) {
        this.httpMethod = method;
    }

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

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer responseCode) {
        this.statusCode = responseCode;
    }

    public Long getResponseTimes() {
        return responseTimes;
    }

    public void setResponseTimes(Long responseTimes) {
        this.responseTimes = responseTimes;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }


}
