package id.example.galungapp.LihatSemua;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import id.example.galungapp.R;

public class LihatSemuaActivity extends AppCompatActivity {

    private FragmentTransaction beginTransaction;

    private LinearLayout ll_satu;
    private ImageView btn_buttonsearch;

    boolean aktifsearch = false;

    private String data;

    private ImageView iv_buttonback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihatsemua);

        ll_satu = (LinearLayout) findViewById(R.id.ll_satu);
        btn_buttonsearch = (ImageView) findViewById(R.id.btn_buttonsearch);
        iv_buttonback = (ImageView) findViewById(R.id.iv_buttonback);
        iv_buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle extras = getIntent().getExtras();

        if(extras.getString("menuSearch") != null){
            ll_satu.setVisibility(View.VISIBLE);
            aktifsearch=true;
        }

        if(extras.getString("data") != null){
            data = extras.getString("data");
        }

        TabLihatSemuaFragment mTabLihatSemuaFragment;
        mTabLihatSemuaFragment = new TabLihatSemuaFragment(data);

        setFragment(mTabLihatSemuaFragment);

        btn_buttonsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(aktifsearch == false) {
                    ll_satu.setVisibility(View.VISIBLE);
                    aktifsearch=true;
                }else if(aktifsearch == true){
                    ll_satu.setVisibility(View.GONE);
                    aktifsearch=false;
                }
            }
        });

    }

    private void setFragment(Fragment fragment) {
        beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.frame_layout, fragment);
        beginTransaction.commit();
    }
}
