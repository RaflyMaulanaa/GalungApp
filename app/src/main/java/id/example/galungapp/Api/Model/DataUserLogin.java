package id.example.galungapp.Api.Model;

import com.google.gson.annotations.SerializedName;

public class DataUserLogin {

    @SerializedName("token")
    String token;

    public DataUserLogin(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
