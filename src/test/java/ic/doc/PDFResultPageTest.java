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
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private final PDFResultPage resultPage = new PDFResultPage("query", "answer");
    private final HttpServletResponse respMock = context.mock(HttpServletResponse.class);

    private ServletOutputStream so;

    @Before
    public void setUp() {
        so = new ServletOutputStream() {
            @Override
            public void write(int b) { }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) { }
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