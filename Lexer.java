//  version 1.4

import java.io.*;
import java.util.*;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';
    
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }
    
    /*  Retro-compatibility fix:
     *
     *  Method switch() does not support type String parameter until Java 7 (1.7.x).
     *  The fix can be enabled uncommenting the marked lines at section "Keywords, Identifiers and
     *  Numbers > Keywords" and the single line below (first one after this box).
     */
    //public enum Keyword { IF, ELSE, WHILE, READ, PRINT  };    // Java 6 fix (uncomment to enable)

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') {
                line++;
            }
            readch(br);
        }
        
        /* Symbols */
        
        switch (peek) {
        case '!':
            peek = ' ';
            return Token.not;
        case '+':
            peek = ' ';
            return Token.plus;
        case '-':
            peek = ' ';
            return Token.minus;
        case '*':
            peek = ' ';
            return Token.mult;
        case '/':
            peek = ' ';
            return Token.div;
        case ';':
            peek = ' ';
            return Token.semicolon;
        case '(':
            peek = ' ';
            return Token.lpt;
        case ')':
            peek = ' ';
            return Token.rpt;
        case '{':
            peek = ' ';
            return Token.lpg;
        case '}':
            peek = ' ';
            return Token.rpg;
        
        /* Operators */
        
        case '&':
            readch(br);
            if (peek == '&') {
                peek = ' ';
                return Word.and;
            } else {
                System.err.println( "Lexer :: wrong character"
                                    + " after '&' : '"  + peek + "'" );
                return null;
            }
        case '|':
            readch(br);
            if (peek == '|') {
                peek = ' ';
                return Word.or;
            } else {
                System.err.println( "Lexer :: wrong character"
                                    + " after '|' : '"  + peek + "'" );
                return null;
            }
        case ':':
            readch(br);
            if (peek == '=') {
                peek = ' ';
                return Word.assign;
            } else {
                System.err.println( "Lexer :: wrong character"
                                    + " after ':' : '"  + peek + "'" );
                return null;
            }
        case '<':
            readch(br);
            if (peek == '=') {
                peek = ' ';
                return Word.le;
            } 
            else if (peek == '>') {
                 peek = ' ';
                 return Word.ne;
            }else{
                return Word.lt;
            }
        case '>':
            readch(br);
            if (peek == '=') {
                peek = ' ';
                return Word.ge;
            } else {
                 return Word.gt;  
            }
        case '=':
            readch(br);
            if (peek == '=') {
                peek = ' ';
                return Word.eq;
            } else {
                System.err.println( "Lexer :: wrong character"
                                    + " after '=' : '"  + peek + "'" );
                return null;
            }
            
        /* End Of File */
        
        case (char)-1:
            return new Token(Tag.EOF);
        
        /* Keywords, Identifiers and Numbers */
        
        default:
            if (Character.isLetter(peek) || peek == '_') {
                String id = Character.toString(peek);
                //Keyword kw = Keyword.valueOf(id.toUpperCase());       // Java 6 fix (uncomment to enable)
                readch(br);
                while (Character.isLetter(peek) || peek == '_' || Character.isDigit(peek)){ 
                    id = id + peek;
                    readch(br);
                }
            
                /* Keywords */
            
                //switch (kw) {         // Java 6 fix (uncomment to enable)
                switch(id){             // Java 6 fix (comment this line if the above one is uncommented)
                //case IF:              // Java 6 fix (uncomment to enable)
                case "if":              // Java 6 fix (comment this line if the above one is uncommented)
                    return Word.iftok;
                //case ELSE:            // Java 6 fix (uncomment to enable)
                case "else":            // Java 6 fix (comment this line if the above one is uncommented)
                    return Word.elsetok;
                //case WHILE:           // Java 6 fix (uncomment to enable)
                case "while":           // Java 6 fix (comment this line if the above one is uncommented)
                    return Word.whiletok;
                //case READ:            // Java 6 fix (uncomment to enable)
                case "read":            // Java 6 fix (comment this line if the above one is uncommented)
                    return Word.read;
                //case PRINT:           // Java 6 fix (uncomment to enable)
                case "print":           // Java 6 fix (comment this line if the above one is uncommented)
                    return Word.print;
                
                /* Identifiers (automata) */
                
                default:
                    int state = 0;
                    char ch = id.charAt(0);
                    for(int i = 0; i < id.length(); i++) {
                        ch = id.charAt(i);
                        switch (state){
                        case 0:
                            if(ch == '_')
                                state = 1;
                            else if(Character.isLetter(ch))
                                state = 2;
                            else
                                state = -1;
                            break;
                        case 1:
                            if(ch == '_')
                                state = 1 ;
                            else if (Character.isLetter(ch) || Character.isDigit(ch))
                                state = 2;
                            else  
                                state = -1;
                            break;
                        case 2:
                            if(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_')
                                state = 2;
                            else 
                                state = -1;
                            break;
                        }
                    }
                    if (state == 2)
                        return new Word( Tag.ID, id);
                    else if (state == 1) {
                        System.err.println( "Lexer :: identifier's name not allowed;"
                                            + " identifiers' names should contain other characters than underscores alone");
                        return null;
                    }else {
                        System.err.println( "Lexer :: bad identifier name;"
                                            + " wrong character '" + ch + "' detected");
                        return null;
                    }
                }
                    
            /* Numbers */
            
            } else if (Character.isDigit(peek)) {
                String num = Character.toString(peek);
                readch(br);
                while (Character.isDigit(peek)){ 
                    num = num + peek;
                    readch(br);
                }
                return new Number(Tag.NUM, num);   
            } else {
                System.err.println( "Lexer :: extraneous character"
                                    + " '" + peek + "' detected");
                return null;
            }
        }
    }
	
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "dataLexer.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Found: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }

}
