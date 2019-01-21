package ic.doc.web;

import java.nio.file.Files;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class MDResultPage extends MDResultPageTemplate {

  public MDResultPage(String query, String answer) {
    super(query, answer);
  }

  public void writeTo(HttpServletResponse resp) throws IOException {
    resp.setContentType("text/markdown");
    resp.setHeader("Content-Disposition", "attachment; filename=result.md");

    File temp = File.createTempFile("result", ".md");
    writeMDTemplateToFile(temp);
    Files.copy(temp.toPath(), resp.getOutputStream());
    temp.delete();
  }
}