package com.sajid.SequentialProxy.controller;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.StreamUtils;
import com.sajid.SequentialProxy.service.ProxyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.util.Enumeration;

@RestController
public class ProxyController {
    @Autowired
    private ProxyService proxyService;

    @RequestMapping("/**")
    public ResponseEntity<byte[]> proxyRequest(HttpServletRequest request) throws IOException {
        // Get client IP
        String clientIp = request.getRemoteAddr();

        // Get ship proxy URL
        String shipProxyUrl = request.getRequestURL().toString();

        // Get shore server URL (original request URL)
        String shoreServerUrl = extractShoreServerUrl(request);

        // Get method
        HttpMethod method = HttpMethod.valueOf(request.getMethod());

        // Copy headers
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.add(headerName, request.getHeader(headerName));
        }

        // Clean headers
        cleanHeaders(headers);

        // Read request body
        byte[] body = StreamUtils.copyToByteArray(request.getInputStream());

        // Forward to shore server and return response
        return proxyService.forwardRequest(
                clientIp, shipProxyUrl, shoreServerUrl, method, headers, body);
    }

    private String extractShoreServerUrl(HttpServletRequest request) {
        // For proxy requests, the shore server URL is in the request line
        String queryString = request.getQueryString();
        return request.getRequestURL().toString() +
                (queryString != null ? "?" + queryString : "");
    }

    private void cleanHeaders(HttpHeaders headers) {
        // Remove hop-by-hop headers
        headers.remove("host");
        headers.remove("connection");
        headers.remove("keep-alive");
        headers.remove("proxy-authenticate");
        headers.remove("proxy-authorization");
        headers.remove("te");
        headers.remove("trailer");
        headers.remove("transfer-encoding");
        headers.remove("upgrade");

        // Add custom headers
        headers.add("X-Proxy-Type", "Sequential-Ship-Proxy");
    }
}