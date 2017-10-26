package com.quduquxie.communal.widget.expression;

import android.content.Context;
import android.text.Spannable;
import android.util.SparseIntArray;

import com.quduquxie.R;

import java.util.HashMap;
import java.util.Map;

public final class ExpressionHandler {
    private ExpressionHandler() {

    }

    private static final SparseIntArray expression_map = new SparseIntArray(1029);
    private static Map<String, Integer> expressions = new HashMap<>();

    static {
        expression_map.put(0x1f60a, R.drawable.emoji_1f60a);
        expression_map.put(0x1f60d, R.drawable.emoji_1f60d);
        expression_map.put(0x1f60f, R.drawable.emoji_1f60f);
        expression_map.put(0x1f61a, R.drawable.emoji_1f61a);
        expression_map.put(0x1f61c, R.drawable.emoji_1f61c);
        expression_map.put(0x1f61d, R.drawable.emoji_1f61d);
        expression_map.put(0x1f61e, R.drawable.emoji_1f61e);
        expression_map.put(0x1f61f, R.drawable.emoji_1f61f);
        expression_map.put(0x1f62b, R.drawable.emoji_1f62b);
        expression_map.put(0x1f62c, R.drawable.emoji_1f62c);
        expression_map.put(0x1f62d, R.drawable.emoji_1f62d);
        expression_map.put(0x1f62f, R.drawable.emoji_1f62f);
        expression_map.put(0x1f601, R.drawable.emoji_1f601);
        expression_map.put(0x1f602, R.drawable.emoji_1f602);
        expression_map.put(0x1f604, R.drawable.emoji_1f604);
        expression_map.put(0x1f605, R.drawable.emoji_1f605);
        expression_map.put(0x1f606, R.drawable.emoji_1f606);
        expression_map.put(0x1f609, R.drawable.emoji_1f609);
        expression_map.put(0x1f611, R.drawable.emoji_1f611);
        expression_map.put(0x1f612, R.drawable.emoji_1f612);
        expression_map.put(0x1f613, R.drawable.emoji_1f613);
        expression_map.put(0x1f614, R.drawable.emoji_1f614);
        expression_map.put(0x1f615, R.drawable.emoji_1f615);
        expression_map.put(0x1f616, R.drawable.emoji_1f616);
        expression_map.put(0x1f618, R.drawable.emoji_1f618);
        expression_map.put(0x1f620, R.drawable.emoji_1f620);
        expression_map.put(0x1f624, R.drawable.emoji_1f624);
        expression_map.put(0x1f625, R.drawable.emoji_1f625);
        expression_map.put(0x1f627, R.drawable.emoji_1f627);
        expression_map.put(0x1f629, R.drawable.emoji_1f629);
        expression_map.put(0x1f630, R.drawable.emoji_1f630);
        expression_map.put(0x1f631, R.drawable.emoji_1f631);
        expression_map.put(0x1f632, R.drawable.emoji_1f632);
        expression_map.put(0x1f633, R.drawable.emoji_1f633);
        expression_map.put(0x1f641, R.drawable.emoji_1f641);
        expression_map.put(0x1f642, R.drawable.emoji_1f642);
        expression_map.put(0x1f644, R.drawable.emoji_1f644);
        expression_map.put(0x1f914, R.drawable.emoji_1f914);
        expression_map.put(0x1f917, R.drawable.emoji_1f917);
        expression_map.put(0x263a, R.drawable.emoji_263a);

        expression_map.put(0x1f31c, R.drawable.emoji_1f31c);
        expression_map.put(0x1f31d, R.drawable.emoji_1f31d);
        expression_map.put(0x1f42d, R.drawable.emoji_1f42d);
        expression_map.put(0x1f42e, R.drawable.emoji_1f42e);
        expression_map.put(0x1f42f, R.drawable.emoji_1f42f);
        expression_map.put(0x1f43b, R.drawable.emoji_1f43b);
        expression_map.put(0x1f43c, R.drawable.emoji_1f43c);
        expression_map.put(0x1f60e, R.drawable.emoji_1f60e);
        expression_map.put(0x1f63b, R.drawable.emoji_1f63b);
        expression_map.put(0x1f63c, R.drawable.emoji_1f63c);
        expression_map.put(0x1f63d, R.drawable.emoji_1f63d);
        expression_map.put(0x1f63f, R.drawable.emoji_1f63f);
        expression_map.put(0x1f64a, R.drawable.emoji_1f64a);
        expression_map.put(0x1f414, R.drawable.emoji_1f414);
        expression_map.put(0x1f419, R.drawable.emoji_1f419);
        expression_map.put(0x1f420, R.drawable.emoji_1f420);
        expression_map.put(0x1f428, R.drawable.emoji_1f428);
        expression_map.put(0x1f430, R.drawable.emoji_1f430);
        expression_map.put(0x1f434, R.drawable.emoji_1f434);
        expression_map.put(0x1f435, R.drawable.emoji_1f435);
        expression_map.put(0x1f436, R.drawable.emoji_1f436);
        expression_map.put(0x1f437, R.drawable.emoji_1f437);
        expression_map.put(0x1f439, R.drawable.emoji_1f439);
        expression_map.put(0x1f494, R.drawable.emoji_1f494);
        expression_map.put(0x1f495, R.drawable.emoji_1f495);
        expression_map.put(0x1f607, R.drawable.emoji_1f607);
        expression_map.put(0x1f608, R.drawable.emoji_1f608);
        expression_map.put(0x1f621, R.drawable.emoji_1f621);
        expression_map.put(0x1f634, R.drawable.emoji_1f634);
        expression_map.put(0x1f637, R.drawable.emoji_1f637);
        expression_map.put(0x1f638, R.drawable.emoji_1f638);
        expression_map.put(0x1f639, R.drawable.emoji_1f639);
        expression_map.put(0x1f640, R.drawable.emoji_1f640);
        expression_map.put(0x1f648, R.drawable.emoji_1f648);
        expression_map.put(0x1f649, R.drawable.emoji_1f649);
        expression_map.put(0x1f910, R.drawable.emoji_1f910);
        expression_map.put(0x1f911, R.drawable.emoji_1f911);
        expression_map.put(0x1f912, R.drawable.emoji_1f912);
        expression_map.put(0x1f913, R.drawable.emoji_1f913);
        expression_map.put(0x1f915, R.drawable.emoji_1f915);

        expression_map.put(0x1f3b2, R.drawable.emoji_1f3b2);
        expression_map.put(0x1f3c8, R.drawable.emoji_1f3c8);
        expression_map.put(0x1f004, R.drawable.emoji_1f004);
        expression_map.put(0x1f4aa, R.drawable.emoji_1f4aa);
        expression_map.put(0x1f4af, R.drawable.emoji_1f4af);
        expression_map.put(0x1f4b4, R.drawable.emoji_1f4b4);
        expression_map.put(0x1f38a, R.drawable.emoji_1f38a);
        expression_map.put(0x1f44a, R.drawable.emoji_1f44a);
        expression_map.put(0x1f44b, R.drawable.emoji_1f44b);
        expression_map.put(0x1f44c, R.drawable.emoji_1f44c);
        expression_map.put(0x1f44d, R.drawable.emoji_1f44d);
        expression_map.put(0x1f44e, R.drawable.emoji_1f44e);
        expression_map.put(0x1f44f, R.drawable.emoji_1f44f);
        expression_map.put(0x1f64f, R.drawable.emoji_1f64f);
        expression_map.put(0x1f389, R.drawable.emoji_1f389);
        expression_map.put(0x1f446, R.drawable.emoji_1f446);
        expression_map.put(0x1f447, R.drawable.emoji_1f447);
        expression_map.put(0x1f448, R.drawable.emoji_1f448);
        expression_map.put(0x1f449, R.drawable.emoji_1f449);
        expression_map.put(0x1f450, R.drawable.emoji_1f450);
        expression_map.put(0x1f590, R.drawable.emoji_1f590);
        expression_map.put(0x1f595, R.drawable.emoji_1f595);
        expression_map.put(0x1f596, R.drawable.emoji_1f596);
        expression_map.put(0x1f918, R.drawable.emoji_1f918);
        expression_map.put(0x261d, R.drawable.emoji_261d);
        expression_map.put(0x270a, R.drawable.emoji_270a);
        expression_map.put(0x270b, R.drawable.emoji_270b);
        expression_map.put(0x270c, R.drawable.emoji_270c);
    }

