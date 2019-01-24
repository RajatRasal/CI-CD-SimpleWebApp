package ic.doc.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.servlet.http.HttpServletResponse;

public class MdResultPage extends MdResultPageTemplate {

  public MdResultPage(String query, String answer) {
    super(query, answer);
  }

  /**
   * Writes the result of a query in markdown format to the HttpServletResponse,
   * ready to be served to the user.
   *
   * @param resp          The HttpServletResponse to be written to.
   * @throws IOException  Possibly occurs due to file calls.
   */
  public void writeTo(HttpServletResponse resp) throws IOException {
    resp.setContentType("text/markdown");
    resp.setHeader("Content-Disposition", "attachment; filename=result.md");

    File temp = File.createTempFile("result", ".md");
    writeMdTemplateToFile(temp);
    Files.copy(temp.toPath(), resp.getOutputStream());
    temp.delete();
  }
}
