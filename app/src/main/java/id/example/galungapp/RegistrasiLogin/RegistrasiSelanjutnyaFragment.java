package id.example.galungapp.RegistrasiLogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.sweetalert.SweetAlertDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.example.galungapp.Api.Client;
import id.example.galungapp.Api.InterfacePost;
import id.example.galungapp.Api.Model.DataUser;
import id.example.galungapp.Api.Model.DataUserLogin;
import id.example.galungapp.Api.Model.User_respon;
import id.example.galungapp.MainActivity;
import id.example.galungapp.ProgresDialog.ProgressDialog;
import id.example.galungapp.R;
import id.example.galungapp.Storage.SharedPMUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrasiSelanjutnyaFragment extends Fragment implements OnBackPressListener {

    private EditText et_email,et_password,et_konfirmasipassword;
    private Button btn_daftar;
    private String email,password,konfirmasipassword;
    private CheckBox checkBox;

    private InterfacePost mInterfacePost;

    private String nama,jeniskelamin,tempatlahir,tanggallahir,nomortlp,alamat,provinsi,kabupatenkota,kecamatan,kelurahan,rt,rw,role;

    private ProgressDialog dialog;

    public RegistrasiSelanjutnyaFragment(String nama, String jeniskelamin,String tempatlahir,String tanggallahir,String nomortlp,String alamat,String provinsi,String kabupatenkota,String kecamatan,String kelurahan,String rt,String rw,String role){
        this.nama = nama;
        this.jeniskelamin = jeniskelamin;
        this.tempatlahir = tempatlahir;
        this.tanggallahir = tanggallahir;
        this.nomortlp = nomortlp;
        this.alamat = alamat;
        this.provinsi = provinsi;
        this.kabupatenkota = kabupatenkota;
        this.kecamatan = kecamatan;
        this.kelurahan = kelurahan;
        this.rt = rt;
        this.rw = rw;
        this.role = role;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registerselanjutnya, container, false);
        et_email = (EditText) view.findViewById(R.id.et_email);
        et_password = (EditText) view.findViewById(R.id.et_password);
        et_konfirmasipassword = (EditText) view.findViewById(R.id.et_konfirmasipassword);
        checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        btn_daftar = (Button) view.findViewById(R.id.btn_daftar);

        mInterfacePost = (InterfacePost) Client.getClient().create(InterfacePost.class);

        dialog = new ProgressDialog(getContext(), "Please Wait..");

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox.isChecked()){
                    checkBox.setTextColor(R.color.colorbgedittextcoklat);
                }
            }
        });

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = et_email.getText().toString().trim();
                password = et_password.getText().toString().trim();
                konfirmasipassword = et_konfirmasipassword.getText().toString();

                if(email.isEmpty()){
                    et_email.setError("Email Masih Kosong");
                    et_email.requestFocus();
                    return;
                }else if(isEmailValid(email) == false){
                    et_email.setError("Format Email Salah");
                    et_email.requestFocus();
                    return;
                }else if(password.isEmpty()){
                    et_password.setError("Password Masih Kosong");
                    et_password.requestFocus();
                    return;
                }else if(konfirmasipassword.isEmpty()) {
                    et_konfirmasipassword.setError("Konfirmasi Password Masih Kosong");
                    et_konfirmasipassword.requestFocus();
                    return;
                }else if(!password.equals(konfirmasipassword)){
                    et_konfirmasipassword.setError("Konfirmasi Password Tidak Sama");
                    et_konfirmasipassword.requestFocus();
                    return;
                }else if(!checkBox.isChecked()){
                    checkBox.setTextColor(Color.RED);
                    return;
                }

                dialog.show();
                mInterfacePost.registrasi(nama,email,password,password,jeniskelamin,tempatlahir,tanggallahir,alamat,kecamatan,kabupatenkota,kelurahan,rt,rw,nomortlp,role).enqueue(new Callback<User_respon>() {
                    @Override
                    public void onResponse(Call<User_respon> call, Response<User_respon> response) {
                        if(response.isSuccessful()) {
                            User_respon user_respon = (User_respon) response.body();
                            if (user_respon.isStatus()) {
                                DataUserLogin mDataUserLogin = new DataUserLogin(user_respon.getToken());
                                SharedPMUser.getmInstance(getActivity()).saveUser(user_respon.getData(),mDataUserLogin);
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("statusLogin",1);
                                startActivity(intent);
                                dialog.dismiss();
                            } else {
                                SweetalertFailed(user_respon.getMessage());
                                dialog.dismiss();
                            }
                        }else{
                            SweetalertFailed( "Gagal terhubung keserver");
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<User_respon> call, Throwable t) {
                        SweetalertFailed("Gagal terhubung keserver");
                        dialog.dismiss();
                    }
                });
            }
        });

        return view;
    }

    @Override
    public boolean onBackPressed() {
        return new BackPress(this).onBackPressed();
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void  SweetalertFailed(String ContentText){
        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops")
                .setContentText(ContentText)
                .show();
    }

}
