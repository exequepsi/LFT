public class Token {
    public String lexeme = "";
    public final int tag;
    public Token(int t) {
        tag = t;
        if (t != -1) lexeme = lexeme + (char) t;
        else lexeme = "EOF";
    }
    public Token(int t, String s) {
        tag = t;
        if (t != -1) lexeme = lexeme + s;
        else lexeme = "EOF";
    }
    public String toString() {
        return "<" + tag + ", " + lexeme + " >";
    }
    public static final Token
        not = new Token('!'),
        plus = new Token('+'),
        minus = new Token('-'),
        mult = new Token('*'),
        div = new Token('/'),
        semicolon = new Token(';'),
        lpt = new Token('('),
        rpt = new Token(')'),
        lpg = new Token('{'),
        rpg = new Token('}');
}
