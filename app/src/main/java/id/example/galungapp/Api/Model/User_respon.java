package id.example.galungapp.Api.Model;

import com.google.gson.annotations.SerializedName;

public class User_respon {

    @SerializedName("status")
    boolean status;
    @SerializedName("message")
    String message;
    @SerializedName("data")
    DataUser data;
    @SerializedName("token")
    String token;

    public User_respon(boolean status, String message, DataUser data, String token) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.token = token;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public DataUser getData() {
        return data;
    }

    public String getToken() {
        return token;
    }
}
