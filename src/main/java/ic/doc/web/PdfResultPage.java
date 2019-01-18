package ic.doc.web;

import java.lang.ProcessBuilder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;

public class PdfResultPage {

  private final String query;
  private final String answer;

  public PdfResultPage(String query, String answer) {
    this.query = query;
    this.answer = answer;
  }

  public void writeTo(HttpServletResponse resp) throws IOException, InterruptedException {
    System.out.println("PDF ========================================");
    File mdFile = File.createTempFile("prefix-", "-suffix");
    File pdfFile = File.createTempFile("prefix-", "-suffix");

    resp.setContentType("application/pdf");
    resp.setHeader("Content-Disposition", "attachment; filename=result.pdf");

    BufferedWriter writer = new BufferedWriter(new FileWriter(mdFile));

    writer.write("# Your query result:\n\n");
    writer.write("(submitted query: **" + query + "**)\n");

    // Content
    if (answer == null || answer.isEmpty()) {
      writer.write("Sorry, we didn't understand *" + query + "*.\n");
    } else {
      for (String line : answer.split("\n")) {
        writer.write("> " + line + "\n");
      }
    }

    String mdName = mdFile.getName();
    String pdfName = mdFile.getName();

    Process pb = new ProcessBuilder("pandoc", "-s", mdName, "-o", pdfName).start();
    pb.waitFor();
    writer.close();

    System.out.println("PROCESS BUILDER *****************************");

    FileInputStream fileInputStream = new FileInputStream(pdfFile);
    OutputStream outputStream = resp.getOutputStream();
    fileInputStream.transferTo(outputStream);
  }
}
