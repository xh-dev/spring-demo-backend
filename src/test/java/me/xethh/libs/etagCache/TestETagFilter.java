package me.xethh.libs.etagCache;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

@DisplayName("Test ETag Filter")
class TestETagFilter {
    @Test
    @DisplayName("Test filter hits")
    void testFilterHits() throws ServletException, IOException {
        final var etagservice = new ETagServiceCaffeine();
        etagservice.putEtag("/api/test?", "hash");
        final var filter = new EtagFilter(etagservice);

        final var mockRequest = Mockito.mock(HttpServletRequest.class);
        final var mockResponse = Mockito.mock(HttpServletResponse.class);

        when(mockRequest.getRequestURI()).thenReturn("/api/test");
        when(mockRequest.getQueryString()).thenReturn("");
        when(mockRequest.getHeader("If-None-Match")).thenReturn("hash");

        filter.doFilter(mockRequest, mockResponse, Mockito.mock(FilterChain.class));
        verify(mockResponse, Mockito.times(1)).setStatus(HttpServletResponse.SC_NOT_MODIFIED);
    }

    @Test
    @DisplayName("Test filter not hits")
    void testFilterNotHits() throws ServletException, IOException {
        final var etagservice = new ETagServiceCaffeine();
        final var filter = new EtagFilter(etagservice);

        final var mockRequest = Mockito.mock(HttpServletRequest.class);
        final var mockResponse = Mockito.mock(HttpServletResponse.class);
        final var mockFilterChain = Mockito.mock(FilterChain.class);

        when(mockRequest.getRequestURI()).thenReturn("/api/test");
        when(mockRequest.getQueryString()).thenReturn("");
        when(mockRequest.getHeader("If-None-Match")).thenReturn(null);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        // Tell the mock to return our custom writer
        when(mockResponse.getWriter()).thenReturn(printWriter);

        // Call the method that uses the response
        mockResponse.getWriter().write("hihi");
        printWriter.flush();

        doAnswer(invocationOnMock -> {
            HttpServletResponse response = invocationOnMock.getArgument(1);
            response.getWriter().write("hihi");
            return null;
        }).when(mockFilterChain).doFilter(any(), any());

        final var generatedETag=ETagService.genETagValue("hihi".getBytes());
        Assertions.assertNull(etagservice.getEtag("/api/test?"));

        Assertions.assertThrows(NullPointerException.class, ()-> filter.doFilter(mockRequest, mockResponse, mockFilterChain) );
        verify(mockResponse, Mockito.times(1)).setHeader("ETag", generatedETag);
        Assertions.assertEquals(generatedETag, etagservice.getEtag("/api/test?"));
    }
}
