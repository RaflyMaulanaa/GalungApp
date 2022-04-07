package id.example.galungapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import id.example.galungapp.MenuDrawer.AkunActivity;
import id.example.galungapp.MenuDrawer.EditSawahActivity;

public class SuksesCheckoutActivity extends AppCompatActivity {

    private ConstraintLayout constraint_info;
    private ImageView iv_info, iv_buttonback;
    private TextView tv_title, tv_subtitle;
    private Button btn_submit;

    private String NoTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sukses_checkout);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            NoTransaksi = extras.getString("NoTranskasi");
        }

        constraint_info = (ConstraintLayout) findViewById(R.id.constraint_info);
        iv_info = (ImageView) findViewById(R.id.iv_info);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        iv_buttonback = (ImageView) findViewById(R.id.iv_buttonback);
        iv_buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuksesCheckoutActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("statusLogin", 1);
                startActivity(intent);
            }
        });

        constraint_info.setVisibility(View.VISIBLE);
        iv_info.setImageResource(R.drawable.ic_suksescheckout);
        tv_title.setText("Sukses Checkout");
        tv_subtitle.setText("No. Transaksi "+NoTransaksi);
        btn_submit.setText("Lihat Pesanan");
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuksesCheckoutActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("lihatPesanan",true);
                intent.putExtra("statusLogin", 1);
                startActivity(intent);
            }
        });

    }
}