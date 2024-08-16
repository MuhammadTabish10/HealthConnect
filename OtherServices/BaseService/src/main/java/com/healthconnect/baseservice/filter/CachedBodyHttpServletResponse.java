package com.healthconnect.baseservice.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class CachedBodyHttpServletResponse extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream cachedBodyOutputStream = new ByteArrayOutputStream();
    private final PrintWriter writer = new PrintWriter(cachedBodyOutputStream);

    public CachedBodyHttpServletResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new CachedBodyServletOutputStream(this.cachedBodyOutputStream);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return this.writer;
    }

    public byte[] getCachedBody() {
        this.writer.flush();  // Ensure writer is flushed
        return this.cachedBodyOutputStream.toByteArray();
    }


    private static class CachedBodyServletOutputStream extends ServletOutputStream {

        private final ByteArrayOutputStream cachedBodyOutputStream;

        public CachedBodyServletOutputStream(ByteArrayOutputStream cachedBodyOutputStream) {
            this.cachedBodyOutputStream = cachedBodyOutputStream;
        }

        @Override
        public void write(int b) throws IOException {
            cachedBodyOutputStream.write(b);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
        }

    }
}