    private static int getExpressionResource(int codePoint) {
        return expression_map.get(codePoint);
    }

    public static void addExpression(Context context, Spannable text, float expressionSize, int expressionAlignType, float expressionTextSize) {
        addExpression(context, text, expressionSize, expressionAlignType, expressionTextSize, 0, -1);
    }

    public static void addExpression(Context context, Spannable text, float expressionSize, int expressionAlignType, float expressionTextSize, int index, int length) {
        int textLength = text.length();
        int textLengthToProcessMax = textLength - index;
        int textLengthToProcess = length < 0 || length >= textLengthToProcessMax ? textLength : (length + index);

        ExpressionSpan[] oldSpans = text.getSpans(0, textLength, ExpressionSpan.class);
        for (int i = 0; i < oldSpans.length; i++) {
            text.removeSpan(oldSpans[i]);
        }

        int skip;
        for (int i = index; i < textLengthToProcess; i += skip) {
            int icon = 0;
            int unicode = Character.codePointAt(text, i);
            skip = Character.charCount(unicode);

            if (unicode > 0xff) {
                icon = getExpressionResource(unicode);
            }

            if (i + skip < textLengthToProcess) {
                int followUnicode = Character.codePointAt(text, i + skip);

                int followSkip = Character.charCount(followUnicode);
                String hexUnicode = Integer.toHexString(unicode);
                String hexFollowUnicode = Integer.toHexString(followUnicode);

                String resourceName = "emoji_" + hexUnicode + "_" + hexFollowUnicode;

                int resourceId = 0;
                if (expressions.containsKey(resourceName)) {
                    resourceId = expressions.get(resourceName);
                } else {
                    resourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getApplicationContext().getPackageName());
                    if (resourceId != 0) {
                        expressions.put(resourceName, resourceId);
                    }
                }

                if (resourceId == 0) {
                    followSkip = 0;
                } else {
                    icon = resourceId;
                }
                skip += followSkip;
            }

            if (icon > 0) {
                text.setSpan(new ExpressionSpan(context, icon, (int) expressionSize, expressionAlignType, (int) expressionTextSize), i, i + skip, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
}
