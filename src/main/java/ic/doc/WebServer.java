package ic.doc;

import ic.doc.web.HtmlResultPage;
import ic.doc.web.IndexPage;
import ic.doc.web.MdResultPage;
import ic.doc.web.Page;
import ic.doc.web.PdfResultPage;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebServer {

  /**
   * Instantiates and starts the web server.
   *
   * @throws Exception if any calls to java servlet calls cause an exception
   */
  public WebServer() throws Exception {
    Server server = new Server(Integer.valueOf(System.getenv("PORT")));

    ServletHandler handler = new ServletHandler();
    handler.addServletWithMapping(new ServletHolder(new Website()), "/*");
    server.setHandler(handler);

    server.start();
  }

  static class Website extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
      String query = req.getParameter("query");
      String format = req.getParameter("format");
      Page page;

      if (query == null) {
        page = new IndexPage();
      } else if (format == null) {
        page = new HtmlResultPage(query, new QueryProcessor().process(query));
      } else if (format.equals("pdf")) {
        page = new PdfResultPage(query, new QueryProcessor().process(query));
      } else {
        page = new MdResultPage(query, new QueryProcessor().process(query));
      }

      page.writeTo(resp);
    }
  }

  public static void main(String[] args) throws Exception {
    new WebServer();
  }
}
