package jonelo.sugar.util;

import java.text.MessageFormat;
import java.util.ArrayList;

public class GeneralString {

    private static final char[] hexDigits = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    public static char nibbleToHexChar(int nibble) {
        return hexDigits[(nibble & 0xF)];
    }

    private static final String specialChars = "=: \t\r\n\f#!";

    public GeneralString() {
    }

    public static String replaceString(String source, String oldString, String newString) {
        int pos = source.indexOf(oldString);
        if (pos > -1) {
            StringBuffer sb = new StringBuffer();
            sb.append(source.substring(0, pos));
            sb.append(newString);
            sb.append(source.substring(pos + oldString.length()));

            return sb.toString();
        } else {
            return source;
        }
    }

    public static String replaceAllStrings(String source, String oldString, String newString) {
        StringBuffer buffer = new StringBuffer(source);
        int idx = source.length();
        int offset = oldString.length();

        while ((idx = buffer.toString().lastIndexOf(oldString, idx - 1)) > -1) {
            buffer.replace(idx, idx + offset, newString);
        }
        return buffer.toString();
    }

    public static void replaceAllStrings(StringBuffer source, String oldString, String newString) {
        int idx = source.length();
        int offset = oldString.length();

        while ((idx = source.toString().lastIndexOf(oldString, idx - 1)) > -1) {
            source.replace(idx, idx + offset, newString);
        }
    }

    public static String removeAllStrings(String source, String oldString) {
        return replaceAllStrings(source, oldString, "");
    }

    public static String replaceString(String s, int pos, String newString) {
        StringBuffer sb = new StringBuffer(s);
        for (int i = 0; i < newString.length(); i++) {
            sb.setCharAt(pos + i, newString.charAt(i));
        }
        return sb.toString();
    }

    public static String translateEscapeSequences(String s) {
        String temp = s;
        temp = replaceAllStrings(temp, "\\t", "\t");  
        temp = replaceAllStrings(temp, "\\n", "\n");  
        temp = replaceAllStrings(temp, "\\r", "\r");  
        temp = replaceAllStrings(temp, "\\\"", "\""); 
        temp = replaceAllStrings(temp, "\\\'", "\'"); 
        temp = replaceAllStrings(temp, "\\\\", "\\"); 
        return temp;
    }

    public static String removeChar(String s, char c) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) {
                sb.append(s.charAt(i)); 
            }
        }
        return sb.toString();
    }

    public static String removeChar(String s, int pos) {
        StringBuffer buf = new StringBuffer(s.length() - 1);
        buf.append(s.substring(0, pos)).append(s.substring(pos + 1));
        return buf.toString();
    }

    public static String replaceChar(String s, char oldC, char newC) {
        StringBuffer sb = new StringBuffer(s);
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == oldC) {
                sb.setCharAt(i, newC);
            }
        }
        return sb.toString();
    }

    public static String replaceChar(String s, int pos, char c) {
        StringBuffer sb = new StringBuffer(s);
        sb.setCharAt(pos, c);
        return sb.toString();
    }

    public static int countChar(String s, char c) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    public static String message(String s, char c) {
        Character character = new Character(c);
        Object aobj[] = {
            character.toString()
        };
        String s1 = MessageFormat.format(s, aobj);
        return s1;
    }

    public static String message(String s, int i) {
        Integer integer = new Integer(i);
        Object aobj[] = {
            integer.toString()
        };
        String s1 = MessageFormat.format(s, aobj);
        return s1;
    }

    public static String message(String s, int i1, int i2) {
        Integer integer = new Integer(i1);
        Integer integer2 = new Integer(i2);
        Object aobj[] = {
            integer.toString(),
            integer2.toString()
        };
        String s1 = MessageFormat.format(s, aobj);
        return s1;
    }

    public static String message(String s, String s1) {
        Object aobj[] = {
            s1
        };
        String s2 = MessageFormat.format(s, aobj);
        return s2;
    }

    public static String decodeEncodedUnicode(String string) {
        char c;
        int length = string.length();
        StringBuffer buffer = new StringBuffer(length);

        for (int x = 0; x < length;) {
            c = string.charAt(x++);
            if (c == '\\') {
                c = string.charAt(x++);
                switch (c) {
                    case 'u': {
                        int value = 0;
                        for (int i = 0; i < 4; i++) {
                            c = string.charAt(x++);
                            if (c >= '0' && c <= '9') {
                                value = (value << 4) + c - '0';
                            } else if (c >= 'a' && c <= 'f') {
                                value = (value << 4) + 10 + c - 'a';
                            } else if (c >= 'A' && c <= 'F') {
                                value = (value << 4) + 10 + c - 'A';
                            } else {
                                throw new IllegalArgumentException("Wrong \\uxxxx encoding");
                            }
                        }
                        buffer.append((char) value);
                    }
                    break;
                    case 'n':
                        buffer.append('\n');
                        break;
                    case 't':
                        buffer.append('\t');
                        break;
                    case 'r':
                        buffer.append('\r');
                        break;
                    case 'f':
                        buffer.append('\f');
                        break;
                    default:
                        buffer.append(c);
                        break;
                } 
            } else {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }

    public static String encodeUnicode(String string) {
        int length = string.length();
        StringBuffer buffer = new StringBuffer(length * 2);

        for (int x = 0; x < length; x++) {
            char c = string.charAt(x);
            switch (c) {
                case ' ':
                    buffer.append(' ');
                    break;
                case '\\':
                    buffer.append('\\');
                    buffer.append('\\');
                    break;
                case '\n':
                    buffer.append('\\');
                    buffer.append('n');
                    break;
                case '\t':
                    buffer.append('\\');
                    buffer.append('t');
                    break;
                case '\r':
                    buffer.append('\\');
                    buffer.append('r');
                    break;
                case '\f':
                    buffer.append('\\');
                    buffer.append('f');
                    break;
                default:
                    if ((c < 0x0020) || (c > 0x007e)) {
                        buffer.append('\\');
                        buffer.append('u');
                        buffer.append(nibbleToHexChar((c >> 12) & 0xF));
                        buffer.append(nibbleToHexChar((c >> 8) & 0xF));
                        buffer.append(nibbleToHexChar((c >> 4) & 0xF));
                        buffer.append(nibbleToHexChar(c & 0xF));
                    } else {
                        if (specialChars.indexOf(c) != -1) {
                            buffer.append('\\');
                        }
                        buffer.append(c);
                    }
            }
        }
        return buffer.toString();
    }

    public static String[] split(String str, String delimiter) {
        ArrayList al = new ArrayList();
        int startpos = 0;
        int found = -1;
        do {
            found = str.substring(startpos).indexOf(delimiter);
            if (found > -1) {
                al.add(str.substring(startpos, startpos + found));
                startpos = startpos + found + delimiter.length();
            }
        } while (found > -1);
        if (startpos < str.length()) {
            al.add(str.substring(startpos));
        }

        String[] s = new String[al.size()];
        for (int i = 0; i < s.length; i++) {
            s[i] = (String) al.get(i);
        }

        return s;
    }

}
