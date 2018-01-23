package com.example.sj.app2.fragmentconn;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sj.app2.LogUtil;
import com.example.sj.app2.R;
import com.example.sj.app2.fragmentconn.messagefragment.MyItemRecyclerViewAdapter;
import com.example.sj.app2.fragmentconn.messagefragment.dummy.DummyContent;
import com.example.sj.app2.fragmentconn.messagefragment.MessageFragment;

public class TestFragmentConnActivity extends AppCompatActivity implements MessageFragment.OnListFragmentInteractionListener, MeFragment.Listener {

    private BottomNavigationView navigation;
    private SFragmentManager mSFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment_conn);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        initFrags(savedInstanceState);
        navigation.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.call:
                        LogUtil.e("TestFragmentConnActivity", "28-----onNavigationItemReselected--->" + "call");
                        break;
                    case R.id.message:
                        LogUtil.e("TestFragmentConnActivity", "32-----onNavigationItemReselected--->" + "message");
                        break;
                    default:
                        break;
                }
            }
        });
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.call:
                        mSFragmentManager.showSelectFrag(0);
                        break;
                    case R.id.message:
                        mSFragmentManager.showSelectFrag(1);
                        break;
                    case R.id.me:
                        mSFragmentManager.showSelectFrag(2);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }

    private void initFrags(Bundle savedInstanceState) {
        if (mSFragmentManager == null) {
            mSFragmentManager = new SFragmentManager(R.id.container, this);
        }
        Functions.getInstance().addFunction(new Functions.FunNoParamsNoResult(CallFragment.TAG) {
            @Override
            void function() {
                Toast.makeText(TestFragmentConnActivity.this, "无参数无返回值", Toast.LENGTH_SHORT).show();
            }
        });

        Functions.getInstance().addFunction(new Functions.FunNoParamsHasResult<String>(MyItemRecyclerViewAdapter.TAG) {
            @Override
            String function() {
                return "我就是我，不一样的烟火";
            }
        });
        mSFragmentManager.setBundle(savedInstanceState)
                .addFragment(new CallFragment())
                .addFragment(new MessageFragment())
                .addFragment(MeFragment.newInstance(5))
                .show();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onItemClicked(int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Functions.getInstance().removeFunction(CallFragment.TAG);
        Functions.getInstance().removeFunction(MyItemRecyclerViewAdapter.TAG);
    }
}
