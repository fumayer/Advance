package com.quduquxie.communal.widget.expression;

import android.os.Parcel;
import android.os.Parcelable;

public class ExpressionItem implements Parcelable {

    public static final Creator<ExpressionItem> CREATOR = new Creator<ExpressionItem>() {
        @Override
        public ExpressionItem createFromParcel(Parcel parcel) {
            return new ExpressionItem(parcel);
        }

        @Override
        public ExpressionItem[] newArray(int size) {
            return new ExpressionItem[size];
        }
    };

    private int icon;

    private char value;

    private String expression;

    public ExpressionItem() {

    }

    public ExpressionItem(Parcel parcel) {
        this.icon = parcel.readInt();
        this.value = (char) parcel.readInt();
        this.expression = parcel.readString();
    }

    public static ExpressionItem fromCodePoint(int code) {
        ExpressionItem expressionItem = new ExpressionItem();
        expressionItem.expression = compileCode(code);
        return expressionItem;
    }

    public static ExpressionItem fromChar(char ch) {
        ExpressionItem expressionItem = new ExpressionItem();
        expressionItem.expression = Character.toString(ch);
        return expressionItem;
    }

    public static final String compileCode(int code) {
        if (Character.charCount(code) == 1) {
            return String.valueOf(code);
        } else {
            return new String(Character.toChars(code));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(icon);
        dest.writeInt(value);
        dest.writeString(expression);
    }

    public char getValue() {
        return value;
    }

    public int getIcon() {
        return icon;
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ExpressionItem && expression.equals(((ExpressionItem) o).expression);
    }

    @Override
    public int hashCode() {
        return expression.hashCode();
    }

}
