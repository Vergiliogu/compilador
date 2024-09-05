public class App {
    public static void main(String[] args) {
      System.out.println(args);
        Lexico lexico = new Lexico();
        //...
        String input = args[1];
        lexico.setInput(input);
        //...
        try {
            Token t = null;
            while ( (t = lexico.nextToken()) != null ) {
                System.out.println(t.getLexeme());
            }
        }
        catch ( LexicalError e ) {
            System.err.println(e.getMessage() + "e;, em e;" + e.getPosition());
        }
    }
}