package com.example.sunjie.knowledgepointshareproject.flyweight;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.sunjie.knowledgepointshareproject.R;

import java.util.Random;

public class FlyWeightActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fly_weight);
    }

    private ChessFactory chessFactory;

    public void go1(View view) {

        chessFactory = ChessFactory.getInstance();
        Random random = new Random();
        AbsChessman absChessman = null;
        for (int i = 0; i < 20; i++) {
            switch (random.nextInt(2)) {
                case 0:
                    absChessman = chessFactory.factory('B');
                    break;

                case 1:
                    absChessman = chessFactory.factory('W');
                    break;
                default:
                    break;

            }
            if (absChessman != null) {
                absChessman.point(i, random.nextInt(20));
            }
        }


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
