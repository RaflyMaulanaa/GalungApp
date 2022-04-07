package id.example.galungapp.Api.Model;

import com.google.gson.annotations.SerializedName;

public class DataGadaiSawah {

    @SerializedName("id")
    int id;
    @SerializedName("periode")
    String periode;
    @SerializedName("harga")
    String harga;
    @SerializedName("keterangan")
    String keterangan;
    @SerializedName("created_at")
    String created_at;
    @SerializedName("updated_at")
    String updated_at;
    @SerializedName("nama_sawah")
    String nama_sawah;
    @SerializedName("alamat")
    String alamat;
    @SerializedName("titik_koordinat")
    String titik_koordinat;

    public DataGadaiSawah(int id, String periode, String harga, String keterangan, String created_at, String updated_at, String nama_sawah, String alamat, String titik_koordinat) {
        this.id = id;
        this.periode = periode;
        this.harga = harga;
        this.keterangan = keterangan;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.nama_sawah = nama_sawah;
        this.alamat = alamat;
        this.titik_koordinat = titik_koordinat;
    }

    public int getId() {
        return id;
    }

    public String getPeriode() {
        return periode;
    }

    public String getHarga() {
        return harga;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getNama_sawah() {
        return nama_sawah;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getTitik_koordinat() {
        return titik_koordinat;
    }
}