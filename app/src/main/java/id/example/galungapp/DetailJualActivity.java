package id.example.galungapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.sweetalert.SweetAlertDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import id.example.galungapp.Api.Client;
import id.example.galungapp.Api.InterfacePost;
import id.example.galungapp.Api.Model.DataUserLogin;
import id.example.galungapp.Api.Model.Data_respon;
import id.example.galungapp.ProgresDialog.ProgressDialog;
import id.example.galungapp.Storage.SharedPMUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailJualActivity extends AppCompatActivity {

    private ImageView iv_buttonback, iv_gambar;
    private TextView tv_nama, tv_harga, tv_minpembelian, tv_stok, tv_keterangan;
    private Button btn_beli;
    LinearLayout btn_home;

    String id, nama, gambar, harga, jenis_data, min_beli, keterangan, imgUrl;
    int stok=0;
    int jumlahbeli=1;

    private InterfacePost mInterfacePost;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_jual);

        final DataUserLogin dataUserLogin = SharedPMUser.getmInstance(this).getTokenUser();
        mInterfacePost = Client.getClientToken(dataUserLogin.getToken()).create(InterfacePost.class);
        dialog = new ProgressDialog(DetailJualActivity.this, "Please Wait..");

        iv_buttonback = (ImageView) findViewById(R.id.iv_buttonback);
        iv_buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_home = (LinearLayout) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailJualActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("statusLogin", 1);
                startActivity(intent);
            }
        });

        iv_gambar = (ImageView) findViewById(R.id.iv_gambar);
        tv_nama = (TextView) findViewById(R.id.tv_nama);
        tv_harga = (TextView) findViewById(R.id.tv_harga);
        tv_minpembelian = (TextView) findViewById(R.id.tv_minpembelian);
        tv_stok = (TextView) findViewById(R.id.tv_stok);
        tv_keterangan = (TextView) findViewById(R.id.tv_keterangan);
        btn_beli = (Button) findViewById(R.id.btn_beli);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            id = String.valueOf(extras.getInt("id"));
            gambar = extras.getString("gambar");
            nama = extras.getString("nama");
            harga = extras.getString("harga");
            jenis_data = extras.getString("jenis_data");
            min_beli = extras.getString("min_beli");
            stok = Integer.parseInt(extras.getString("stok"));
            keterangan = extras.getString("keterangan");
        }

        jumlahbeli = Integer.parseInt(min_beli);

        imgUrl = "https://galung-app.asdar12.my.id/storage/"+gambar;
        Glide.with(this)
                .load(imgUrl)
                .placeholder(R.drawable.dua)
                .centerCrop()
                .into(iv_gambar);
        tv_nama.setText(nama);
        if(jenis_data.equals("Beras") || jenis_data.equals("BibitPupuk")) {
            tv_stok.setText("Tersisa " + String.valueOf(stok) + " Karung");
            tv_harga.setText(formatRupiah(Double.parseDouble(harga)) + " / Kg");
            tv_minpembelian.setVisibility(View.VISIBLE);
            tv_minpembelian.setText("min. " + min_beli + " Kg");
        }
        if(jenis_data.equals("Alat")){
            tv_stok.setText("Tersisa " + String.valueOf(stok) + " Unit");
            tv_harga.setText(formatRupiah(Double.parseDouble(harga)) + " / Unit");
            tv_minpembelian.setVisibility(View.GONE);
        }
        tv_keterangan.setText(keterangan);


        btn_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        DetailJualActivity.this, R.style.BottomSheetDialogTheme
                );
                final View bottomSheetView = getLayoutInflater()
                        .inflate(
                                R.layout.bottom_sheet_item_detail_jual,
                                (LinearLayout)findViewById(R.id.bottom_sheet)
                        );
                jumlahbeli = Integer.parseInt(min_beli);
                ImageView iv_gambarbottom = (ImageView) bottomSheetView.findViewById(R.id.iv_gambarbottom);
                TextView tv_namabottom = (TextView) bottomSheetView.findViewById(R.id.tv_namabottom);
                final TextView tv_jumlahbelibottom = (TextView) bottomSheetView.findViewById(R.id.tv_jumlahbelibottom);
                TextView tv_hargabottom = (TextView) bottomSheetView.findViewById(R.id.tv_hargabottom);
                Button btn_kurang = (Button) bottomSheetView.findViewById(R.id.btn_kurang);
                Button btn_tambah = (Button) bottomSheetView.findViewById(R.id.btn_tambah);
                Button btn_tambahkeranjang = (Button) bottomSheetView.findViewById(R.id.btn_tambahkeranjang);
                imgUrl = "https://galung-app.asdar12.my.id/storage/"+gambar;
                Glide.with(DetailJualActivity.this)
                        .load(imgUrl)
                        .placeholder(R.drawable.dua)
                        .centerCrop()
                        .into(iv_gambarbottom);
                tv_namabottom.setText(nama);
                tv_jumlahbelibottom.setText(String.valueOf(jumlahbeli));
                if(jenis_data.equals("Beras") || jenis_data.equals("BibitPupuk")) {
                    tv_hargabottom.setText(formatRupiah(Double.parseDouble(harga)) + " / Kg");
                }
                if(jenis_data.equals("Alat")){
                    tv_hargabottom.setText(formatRupiah(Double.parseDouble(harga)) + " / Unit");
                }
                btn_tambahkeranjang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tambahKerangjang();
                        bottomSheetDialog.dismiss();
                    }
                });
                btn_kurang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_jumlahbelibottom.setTextColor(getResources().getColor(R.color.textTitleGabah));
                        if(jumlahbeli > 1) {
                            if(jumlahbeli > Integer.parseInt(min_beli)) {
                                jumlahbeli = jumlahbeli - 1;
                                tv_jumlahbelibottom.setText(String.valueOf(jumlahbeli));
                            }else{
                                tv_jumlahbelibottom.setTextColor(Color.RED);
                            }
                        }
                    }
                });
                btn_tambah.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_jumlahbelibottom.setTextColor(getResources().getColor(R.color.textTitleGabah));
                        if(jumlahbeli < stok) {
                            jumlahbeli = jumlahbeli+1;
                            tv_jumlahbelibottom.setText(String.valueOf(jumlahbeli));
                        }else{
                            tv_jumlahbelibottom.setTextColor(Color.RED);
                        }
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
    }

    private void tambahKerangjang(){
        dialog.show();
        mInterfacePost.TambahKeranjang(id,String.valueOf(jumlahbeli)).enqueue(new Callback<Data_respon>() {
            @Override
            public void onResponse(Call<Data_respon> call, Response<Data_respon> response) {
                if(response.isSuccessful()) {
                    Data_respon mData_respon = (Data_respon) response.body();
                    if (mData_respon.isStatus()) {
                        SweetalertSuccess("Berhasil ditambahkan ke keranjang");
                    } else {
                        SweetalertFailed(mData_respon.getMessage());
                    }
                    dialog.dismiss();
                }else{
                    notValidAksesToken();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Data_respon> call, Throwable t) {
                SweetalertFailed("Gagal terhubung keserver");
                dialog.dismiss();
            }
        });
    }


    private String formatRupiah(Double number){
        DecimalFormat formatRupiah = (DecimalFormat) NumberFormat.getInstance();
        Locale localeID = new Locale("in", "ID");
        String symbol = Currency.getInstance(localeID).getSymbol(localeID);
        formatRupiah.setGroupingUsed(true);
        formatRupiah.setPositivePrefix(symbol+" ");
        formatRupiah.setNegativePrefix(symbol+" -");
        return formatRupiah.format(number);
    }

    private void notValidAksesToken(){
        if(SharedPMUser.getmInstance(DetailJualActivity.this).isUserReady()) {
            SharedPMUser.getmInstance(DetailJualActivity.this).clear();
        }
        Intent intent = new Intent(DetailJualActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("statusLogin",0);
        intent.putExtra("statusfailed","accessToken");
        startActivity(intent);
    }

    private void  SweetalertFailed(String ContentText){
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops")
                .setContentText(ContentText)
                .show();
    }

    private void  SweetalertSuccess(String ContentText){
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Sukses")
                .setContentText(ContentText)
                .show();
    }
}
