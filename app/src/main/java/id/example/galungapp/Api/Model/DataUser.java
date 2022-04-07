package id.example.galungapp.Api.Model;

import com.google.gson.annotations.SerializedName;

public class DataUser {

    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("email")
    String email;
    @SerializedName("tempat_lahir")
    String tempat_lahir;
    @SerializedName("tanggal_lahir")
    String tanggal_lahir;
    @SerializedName("alamat_id")
    String alamat_id;
    @SerializedName("alamat")
    String alamat;
    @SerializedName("kecamatan")
    String kecamatan;
    @SerializedName("kelurahan")
    String kelurahan;
    @SerializedName("nohp")
    String nohp;
    @SerializedName("petani_verified")
    String petani_verified;
    @SerializedName("jkel")
    String jkel;
    @SerializedName("rt")
    String rt;
    @SerializedName("rw")
    String rw;
    @SerializedName("role")
    String role;

    public DataUser(int id, String name, String email, String tempat_lahir, String tanggal_lahir, String alamat_id, String alamat, String kecamatan, String kelurahan, String nohp, String petani_verified, String jkel, String rt, String rw, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.tempat_lahir = tempat_lahir;
        this.tanggal_lahir = tanggal_lahir;
        this.alamat_id = alamat_id;
        this.alamat = alamat;
        this.kecamatan = kecamatan;
        this.kelurahan = kelurahan;
        this.nohp = nohp;
        this.petani_verified = petani_verified;
        this.jkel = jkel;
        this.rt = rt;
        this.rw = rw;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTempat_lahir() {
        return tempat_lahir;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public String getAlamat_id() {
        return alamat_id;
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

    public String getNohp() {
        return nohp;
    }

    public String getPetani_verified() {
        return petani_verified;
    }

    public String getJkel() {
        return jkel;
    }

    public String getRt() {
        return rt;
    }

    public String getRw() {
        return rw;
    }

    public String getRole() {
        return role;
    }
}
