/*
 * Copyright (c) 1997-2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.catalina.core;

import org.apache.catalina.Connector;
import org.apache.catalina.Context;
import org.apache.catalina.HttpResponse;
import org.apache.catalina.Request;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

/**
 * Dummy response object, used for JSP precompilation.
 *
 * @author Remy Maucherat
 * @version $Revision: 1.2 $ $Date: 2005/12/08 01:27:34 $
 */

public class DummyResponse implements HttpResponse, HttpServletResponse {

    public DummyResponse() {
    }

    @Override
    public void setAppCommitted(boolean appCommitted) {
    }

    @Override
    public boolean isAppCommitted() {
        return false;
    }

    @Override
    public Connector getConnector() {
        return null;
    }

    @Override
    public void setConnector(Connector connector) {
    }

    @Override
    public int getContentCount() {
        return -1;
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void setContext(Context context) {
    }

    @Override
    public boolean getIncluded() {
        return false;
    }

    @Override
    public void setIncluded(boolean included) {
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public Request getRequest() {
        return null;
    }

    @Override
    public void setRequest(Request request) {
    }

    @Override
    public ServletResponse getResponse() {
        return null;
    }

    @Override
    public OutputStream getStream() {
        return null;
    }

    @Override
    public void setStream(OutputStream stream) {
    }

    @Override
    public void setSuspended(boolean suspended) {
    }

    @Override
    public boolean isSuspended() {
        return false;
    }

    @Override
    public void setError() {
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public ServletOutputStream createOutputStream() throws IOException {
        return null;
    }

    @Override
    public void finishResponse() throws IOException {
    }

    @Override
    public int getContentLength() {
        return -1;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public PrintWriter getReporter() {
        return null;
    }

    @Override
    public void recycle() {
    }

    public void write(int b) throws IOException {
    }

    public void write(byte b[]) throws IOException {
    }

    public void write(byte b[], int off, int len) throws IOException {
    }

    @Override
    public void flushBuffer() throws IOException {
    }

    @Override
    public int getBufferSize() {
        return -1;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String charEncoding) {
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return null;
    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {
    }

    @Override
    public void resetBuffer() {
    }

    @Override
    public void resetBuffer(boolean resetWriterStreamFlags) {
    }

    @Override
    public void setBufferSize(int size) {
    }

    @Override
    public void setContentLength(int length) {
    }

    @Override
    public void setContentLengthLong(long length) {
    }

    @Override
    public void setContentType(String type) {
    }

    @Override
    public void setLocale(Locale locale) {
    }

    @Override
    public String getHeader(String name) {
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return null;
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return null;
    }

    @Override
    public void addSessionCookieInternal(final Cookie cookie) {
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public int getStatus() {
        return -1;
    }

    @Override
    public void reset(int status, String message) {
    }

    @Override
    public void addCookie(Cookie cookie) {
    }

    @Override
    public void addDateHeader(String name, long value) {
    }

    @Override
    public void addHeader(String name, String value) {
    }

    @Override
    public void addIntHeader(String name, int value) {
    }

    @Override
    public boolean containsHeader(String name) {
        return false;
    }

    @Override
    public String encodeRedirectURL(String url) {
        return null;
    }

    public String encodeRedirectUrl(String url) {
        return null;
    }

    @Override
    public String encodeURL(String url) {
        return null;
    }

    public String encodeUrl(String url) {
        return null;
    }

    @Override
    public String encode(String url) {
        return null;
    }

    @Override
    public void sendAcknowledgement() throws IOException {
    }

    @Override
    public void sendError(int status) throws IOException {
    }

    @Override
    public void sendError(int status, String message) throws IOException {
    }

    @Override
    public void setError(int status, String message) {
    }

    @Override
    public void sendRedirect(String location) throws IOException {
    }

    @Override
    public void setDateHeader(String name, long value) {
    }

    @Override
    public void setHeader(String name, String value) {
    }

    @Override
    public void setIntHeader(String name, int value) {
    }

    @Override
    public void setStatus(int status) {
    }

    public void setStatus(int status, String message) {
    }

    @Override
    public void setDetailMessage(String msg) {
    }

    @Override
    public String getDetailMessage() {
        return null;
    }

    @Override
    public void sendRedirect(String location, int sc, boolean clearBuffer) throws IOException {
    }

}
