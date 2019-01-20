package ic.doc.web;

import java.nio.file.Files;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class MDResultPage implements Page {

  private final String query;
  private final String answer;

  public MDResultPage(String query, String answer) {
    this.query = query;
    this.answer = answer;
  }

  public void writeTo(HttpServletResponse resp) throws IOException {
    resp.setContentType("text/markdown");

    File temp = File.createTempFile("result", ".md");
    PrintWriter writer = new PrintWriter(temp);

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

    Files.copy(temp.toPath(), resp.getOutputStream());
    temp.delete();
  }
}
