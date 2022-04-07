package id.example.galungapp.Api.Model;

import com.google.gson.annotations.SerializedName;

public class DataItemPesanan {

    @SerializedName("id")
    String id;
    @SerializedName("nama")
    String nama;
    @SerializedName("jenis")
    String jenis;
    @SerializedName("harga")
    String harga;
    @SerializedName("jumlah")
    String jumlah;
    @SerializedName("subtotal")
    String subtotal;
    @SerializedName("transaksi_id")
    String transaksi_id;

    public DataItemPesanan(String id, String nama, String jenis, String harga, String jumlah, String subtotal, String transaksi_id) {
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
        this.harga = harga;
        this.jumlah = jumlah;
        this.subtotal = subtotal;
        this.transaksi_id = transaksi_id;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getJenis() {
        return jenis;
    }

    public String getHarga() {
        return harga;
    }

    public String getJumlah() {
        return jumlah;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public String getTransaksi_id() {
        return transaksi_id;
    }
}
