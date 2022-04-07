package id.example.galungapp.RegistrasiLogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import id.example.galungapp.R;

public class LoginRegisterActivity extends AppCompatActivity  {

    private TextView tv_title,tv_bottom,tv_button;
    private int position;
    private FragmentTransaction beginTransaction;
    private ImageView iv_buttonback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginregister);

        Bundle extras = getIntent().getExtras();
        position = extras.getInt("position");

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_bottom = (TextView) findViewById(R.id.tv_bottom);
        tv_button = (TextView) findViewById(R.id.tv_button);
        iv_buttonback = (ImageView) findViewById(R.id.iv_buttonback);

        iv_buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position == 1){
                    position = 0;
                    getPosition(0);
                }else if(position == 0){
                    position = 1;
                    getPosition(1);
                }
            }
        });
        getPosition(position);
    }

    private void getPosition(int position){
        if(position == 1){
            tv_title.setText("Login");
            tv_bottom.setText("Belum Punya Akun?");
            LoginFragment mLoginFragment = new LoginFragment();
            setFragment(mLoginFragment);
            tv_button.setText("Daftar Sekarang");
        }else{
            tv_title.setText("Registrasi");
            tv_bottom.setText("Sudah Punya Akun?");
            TabFragment mTabFragment;
            mTabFragment = new TabFragment();
            setFragment(mTabFragment);
            tv_button.setText("Masuk");
        }
    }

    private void setFragment(Fragment fragment) {
        beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.frame_loginregister, fragment);
        beginTransaction.commit();
    }

}
