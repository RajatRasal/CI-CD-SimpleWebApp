package ic.doc.web;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public interface Page {
  /**
   * Write some data to a HttpServletResponse, which will be
   * served to the user. Behaviour depends on needs of implementation.
   *
   * @param resp          The HttpServletResponse to be written to.
   * @throws IOException  Possibly occurs due to file calls.
   */
  void writeTo(HttpServletResponse resp) throws IOException;
}
