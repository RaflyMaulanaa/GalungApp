package id.example.galungapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import id.example.galungapp.MenuUtama.TabFragment;

public class PesananAcitivty extends AppCompatActivity {

    private FragmentTransaction beginTransaction;
    private ImageView iv_buttonback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan);

        iv_buttonback = (ImageView) findViewById(R.id.iv_buttonback);

        iv_buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        TabPesananFragment mTabPesananFragment = new TabPesananFragment();
        beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.frame_layout, mTabPesananFragment);
        beginTransaction.commit();
    }
}
