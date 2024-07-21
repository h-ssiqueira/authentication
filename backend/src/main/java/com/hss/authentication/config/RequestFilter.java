package com.hss.authentication.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@Component
public class RequestFilter implements Filter {
    private static final String CHAR_HIDE_CONTENT = "*";
    private static final String NO_CONTENT_CHAR = "-";
    private static final int INDENT_SPACES = 3;
    private static final String FORM_URL_ENCODED = "x-www-form-urlencoded";
    private static final int LENGTH_CHARACTERS = 6;
    private static final List<String> IGNORE_FIELDS = asList("password", "email");

    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("Initializing requestFilter");
    }

    @Override
    public void destroy() {
        log.debug("Destroying requestFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpServletRequest = (HttpServletRequest) request;
        final var responseWrapper = new HttpServletResponseWrapper((HttpServletResponse) response);
        if(nonNull(httpServletRequest.getContentType()) && httpServletRequest.getContentType().contains(FORM_URL_ENCODED)) {
            var requestWrapped = new ContentCachingRequestWrapper(httpServletRequest);
            requestWrapped.getParameterMap();
            chain.doFilter(requestWrapped, responseWrapper);
            logBodies(requestWrapped, responseWrapper);
            return;
        }
        final var requestWrapped = new HttpServletRequestWrapper(httpServletRequest);
        chain.doFilter(requestWrapped, responseWrapper);
        logBodies(requestWrapped, responseWrapper);
    }

    private void logBodies(HttpServletRequestWrapper requestWrapper, HttpServletResponseWrapper responseWrapper) throws IOException {
        try {
            log.info("""

                    ###############################Request info########################################
                    %s
                    ###################################################################################""".formatted(buildRequestInfo(requestWrapper)));
            log.info("""

                    ##############################Response info########################################
                    %s
                    ###################################################################################""".formatted(buildResponseInfo(responseWrapper)));
        } catch(JSONException e) {
            log.warn("Fail to extract data", e);
        }
    }

    private String buildRequestInfo(HttpServletRequestWrapper requestWrapper) throws IOException {
        var requestBody = String.join("", IOUtils.readLines(requestWrapper.getInputStream(), UTF_8));
        var userPrincipal = requestWrapper.getUserPrincipal();
        var username = isNull(userPrincipal) ? NO_CONTENT_CHAR : userPrincipal.getName();
        return """
                User: %s
                URL: %s
                Method: %s
                Content-type: %s
                Body: %s""".formatted(username,requestWrapper.getRequestURL().toString(),
                requestWrapper.getMethod(), requestWrapper.getContentType(),
                formatJson(requestBody));
    }

    private String buildResponseInfo(HttpServletResponseWrapper responseWrapper) {
        try {
            var res = responseWrapper.getResponse().getOutputStream().toString();
            var responseStatus = responseWrapper.getStatus();
            return """
                    Status: [%s] %s
                    Content: %s""".formatted(responseStatus, HttpStatus.valueOf(responseStatus).getReasonPhrase(), formatJson(res));
        } catch(IOException e) {
            log.warn("Could not read response body info.");
        }
        return StringUtils.EMPTY;
    }

    private String formatJson(String text) {
        if(StringUtils.isBlank(text)) {
            return NO_CONTENT_CHAR;
        }
        var json = new JSONObject(text);
        IGNORE_FIELDS.forEach(field -> {
            try {
                hideFields(json, field);
            } catch (JSONException e) {
                log.warn("Failed to hide JSON data.", e);
            }
        });
        return json.toString(INDENT_SPACES);
    }

    private void hideFields(JSONObject json, String field) {
        var removed = json.remove(field);
        if(nonNull(removed) && !StringUtils.equals(removed.toString(), "null")) {
            json.put(field, StringUtils.leftPad(StringUtils.EMPTY, LENGTH_CHARACTERS, CHAR_HIDE_CONTENT));
        }
        var iterator = json.keys();
        while(iterator.hasNext()) {
            var key = iterator.next();
            var childObj = json.get(key);
            if(childObj instanceof JSONArray arrayChildObj) {
                for(var i = 0; i < arrayChildObj.length(); i++) {
                    hideFieldOfJsonObject(arrayChildObj.get(i), field);
                }
            }
            hideFieldOfJsonObject(childObj, field);
        }
    }

    private void hideFieldOfJsonObject(Object item, String field) {
        if(item instanceof JSONObject obj) {
            hideFields(obj, field);
        }
    }
}