package id.example.galungapp.Api.Model.Wilayah;

import com.google.gson.annotations.SerializedName;

public class DataKabupatenKota {

    @SerializedName("id")
    int id;
    @SerializedName("nama_kota")
    String nama_kota;

    public DataKabupatenKota(int id, String nama_kota) {
        this.id = id;
        this.nama_kota = nama_kota;
    }

    public int getId() {
        return id;
    }

    public String getNama_kota() {
        return nama_kota;
    }
}
