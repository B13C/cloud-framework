package cn.maple.core.framework.util;

import cn.hutool.core.util.StrUtil;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * GXDBStringEscapeUtils ，数据库字符串转义
 *
 * @author 塵子曦
 */
public class GXDBStringEscapeUtils {
    private static final Pattern SQL_SYNTAX_PATTERN = Pattern.compile("(insert|delete|update|select|create|drop|truncate|grant|alter|deny|revoke|call|execute|exec|declare|show|rename|set)\\s+.*(into|from|set|where|table|database|view|index|on|cursor|procedure|trigger|for|password|union|and|or)|(select\\s*\\*\\s*from\\s+)|(and|or)\\s+.*", 2);
    private static final Pattern SQL_COMMENT_PATTERN = Pattern.compile("'.*(or|union|--|#|/\\*|;)", 2);

    /**
     * 字符串是否需要转义
     *
     * @param str ignore
     * @param len ignore
     * @return 是否需要转义
     */
    private static boolean isEscapeNeededForString(String str, int len) {
        boolean needsHexEscape = false;
        for (int i = 0; i < len; ++i) {
            char c = str.charAt(i);
            switch (c) {
                /* Must be escaped for 'mysql' */
                case 0:
                    needsHexEscape = true;
                    break;
                /* Must be escaped for logs */
                case '\n':
                    needsHexEscape = true;
                    break;
                case '\r':
                    needsHexEscape = true;
                    break;
                case '\\':
                    needsHexEscape = true;
                    break;
                case '\'':
                    needsHexEscape = true;
                    break;
                /* Better safe than sorry */
                case '"':
                    needsHexEscape = true;
                    break;
                /* This gives problems on Win32 */
                case '\032':
                    needsHexEscape = true;
                    break;
                default:
                    break;
            }
            if (needsHexEscape) {
                // no need to scan more
                break;
            }
        }
        return needsHexEscape;
    }

    /**
     * 转义字符串。纯转义，不添加单引号。
     *
     * @param escapeStr 被转义的字符串
     * @return 转义后的字符串
     */
    public static String escapeRawString(String escapeStr) {
        int stringLength = escapeStr.length();
        if (isEscapeNeededForString(escapeStr, stringLength)) {
            StringBuilder buf = new StringBuilder((int) (escapeStr.length() * 1.1));
            //
            // Note: buf.append(char) is _faster_ than appending in blocks,
            // because the block append requires a System.arraycopy().... go
            // figure...
            //
            for (int i = 0; i < stringLength; ++i) {
                char c = escapeStr.charAt(i);
                switch (c) {
                    /* Must be escaped for 'mysql' */
                    case 0:
                        buf.append('\\');
                        buf.append('0');

                        break;
                    /* Must be escaped for logs */
                    case '\n':
                        buf.append('\\');
                        buf.append('n');

                        break;

                    case '\r':
                        buf.append('\\');
                        buf.append('r');

                        break;

                    case '\\':
                        buf.append('\\');
                        buf.append('\\');

                        break;

                    case '\'':
                        buf.append('\\');
                        buf.append('\'');

                        break;
                    /* Better safe than sorry */
                    case '"':
                        buf.append('\\');
                        buf.append('"');

                        break;
                    /* This gives problems on Win32 */
                    case '\032':
                        buf.append('\\');
                        buf.append('Z');
                        break;
                    default:
                        buf.append(c);
                }
            }
            return buf.toString();
        } else {
            return escapeStr;
        }
    }

    /**
     * 转义字符串
     *
     * @param escapeStr 被转义的字符串
     * @return 转义后的字符串
     */
    public static String escapeString(String escapeStr) {
        if (escapeStr.matches("'(.+)'")) {
            escapeStr = escapeStr.substring(1, escapeStr.length() - 1);
        }
        return "'" + escapeRawString(escapeStr) + "'";
    }

    public static boolean check(String value) {
        Objects.requireNonNull(value);
        return SQL_COMMENT_PATTERN.matcher(value).find() || SQL_SYNTAX_PATTERN.matcher(value).find();
    }

    public static String removeEscapeCharacter(String text) {
        Objects.nonNull(text);
        return text.replaceAll("\"", "").replaceAll("'", "");
    }

    /**
     * 转义 SQL 中的特殊字符，处理包括：
     * <ul>
     *   <li>反斜杠： "\" 替换为 "\\\\"</li>
     *   <li>空字符： "\0" 替换为 "\\0"</li>
     *   <li>换行符： "\n" 替换为 "\\n"</li>
     *   <li>回车符： "\r" 替换为 "\\r"</li>
     *   <li>Ctrl+Z（\032）： 替换为 "\\Z"</li>
     *   <li>单引号： "'" 替换为 "''"（SQL 标准转义）</li>
     *   <li>双引号： "\"" 替换为 "\\\""</li>
     * </ul>
     *
     * @param input 原始字符串
     * @return 转义后的字符串
     */
    public static String escapeSql(String input) {
        if (input == null) {
            return null;
        }
        // 注意顺序，先转义反斜杠，否则后续替换可能会出现重复替换的问题
        String result = input;
        result = StrUtil.replace(result, "\\", "\\\\");
        result = StrUtil.replace(result, "\0", "\\0");
        result = StrUtil.replace(result, "\n", "\\n");
        result = StrUtil.replace(result, "\r", "\\r");
        result = StrUtil.replace(result, "\032", "\\Z");
        // 单引号转义（SQL 标准做法）
        result = StrUtil.replace(result, "'", "''");
        // 双引号转义，根据实际需求决定是否需要
        result = StrUtil.replace(result, "\"", "\\\"");
        return result;
    }

    /**
     * 针对 SQL LIKE 查询的转义方法。
     * 除了执行常规 SQL 字符转义外，还需要对 LIKE 模式下的通配符进行转义：
     * <ul>
     *   <li>% -> ESCAPE字符 + %</li>
     *   <li>_ -> ESCAPE字符 + _</li>
     * </ul>
     * 并且在 SQL 中需要指定 ESCAPE 字符。
     *
     * @param input      原始字符串
     * @param escapeChar 用于转义的字符，一般为 '\'（反斜杠）
     * @return 转义后的字符串
     */
    public static String escapeSqlForLike(String input, char escapeChar) {
        if (input == null) {
            return null;
        }
        // 先进行常规 SQL 转义
        String escaped = escapeSql(input);
        String escStr = String.valueOf(escapeChar);
        // 对 LIKE 模式下的特殊字符 % 和 _ 进行转义
        escaped = StrUtil.replace(escaped, "%", escStr + "%");
        escaped = StrUtil.replace(escaped, "_", escStr + "_");
        return escaped;
    }
}