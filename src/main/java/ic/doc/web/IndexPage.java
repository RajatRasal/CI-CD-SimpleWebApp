package ic.doc.web;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

public class IndexPage implements Page {

  /**
   * Writes the home page of the site in HTML to a HttpServletResponse, ready to be
   * served to the user.
   *
   * @param resp          The HttpServletResponse to be written to.
   * @throws IOException  Possibly occurs due to file calls.
   */
  public void writeTo(HttpServletResponse resp) throws IOException {
    resp.setContentType("text/html");
    PrintWriter writer = resp.getWriter();

    // Header
    writer.println("<html>");
    writer.println("<head><title>Welcome</title></head>");
    writer.println("<body>");

    // Content
    writer.println(
        "<h1>Welcome!!</h1>"
            + "<p>Enter your query in the box below: "
            + "<form>"
            + "<input type=\"text\" name=\"query\" />"
            + "<br><br><input type=\"submit\">"
            + "<br><br><input type=\"checkbox\" name=\"format\" value=\"md\"> Download as markdown?"
            + "<br><br><input type=\"checkbox\" name=\"format\" value=\"pdf\"> Download as pdf?"
            + "</form>"
            + "</p>");

    // Footer
    writer.println("</body>");
    writer.println("</html>");
  }
}
