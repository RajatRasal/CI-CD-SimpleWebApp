package ic.doc;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;

import ic.doc.web.MdResultPage;
import java.io.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.*;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MDResultPageTest {
  @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
  private final MdResultPage resultPage = new MdResultPage("query", "answer");
  private final HttpServletResponse respMock = context.mock(HttpServletResponse.class);

  private ServletOutputStream so;
  private BufferedReader reader;
  private OutputStream out;

  @Before
  public void setUp() throws IOException {
    PipedInputStream pipeInput = new PipedInputStream();
    reader = new BufferedReader(new InputStreamReader(pipeInput));
    out = new BufferedOutputStream(new PipedOutputStream(pipeInput));

    int i = 0;
    so =
        new ServletOutputStream() {
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
          public void setWriteListener(WriteListener writeListener) {}
        };
  }

  @Test
  public void correctContentTypeIsSet() throws IOException {
    context.checking(
        new Expectations() {
          {
            allowing(respMock).getOutputStream();
            will(returnValue(so));
            oneOf(respMock).setContentType("text/markdown");
            allowing(respMock);
          }
        });

    resultPage.writeTo(respMock);
  }

  @Test
  public void markdownHasCorrectContentDisposition() throws IOException {
    context.checking(
        new Expectations() {
          {
            allowing(respMock).getOutputStream();
            will(returnValue(so));
            oneOf(respMock)
                .setHeader(with("Content-Disposition"), with(containsString("attachment")));
            allowing(respMock);
          }
        });

    resultPage.writeTo(respMock);
  }

  @Test
  public void markdownDownloadHasCorrectFileType() throws IOException {
    context.checking(
        new Expectations() {
          {
            allowing(respMock).getOutputStream();
            will(returnValue(so));
            oneOf(respMock).setHeader(with("Content-Disposition"), with(containsString(".md")));
            allowing(respMock);
          }
        });

    resultPage.writeTo(respMock);
  }

  @Test
  public void dataIsWrittenToOutputStream() throws IOException {
    context.checking(
        new Expectations() {
          {
            allowing(respMock).getOutputStream();
            will(returnValue(so));
            allowing(respMock);
          }
        });

    resultPage.writeTo(respMock);
    assertTrue(reader.ready());
  }
}
