package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.annotation.CJson;
import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验字符串是否符合 JSON 语法及配置的根节点类型。
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CJsonValidator implements ConstraintValidator<CJson, String> {
    private CJson.Type type;

    @Override
    public void initialize(CJson annotation) {
        type = annotation.type();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        int nullResult = CValidNullUtil.validNull(value, context);
        if (nullResult != 0) return nullResult == 1;
        Parser parser = new Parser(value);
        char first = parser.peekNonWhitespace();
        if (first == '\0') return false;
        if (type == CJson.Type.STRUCT && first != '{' && first != '[') return false;
        if (type == CJson.Type.VALUE && (first == '{' || first == '[')) return false;
        if (type == CJson.Type.OBJECT && first != '{') return false;
        if (type == CJson.Type.ARRAY && first != '[') return false;
        return parser.parseValue() && parser.isEnd();
    }

    /** 不依赖第三方库的轻量 JSON 递归解析器。 */
    private static final class Parser {
        private final String text;
        private int index;

        private Parser(String text) { this.text = text; }

        private char peekNonWhitespace() {
            skipWhitespace();
            return index < text.length() ? text.charAt(index) : '\0';
        }

        private boolean isEnd() {
            skipWhitespace();
            return index == text.length();
        }

        /** 根据首字符分派到对应的 JSON 值解析逻辑。 */
        private boolean parseValue() {
            skipWhitespace();
            if (index >= text.length()) return false;
            char c = text.charAt(index);
            if (c == '{') return parseObject();
            if (c == '[') return parseArray();
            if (c == '"') return parseString();
            if (startsWith("true")) return consume("true");
            if (startsWith("false")) return consume("false");
            if (startsWith("null")) return consume("null");
            return parseNumber();
        }

        private boolean parseObject() {
            index++;
            skipWhitespace();
            if (consumeChar('}')) return true;
            while (true) {
                if (!parseString()) return false;
                skipWhitespace();
                if (!consumeChar(':') || !parseValue()) return false;
                skipWhitespace();
                if (consumeChar('}')) return true;
                if (!consumeChar(',')) return false;
            }
        }

        private boolean parseArray() {
            index++;
            skipWhitespace();
            if (consumeChar(']')) return true;
            while (true) {
                if (!parseValue()) return false;
                skipWhitespace();
                if (consumeChar(']')) return true;
                if (!consumeChar(',')) return false;
            }
        }

        private boolean parseString() {
            if (!consumeChar('"')) return false;
            while (index < text.length()) {
                char c = text.charAt(index++);
                if (c == '"') return true;
                if (c < 0x20) return false;
                if (c == '\\') {
                    if (index >= text.length()) return false;
                    char escaped = text.charAt(index++);
                    if (escaped == 'u') {
                        if (index + 4 > text.length()) return false;
                        for (int i = 0; i < 4; i++) {
                            if (Character.digit(text.charAt(index++), 16) < 0) return false;
                        }
                    } else if ("\"\\/bfnrt".indexOf(escaped) < 0) {
                        return false;
                    }
                }
            }
            return false;
        }

        /** 按 JSON 数字语法解析整数、小数和科学计数法。 */
        private boolean parseNumber() {
            int start = index;
            if (index < text.length() && text.charAt(index) == '-') index++;
            if (index >= text.length()) return false;
            if (text.charAt(index) == '0') {
                index++;
            } else {
                if (!isDigitOneToNine(text.charAt(index))) return false;
                while (index < text.length() && Character.isDigit(text.charAt(index))) index++;
            }
            if (index < text.length() && text.charAt(index) == '.') {
                index++;
                int fractionStart = index;
                while (index < text.length() && Character.isDigit(text.charAt(index))) index++;
                if (fractionStart == index) return false;
            }
            if (index < text.length() && (text.charAt(index) == 'e' || text.charAt(index) == 'E')) {
                index++;
                if (index < text.length() && (text.charAt(index) == '+' || text.charAt(index) == '-')) index++;
                int exponentStart = index;
                while (index < text.length() && Character.isDigit(text.charAt(index))) index++;
                if (exponentStart == index) return false;
            }
            return index > start;
        }

        private boolean startsWith(String value) { return text.startsWith(value, index); }
        private boolean consume(String value) { index += value.length(); return true; }
        private boolean consumeChar(char value) {
            if (index < text.length() && text.charAt(index) == value) { index++; return true; }
            return false;
        }
        private void skipWhitespace() { while (index < text.length() && Character.isWhitespace(text.charAt(index))) index++; }
        private static boolean isDigitOneToNine(char c) { return c >= '1' && c <= '9'; }
    }
}
