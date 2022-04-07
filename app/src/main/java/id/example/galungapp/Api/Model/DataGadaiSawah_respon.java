package id.example.galungapp.Api.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataGadaiSawah_respon {

    @SerializedName("status")
    boolean status;
    @SerializedName("message")
    String message;
    @SerializedName("data")
    List<DataGadaiSawah> data;

    public DataGadaiSawah_respon(boolean status, String message, List<DataGadaiSawah> data) {
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

    public List<DataGadaiSawah> getData() {
        return data;
    }
}
