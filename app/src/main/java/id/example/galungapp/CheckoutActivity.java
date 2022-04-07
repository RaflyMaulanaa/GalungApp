package id.example.galungapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sweetalert.SweetAlertDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import id.example.galungapp.Api.Client;
import id.example.galungapp.Api.InterfaceGet;
import id.example.galungapp.Api.InterfacePost;
import id.example.galungapp.Api.Model.DataPengiriman;
import id.example.galungapp.Api.Model.DataUser;
import id.example.galungapp.Api.Model.DataUserLogin;
import id.example.galungapp.Api.Model.Data_respon;
import id.example.galungapp.MenuDrawer.AkunActivity;
import id.example.galungapp.MenuDrawer.DaftarSawahActivity;
import id.example.galungapp.ProgresDialog.ProgressDialog;
import id.example.galungapp.Storage.SharedPMPengiriman;
import id.example.galungapp.Storage.SharedPMUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private LinearLayout btn_ubahalamat;
    private TextView tv_total;
    private int id=0, total=0;

    private TextView tv_nama, tv_alamat, tv_provinsi, tv_kubupatenkota, tv_kecamatan, tv_kelurahan, tv_rt, tv_rw, tv_telp;
    private DataUser dataUser;
    private Button btn_checkout;

    private String nama, alamat, provinsi, kabupatenkota, kecamatan, kelurahan, rt, rw, notelp, keterangan=null;

    private ImageView iv_buttonback;

    private DataPengiriman mDataPengiriman;

    private InterfacePost interfacePost;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        tv_total = (TextView) findViewById(R.id.tv_total);
        tv_nama = (TextView) findViewById(R.id.tv_nama);
        tv_alamat = (TextView) findViewById(R.id.tv_alamat);
//        tv_provinsi = (TextView) findViewById(R.id.tv_provinsi);
//        tv_kubupatenkota = (TextView) findViewById(R.id.tv_kubupatenkota);
        tv_kecamatan = (TextView) findViewById(R.id.tv_kecamatan);
        tv_kelurahan = (TextView) findViewById(R.id.tv_kelurahan);
        tv_rt = (TextView) findViewById(R.id.tv_rt);
        tv_rw = (TextView) findViewById(R.id.tv_rw);
        tv_telp = (TextView) findViewById(R.id.tv_telp);
        btn_checkout = (Button) findViewById(R.id.btn_checkout);

        iv_buttonback = (ImageView) findViewById(R.id.iv_buttonback);
        iv_buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final DataUserLogin dataUserLogin = SharedPMUser.getmInstance(this).getTokenUser();
        dialog = new ProgressDialog(CheckoutActivity.this, "Please Wait..");
        interfacePost = (InterfacePost) Client.getClientToken(dataUserLogin.getToken()).create(InterfacePost.class);

        if(SharedPMPengiriman.getmInstance(CheckoutActivity.this).isDataReady()) {
            mDataPengiriman = SharedPMPengiriman.getmInstance(CheckoutActivity.this).getDataPengiriman();

            nama=mDataPengiriman.getNama_penerima();
            alamat=mDataPengiriman.getAlamat();
            kabupatenkota=mDataPengiriman.getKabupatenkota();
            kecamatan=mDataPengiriman.getKecamatan();
            kelurahan=mDataPengiriman.getKelurahan();
            rt=mDataPengiriman.getRt();
            rw=mDataPengiriman.getRw();
            notelp=mDataPengiriman.getNohp();
            keterangan=mDataPengiriman.getKeterangan();
        }else {
            dataUser = SharedPMUser.getmInstance(this).getUser();
            nama=dataUser.getName();
            alamat=dataUser.getAlamat();
            kabupatenkota=dataUser.getAlamat_id();
            kecamatan=dataUser.getKecamatan();
            kelurahan=dataUser.getKelurahan();
            rt=dataUser.getRt();
            rw=dataUser.getRw();
            notelp=dataUser.getNohp();
        }

        tv_nama.setText(nama);
        tv_alamat.setText(alamat);
//        tv_provinsi.setText("Sulawesi Selatan");
//        tv_kubupatenkota.setText("Pinrang");
        tv_kecamatan.setText(kecamatan);
        tv_kelurahan.setText(kelurahan);
        tv_rt.setText(rt);
        tv_rw.setText(rw);
        tv_telp.setText(notelp);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            id = extras.getInt("id");
            total = extras.getInt("total");
            tv_total.setText(String.valueOf(formatRupiah(Double.parseDouble(String.valueOf(total)))));
        }

        btn_ubahalamat = (LinearLayout) findViewById(R.id.btn_ubahalamat);
        btn_ubahalamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckoutActivity.this, UbahAlamatActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("total", total);
                startActivity(intent);
            }
        });

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                interfacePost.Checkout(String.valueOf(id),nama,notelp,kabupatenkota,alamat,kecamatan,kelurahan,rt,rw,"Tess").enqueue(new Callback<Data_respon>() {
                    @Override
                    public void onResponse(Call<Data_respon> call, Response<Data_respon> response) {
                        if(response.isSuccessful()){
                            Data_respon data_respon = (Data_respon) response.body();
                            if(data_respon.isStatus()){
                                if(SharedPMPengiriman.getmInstance(CheckoutActivity.this).isDataReady()) {
                                    SharedPMPengiriman.getmInstance(CheckoutActivity.this).clear();
                                }
                                Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("suksesCheckout",true);
                                intent.putExtra("NoTransaksi",data_respon.getMessage());
                                intent.putExtra("statusLogin", 1);
                                startActivity(intent);
                            }else{
                                SweetalertFailed(data_respon.getMessage());
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
        if(SharedPMUser.getmInstance(CheckoutActivity.this).isUserReady()) {
            SharedPMUser.getmInstance(CheckoutActivity.this).clear();
        }
        Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
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
}
