package ic.doc.web;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

public class HtmlResultPage implements Page {

  private final String query;
  private final String answer;

  public HtmlResultPage(String query, String answer) {
    this.query = query;
    this.answer = answer;
  }
  
  /**
   * Writes a HTML formatted result page to the given HttpServletResponse
   * writer. Uses the query and answer given on construction.
   *
   * @param resp - the HttpServletResponse which will be served
   * @throws IOException - potentially caused by resp.getWriter();
   */
  public void writeTo(HttpServletResponse resp) throws IOException {
    resp.setContentType("text/html");
    PrintWriter writer = resp.getWriter();

    // Header
    writer.println("<html>");
    writer.println("<head><title>" + query + "</title></head>");
    writer.println("<body>");

    // Content
    if (answer == null || answer.isEmpty()) {
      writer.println("<h1>Sorry</h1>");
      writer.print("<p>Sorry, we didn't understand <em>" + query + "</em></p>");
    } else {
      writer.println("<h1>" + query + "</h1>");
      writer.println("<p>" + answer.replace("\n", "<br>") + "</p>");
    }

    writer.println("<p><a href=\"/\">Back to Search Page</a></p>");

    // Footer
    writer.println("</body>");
    writer.println("</html>");
  }
}
