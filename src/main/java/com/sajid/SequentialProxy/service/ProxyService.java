package com.sajid.SequentialProxy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import  com.sajid.SequentialProxy.model.ProxyLog;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProxyService {
    private final Object lock = new Object();
    private final AtomicLong requestCounter = new AtomicLong(0);

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<byte[]> forwardRequest(String clientIp, String shipProxyUrl,
                                                 String shoreServerUrl, HttpMethod method, HttpHeaders headers, byte[] body) {
        long startTime = System.currentTimeMillis();
        long requestId = requestCounter.incrementAndGet();

        synchronized (lock) {
            try {
                // Log request start
                System.out.printf("[%d] [%s] Ship Proxy >> Forwarding to Shore: %s %s%n",
                        requestId, clientIp, method, shoreServerUrl);

                // Prepare request to shore server
                HttpEntity<byte[]> requestEntity = new HttpEntity<>(body, headers);
                ResponseEntity<byte[]> response = restTemplate.exchange(
                        shoreServerUrl, method, requestEntity, byte[].class);

                // Log response
                System.out.printf("[%d] [%s] Shore Server >> Response: %d %s%n",
                        requestId, clientIp, response.getStatusCodeValue(), shoreServerUrl);

                // Create log
                ProxyLog log = new ProxyLog();
                log.setTimestamp(new Date());
                log.setClientIp(clientIp);
                log.setMethod(method.name());
                log.setShipProxyUrl(shipProxyUrl);
                log.setShoreServerUrl(shoreServerUrl);
                log.setStatusCode(response.getStatusCodeValue());
                log.setProcessingTimeMs(System.currentTimeMillis() - startTime);

                return response;
            } catch (Exception e) {
                System.err.printf("[%d] [%s] Error: %s%n", requestId, clientIp, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body(("Proxy error: " + e.getMessage()).getBytes());
            }
        }
    }
}
