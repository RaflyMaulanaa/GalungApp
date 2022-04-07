package id.example.galungapp.MenuUtama;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sweetalert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import id.example.galungapp.Api.Client;
import id.example.galungapp.Api.InterfaceGet;
import id.example.galungapp.Api.InterfacePost;
import id.example.galungapp.Api.Model.DataHargaGabah;
import id.example.galungapp.Api.Model.DataSawah;
import id.example.galungapp.Api.Model.DataSawah_respon;
import id.example.galungapp.Api.Model.DataUserLogin;
import id.example.galungapp.Api.Model.Data_respon;
import id.example.galungapp.MainActivity;
import id.example.galungapp.Maps.MapsActivity;
import id.example.galungapp.MenuDrawer.Adapter.AdapterSawahUser;
import id.example.galungapp.MenuDrawer.AkunActivity;
import id.example.galungapp.MenuDrawer.DaftarSawahActivity;
import id.example.galungapp.MenuDrawer.EditSawahActivity;
import id.example.galungapp.ProgresDialog.ProgressDialog;
import id.example.galungapp.R;
import id.example.galungapp.Searchablespinnerlibrary.SearchableSpinner;
import id.example.galungapp.Storage.SharedPMUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GadaiSawahActivity extends AppCompatActivity {

    private InterfaceGet interfaceGet;
    private SearchableSpinner spin_sawah;
    private List<String> listSpinner;
    private List<DataSawah> dataSawahList;

    private TextView tv_luassawah, tv_lokasisawah;
    private EditText et_nilaigadai, et_periodegadai;
    private Button btn_gadai;

    private ProgressDialog dialog;

    private int idsawah=0;

    private String data_latilong, nilaigadai, periodegadai;

    private ImageView iv_buttonback;

    private InterfacePost interfacePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gadaisawah);

        tv_lokasisawah = (TextView) findViewById(R.id.tv_lokasisawah);
        tv_luassawah = (TextView) findViewById(R.id.tv_luassawah);

        et_nilaigadai = (EditText) findViewById(R.id.et_nilaigadai);
        et_periodegadai = (EditText) findViewById(R.id.et_periodegadai);
        btn_gadai = (Button) findViewById(R.id.btn_gadai);

        iv_buttonback = (ImageView) findViewById(R.id.iv_buttonback);

        iv_buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        final DataUserLogin dataUserLogin = SharedPMUser.getmInstance(this).getTokenUser();
        interfaceGet = (InterfaceGet) Client.getClientToken(dataUserLogin.getToken()).create(InterfaceGet.class);
        interfacePost = (InterfacePost) Client.getClientToken(dataUserLogin.getToken()).create(InterfacePost.class);

        dialog = new ProgressDialog(GadaiSawahActivity.this, "Please Wait..");

        spin_sawah = (SearchableSpinner) findViewById(R.id.spin_sawah);

        listSpinner = new ArrayList<String>();

        dialog.show();
        interfaceGet.getSawah().enqueue(new Callback<DataSawah_respon>() {
            @Override
            public void onResponse(Call<DataSawah_respon> call, Response<DataSawah_respon> response) {
                if(response.isSuccessful()) {
                    DataSawah_respon mDataSawah_respon = (DataSawah_respon) response.body();
                    if(mDataSawah_respon.isStatus()){
                        dataSawahList = response.body().getData();
                        if(dataSawahList.size() > 0) {
                            for (int i = 0; i < dataSawahList.size(); i++) {
                                listSpinner.add(dataSawahList.get(i).getNama());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(GadaiSawahActivity.this,
                                    R.layout.spinner_item, listSpinner);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spin_sawah.setAdapter(adapter);
                        }else{
                            Intent intent = new Intent(GadaiSawahActivity.this, MenuUtamaAcitivty.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("menu","Gadai Sawah");
                            intent.putExtra("data_sawah","kosong");
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }else{
                        SweetalertFailedandBack(mDataSawah_respon.getMessage());
                        dialog.dismiss();
                    }
                }else{
                    notValidAksesToken();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DataSawah_respon> call, Throwable t) {
                SweetalertFailedandBack("Gagal terhubung keserver");
                dialog.dismiss();
            }
        });

        spin_sawah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                idsawah = dataSawahList.get(position).getId();
                tv_lokasisawah.setText(dataSawahList.get(position).getAlamat());
                tv_luassawah.setText(dataSawahList.get(position).getLuas_sawah());
                data_latilong = dataSawahList.get(position).getTitik_koordinat();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        tv_lokasisawah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(data_latilong != null) {
                    String[] latilong = data_latilong.split(",");
                    if(latilong != null && latilong.length == 2) {
                        Intent mIntent = new Intent(GadaiSawahActivity.this, MapsActivity.class);
                        Bundle bundleData = new Bundle();
                        bundleData.putDouble("data_latitude", Double.parseDouble(latilong[0]));
                        bundleData.putDouble("data_longitude", Double.parseDouble(latilong[1]));
                        mIntent.putExtras(bundleData);
                        mIntent.putExtra("data_getmap", "view");
                        startActivity(mIntent);
                    }
                }
            }
        });

        btn_gadai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nilaigadai = et_nilaigadai.getText().toString();
                periodegadai = et_periodegadai.getText().toString();

                if(idsawah == 0){
                    TextView errorTextview = (TextView) spin_sawah.getSelectedView();
                    errorTextview.setTextColor(Color.RED);
                    errorTextview.setError("Sawah Belum Dipilih");
                    return;
                }else if(nilaigadai.isEmpty()){
                    et_nilaigadai.setError("Nilai Gadai Masih Kosong");
                    return;
                }else if(periodegadai.isEmpty()){
                    et_periodegadai.setError("Periode Gadai Masih Kosong");
                    return;
                }

                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(GadaiSawahActivity.this, SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setTitleText("Anda Yakin Data Sudah Benar?");
                sweetAlertDialog.setContentText("Data sawah yang sudah digadaikan tidak dapat dirubah");
                sweetAlertDialog.setConfirmText("Ya, Gadai Sawah!");
                sweetAlertDialog.setCancelText("Batal");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                GadaiSawah();
                            }
                        });
                sweetAlertDialog.show();

            }
        });
    }

    private void GadaiSawah(){
        dialog.show();
        interfacePost.GadaiSawah(String.valueOf(idsawah),nilaigadai,periodegadai).enqueue(new Callback<Data_respon>() {
            @Override
            public void onResponse(Call<Data_respon> call, Response<Data_respon> response) {
                if(response.isSuccessful()){
                    final Data_respon data_respon = (Data_respon) response.body();
                    if(data_respon.isStatus()){
                        new SweetAlertDialog(GadaiSawahActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Sukses")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Intent intent = new Intent(GadaiSawahActivity.this, MenuUtamaAcitivty.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("menu","Gadai Sawah");
                                        startActivity(intent);
                                    }
                                })
                                .setContentText(data_respon.getMessage())
                                .show();
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

    private void notValidAksesToken(){
        if(SharedPMUser.getmInstance(GadaiSawahActivity.this).isUserReady()) {
            SharedPMUser.getmInstance(GadaiSawahActivity.this).clear();
        }
        Intent intent = new Intent(GadaiSawahActivity.this, MainActivity.class);
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

    private void SweetalertFailedandBack(String ContentText){
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        onBackPressed();
                    }
                })
                .setContentText(ContentText)
                .show();
    }
}