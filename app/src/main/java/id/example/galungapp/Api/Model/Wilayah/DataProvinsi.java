package id.example.galungapp.Api.Model.Wilayah;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataProvinsi {

    @SerializedName("id")
    int id;
    @SerializedName("nama_provinsi")
    String nama_provinsi;
    @SerializedName("kabupaten")
    List<DataKabupatenKota> kabupaten;

    public DataProvinsi(int id, String nama_provinsi, List<DataKabupatenKota> kabupaten) {
        this.id = id;
        this.nama_provinsi = nama_provinsi;
        this.kabupaten = kabupaten;
    }

    public int getId() {
        return id;
    }

    public String getNama_provinsi() {
        return nama_provinsi;
    }

    public List<DataKabupatenKota> getKabupaten() {
        return kabupaten;
    }
}
