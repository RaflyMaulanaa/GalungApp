package id.example.galungapp.Api.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataJual_respon {

    @SerializedName("status")
    boolean status;
    @SerializedName("message")
    String message;
    @SerializedName("data")
    List<DataJual> data;

    public DataJual_respon(boolean status, String message, List<DataJual> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<DataJual> getData() {
        return data;
    }
}
