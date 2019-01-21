package ic.doc.web;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.lang.ProcessBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.servlet.http.HttpServletResponse;

public class PDFResultPage extends MDResultPageTemplate {

  public PDFResultPage(String query, String answer) {
    super(query, answer);
  }

  public void writeTo(HttpServletResponse resp) throws IOException {
    File mdFile = File.createTempFile("result", ".md");
    File pdfFile = File.createTempFile("result", ".pdf");

    resp.setContentType("application/pdf");

    writeMDTemplateToFile(mdFile);

    String mdName = mdFile.getPath();
    String pdfName = pdfFile.getPath();
    
    ProcessBuilder pb = new ProcessBuilder("pandoc", mdName, "-f", "markdown", "-o", pdfName);
    try {
      pb.start().waitFor();
    } catch (InterruptedException e) {
      e.printStackTrace();
      throw new IOException();
    }

    Files.copy(pdfFile.toPath(), resp.getOutputStream());

    mdFile.delete();
    pdfFile.delete();
  }
}
