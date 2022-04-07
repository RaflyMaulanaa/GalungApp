package id.example.galungapp.MenuDrawer;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import id.example.galungapp.Api.Client;
import id.example.galungapp.Api.InterfacePost;
import id.example.galungapp.Api.Model.DataUserLogin;
import id.example.galungapp.Api.Model.Data_respon;
import id.example.galungapp.Api.Model.Wilayah.DataKabupatenKota;
import id.example.galungapp.Api.Model.Wilayah.DataProvinsi;
import id.example.galungapp.MainActivity;
import id.example.galungapp.Maps.MapsActivity;
import id.example.galungapp.MenuUtama.JualGabahActivity;
import id.example.galungapp.ProgresDialog.ProgressDialog;
import id.example.galungapp.R;
import id.example.galungapp.RegistrasiLogin.LoginRegisterActivity;
import id.example.galungapp.Searchablespinnerlibrary.SearchableSpinner;
import id.example.galungapp.Storage.SharedPMUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSawahActivity extends AppCompatActivity {

    private TextView tv_lokasisawah;
    private SearchableSpinner spin_provinsi, spin_kabupatenkota;
    private EditText et_kecamatan, et_kelurahan, et_luassawah, et_namasawah;
    private Button btn_simpan, btn_hapus;
    private List<DataProvinsi> dataProvinsi;
    private List<DataKabupatenKota> dataKabupatenKota;

    private String namasawah, titikkoordinat, kecamatan, kelurahan, luassawah, alamatlengkap;
    private int provinsi, kabupatenkota, id_sawah;

    private ImageView iv_buttonback;

    private InterfacePost interfacePost;

    private ProgressDialog dialog;

    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editsawah);

        extras = getIntent().getExtras();

        tv_lokasisawah = (TextView) findViewById(R.id.tv_lokasisawah);

        if(extras != null) {
            titikkoordinat = extras.getString("titik_koordinat");
        }

        iv_buttonback = (ImageView) findViewById(R.id.iv_buttonback);

        iv_buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        DataUserLogin dataUserLogin = SharedPMUser.getmInstance(this).getTokenUser();
        interfacePost = (InterfacePost) Client.getClientToken(dataUserLogin.getToken()).create(InterfacePost.class);
        dialog = new ProgressDialog(EditSawahActivity.this, "Please Wait..");

        et_kecamatan = (EditText) findViewById(R.id.et_kecamatan);
        et_kelurahan = (EditText) findViewById(R.id.et_kelurahan);
        et_luassawah = (EditText) findViewById(R.id.et_luassawah);
        et_namasawah = (EditText) findViewById(R.id.et_namasawah);
        btn_simpan = (Button) findViewById(R.id.btn_simpan);
        btn_hapus = (Button) findViewById(R.id.btn_hapus);

        spin_provinsi = (SearchableSpinner) findViewById(R.id.spin_provinsi);
        spin_kabupatenkota = (SearchableSpinner) findViewById(R.id.spin_kabupatenkota);
        spin_provinsi.setTitle("Pilih Provinsi");
        spin_kabupatenkota.setTitle("Pilih Kabupaten/Kota");

        getProvinsi();
        LoadDataSawah();

        tv_lokasisawah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(EditSawahActivity.this, MapsActivity.class);
                mIntent.putExtra("id_sawah",extras.getInt("id_sawah"));
                mIntent.putExtra("kecamatan",extras.getString("kecamatan"));
                mIntent.putExtra("kelurahan",extras.getString("kelurahan"));
                mIntent.putExtra("nama_sawah",extras.getString("nama_sawah"));
                mIntent.putExtra("luas_sawah",extras.getString("luas_sawah"));
                String data_latilong = extras.getString("titik_koordinat");
                if(data_latilong != null) {
                    String[] latilong = data_latilong.split(",");
                    if(latilong != null && latilong.length == 2) {
                        Bundle bundleData = new Bundle();
                        bundleData.putDouble("data_latitude", Double.parseDouble(latilong[0]));
                        bundleData.putDouble("data_longitude", Double.parseDouble(latilong[1]));
                        mIntent.putExtras(bundleData);
                    }
                }
                mIntent.putExtra("data_getmap", "edit");
                startActivity(mIntent);
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditSawahActivity.this,
                        R.layout.spinner_item, listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin_kabupatenkota.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namasawah = et_namasawah.getText().toString();
                kecamatan = et_kecamatan.getText().toString();
                kelurahan = et_kelurahan.getText().toString();
                luassawah = et_luassawah.getText().toString();
                alamatlengkap = tv_lokasisawah.getText().toString();
                if (namasawah.isEmpty()) {
                    et_namasawah.setError("Nama Sawah Masih Kosong");
                    et_namasawah.requestFocus();
                    return;
                }else if(titikkoordinat == null || alamatlengkap.isEmpty()){
                    Intent mIntent = new Intent(EditSawahActivity.this, MapsActivity.class);
                    mIntent.putExtra("data_getmap", "edit");
                    startActivity(mIntent);
                    return;
                }else if(provinsi == 0) {
                    TextView errorTextview = (TextView) spin_provinsi.getSelectedView();
                    errorTextview.setTextColor(Color.RED);
                    errorTextview.setError("Provinsi Sawah Belum Dipilih");
                    return;
                }else if(kabupatenkota == 0) {
                    TextView errorTextview = (TextView) spin_kabupatenkota.getSelectedView();
                    errorTextview.setTextColor(Color.RED);
                    errorTextview.setError("Kabupaten/Kota Sawah Belum Dipilih");
                    return;
                }else if(kecamatan.isEmpty()){
                    et_kecamatan.setError("Kecamatan Sawah Masih Kosong");
                    et_kecamatan.requestFocus();
                    return;
                }else if(kelurahan.isEmpty()){
                    et_kelurahan.setError("Kelurahan Sawah Masih Kosong");
                    et_kelurahan.requestFocus();
                    return;
                }else if(luassawah.isEmpty()){
                    et_luassawah.setError("Luas Sawah Masih Kosong");
                    et_luassawah.requestFocus();
                    return;
                }

                dialog.show();
                interfacePost.Inputsawah(String.valueOf(id_sawah),luassawah,titikkoordinat,String.valueOf(kabupatenkota),kelurahan,kecamatan,alamatlengkap,namasawah).enqueue(new Callback<Data_respon>() {
                    @Override
                    public void onResponse(Call<Data_respon> call, Response<Data_respon> response) {
                        if(response.isSuccessful()){
                            Data_respon data_respon = (Data_respon) response.body();
                            if(data_respon.isStatus()){
                                Intent intent = new Intent(EditSawahActivity.this, AkunActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("pesan_berhasil",data_respon.getMessage());
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

        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(EditSawahActivity.this, SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setTitleText("Hapus Sawah");
                sweetAlertDialog.setContentText("Apakah anda yakin ingin menghapus data "+extras.getString("nama_sawah")+" ?");
                sweetAlertDialog.setConfirmText("Ya, Hapus!");
                sweetAlertDialog.setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                dialog.show();
                                interfacePost.DeteleSawah(String.valueOf(extras.getInt("id_sawah"))).enqueue(new Callback<Data_respon>() {
                                    @Override
                                    public void onResponse(Call<Data_respon> call, Response<Data_respon> response) {
                                        if(response.isSuccessful()) {
                                            Data_respon data_respon = (Data_respon) response.body();
                                            if (data_respon.isStatus()) {
                                                sweetAlertDialog.dismiss();
                                                Intent intent = new Intent(EditSawahActivity.this, AkunActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.putExtra("pesan_berhasil", data_respon.getMessage());
                                                startActivity(intent);
                                            } else {
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
                sweetAlertDialog.show();
            }
        });
    }

    private void LoadDataSawah(){
        id_sawah = extras.getInt("id_sawah");
        spin_provinsi.setSelection(1);
        if(extras.getString("alamat") != null) {
            tv_lokasisawah.setText(extras.getString("alamat"));
        }else if(extras.getString("lokasi") != null){
            tv_lokasisawah.setText(extras.getString("lokasi"));
        }
        et_kecamatan.setText(extras.getString("kecamatan"));
        et_kelurahan.setText(extras.getString("kelurahan"));
        et_namasawah.setText(extras.getString("nama_sawah"));
        et_namasawah.setSelection(et_namasawah.getText().length());
        et_luassawah.setText(extras.getString("luas_sawah"));
    }

    public String loadJSONFromAsset() {
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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.spinner_item, listSpinner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin_provinsi.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void notValidAksesToken(){
        if(SharedPMUser.getmInstance(EditSawahActivity.this).isUserReady()) {
            SharedPMUser.getmInstance(EditSawahActivity.this).clear();
        }
        Intent intent = new Intent(EditSawahActivity.this, MainActivity.class);
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
