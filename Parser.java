//  version 4.1

import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    
    void move() {
        look = lex.lexical_scan(pbr);
        if (look == null) error("unidentified token");
        else System.err.println("token found: " + look);
    }

    void error(String s) { 
        throw new Error(s + " (at input line " + lex.line + ")"); 
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("token with tag " + look + " found, <" + t + "> was expected");
    }

    public void start() {
        int expr_val = 0;
        switch (look.tag) {
        case Tag.NUM:
        case '(':
            expr_val = expr();
            match(Tag.EOF);
            break;
        default:
            error("start(): wrong or misplaced " + look + " token found; "
                + "unknown was expected.");
            break;
        }
        System.out.println("\n"
                             + "Syntax analysis: OK." + "\n"
                             + "Result: " + expr_val);
    }

    private int expr() {
        int term_val, exprp_val = 0;
        switch (look.tag) {
        case Tag.NUM:
        case '(':
            term_val = term();
            exprp_val = exprp(term_val);
            break;
        default:
            error("start(): wrong or misplaced " + look + " token found; "
                + "unknown was expected.");
            break;
        }
        return exprp_val;
    }

    private int exprp(int exprp_i) {
        int term_val, exprp_val = 0;
        switch (look.tag) {
        case '+':
            match('+');
            term_val = term();
            exprp_val = exprp(exprp_i + term_val);
            break;
        case '-':
            match('-');
            term_val = term();
            exprp_val = exprp(exprp_i - term_val);
            break;
        case ')':
        case Tag.EOF:
            exprp_val = exprp_i;
            break;
        default:
            error("start(): wrong or misplaced " + look + " token found; "
                + "unknown was expected.");
            break;
        }
        return exprp_val;
    }

    private int term() {
        int fact_val, termp_val = 0;
        switch (look.tag) {
        case Tag.NUM:
        case '(':
            fact_val = fact();
            termp_val = termp(fact_val);
            break;
        default:
            error("start(): wrong or misplaced " + look + " token found; "
                + "unknown was expected.");
            break;
        }
        return termp_val;
    }
    
    private int termp(int termp_i) {
        int fact_val, termp_val = 0;
        switch (look.tag) {
        case '*':
            match('*');
            fact_val = fact();
            termp_val = termp(termp_i * fact_val);
            break;
        case '/':
            match('/');
            fact_val = fact();
            termp_val = termp(termp_i / fact_val);
            break;
        case '+':
        case '-':
        case ')':
        case Tag.EOF:
            termp_val = termp_i;
            break;
        default:
            error("start(): wrong or misplaced " + look + " token found; "
                + "unknown was expected.");
            break;
        }
        return termp_val;
    }
    
    private int fact() {
        int fact_val = 0;
        switch (look.tag) {
        case Tag.NUM:
            fact_val = Integer.parseInt(look.lexeme);
            move();
            break;
        case '(':
            match('(');
            fact_val = expr();
            match(')');
            break;
        default:
            error("start(): wrong or misplaced " + look + " token found; "
                + "unknown was expected.");
            break;
        }
        return fact_val;
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "dataParser.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }
}
