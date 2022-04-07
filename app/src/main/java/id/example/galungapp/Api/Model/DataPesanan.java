package id.example.galungapp.Api.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataPesanan {

    @SerializedName("id")
    String id;
    @SerializedName("transaksi_code")
    String transaksi_code;
    @SerializedName("penerima")
    String penerima;
    @SerializedName("nohp")
    String nohp;
    @SerializedName("alamat")
    String alamat;
    @SerializedName("kecamatan")
    String kecamatan;
    @SerializedName("kelurahan")
    String kelurahan;
    @SerializedName("rt")
    String rt;
    @SerializedName("rw")
    String rw;
    @SerializedName("total")
    String total;
    @SerializedName("items")
    List<DataItemPesanan> items;

    public DataPesanan(String id, String transaksi_code, String penerima, String nohp, String alamat, String kecamatan, String kelurahan, String rt, String rw, String total, List<DataItemPesanan> items) {
        this.id = id;
        this.transaksi_code = transaksi_code;
        this.penerima = penerima;
        this.nohp = nohp;
        this.alamat = alamat;
        this.kecamatan = kecamatan;
        this.kelurahan = kelurahan;
        this.rt = rt;
        this.rw = rw;
        this.total = total;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public String getTransaksi_code() {
        return transaksi_code;
    }

    public String getPenerima() {
        return penerima;
    }

    public String getNohp() {
        return nohp;
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

    public String getRt() {
        return rt;
    }

    public String getRw() {
        return rw;
    }

    public String getTotal() {
        return total;
    }

    public List<DataItemPesanan> getItems() {
        return items;
    }
}
