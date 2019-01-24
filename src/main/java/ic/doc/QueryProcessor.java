package ic.doc;

public class QueryProcessor {
  
  /**
   * Takes a query and returns the result of this query in the database of known information.
   *
   * @param query   the query to be run against the database
   * @return        result of query (or an empty string if no result is present)
   */
  public String process(String query) {
    StringBuilder results = new StringBuilder();
    if (query.toLowerCase().contains("shakespeare")) {
      results.append(
          "William Shakespeare (26 April 1564 - 23 April 1616) was an\n"
              + "English poet, playwright, and actor, widely regarded as the greatest\n"
              + "writer in the English language and the world's pre-eminent dramatist. \n");
      results.append(System.lineSeparator());
    }

    if (query.toLowerCase().contains("asimov")) {
      results.append(
          "Isaac Asimov (2 January 1920 - 6 April 1992) was an\n"
              + "American writer and professor of Biochemistry, famous for\n"
              + "his works of hard science fiction and popular science. \n");
      results.append(System.lineSeparator());
    }
    return results.toString();
  }
}
