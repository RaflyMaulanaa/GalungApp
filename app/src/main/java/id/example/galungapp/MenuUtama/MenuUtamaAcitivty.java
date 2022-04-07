package id.example.galungapp.MenuUtama;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import id.example.galungapp.R;

public class MenuUtamaAcitivty extends AppCompatActivity {

    private FragmentTransaction beginTransaction;
    private ImageView iv_buttonback;
    private TextView tv_title;

    private String menu, berhasilInput, dataSawah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama);

        iv_buttonback = (ImageView) findViewById(R.id.iv_buttonback);
        tv_title = (TextView) findViewById(R.id.tv_title);

        Bundle extras = getIntent().getExtras();

        iv_buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(extras != null) {
            if(extras.getString("menu") != null) {
                menu = extras.getString("menu");
                tv_title.setText(menu);
            }
            if(extras.getString("berhasil_input") != null){
                berhasilInput = extras.getString("berhasil_input");
            }
            if(extras.getString("data_sawah") != null){
                dataSawah = extras.getString("data_sawah");
            }
        }

        TabFragment mTabFragment = new TabFragment(berhasilInput,menu,dataSawah);
        beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.frame_layout, mTabFragment);
        beginTransaction.commit();
    }
}
