package com.sajid.SequentialProxy.model;
import java.util.Date;

public class ProxyLog {
    private Date timestamp;
    private String clientIp;
    private String method;
    private String shipProxyUrl;
    private String shoreServerUrl;
    private int statusCode;
    private long processingTimeMs;

    // Getters and Setters
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getShipProxyUrl() {
        return shipProxyUrl;
    }

    public void setShipProxyUrl(String shipProxyUrl) {
        this.shipProxyUrl = shipProxyUrl;
    }

    public String getShoreServerUrl() {
        return shoreServerUrl;
    }

    public void setShoreServerUrl(String shoreServerUrl) {
        this.shoreServerUrl = shoreServerUrl;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public long getProcessingTimeMs() {
        return processingTimeMs;
    }

    public void setProcessingTimeMs(long processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }

    @Override
    public String toString() {
        return "ProxyLog{" +
                "timestamp=" + timestamp +
                ", clientIp='" + clientIp + '\'' +
                ", method='" + method + '\'' +
                ", shipProxyUrl='" + shipProxyUrl + '\'' +
                ", shoreServerUrl='" + shoreServerUrl + '\'' +
                ", statusCode=" + statusCode +
                ", processingTimeMs=" + processingTimeMs +
                '}';
    }
}
