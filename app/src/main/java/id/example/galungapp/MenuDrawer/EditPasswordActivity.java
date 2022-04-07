package id.example.galungapp.MenuDrawer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sweetalert.SweetAlertDialog;

import java.text.ParseException;
import java.util.Date;

import id.example.galungapp.Api.Client;
import id.example.galungapp.Api.InterfacePost;
import id.example.galungapp.Api.Model.DataUserLogin;
import id.example.galungapp.Api.Model.Data_respon;
import id.example.galungapp.MainActivity;
import id.example.galungapp.MenuUtama.JualGabahActivity;
import id.example.galungapp.ProgresDialog.ProgressDialog;
import id.example.galungapp.R;
import id.example.galungapp.RegistrasiLogin.LoginRegisterActivity;
import id.example.galungapp.Storage.SharedPMUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPasswordActivity extends AppCompatActivity {

    private EditText et_passwordlama, et_passwordbaru, et_passwordbaru1;
    private Button btn_simpan;
    private ImageView iv_buttonback;

    private String passwordlama, passwordbaru, passwordbaru1;

    private InterfacePost interfacePost;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpassword);

        DataUserLogin dataUserLogin = SharedPMUser.getmInstance(this).getTokenUser();
        interfacePost = (InterfacePost) Client.getClientToken(dataUserLogin.getToken()).create(InterfacePost.class);
        dialog = new ProgressDialog(EditPasswordActivity.this, "Please Wait..");

        iv_buttonback = (ImageView) findViewById(R.id.iv_buttonback);
        et_passwordlama = (EditText) findViewById(R.id.et_passwordlama);
        et_passwordbaru = (EditText) findViewById(R.id.et_passwordbaru);
        et_passwordbaru1 = (EditText) findViewById(R.id.et_passwordbaru1);
        btn_simpan = (Button) findViewById(R.id.btn_simpan);

        iv_buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordlama = et_passwordlama.getText().toString();
                passwordbaru = et_passwordbaru.getText().toString();
                passwordbaru1 = et_passwordbaru1.getText().toString();

                if(passwordlama.isEmpty()) {
                    et_passwordlama.setError("Password Lama Masih Kosong");
                    et_passwordlama.requestFocus();
                    return;
                }else if(passwordbaru.isEmpty()) {
                    et_passwordbaru.setError("Password Baru Masih Kosong");
                    et_passwordbaru.requestFocus();
                    return;
                }else if(passwordbaru1.isEmpty()){
                    et_passwordbaru1.setError("Ulang Password Baru Masih Kosong");
                    et_passwordbaru1.requestFocus();
                    return;
                }else if(!passwordbaru.equals(passwordbaru1)){
                    et_passwordbaru1.setError("Password Baru Tidak Sama");
                    et_passwordbaru1.requestFocus();
                    return;
                }


                dialog.show();
                interfacePost.EditUser(null,null,null,null,null,null,null,null,null,null,null,passwordlama,passwordbaru,passwordbaru1).enqueue(new Callback<Data_respon>() {
                    @Override
                    public void onResponse(Call<Data_respon> call, Response<Data_respon> response) {
                        if(response.isSuccessful()){
                            Data_respon data_respon = (Data_respon) response.body();
                            if(data_respon.isStatus()){
                                Intent intent = new Intent(EditPasswordActivity.this, AkunActivity.class);
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
    }

    private void notValidAksesToken(){
        if(SharedPMUser.getmInstance(EditPasswordActivity.this).isUserReady()) {
            SharedPMUser.getmInstance(EditPasswordActivity.this).clear();
        }
        Intent intent = new Intent(EditPasswordActivity.this, MainActivity.class);
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
