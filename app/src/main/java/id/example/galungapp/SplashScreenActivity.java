package id.example.galungapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.sweetalert.SweetAlertDialog;

import id.example.galungapp.Api.Client;
import id.example.galungapp.Api.InterfaceGet;
import id.example.galungapp.Api.Model.DataUserLogin;
import id.example.galungapp.Api.Model.User_respon;
import id.example.galungapp.MenuUtama.JualGabahActivity;
import id.example.galungapp.Storage.SharedPMUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {

    private Handler handler;
    private InterfaceGet mInterfaceGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        cekStatusLogin();

    }

    private void cekStatusLogin(){
        if(SharedPMUser.getmInstance(SplashScreenActivity.this).isUserReady()){
            DataUserLogin dataUserLogin = SharedPMUser.getmInstance(this).getTokenUser();
            mInterfaceGet = (InterfaceGet) Client.getClientToken(dataUserLogin.getToken()).create(InterfaceGet.class);
            mInterfaceGet.getUser().enqueue(new Callback<User_respon>() {
                @Override
                public void onResponse(Call<User_respon> call, Response<User_respon> response) {
                    if(response.isSuccessful()) {
                        User_respon mUser_respon = (User_respon) response.body();
                        if (mUser_respon.isStatus()) {
                            SharedPMUser.getmInstance(SplashScreenActivity.this).saveUser(mUser_respon.getData(),null);
                            getAcitivty(1);
                        } else {
                            getAcitivty(2);
                        }
                    }else{
                        if(SharedPMUser.getmInstance(SplashScreenActivity.this).isUserReady()) {
                            SharedPMUser.getmInstance(SplashScreenActivity.this).clear();
                        }
                        getAcitivty(0);
                    }
                }

                @Override
                public void onFailure(Call<User_respon> call, Throwable t) {
                    getAcitivty(2);
                }
            });
        }else{
            getAcitivty(0);
        }
    }

    private void getAcitivty(final int statusLogin){
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(statusLogin == 2){
                    new SweetAlertDialog(SplashScreenActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops")
                            .setContentText("Gagal terhubung keserver")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent intent = new Intent(SplashScreenActivity.this, SplashScreenActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }else{
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("statusLogin", statusLogin);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
}
