package ic.doc;

import ic.doc.web.HTMLResultPage;
import ic.doc.web.IndexPage;
import ic.doc.web.MDResultPage;
import ic.doc.web.PDFResultPage;
import ic.doc.web.Page;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebServer {

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
          page = new HTMLResultPage(query, new QueryProcessor().process(query));
        } else if (format.equals("pdf")) {
          page = new PDFResultPage(query, new QueryProcessor().process(query));
        } else {
          page = new MDResultPage(query, new QueryProcessor().process(query));
        }

        page.writeTo(resp);
     }
  }

  public static void main(String[] args) throws Exception {
     new WebServer();
  }
}

