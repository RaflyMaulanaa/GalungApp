package id.example.galungapp.Api.Model;

import com.google.gson.annotations.SerializedName;

public class DataJual {

    @SerializedName("id")
    int id;
    @SerializedName("nama")
    String nama;
    @SerializedName("harga")
    String harga;
    @SerializedName("min_beli")
    String min_beli;
    @SerializedName("stok")
    String stok;
    @SerializedName("keterangan")
    String keterangan;
    @SerializedName("gambar")
    String gambar;

    public DataJual(int id, String nama, String harga, String min_beli, String stok, String keterangan, String gambar) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.min_beli = min_beli;
        this.stok = stok;
        this.keterangan = keterangan;
        this.gambar = gambar;
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

    public String getMin_beli() {
        return min_beli;
    }

    public String getStok() {
        return stok;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getGambar() {
        return gambar;
    }
}
