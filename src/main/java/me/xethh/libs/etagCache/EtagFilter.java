package me.xethh.libs.etagCache;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

public class EtagFilter extends OncePerRequestFilter {
    public EtagFilter(ETagService etagService){
        this.etagService = etagService;
    }
    private final ETagService etagService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String urlKey = request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        String clientEtag = request.getHeader("If-None-Match");
        String serverEtag = etagService.getEtag(urlKey);

        if (serverEtag != null && serverEtag.equals(clientEtag)) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }

        ContentCachingResponseWrapper wrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(request, wrapper);

        byte[] responseBody = wrapper.getContentAsByteArray();
        if (responseBody.length > 0) {
            final var generatedETag=ETagService.genETagValue(responseBody);
            etagService.putEtag(urlKey, generatedETag);
            response.setHeader("ETag", generatedETag);
        }

        wrapper.copyBodyToResponse();
    }
}