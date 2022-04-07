package id.example.galungapp.Api.Model;

import com.google.gson.annotations.SerializedName;

public class Data_respon {

    @SerializedName("status")
    boolean status;
    @SerializedName("message")
    String message;

    public Data_respon(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
