package id.example.galungapp.Api.Model;

import com.google.gson.annotations.SerializedName;

public class DataModalTanam {

    @SerializedName("id")
    int id;
    @SerializedName("periode_tanam")
    String periode_tanam;
    @SerializedName("jenis_pupuk")
    String jenis_pupuk;
    @SerializedName("jenis_bibit")
    String jenis_bibit;
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

    public DataModalTanam(int id, String periode_tanam, String jenis_pupuk, String jenis_bibit, String keterangan, String created_at, String updated_at, String nama_sawah, String alamat, String titik_koordinat) {
        this.id = id;
        this.periode_tanam = periode_tanam;
        this.jenis_pupuk = jenis_pupuk;
        this.jenis_bibit = jenis_bibit;
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

    public String getPeriode_tanam() {
        return periode_tanam;
    }

    public String getJenis_pupuk() {
        return jenis_pupuk;
    }

    public String getJenis_bibit() {
        return jenis_bibit;
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
