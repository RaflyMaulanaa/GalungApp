package id.example.galungapp.Api.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataKeranjang_respon {

    @SerializedName("status")
    boolean status;
    @SerializedName("message")
    String message;
    @SerializedName("id_transaski")
    int id_transaski;
    @SerializedName("total")
    String total;
    @SerializedName("data")
    List<DataKeranjang> data;

    public DataKeranjang_respon(boolean status, String message, int id_transaski, String total, List<DataKeranjang> data) {
        this.status = status;
        this.message = message;
        this.id_transaski = id_transaski;
        this.total = total;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getId_transaski() {
        return id_transaski;
    }

    public String getTotal() {
        return total;
    }

    public List<DataKeranjang> getData() {
        return data;
    }
}
