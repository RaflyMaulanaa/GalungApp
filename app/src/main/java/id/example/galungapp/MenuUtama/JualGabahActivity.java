package id.example.galungapp.MenuUtama;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sweetalert.SweetAlertDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.example.galungapp.Api.Client;
import id.example.galungapp.Api.InterfaceGet;
import id.example.galungapp.Api.InterfacePost;
import id.example.galungapp.Api.Model.DataHargaGabah;
import id.example.galungapp.Api.Model.DataHargaGabah_respon;
import id.example.galungapp.Api.Model.DataSawah_respon;
import id.example.galungapp.Api.Model.DataUserLogin;
import id.example.galungapp.Api.Model.Data_respon;
import id.example.galungapp.Api.Model.Wilayah.DataProvinsi;
import id.example.galungapp.MainActivity;
import id.example.galungapp.Maps.MapsActivity;
import id.example.galungapp.MenuDrawer.AkunActivity;
import id.example.galungapp.MenuDrawer.DaftarSawahActivity;
import id.example.galungapp.MenuDrawer.EditSawahActivity;
import id.example.galungapp.ProgresDialog.ProgressDialog;
import id.example.galungapp.R;
import id.example.galungapp.RegistrasiLogin.LoginRegisterActivity;
import id.example.galungapp.RegistrasiLogin.RegisterKonsumenFragment;
import id.example.galungapp.Searchablespinnerlibrary.SearchableSpinner;
import id.example.galungapp.Storage.SharedPMUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JualGabahActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private InterfaceGet interfaceGet;
    private InterfacePost interfacePost;

    private SearchableSpinner spin_jenisgabah;
    private List<String> listSpinner;
    private List<DataHargaGabah> dataHargaGabahList;

    private TextView tv_hargagabah, tv_tanggaljemput;
    private String hargagabah, jumlah, alamat, tngljemput, kabupatenkota, kecamatan, kelurahan, tanggaljemput, keterangan;
    private int idgabah=0;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat view = new SimpleDateFormat("MMMM dd,yyyy");

    private EditText et_jumlah, et_alamat, et_kecamatan, et_kelurahan, et_keterangan;

    private Button btn_jual;
    private ImageView iv_buttonback;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jualgabah);

        iv_buttonback = (ImageView) findViewById(R.id.iv_buttonback);

        iv_buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        interfaceGet = (InterfaceGet) Client.getClient().create(InterfaceGet.class);
        final DataUserLogin dataUserLogin = SharedPMUser.getmInstance(this).getTokenUser();
        interfacePost = (InterfacePost) Client.getClientToken(dataUserLogin.getToken()).create(InterfacePost.class);

        spin_jenisgabah = (SearchableSpinner) findViewById(R.id.spin_jenisgabah);

        dialog = new ProgressDialog(JualGabahActivity.this, "Please Wait..");

        tv_hargagabah = (TextView) findViewById(R.id.tv_hargagabah);
        tv_tanggaljemput = (TextView) findViewById(R.id.tv_tanggaljemput);
        et_jumlah = (EditText) findViewById(R.id.et_jumlah);
        et_alamat = (EditText) findViewById(R.id.et_alamat);
        et_kecamatan = (EditText) findViewById(R.id.et_kecamatan);
        et_kelurahan = (EditText) findViewById(R.id.et_kelurahan);
        et_keterangan = (EditText) findViewById(R.id.et_keterangan);
        btn_jual = (Button) findViewById(R.id.btn_jual);


        listSpinner = new ArrayList<String>();

        dialog.show();
        interfaceGet.getHargaGabah().enqueue(new Callback<DataHargaGabah_respon>() {
            @Override
            public void onResponse(Call<DataHargaGabah_respon> call, Response<DataHargaGabah_respon> response) {
                DataHargaGabah_respon mDataHargaGabah_respon = (DataHargaGabah_respon) response.body();
                if(mDataHargaGabah_respon.isStatus()){
                    dataHargaGabahList = response.body().getData();
                    for (int i = 0; i < dataHargaGabahList.size(); i++){
                        listSpinner.add(dataHargaGabahList.get(i).getNama());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(JualGabahActivity.this,
                            R.layout.spinner_item, listSpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_jenisgabah.setAdapter(adapter);
                }else{
                    SweetalertFailedandBack(mDataHargaGabah_respon.getMessage());
                    dialog.dismiss();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<DataHargaGabah_respon> call, Throwable t) {
                SweetalertFailedandBack("");
                dialog.dismiss();
            }
        });

        spin_jenisgabah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                idgabah = dataHargaGabahList.get(position).getId();
                hargagabah = "Rp. "+dataHargaGabahList.get(position).getHarga()+" /Kg";
                tv_hargagabah.setText(hargagabah);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        datePickerDialog = new DatePickerDialog(
                this,
                JualGabahActivity.this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        tv_tanggaljemput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        btn_jual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumlah = et_jumlah.getText().toString();
                alamat = et_alamat.getText().toString();
                kecamatan = et_kecamatan.getText().toString();
                kelurahan = et_kelurahan.getText().toString();
                tngljemput = tv_tanggaljemput.getText().toString();
                keterangan = et_keterangan.getText().toString();

                if(idgabah == 0) {
                    TextView errorTextview = (TextView) spin_jenisgabah.getSelectedView();
                    errorTextview.setTextColor(Color.RED);
                    errorTextview.setError("Jenis Gabah Belum Dipilih");
                    return;
                }else if(jumlah.isEmpty()){
                    et_jumlah.setError("Jumlah Perkiraat Berat Gabah Masih Kosong");
                    et_jumlah.requestFocus();
                    return;
                }else if(alamat.isEmpty()){
                    et_alamat.setError("Alamat Penjemputan Gabah Masih Kosong");
                    et_alamat.requestFocus();
                    return;
                }else if(kecamatan.isEmpty()){
                    et_kecamatan.setError("Kecamatan Masih Kosong");
                    et_kecamatan.requestFocus();
                    return;
                }else if(kelurahan.isEmpty()){
                    et_kelurahan.setError("Kelurahan Masih Kosong");
                    et_kelurahan.requestFocus();
                    return;
                }else if(tngljemput.isEmpty()){
                    datePickerDialog.show();
                    return;
                }

                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(JualGabahActivity.this, SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setTitleText("Anda Yakin Data Sudah Benar?");
                sweetAlertDialog.setContentText("Data gabah yang sudah dijual dan alamat penjemputan tidak dapat dirubah");
                sweetAlertDialog.setConfirmText("Ya, Jual Gabah!");
                sweetAlertDialog.setCancelText("Batal");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        JualGabah();
                    }
                });
                sweetAlertDialog.show();
            }
        });

    }

    private void JualGabah(){
        dialog.show();
        interfacePost.JualGabah(String.valueOf(idgabah),jumlah,alamat,kecamatan,kelurahan,tanggaljemput+" 00:00:00",keterangan).enqueue(new Callback<Data_respon>() {
            @Override
            public void onResponse(Call<Data_respon> call, Response<Data_respon> response) {
                if(response.isSuccessful()){
                    final Data_respon data_respon = (Data_respon) response.body();
                    if(data_respon.isStatus()){
                        new SweetAlertDialog(JualGabahActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Sukses")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Intent intent = new Intent(JualGabahActivity.this, MenuUtamaAcitivty.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("menu","Gabahku");
                                        intent.putExtra("berhasil_input",data_respon.getMessage());
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

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(i, i1, i2);
        tanggaljemput = input.format(calendar.getTime());
        tv_tanggaljemput.setText(view.format(calendar.getTime()));
    }

    private void notValidAksesToken(){
        if(SharedPMUser.getmInstance(JualGabahActivity.this).isUserReady()) {
            SharedPMUser.getmInstance(JualGabahActivity.this).clear();
        }
        Intent intent = new Intent(JualGabahActivity.this, MainActivity.class);
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
