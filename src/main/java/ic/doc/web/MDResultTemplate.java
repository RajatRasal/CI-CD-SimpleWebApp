package ic.doc.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public abstract class MDResultTemplate {

  private final String query;
  private final String answer;

  protected MDResultTemplate(String query, String answer) {
    this.query = query;
    this.answer = answer;
  }

  protected void writeMDTemplateToFile(File file) throws FileNotFoundException {
    PrintWriter writer = new PrintWriter(file);

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
  }

}
