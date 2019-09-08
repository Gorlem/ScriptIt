package ml.gorlem.scriptit.api.util;

import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Color {
    private static Pattern hexPattern = Pattern.compile("^#?(\\p{XDigit}{3}|\\p{XDigit}{6}|\\p{XDigit}{8})$");
    private static Pattern rgbPattern = Pattern.compile("^(?:rgba?\\()?(\\d{1,3},\\d{1,3},\\d{1,3}(,\\d{1,3})?)\\)?$");
    private static Pattern namePattern = Pattern.compile("^(\\w+) ?(\\d{1,3}%)?$");

    private static Splitter twoSplitter = Splitter.fixedLength(2);
    private static Splitter commaSplitter = Splitter.on(",");

    private static int MAX_VALUE = 255;

    private static List<Color> colors = new ArrayList<>();
    // clrs.cc
    public static Color NAVY = parseAndAdd("#001F3F", "NAVY");
    public static Color BLUE = parseAndAdd("#0074D9", "BLUE");
    public static Color AQUA = parseAndAdd("#7FDBFF", "AQUA");
    public static Color TEAL = parseAndAdd("#39CCCC", "TEAL");
    public static Color OLIVE = parseAndAdd("#3D9970", "OLIVE");
    public static Color GREEN = parseAndAdd("#2ECC40", "GREEN");
    public static Color LIME = parseAndAdd("#01FF70", "LIME");
    public static Color YELLOW = parseAndAdd("#FFDC00", "YELLOW");
    public static Color ORANGE = parseAndAdd("#FF851B", "ORANGE");
    public static Color RED = parseAndAdd("#FF4136", "RED");
    public static Color MAROON = parseAndAdd("#85144b", "MAROON");
    public static Color FUCHSIA = parseAndAdd("#F012BE", "FUCHSIA");
    public static Color PURPLE = parseAndAdd("#B10DC9", "PURPLE");
    public static Color BLACK = parseAndAdd("#111111", "BLACK");
    public static Color GREY = parseAndAdd("#AAAAAA", "GREY");
    public static Color SILVER = parseAndAdd("#DDDDDD", "SILVER");
    public static Color WHITE = parseAndAdd("#FFFFFF", "WHITE");

    private static Color parseAndAdd(String string, String original) {
        Color color = parse(string);
        color.original = original;
        colors.add(color);
        return color;
    }

    public static Color parse(String string) {
        Matcher hexMatcher = hexPattern.matcher(string);
        if (hexMatcher.matches()) {
            String value = hexMatcher.group(1);
            if (value.length() == 3) {
                value = value.replaceAll(".", "$0$0");
            }

            if (value.length() == 6) {
                List<Integer> integers = splitToInts(value, twoSplitter, 16);
                return new Color(string, integers.get(0), integers.get(1), integers.get(2));
            }

            List<Integer> integers = splitToInts(value, twoSplitter, 16);
            return new Color(string, integers.get(0), integers.get(1), integers.get(2), integers.get(3));
        }

        Matcher rgbMatcher = rgbPattern.matcher(string);
        if (rgbMatcher.matches()) {
            String value = rgbMatcher.group(1);
            List<Integer> integers = splitToInts(value, commaSplitter, 10);

            if (integers.size() == 3) {
                return new Color(string, integers.get(0), integers.get(1), integers.get(2));
            }

            return new Color(string, integers.get(0), integers.get(1), integers.get(2), integers.get(3));
        }

        Matcher nameMatcher = namePattern.matcher(string);
        if (nameMatcher.matches()) {
            String colorName = nameMatcher.group(1);
            float alpha = -1;

            String percentage = nameMatcher.group(2);
            if (percentage != null)
            {
                String onlyNumber = percentage.replace("%", "").trim();
                alpha = Integer.parseInt(onlyNumber);
            }

            for (Color color : colors) {
                if (color.original.equalsIgnoreCase(colorName)) {
                    if (alpha == -1) {
                        return color;
                    }

                    Color colorWithAlpha = color.copyWithAlpha((int)((alpha / 100) * MAX_VALUE));
                    colorWithAlpha.original = string;
                    return colorWithAlpha;
                }
            }
        }

        return null;
    }

    private static List<Integer> splitToInts(String hex, Splitter splitter, int radix) {
        Iterable<String> pieces = splitter.split(hex);
        return StreamSupport
                .stream(pieces.spliterator(), false)
                .map((piece) -> Integer.parseInt(piece, radix))
                .collect(Collectors.toList());
    }
    int red;
    int green;
    int blue;
    int alpha;

    String original;

    private Color(String original, int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.original = original;
    }

    private Color(String original, int red, int green, int blue) {
        this(original, red, green, blue, MAX_VALUE);
    }

    public int getValue() {
        return (red << 16) | (green << 8) | (blue << 0) | (alpha << 24);
    }

    public Color copyWithAlpha(int alpha) {
        Color color = new Color(original, red, green, blue, alpha);
        color.original = color.toHexString();
        return color;
    }

    @Override
    public String toString() {
        return original;
    }

    public String toRgbString() {
        StringBuilder builder = new StringBuilder();
        builder.append("rgba(");
        builder.append(red);
        builder.append(",");
        builder.append(green);
        builder.append(",");
        builder.append(blue);
        builder.append(",");
        builder.append(alpha);
        builder.append(")");

        return builder.toString();
    }

    public String toHexString() {
        StringBuilder builder = new StringBuilder();
        builder.append("#");
        builder.append(Integer.toHexString(red));
        builder.append(Integer.toHexString(green));
        builder.append(Integer.toHexString(blue));
        builder.append(Integer.toHexString(alpha));

        return builder.toString();
    }
}
