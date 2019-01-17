package ic.doc.web;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class MarkupResultPage {

  private final String query;
  private final String answer;

  public MarkupResultPage(String query, String answer) {
    this.query = query;
    this.answer = answer;
  }

  public void writeTo(HttpServletResponse resp) throws IOException {

    File tempFile = File.createTempFile("prefix-", "-suffix");

    resp.setContentType("text/plain");
    resp.setHeader("Content-Disposition", "attachment; filename=result.md");

    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

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

    writer.close();

    FileInputStream fileInputStream = new FileInputStream(tempFile);

    OutputStream outputStream = resp.getOutputStream();
    fileInputStream.transferTo(outputStream);
  }
}
