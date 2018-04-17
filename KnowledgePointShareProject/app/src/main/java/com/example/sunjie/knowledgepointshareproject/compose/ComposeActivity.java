package com.example.sunjie.knowledgepointshareproject.compose;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.sunjie.knowledgepointshareproject.R;

public class ComposeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
    }

    public void go1(View view) {
        Toast.makeText(this, "触发go1", Toast.LENGTH_SHORT).show();

        TreeBranch 树根 = new TreeBranch("树根");
        TreeBranch 树干A = new TreeBranch("树干A");
        TreeBranch 树干B = new TreeBranch("树干B");
        TreeLeaf 树叶a = new TreeLeaf("树叶a");
        TreeLeaf 树叶b = new TreeLeaf("树叶b");
        TreeLeaf 树叶b1 = new TreeLeaf("树叶b1");
        树根.addTreeComponent(树干A);
        树根.addTreeComponent(树干B);
        树干A.addTreeComponent(树叶a);
        树干B.addTreeComponent(树叶b);
        树干B.addTreeComponent(树叶b1);
        树根.doSomething();
    }

    public void go2(View view) {
        Toast.makeText(this, "触发go2", Toast.LENGTH_SHORT).show();
    }

    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
    }

    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
    }
}
