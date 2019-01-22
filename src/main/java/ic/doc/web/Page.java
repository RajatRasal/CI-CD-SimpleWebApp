package ic.doc.web;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public interface Page {
  void writeTo(HttpServletResponse resp) throws IOException;
}
