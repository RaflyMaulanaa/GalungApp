package id.example.galungapp.RegistrasiLogin;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.example.galungapp.Api.Model.Wilayah.DataKabupatenKota;
import id.example.galungapp.Api.Model.Wilayah.DataProvinsi;
import id.example.galungapp.R;
import id.example.galungapp.Searchablespinnerlibrary.SearchableSpinner;


public class RegisterKonsumenFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

        private EditText et_nama,et_tempatlahir,et_nomortlp,et_alamat,et_kecamatan,et_kelurahan,et_rt,et_rw;
        private RadioGroup rg_jkel;
        private Button btn_selanjutnya;
        private String nama,jeniskelamin="L",tempatlahir,tngllahir,tanggallahir,nomortlp,alamat,kecamatan,kelurahan,rt,rw;
        private TextView tv_tanggallahir;
        private ImageView iv_calendar;
        private SearchableSpinner spin_provinsi, spin_kabupatenkota;

        private SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        private SimpleDateFormat view = new SimpleDateFormat("MMMM dd,yyyy");

        private DatePickerDialog datePickerDialog;

        private List<DataProvinsi> dataProvinsi;
        private List<DataKabupatenKota> dataKabupatenKota;

        private int provinsi,kabupatenkota;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.fragment_registerasi, container, false);
                et_nama = (EditText) view.findViewById(R.id.et_nama);
                rg_jkel = (RadioGroup) view.findViewById(R.id.rg_jkel);
                et_tempatlahir = (EditText) view.findViewById(R.id.et_tempatlahir);
                et_nomortlp = (EditText) view.findViewById(R.id.et_nomortlp);
                tv_tanggallahir = (TextView) view.findViewById(R.id.tv_tanggallahir);
                iv_calendar = (ImageView) view.findViewById(R.id.iv_calendar);
                et_alamat = (EditText) view.findViewById(R.id.et_alamat);
                et_kecamatan = (EditText) view.findViewById(R.id.et_kecamatan);
                et_kelurahan = (EditText) view.findViewById(R.id.et_kelurahan);
                et_rt = (EditText) view.findViewById(R.id.et_rt);
                et_rw = (EditText) view.findViewById(R.id.et_rw);
                btn_selanjutnya = (Button) view.findViewById(R.id.btn_selanjutnya);
                spin_provinsi = (SearchableSpinner) view.findViewById(R.id.spin_provinsi);
                spin_kabupatenkota = (SearchableSpinner) view.findViewById(R.id.spin_kabupatenkota);
                spin_provinsi.setTitle("Pilih Provinsi");
                spin_kabupatenkota.setTitle("Pilih Kabupaten/Kota");

                getProvinsi();

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
                        getContext(),
                        RegisterKonsumenFragment.this,
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


                btn_selanjutnya.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                getDaftar();
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
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
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

                return view;
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(i, i1, i2);
                tanggallahir = input.format(calendar.getTime());
                tv_tanggallahir.setText(view.format(calendar.getTime()));
        }

        public String loadJSONFromAsset() {
                String json = null;
                try {
                        InputStream is = getActivity().getAssets().open("provinsi.json");
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                R.layout.spinner_item, listSpinner);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_provinsi.setAdapter(adapter);
                } catch (JSONException e) {
                        e.printStackTrace();
                }
        }


        private void getDaftar(){
                nama = et_nama.getText().toString();
                tempatlahir = et_tempatlahir.getText().toString();
                tngllahir = tv_tanggallahir.getText().toString();
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
                }else if(tngllahir.isEmpty()){
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

                RegistrasiSelanjutnyaFragment mRegistrasiSelanjutnyaFragment = new RegistrasiSelanjutnyaFragment(nama,jeniskelamin,tempatlahir,tanggallahir,nomortlp,alamat,String.valueOf(provinsi),String.valueOf(kabupatenkota),kecamatan,kelurahan,rt,rw,"konsumen");
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_framepetani, mRegistrasiSelanjutnyaFragment);
                transaction.addToBackStack(null);
                transaction.commit();
        }
}
