package id.example.galungapp.Api.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataGabah {

    @SerializedName("id")
    int id;
    @SerializedName("jumlah")
    String jumlah;
    @SerializedName("harga")
    String harga;
    @SerializedName("alamat")
    String alamat;
    @SerializedName("kecamatan")
    String kecamatan;
    @SerializedName("kelurahan")
    String kelurahan;
    @SerializedName("keterangan")
    String keterangan;
    @SerializedName("waktu_jemput")
    String waktu_jemput;
    @SerializedName("nama_gabah")
    String nama_gabah;
    @SerializedName("created_at")
    String created_at;
    @SerializedName("updated_at")
    String updated_at;

    public DataGabah(int id, String jumlah, String harga, String alamat, String kecamatan, String kelurahan, String keterangan, String waktu_jemput, String nama_gabah, String created_at, String updated_at) {
        this.id = id;
        this.jumlah = jumlah;
        this.harga = harga;
        this.alamat = alamat;
        this.kecamatan = kecamatan;
        this.kelurahan = kelurahan;
        this.keterangan = keterangan;
        this.waktu_jemput = waktu_jemput;
        this.nama_gabah = nama_gabah;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public String getJumlah() {
        return jumlah;
    }

    public String getHarga() {
        return harga;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getWaktu_jemput() {
        return waktu_jemput;
    }

    public String getNama_gabah() {
        return nama_gabah;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
