package ic.doc;

import ic.doc.web.MDResultPage;
import ic.doc.web.PDFResultPage;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.*;
import java.io.*;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;

public class MDResultPageTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    MDResultPage resultPage = new MDResultPage("query", "answer");
    HttpServletResponse respMock = context.mock(HttpServletResponse.class);

    ServletOutputStream so;
    BufferedReader reader;
    OutputStream out;

    Writer writer;

    @Before
    public void setUp() throws IOException {
        PipedInputStream pipeInput = new PipedInputStream(2000000);
        reader = new BufferedReader(
                new InputStreamReader(pipeInput));
        out = new BufferedOutputStream(
                new PipedOutputStream(pipeInput));

        int i = 0;
        so = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                out.write(b);
                out.flush();
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
            oneOf(respMock).setContentType("text/markdown");
            allowing(respMock);
        }});

        resultPage.writeTo(respMock);
    }

    @Test
    public void markdownHasCorrectContentDisposition() throws IOException {
        context.checking(new Expectations() {{
            allowing(respMock).getOutputStream(); will(returnValue(so));
            oneOf(respMock).setHeader(with("Content-Disposition"), with(containsString("attachment")));
            allowing(respMock);
        }});

        resultPage.writeTo(respMock);
    }

    @Test
    public void markdownDownloadHasCorrectFileType() throws IOException {
        context.checking(new Expectations() {{
            allowing(respMock).getOutputStream(); will(returnValue(so));
            oneOf(respMock).setHeader(with("Content-Disposition"), with(containsString(".md")));
            allowing(respMock);
        }});

        resultPage.writeTo(respMock);
    }


    @Test
    public void dataIsWrittenToOutputStream() throws IOException {
        context.checking(new Expectations() {{
            allowing(respMock).getOutputStream(); will(returnValue(so));
            allowing(respMock);
        }});

        resultPage.writeTo(respMock);
        assertTrue(reader.ready());
    }

}