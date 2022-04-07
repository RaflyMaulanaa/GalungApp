package id.example.galungapp.Api.Model;

import com.google.gson.annotations.SerializedName;

public class DataHargaGabah {

    @SerializedName("id")
    int id;
    @SerializedName("nama")
    String nama;
    @SerializedName("harga")
    String harga;

    public DataHargaGabah(int id, String nama, String harga) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getHarga() {
        return harga;
    }
}
