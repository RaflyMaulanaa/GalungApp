package id.example.galungapp.Api.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataPesanan_respon {

    @SerializedName("status")
    boolean status;
    @SerializedName("message")
    String message;
    @SerializedName("data")
    List<DataPesanan> data;

    public DataPesanan_respon(boolean status, String message, List<DataPesanan> data) {
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

    public List<DataPesanan> getData() {
        return data;
    }
}
