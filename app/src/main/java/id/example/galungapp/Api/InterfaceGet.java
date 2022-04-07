package id.example.galungapp.Api;

import id.example.galungapp.Api.Model.DataJual_respon;
import id.example.galungapp.Api.Model.DataGabah_respon;
import id.example.galungapp.Api.Model.DataGadaiSawah_respon;
import id.example.galungapp.Api.Model.DataHargaGabah_respon;
import id.example.galungapp.Api.Model.DataModalTanam_respon;
import id.example.galungapp.Api.Model.DataPesanan_respon;
import id.example.galungapp.Api.Model.DataSawah_respon;
import id.example.galungapp.Api.Model.DataKeranjang_respon;
import id.example.galungapp.Api.Model.User_respon;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface InterfaceGet {

    @GET("user")
    Call<User_respon> getUser();

    @GET("sawah")
    Call<DataSawah_respon> getSawah();

    //Menu Gabah
    @GET("gabah-all")
    Call<DataHargaGabah_respon> getHargaGabah();

    @GET("riwayat-transaksi-gabah")
    Call<DataGabah_respon> getGabahTerjual();

    @GET("transaksi-gabah")
    Call<DataGabah_respon> getGabahDijual();

    @GET("batal-transaksi-gabah")
    Call<DataGabah_respon> getGabahDibatalkan();


    //Menu Gadai Sawah
    @GET("list-sedang-gadai-sawah")
    Call<DataGadaiSawah_respon> getSawahTergadai();

    @GET("list-daftar-gadai-sawah")
    Call<DataGadaiSawah_respon> getSawahDigadai();

    @GET("list-batal-gadai-sawah")
    Call<DataGadaiSawah_respon> getGadaiSawahDibatalkan();

    @GET("list-riwayat-gadai-sawah")
    Call<DataGadaiSawah_respon> getGadaiSawahSelesai();


    //Menu Modal Tanam
    @GET("list-sedang-gadai-modal-tanam")
    Call<DataModalTanam_respon> getDiModali();

    @GET("list-daftar-modal-tanam")
    Call<DataModalTanam_respon> getModalTanam();

    @GET("list-batal-modal-tanam")
    Call<DataModalTanam_respon> getModalTanamBatal();

    @GET("list-riwayat-modal-tanam")
    Call<DataModalTanam_respon> getModalTanamSelesai();

    @GET("beras")
    Call<DataJual_respon> getDataBeras(@Query("page") int str);

    @GET("bibit-pupuk")
    Call<DataJual_respon> getBibitPupuk(@Query("page") int str);

    @GET("alat")
    Call<DataJual_respon> getAlat(@Query("page") int str);

    @GET("cart")
    Call<DataKeranjang_respon> getKeranjang();

    @GET("proses-belanja")
    Call<DataPesanan_respon> getPesananProses();

    @GET("riwayat-belanja")
    Call<DataPesanan_respon> getPesananSelesai();

    @GET("batal-belanja")
    Call<DataPesanan_respon> getPesananDibatalkan();
}
