package com.quduquxie.module.comment.util;

import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;

import com.quduquxie.model.CommentUser;
import com.quduquxie.model.Fiction;
import com.quduquxie.module.comment.listener.BookClickedListener;
import com.quduquxie.module.comment.listener.UserClickedListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created on 17/2/28.
 * Created by crazylei.
 */

public class RenderUtil {

    public static void renderBook(String content, Fiction fiction, SpannableStringBuilder spannableStringBuilder, BookClickedListener bookClickedListener) {
        List<DecorationItem> items = findTagsInText(content, "《" + fiction.name + "》");
        for (DecorationItem decoratorItem : items) {
            makeStringClickable(spannableStringBuilder, decoratorItem.start, decoratorItem.text, new BookClickableSpan(fiction.id, bookClickedListener));
        }
    }

    public static void renderUser(String content, CommentUser sender, CommentUser receiver, SpannableStringBuilder spannableStringBuilder, UserClickedListener userClickedListener) {
        List<DecorationItem> senderItems = findTagsInText(content, sender.name);
        for (DecorationItem decoratorItem : senderItems) {
            makeStringClickable(spannableStringBuilder, decoratorItem.start, decoratorItem.text, new UserClickableSpan(sender, userClickedListener));
        }

        List<DecorationItem> receiverItems = findTagsInText(content, "@" + receiver.name);
        for (DecorationItem decoratorItem : receiverItems) {
            makeStringClickable(spannableStringBuilder, decoratorItem.start, decoratorItem.text, new UserClickableSpan(receiver, userClickedListener));
        }
    }

    private static void makeStringClickable(SpannableStringBuilder spannableStringBuilder, int start, final String text, ClickableSpan clickableSpan) {
        spannableStringBuilder.setSpan(clickableSpan, start, start + text.length(), 0);
    }

    private static List<DecorationItem> findTagsInText(String fullString, String tag) {
        int lastIndex = 0;
        List<DecorationItem> decoratorItems = new LinkedList<>();
        while (lastIndex != -1) {
            lastIndex = fullString.indexOf(tag, lastIndex);
            if (lastIndex != -1) {
                decoratorItems.add(new DecorationItem(lastIndex, tag));
                if(lastIndex == lastIndex + tag.length()){
                    break;
                }
                lastIndex += tag.length();
            }
        }

        return decoratorItems;
    }

    private static class DecorationItem {
        int start;
        String text;

        public DecorationItem(int start, String text) {
            this.start = start;
            this.text = text;
        }
    }
}
