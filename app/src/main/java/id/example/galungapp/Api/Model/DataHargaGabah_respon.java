package id.example.galungapp.Api.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import id.example.galungapp.Api.Model.DataSawah;

public class DataHargaGabah_respon {

    @SerializedName("status")
    boolean status;
    @SerializedName("message")
    String message;
    @SerializedName("data")
    List<DataHargaGabah> data;

    public DataHargaGabah_respon(boolean status, String message, List<DataHargaGabah> data) {
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

    public List<DataHargaGabah> getData() {
        return data;
    }
}
