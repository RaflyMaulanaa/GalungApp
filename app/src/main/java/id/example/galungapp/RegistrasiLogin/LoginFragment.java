package id.example.galungapp.RegistrasiLogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import id.example.galungapp.Storage.SharedPMPengiriman;
import id.example.galungapp.Storage.SharedPMUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private EditText et_email,et_password;
    private Button btn_login;

    private String email,password;

    private InterfacePost mInterfacePost;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        et_email = (EditText) view.findViewById(R.id.et_email);
        et_password = (EditText) view.findViewById(R.id.et_password);
        btn_login = (Button) view.findViewById(R.id.btn_masuk);

        mInterfacePost = (InterfacePost) Client.getClient().create(InterfacePost.class);
        dialog = new ProgressDialog(getContext(), "Please Wait..");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = et_email.getText().toString().trim();
                password = et_password.getText().toString().trim();
                if(email.isEmpty()) {
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
                }

                dialog.show();
                mInterfacePost.login(email,password).enqueue(new Callback<User_respon>() {
                    @Override
                    public void onResponse(Call<User_respon> call, Response<User_respon> response) {
                        if(response.isSuccessful()) {
                            User_respon user_respon = (User_respon) response.body();
                            if (user_respon.isStatus()) {
                                if(SharedPMPengiriman.getmInstance(getContext()).isDataReady()) {
                                    SharedPMPengiriman.getmInstance(getContext()).clear();
                                }
                                DataUserLogin mDataUserLogin = new DataUserLogin(user_respon.getToken());
                                SharedPMUser.getmInstance(getActivity()).saveUser(user_respon.getData(),mDataUserLogin);
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("statusLogin", 1);
                                startActivity(intent);
                                dialog.dismiss();
                            } else {
                                SweetalertFailed(user_respon.getMessage());
                                dialog.dismiss();
                            }
                        }else{
                            SweetalertFailed("Gagal terhubung keserver");
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
