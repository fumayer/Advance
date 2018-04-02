package com.example.sunjie.knowledgepointshareproject.factory2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sunjie.knowledgepointshareproject.R;

public class Factory2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory2);
        IShape shape = ShapeFactory.getShape(ShapeFactory.ShapeType.CIRCLE);
        shape.draw();
    }
}
