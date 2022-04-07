package id.example.galungapp.MenuDrawer;

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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetalert.SweetAlertDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import id.example.galungapp.MenuDrawer.Adapter.AdapterSawahUser;
import id.example.galungapp.Api.Client;
import id.example.galungapp.Api.InterfaceGet;
import id.example.galungapp.Api.InterfacePost;
import id.example.galungapp.Api.Model.DataSawah_respon;
import id.example.galungapp.Api.Model.DataUser;
import id.example.galungapp.Api.Model.DataUserLogin;
import id.example.galungapp.Api.Model.Data_respon;
import id.example.galungapp.Api.Model.Wilayah.DataKabupatenKota;
import id.example.galungapp.Api.Model.Wilayah.DataProvinsi;
import id.example.galungapp.MainActivity;
import id.example.galungapp.MenuUtama.JualGabahActivity;
import id.example.galungapp.ProgresDialog.ProgressDialog;
import id.example.galungapp.R;
import id.example.galungapp.RegistrasiLogin.LoginRegisterActivity;
import id.example.galungapp.Searchablespinnerlibrary.SearchableSpinner;
import id.example.galungapp.Storage.SharedPMUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AkunActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText et_nama,et_tempatlahir,et_nomortlp,et_alamat,et_kecamatan,et_kelurahan,et_rt,et_rw;
    private RadioGroup rg_jkel;
    private Button btn_simpan;
    private String nama,jeniskelamin,tempatlahir,tanggallahir,nomortlp,alamat,kecamatan,kelurahan,rt,rw;
    private TextView tv_tanggallahir,tv_email;
    private Button btn_tambahsawah;
    private ImageView iv_calendar;
    private RecyclerView sawahView;
    private LinearLayoutManager layoutManager;
    private LinearLayout linear_password,linear_listsawah;

    private DatePickerDialog datePickerDialog;
    private ImageView iv_buttonback;

    private SearchableSpinner spin_provinsi, spin_kabupatenkota;

    private SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat view = new SimpleDateFormat("MMMM dd,yyyy");

    private List<DataProvinsi> dataProvinsi;
    private List<DataKabupatenKota> dataKabupatenKota;

    private AdapterSawahUser adapterSawahUser;
    private InterfaceGet interfaceGet;
    private InterfacePost interfacePost;

    private ProgressDialog dialog;

    private int provinsi,kabupatenkota;

    private DataUser dataUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);
        iv_buttonback = (ImageView) findViewById(R.id.iv_buttonback);

        iv_buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        dataUser = SharedPMUser.getmInstance(this).getUser();

        final DataUserLogin dataUserLogin = SharedPMUser.getmInstance(this).getTokenUser();
        interfaceGet = (InterfaceGet) Client.getClientToken(dataUserLogin.getToken()).create(InterfaceGet.class);
        interfacePost = (InterfacePost) Client.getClientToken(dataUserLogin.getToken()).create(InterfacePost.class);
        dialog = new ProgressDialog(AkunActivity.this, "Please Wait..");

        et_nama = (EditText) findViewById(R.id.et_nama);
        rg_jkel = (RadioGroup) findViewById(R.id.rg_jkel);
        et_tempatlahir = (EditText) findViewById(R.id.et_tempatlahir);
        et_nomortlp = (EditText) findViewById(R.id.et_nomortlp);
        tv_tanggallahir = (TextView) findViewById(R.id.tv_tanggallahir);
        iv_calendar = (ImageView) findViewById(R.id.iv_calendar);
        et_alamat = (EditText) findViewById(R.id.et_alamat);
        et_kecamatan = (EditText) findViewById(R.id.et_kecamatan);
        et_kelurahan = (EditText) findViewById(R.id.et_kelurahan);
        et_rt = (EditText) findViewById(R.id.et_rt);
        et_rw = (EditText) findViewById(R.id.et_rw);
        spin_provinsi = (SearchableSpinner) findViewById(R.id.spin_provinsi);
        spin_kabupatenkota = (SearchableSpinner) findViewById(R.id.spin_kabupatenkota);
        tv_email = (TextView) findViewById(R.id.tv_email);
        linear_password = (LinearLayout) findViewById(R.id.linear_password);
        sawahView = (RecyclerView) findViewById(R.id.sawahView);
        layoutManager = new LinearLayoutManager(this);
        sawahView.setLayoutManager(layoutManager);
        btn_tambahsawah = (Button) findViewById(R.id.btn_tambahsawah);
        linear_listsawah = (LinearLayout) findViewById(R.id.linear_listsawah);
        btn_simpan = (Button) findViewById(R.id.btn_simpan);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            String pesanBerhasil = extras.getString("pesan_berhasil");
            if(pesanBerhasil != null){
                SweetalertSuccess(pesanBerhasil);
            }
        }

        rg_jkel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);
                if(index == 0){
                    jeniskelamin = "L";
                }else if(index == 1){
                    jeniskelamin = "P";
                }
            }
        });

        datePickerDialog = new DatePickerDialog(
                this,
                AkunActivity.this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        iv_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        tv_tanggallahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        spin_provinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                provinsi = dataProvinsi.get(position).getId();
                dataKabupatenKota = dataProvinsi.get(position).getKabupaten();
                List<String> listSpinner = new ArrayList<String>();
                for (int i = 0; i < dataKabupatenKota.size(); i++){
                    listSpinner.add(dataKabupatenKota.get(i).getNama_kota());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AkunActivity.this,
                        R.layout.spinner_item, listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin_kabupatenkota.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        linear_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AkunActivity.this,EditPasswordActivity.class);
                startActivity(intent);
            }
        });

        spin_kabupatenkota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                kabupatenkota = dataKabupatenKota.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if(dataUser.getRole().equals("petani")){
            linear_listsawah.setVisibility(View.VISIBLE);
            getDataSawah();
        }else{
            linear_listsawah.setVisibility(View.GONE);
        }

        btn_tambahsawah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AkunActivity.this,DaftarSawahActivity.class);
                startActivity(intent);
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vieww) {
                nama = et_nama.getText().toString();
                tempatlahir = et_tempatlahir.getText().toString();
                nomortlp = et_nomortlp.getText().toString();
                alamat = et_alamat.getText().toString();
                kecamatan = et_kecamatan.getText().toString();
                kelurahan = et_kelurahan.getText().toString();
                rt = et_rt.getText().toString();
                rw = et_rw.getText().toString();

                if(nama.isEmpty()){
                    et_nama.setError("Nama Masih Kosong");
                    et_nama.requestFocus();
                    return;
                }else if(tempatlahir.isEmpty()){
                    et_tempatlahir.setError("Tempat Lahir Masih Kosong");
                    et_tempatlahir.requestFocus();
                    return;
                }else if(tanggallahir.isEmpty()){
                    datePickerDialog.show();
                    return;
                }else if(nomortlp.isEmpty()){
                    et_nomortlp.setError("Nomor Handphone Masih Kosong");
                    et_nomortlp.requestFocus();
                    return;
                }else if(alamat.isEmpty()) {
                    et_alamat.setError("Alamat Masih Kosong");
                    et_alamat.requestFocus();
                    return;
                }else if(provinsi == 0) {
                    TextView errorTextview = (TextView) spin_provinsi.getSelectedView();
                    errorTextview.setTextColor(Color.RED);
                    errorTextview.setError("Provinsi Belum Dipilih");
                    return;
                }else if(kabupatenkota == 0){
                    TextView errorTextview = (TextView) spin_kabupatenkota.getSelectedView();
                    errorTextview.setTextColor(Color.RED);
                    errorTextview.setError("Kabupaten/Kota Belum Dipilih");
                    return;
                }else if(kecamatan.isEmpty()){
                    et_kecamatan.setError("Kecamatan Masih Kosong");
                    et_kecamatan.requestFocus();
                    return;
                }else if(kelurahan.isEmpty()) {
                    et_kelurahan.setError("Kelurahan Masih Kosong");
                    et_kelurahan.requestFocus();
                    return;
                }else if(rt.isEmpty()) {
                    et_rt.setError("RT Masih Kosong");
                    et_rt.requestFocus();
                    return;
                }else if(rw.isEmpty()){
                    et_rw.setError("RW Masih Kosong");
                    et_rw.requestFocus();
                    return;
                }

                dialog.show();
                interfacePost.EditUser(nama,jeniskelamin,tempatlahir,tanggallahir,alamat,kecamatan,String.valueOf(kabupatenkota),kelurahan,rt,rw,nomortlp,null,null,null).enqueue(new Callback<Data_respon>() {
                    @Override
                    public void onResponse(Call<Data_respon> call, Response<Data_respon> response) {
                        if(response.isSuccessful()){
                            Data_respon data_respon = (Data_respon) response.body();
                            if(data_respon.isStatus()){
                                et_nama.setText(nama);
                                et_tempatlahir.setText(tempatlahir);
                                et_nomortlp.setText(nomortlp);
                                et_alamat.setText(alamat);
                                Date newDate = null;
                                try {
                                    newDate = input.parse(tanggallahir);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                tv_tanggallahir.setText(view.format(newDate));
                                spin_provinsi.setSelection(1);
                                et_rt.setText(rt);
                                et_rw.setText(rw);
                                et_kecamatan.setText(kecamatan);
                                et_kelurahan.setText(kelurahan);

                                DataUser userData = new DataUser(dataUser.getId(),nama,dataUser.getEmail(),tempatlahir,tanggallahir,String.valueOf(kabupatenkota),alamat,kecamatan,kelurahan,nomortlp,dataUser.getPetani_verified(),jeniskelamin,rt,rw,dataUser.getRole());
                                DataUserLogin loginUser = new DataUserLogin(dataUserLogin.getToken());
                                SharedPMUser.getmInstance(AkunActivity.this).saveUser(userData,loginUser);
                                SweetalertSuccess(data_respon.getMessage());
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

        getProvinsi();
        getDataUser();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(i, i1, i2);
        tanggallahir = input.format(calendar.getTime());
        tv_tanggallahir.setText(view.format(calendar.getTime()));
    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("provinsi.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void getProvinsi(){
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("provinsi");
            Gson gson = new Gson();
            Type dataProvinsii = new TypeToken<List<DataProvinsi>>(){}.getType();
            dataProvinsi = gson.fromJson(m_jArry.toString(), dataProvinsii);
            List<String> listSpinner = new ArrayList<String>();
            for (int i = 0; i < dataProvinsi.size(); i++){
                listSpinner.add(dataProvinsi.get(i).getNama_provinsi());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AkunActivity.this,
                    R.layout.spinner_item, listSpinner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin_provinsi.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDataUser(){
        tv_email.setText(dataUser.getEmail());
        et_nama.setText(dataUser.getName());
        et_nama.setSelection(et_nama.getText().length());
        if(dataUser.getJkel().equals("L")){
            rg_jkel.check(R.id.rb_laki);
        }else if(dataUser.getJkel().equals("P")){
            rg_jkel.check(R.id.rb_perempuan);
        }
        et_tempatlahir.setText(dataUser.getTempat_lahir());
        et_nomortlp.setText(dataUser.getNohp());
        et_alamat.setText(dataUser.getAlamat());
        tanggallahir = dataUser.getTanggal_lahir();
        Date newDate = null;
        try {
            newDate = input.parse(dataUser.getTanggal_lahir());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv_tanggallahir.setText(view.format(newDate));
        spin_provinsi.setSelection(1);
        et_rt.setText(dataUser.getRt());
        et_rw.setText(dataUser.getRw());
        et_kecamatan.setText(dataUser.getKecamatan());
        et_kelurahan.setText(dataUser.getKelurahan());
    }

    private void getDataSawah(){
        dialog.show();
        interfaceGet.getSawah().enqueue(new Callback<DataSawah_respon>() {
            @Override
            public void onResponse(Call<DataSawah_respon> call, Response<DataSawah_respon> response) {
                if(response.isSuccessful()) {
                    DataSawah_respon mDataSawah_respon = (DataSawah_respon) response.body();
                    if(mDataSawah_respon.isStatus()){
                        adapterSawahUser = new AdapterSawahUser(((DataSawah_respon) response.body()).getData(), AkunActivity.this);
                        sawahView.setAdapter(adapterSawahUser);
                        dialog.dismiss();
                    }else{
                        SweetalertFailed(mDataSawah_respon.getMessage());
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
    }

    private void notValidAksesToken(){
        if(SharedPMUser.getmInstance(AkunActivity.this).isUserReady()) {
            SharedPMUser.getmInstance(AkunActivity.this).clear();
        }
        Intent intent = new Intent(AkunActivity.this, MainActivity.class);
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
