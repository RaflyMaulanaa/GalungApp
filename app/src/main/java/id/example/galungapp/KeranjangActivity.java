package id.example.galungapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import id.example.galungapp.Adapter.AdapterDetailPembayaran;
import id.example.galungapp.Adapter.AdapterKeranjang;
import id.example.galungapp.Api.Client;
import id.example.galungapp.Api.InterfaceGet;
import id.example.galungapp.Api.InterfacePost;
import id.example.galungapp.Api.Model.DataKeranjang;
import id.example.galungapp.Api.Model.DataUserLogin;
import id.example.galungapp.Api.Model.DataKeranjang_respon;
import id.example.galungapp.Api.Model.Data_respon;
import id.example.galungapp.ProgresDialog.ProgressDialog;
import id.example.galungapp.Storage.SharedPMUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KeranjangActivity extends AppCompatActivity {

    private LinearLayoutManager mLinearLayoutManagerKeranjang, mLinearLayoutManagerDetailPembayaran;
    private InterfaceGet interfaceGet;
    private InterfacePost interfacePost;
    private AdapterKeranjang adapterKeranjang;
    private AdapterDetailPembayaran adapterDetailPembayaran;
    private List<DataKeranjang> dataKeranjang = new ArrayList();

    private RecyclerView recyclerKeranjang;
    private RecyclerView recyclerDetailPembayaran;

    private ConstraintLayout constraint_info;
    private ImageView iv_info;
    private TextView tv_totalpesanan, tv_title, tv_subtitle, tv_total;
    private Button btn_submit, btn_pesan;

    private ImageView iv_buttonback;

    private SwipeRefreshLayout swiperefresh;

    private ProgressDialog dialog;

    private int id=0,total=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        final DataUserLogin dataUserLogin = SharedPMUser.getmInstance(this).getTokenUser();
        interfaceGet = (InterfaceGet) Client.getClientToken(dataUserLogin.getToken()).create(InterfaceGet.class);
        interfacePost = (InterfacePost) Client.getClientToken(dataUserLogin.getToken()).create(InterfacePost.class);
        recyclerKeranjang = (RecyclerView) findViewById(R.id.recyclerKeranjang);
        recyclerDetailPembayaran = (RecyclerView) findViewById(R.id.recyclerDetailPembayaran);

        mLinearLayoutManagerKeranjang = new LinearLayoutManager(this);
        recyclerKeranjang.setLayoutManager(mLinearLayoutManagerKeranjang);
        mLinearLayoutManagerDetailPembayaran = new LinearLayoutManager(this);
        recyclerDetailPembayaran.setLayoutManager(mLinearLayoutManagerDetailPembayaran);

        constraint_info = (ConstraintLayout) findViewById(R.id.constraint_info);
        tv_totalpesanan = (TextView) findViewById(R.id.tv_totalpesanan);
        iv_info = (ImageView) findViewById(R.id.iv_info);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
        tv_total = (TextView) findViewById(R.id.tv_total);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_pesan = (Button) findViewById(R.id.btn_pesan);

        iv_buttonback = (ImageView) findViewById(R.id.iv_buttonback);
        iv_buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        dialog = new ProgressDialog(this, "Please Wait..");

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataKeranjang.clear();
                loadData();
            }
        });

        loadData();

        btn_pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id != 0 && total != 0) {
                    Intent intent = new Intent(KeranjangActivity.this, CheckoutActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("total", total);
                    startActivity(intent);
                }
            }
        });
    }

    private void loadData() {
        dialog.show();
        interfaceGet.getKeranjang().enqueue(new Callback<DataKeranjang_respon>() {
            @Override
            public void onResponse(Call<DataKeranjang_respon> call, Response<DataKeranjang_respon> response) {
                if(response.isSuccessful()) {
                    constraint_info.setVisibility(View.GONE);
                    DataKeranjang_respon mDataKeranjang_respon = (DataKeranjang_respon) response.body();
                    if(mDataKeranjang_respon.isStatus()){
                        if(response.body().getData().size() > 0) {
                            tv_totalpesanan.setText(String.valueOf(mDataKeranjang_respon.getData().size()));
                            adapterKeranjang = new AdapterKeranjang(((DataKeranjang_respon) response.body()).getData(), KeranjangActivity.this);
                            recyclerKeranjang.setAdapter(adapterKeranjang);
                            adapterDetailPembayaran = new AdapterDetailPembayaran(((DataKeranjang_respon) response.body()).getData(), KeranjangActivity.this);
                            recyclerDetailPembayaran.setAdapter(adapterDetailPembayaran);
                            id=mDataKeranjang_respon.getId_transaski();
                            total=Integer.parseInt(mDataKeranjang_respon.getTotal());
                            tv_total.setText(formatRupiah(Double.parseDouble(mDataKeranjang_respon.getTotal())));
                            dialog.dismiss();
                            swiperefresh.setRefreshing(false);
                        }else{
                            showInfo("Data Kosong","Data keranjang kosong","Refresh");
                            dialog.dismiss();
                            swiperefresh.setRefreshing(false);
                        }
                    }else{
                        showInfo("Data Kosong",mDataKeranjang_respon.getMessage(),"Refresh");
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }
                }else{
                    notValidAksesToken();
                    dialog.dismiss();
                    swiperefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<DataKeranjang_respon> call, Throwable t) {
                showInfo("Gagal terhubung keserver","Data keranjang tidak dapat ditampilkan","Refresh");
                dialog.dismiss();
                swiperefresh.setRefreshing(false);
            }
        });
    }

    public void hapusKeranjang(String id_item){
        dialog.show();
        interfacePost.HapusKeranjang(id_item).enqueue(new Callback<Data_respon>() {
            @Override
            public void onResponse(Call<Data_respon> call, Response<Data_respon> response) {
                if(response.isSuccessful()) {
                    Data_respon mData_respon = (Data_respon) response.body();
                    if(mData_respon.isStatus()){
                        dataKeranjang.clear();
                        loadData();
                    }else{
                        showInfo("Gagal terhubung keserver",mData_respon.getMessage(),"Refresh");
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }
                }else{
                    notValidAksesToken();
                    dialog.dismiss();
                    swiperefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<Data_respon> call, Throwable t) {
                showInfo("Gagal terhubung keserver","Data keranjang tidak dapat ditampilkan","Refresh");
                dialog.dismiss();
                swiperefresh.setRefreshing(false);
            }
        });
    }

    private void showInfo(String title, String subtitle, String button){
        constraint_info.setVisibility(View.VISIBLE);
        tv_title.setText(title);
        tv_subtitle.setText(subtitle);
        btn_submit.setText(button);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });
    }

    private void notValidAksesToken(){
        if(SharedPMUser.getmInstance(KeranjangActivity.this).isUserReady()) {
            SharedPMUser.getmInstance(KeranjangActivity.this).clear();
        }
        Intent intent = new Intent(KeranjangActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("statusLogin",0);
        intent.putExtra("statusfailed","accessToken");
        startActivity(intent);
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
}
