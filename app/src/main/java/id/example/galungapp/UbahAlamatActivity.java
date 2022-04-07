package id.example.galungapp;

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

import androidx.appcompat.app.AppCompatActivity;

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

import id.example.galungapp.Api.Model.DataPengiriman;
import id.example.galungapp.Api.Model.DataUser;
import id.example.galungapp.Api.Model.Wilayah.DataKabupatenKota;
import id.example.galungapp.Api.Model.Wilayah.DataProvinsi;
import id.example.galungapp.MenuDrawer.AkunActivity;
import id.example.galungapp.Searchablespinnerlibrary.SearchableSpinner;
import id.example.galungapp.Storage.SharedPMPengiriman;
import id.example.galungapp.Storage.SharedPMUser;

public class UbahAlamatActivity extends AppCompatActivity {

    private EditText et_nama, et_nomortlp, et_alamat, et_kecamatan, et_kelurahan, et_rt, et_rw, et_keterangan;
    private Button btn_simpan;
    private String nama, notelp, alamat, kecamatan, kelurahan, rt, rw, keterangan=null;
    private SearchableSpinner spin_provinsi, spin_kabupatenkota;

    private int provinsi,kabupatenkota;
    private List<DataProvinsi> dataProvinsi;
    private List<DataKabupatenKota> dataKabupatenKota;

    private DataPengiriman mDataPengiriman;
    private DataUser dataUser;

    private ImageView iv_buttonback;

    private int id=0, total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubahalamat);

        et_nama = (EditText) findViewById(R.id.et_nama);
        et_nomortlp = (EditText) findViewById(R.id.et_nomortlp);
        et_alamat = (EditText) findViewById(R.id.et_alamat);
        et_kecamatan = (EditText) findViewById(R.id.et_kecamatan);
        et_kelurahan = (EditText) findViewById(R.id.et_kelurahan);
        et_rt = (EditText) findViewById(R.id.et_rt);
        et_rw = (EditText) findViewById(R.id.et_rw);
        et_keterangan = (EditText) findViewById(R.id.et_keterangan);

        spin_provinsi = (SearchableSpinner) findViewById(R.id.spin_provinsi);
        spin_kabupatenkota = (SearchableSpinner) findViewById(R.id.spin_kabupatenkota);

        iv_buttonback = (ImageView) findViewById(R.id.iv_buttonback);
        iv_buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            id = extras.getInt("id");
            total = extras.getInt("total");
        }

        btn_simpan = (Button) findViewById(R.id.btn_simpan);

        if(SharedPMPengiriman.getmInstance(UbahAlamatActivity.this).isDataReady()) {
            mDataPengiriman = SharedPMPengiriman.getmInstance(UbahAlamatActivity.this).getDataPengiriman();
            nama=mDataPengiriman.getNama_penerima();
            alamat=mDataPengiriman.getAlamat();
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
            kecamatan=dataUser.getKecamatan();
            kelurahan=dataUser.getKelurahan();
            rt=dataUser.getRt();
            rw=dataUser.getRw();
            notelp=dataUser.getNohp();
        }

        et_nama.setText(nama);
        et_nama.setSelection(et_nama.getText().length());
        et_alamat.setText(alamat);
        et_kecamatan.setText(kecamatan);
        et_kelurahan.setText(kelurahan);
        et_rt.setText(rt);
        et_rw.setText(rw);
        et_nomortlp.setText(notelp);
        if(keterangan!=null){
            et_keterangan.setText(keterangan);
        }

        spin_provinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                provinsi = dataProvinsi.get(position).getId();
                dataKabupatenKota = dataProvinsi.get(position).getKabupaten();
                List<String> listSpinner = new ArrayList<String>();
                for (int i = 0; i < dataKabupatenKota.size(); i++){
                    listSpinner.add(dataKabupatenKota.get(i).getNama_kota());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(UbahAlamatActivity.this,
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
                if(nama.isEmpty()){
                    et_nama.setError("Nama Penerima Masih Kosong");
                    et_nama.requestFocus();
                    return;
                }else if(notelp.isEmpty()) {
                    et_kecamatan.setError("Nomor Handphone Masih Kosong");
                    et_kecamatan.requestFocus();
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
                    et_rt.setError("Rt Masih Kosong");
                    et_rt.requestFocus();
                    return;
                }else if(rw.isEmpty()) {
                    et_rw.setError("Rw Masih Kosong");
                    et_rw.requestFocus();
                    return;
                }

                nama = et_nama.getText().toString();
                notelp = et_nomortlp.getText().toString();
                alamat = et_alamat.getText().toString();
                kecamatan = et_kecamatan.getText().toString();
                kelurahan = et_kelurahan.getText().toString();
                rt = et_rt.getText().toString();
                rw = et_rw.getText().toString();
                keterangan = et_keterangan.getText().toString();

                DataPengiriman mDataPengiriman = new DataPengiriman(true,nama,notelp,alamat,String.valueOf(kabupatenkota),kecamatan,kelurahan,rt,rw,keterangan);
                SharedPMPengiriman.getmInstance(UbahAlamatActivity.this).saveAlamat(mDataPengiriman);
                Intent intent = new Intent(UbahAlamatActivity.this, CheckoutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", id);
                intent.putExtra("total", total);
                startActivity(intent);
            }
        });

        getProvinsi();
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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(UbahAlamatActivity.this,
                    R.layout.spinner_item, listSpinner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin_provinsi.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
