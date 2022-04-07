package id.example.galungapp.Api.Model;

import com.google.gson.annotations.SerializedName;

public class DataSawah {

    @SerializedName("id")
    int id;
    @SerializedName("nama")
    String nama;
    @SerializedName("titik_koordinat")
    String titik_koordinat;
    @SerializedName("alamat_id")
    String alamat_id;
    @SerializedName("kecamatan")
    String kecamatan;
    @SerializedName("kelurahan")
    String kelurahan;
    @SerializedName("alamat")
    String alamat;
    @SerializedName("luas_sawah")
    String luas_sawah;

    public DataSawah(int id, String nama, String titik_koordinat, String alamat_id, String kecamatan, String kelurahan, String alamat, String luas_sawah) {
        this.id = id;
        this.nama = nama;
        this.titik_koordinat = titik_koordinat;
        this.alamat_id = alamat_id;
        this.kecamatan = kecamatan;
        this.kelurahan = kelurahan;
        this.alamat = alamat;
        this.luas_sawah = luas_sawah;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getTitik_koordinat() {
        return titik_koordinat;
    }

    public String getAlamat_id() {
        return alamat_id;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getLuas_sawah() {
        return luas_sawah;
    }
}
