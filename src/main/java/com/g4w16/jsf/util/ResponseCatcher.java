/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.jsf.util;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
*
* @author SK
*/
public class ResponseCatcher implements HttpServletResponse {
/** the backing output stream for text content */
    CharArrayWriter output;
    /** a writer for the servlet to use */
    PrintWriter writer;
    /** a real response object to pass tricky methods to */
    HttpServletResponse response;
    private ServletOutputStream soStream;

    /**
     * Create the response wrapper.
     */
    public ResponseCatcher(HttpServletResponse response) {
        this.response = response;
        output = new CharArrayWriter();//loaded
        writer = new PrintWriter(output, true);
    }

    /**
     * Return a print writer so it can be used by the servlet. The print
     * writer is used for text output.
     */
    public PrintWriter getWriter() {
        return writer;
    }
    public void flushBuffer() throws IOException {
        writer.flush();
    }
    public boolean isCommitted() {
        return false;
    }
    public boolean containsHeader(String arg0) {
        return false;
    }
    /* wrapped methods */
    public String encodeURL(String arg0) {
        return response.encodeURL(arg0);
    }
    public String encodeRedirectURL(String arg0) {
        return response.encodeRedirectURL(arg0);
    }
    public String encodeUrl(String arg0) {
        return response.encodeUrl(arg0);
    }
    public String encodeRedirectUrl(String arg0) {
        return response.encodeRedirectUrl(arg0);
    }
    public String getCharacterEncoding() {
        return response.getCharacterEncoding();
    }
    public String getContentType() {
        return response.getContentType();
    }
    public int getBufferSize() {
        return response.getBufferSize();
    }
    public Locale getLocale() {
        return response.getLocale();
    }
    public void sendError(int arg0, String arg1) throws IOException {
        response.sendError(arg0, arg1);
    }
    public void sendError(int arg0) throws IOException {
        response.sendError(arg0);
    }
    public void sendRedirect(String arg0) throws IOException {
        response.sendRedirect(arg0);
    }
    /* null ops */
    public void addCookie(Cookie arg0) {}
    public void setDateHeader(String arg0, long arg1) {}
    public void addDateHeader(String arg0, long arg1) {}
    public void setHeader(String arg0, String arg1) {}
    public void addHeader(String arg0, String arg1) {}
    public void setIntHeader(String arg0, int arg1) {}
    public void addIntHeader(String arg0, int arg1) {}
    public void setStatus(int arg0) {}
    public void setStatus(int arg0, String arg1) {}
    public void setCharacterEncoding(String arg0) {}
    public void setContentLength(int arg0) {}
    public void setContentType(String arg0) {}
    public void setBufferSize(int arg0) {}
    public void resetBuffer() {}
    public void reset() {}
    public void setLocale(Locale arg0) {}
    /* unsupported methods */
    public ServletOutputStream getOutputStream() throws IOException {
        return soStream;
    }
    /**
     * Return the captured content.
     */
    @Override
    public String toString() {
        return output.toString();
    }
    public String getHeader(String string) {
        return null;
    }
    public Collection<String> getHeaders(String string) {
        return null;
    }
    public Collection<String> getHeaderNames() {
        return null;
    }
    public int getStatus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setContentLengthLong(long l) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}