package ic.doc.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.servlet.http.HttpServletResponse;

public class PdfResultPage extends MdResultPageTemplate {

  public PdfResultPage(String query, String answer) {
    super(query, answer);
  }

  /**
   * Write result of the query to a HttpServletResponse in pdf format, which will be served to the
   * user.
   *
   * @param resp The HttpServletResponse to be written to.
   * @throws IOException Possibly occurs due to file calls.
   */
  public void writeTo(HttpServletResponse resp) throws IOException {
    File mdFile = File.createTempFile("result", ".md");
    File pdfFile = File.createTempFile("result", ".pdf");

    resp.setContentType("application/pdf");

    writeMdTemplateToFile(mdFile);

    String mdName = mdFile.getPath();
    String pdfName = pdfFile.getPath();

    ProcessBuilder pb = new ProcessBuilder("pandoc", mdName, "-f", "markdown", "-o", pdfName);
    try {
      pb.start().waitFor();
    } catch (InterruptedException ie) {
      ie.printStackTrace();
      throw new IOException();
    }

    Files.copy(pdfFile.toPath(), resp.getOutputStream());

    mdFile.delete();
    pdfFile.delete();
  }
}
