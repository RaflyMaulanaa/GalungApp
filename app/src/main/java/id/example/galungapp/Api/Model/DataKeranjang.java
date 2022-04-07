package id.example.galungapp.Api.Model;

import com.google.gson.annotations.SerializedName;

public class DataKeranjang {

    @SerializedName("id_item")
    String id_item;
    @SerializedName("jumlah")
    String jumlah;
    @SerializedName("harga_satuan")
    String harga_satuan;
    @SerializedName("nama")
    String nama;
    @SerializedName("jenis")
    String jenis;
    @SerializedName("subtotal")
    String subtotal;
    @SerializedName("gambar")
    String gambar;
    @SerializedName("min_beli")
    String min_beli;
    @SerializedName("stok")
    String stok;

    public DataKeranjang(String id_item, String jumlah, String harga_satuan, String nama, String jenis, String subtotal, String gambar, String min_beli, String stok) {
        this.id_item = id_item;
        this.jumlah = jumlah;
        this.harga_satuan = harga_satuan;
        this.nama = nama;
        this.jenis = jenis;
        this.subtotal = subtotal;
        this.gambar = gambar;
        this.min_beli = min_beli;
        this.stok = stok;
    }

    public String getId_item() {
        return id_item;
    }

    public String getJumlah() {
        return jumlah;
    }

    public String getHarga_satuan() {
        return harga_satuan;
    }

    public String getNama() {
        return nama;
    }

    public String getJenis() {
        return jenis;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public String getGambar() {
        return gambar;
    }

    public String getMin_beli() {
        return min_beli;
    }

    public String getStok() {
        return stok;
    }
}
