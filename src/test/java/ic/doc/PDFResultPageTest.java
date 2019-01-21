package ic.doc;

import ic.doc.web.PDFResultPage;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.*;
import java.io.*;
import java.util.concurrent.CountDownLatch;

import static junit.framework.TestCase.assertTrue;

public class PDFResultPageTest {
    Synchroniser synchroniser = new Synchroniser();

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery() {{
        setThreadingPolicy(synchroniser);
    }};

    PDFResultPage resultPage = new PDFResultPage("query", "answer");
    HttpServletResponse respMock = context.mock(HttpServletResponse.class);

    ServletOutputStream so;
    BufferedReader reader;
    OutputStream out;

    private CountDownLatch lock;

    Writer writer;

    @Before
    public void setUp() throws IOException {
        lock = new CountDownLatch(1);

        int i = 0;
        so = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                lock.countDown();
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
            }
        };

        writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                out.write(new String(cbuf).getBytes(), off, len);
                out.flush();
            }

            @Override
            public void flush() throws IOException { out.flush(); }

            @Override
            public void close() throws IOException { out.close(); }
        };
    }

    @Test
    public void correctContentTypeIsSet() throws IOException {
        context.checking(new Expectations() {{
            allowing(respMock).getOutputStream(); will(returnValue(so));
            oneOf(respMock).setContentType("application/pdf");
            allowing(respMock);
        }});

        resultPage.writeTo(respMock);
    }
}