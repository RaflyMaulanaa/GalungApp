package id.example.galungapp.MenuDrawer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import id.example.galungapp.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KontakKamiActivity extends AppCompatActivity {

    private ImageView iv_buttonback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kontak_kami);

        iv_buttonback = (ImageView) findViewById(R.id.iv_buttonback);
        iv_buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}