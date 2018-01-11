package com.example.sj.app2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class UnitConversionActivity extends AppCompatActivity {
    private EditText mEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_conversion);
        mEt = (EditText) findViewById(R.id.et);

    }

    private float FenConversionYuan = 100.00f;
    private int FenConversionYuanI = 100;

    //    int 转换为float
    public void go1(View view) {
        Toast.makeText(this, "触发go1", Toast.LENGTH_SHORT).show();
        int fen = Integer.parseInt(mEt.getText().toString().trim());
        LogUtil.e("UnitConversionActivity", "25-----go1--->" + fen);
        float yuan = fen / FenConversionYuan;
        LogUtil.e("UnitConversionActivity", "28-----go1--->" + yuan);
        int yuanI = fen / FenConversionYuanI;
        LogUtil.e("UnitConversionActivity", "32-----go1--->" + yuanI);

        if (yuan > yuanI) {
            LogUtil.e("UnitConversionActivity", "35-----go1--->就这么搞");
        }
//        int 转换为 带两位小数的float
        float priceF = (float) (Math.round((fen * 100) / 100));
        float seekBarChange = (float) (Math.round(fen * 100)) / 100;
        LogUtil.e("UnitConversionActivity", "39-----go1--->" + priceF);
        LogUtil.e("UnitConversionActivity", "41-----go1--->" + seekBarChange);


        BigDecimal b = new BigDecimal(fen / 100.00f);
        float decimalF = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        LogUtil.e("UnitConversionActivity", "48-----go1--->" + decimalF);

        //   b.setScale(2,   BigDecimal.ROUND_HALF_UP)   表明四舍五入，保留两位小数,如果不够两位，不会补0  不可取
        LogUtil.e("UnitConversionActivity", "51-----go1--->" + retainDecimalNum(2, fen / 1.00f));
        LogUtil.e("UnitConversionActivity", "51-----go1--->" + retainDecimalNum(3, fen / 1.00f));

        LogUtil.e("UnitConversionActivity", "51-----go1--->" + retainDecimalNum(2, fen));
        LogUtil.e("UnitConversionActivity", "52-----go1--->" + retainDecimalNum(3, fen));

        DecimalFormat fnum = new DecimalFormat("##0.000");
        String dd = fnum.format(-fen);
        String ee = fnum.format(fen / 1.00f);
        LogUtil.e("UnitConversionActivity", "60-----go1--->" + dd);
        LogUtil.e("UnitConversionActivity", "62-----go1--->" + ee);


    }


    public float retainDecimalNum(int decimalNum, float f) {
        BigDecimal decimal = new BigDecimal(f);
        return decimal.setScale(decimalNum, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    int i = 11;
    float f = 11.00f;

    public void go2(View view) {
        Toast.makeText(this, "触发go2", Toast.LENGTH_SHORT).show();
        if (i > f) {
            LogUtil.e("UnitConversionActivity", "39-----go2--->  i>f");
        } else if (i == f) {
            LogUtil.e("UnitConversionActivity", "42-----go2--->  i==f");
        } else if (i < f) {
            LogUtil.e("UnitConversionActivity", "44-----go2--->  i<f");
        }

    }


    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();

    }



    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
    }

    public void go5(View view) {
        Toast.makeText(this, "触发go5", Toast.LENGTH_SHORT).show();
    }

    public void go6(View view) {
        Toast.makeText(this, "触发go6", Toast.LENGTH_SHORT).show();
    }

    public void go7(View view) {
        Toast.makeText(this, "触发go7", Toast.LENGTH_SHORT).show();
    }

    public void go8(View view) {
        Toast.makeText(this, "触发go8", Toast.LENGTH_SHORT).show();
    }

}
