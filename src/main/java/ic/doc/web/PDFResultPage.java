package ic.doc.web;

import java.io.PrintWriter;
import java.lang.ProcessBuilder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import javax.servlet.http.HttpServletResponse;

public class PDFResultPage {

  private final String query;
  private final String answer;

  public PDFResultPage(String query, String answer) {
    this.query = query;
    this.answer = answer;
  }

  public void writeTo(HttpServletResponse resp) throws IOException, InterruptedException {
    File mdFile = File.createTempFile("result", ".md");
    File pdfFile = File.createTempFile("result", ".pdf");

    resp.setContentType("application/pdf");

    PrintWriter writer = new PrintWriter(mdFile);

    writer.write("# Your query result:\n\n");
    writer.write("(submitted query: **" + query + "**)\n");
    if (answer == null || answer.isEmpty()) {
      writer.write("Sorry, we didn't understand *" + query + "*.\n");
    } else {
      for (String line : answer.split("\n")) {
        writer.write("> " + line + "\n");
      }
    }

    writer.close();

    String mdName = mdFile.getPath();
    String pdfName = pdfFile.getPath();

    ProcessBuilder pb = new ProcessBuilder("pandoc", mdName, "-f", "markdown", "-o", pdfName);
    pb.start().waitFor();

    Files.copy(pdfFile.toPath(), resp.getOutputStream());
    mdFile.delete();
    pdfFile.delete();
  }
}
