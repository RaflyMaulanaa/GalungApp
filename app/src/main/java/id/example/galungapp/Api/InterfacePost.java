package id.example.galungapp.Api;

import id.example.galungapp.Api.Model.Data_respon;
import id.example.galungapp.Api.Model.User_respon;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InterfacePost {
    @FormUrlEncoded
    @POST("login")
    Call<User_respon> login(@Field("email") String str,
                            @Field("password") String str1);

    @FormUrlEncoded
    @POST("register")
    Call<User_respon> registrasi(@Field("name") String str,
                                 @Field("email") String str1,
                                 @Field("password") String str2,
                                 @Field("password_confirmation") String str3,
                                 @Field("jkel") String str4,
                                 @Field("tempat_lahir") String str5,
                                 @Field("tanggal_lahir") String str6,
                                 @Field("alamat_lengkap") String str7,
                                 @Field("kecamatan") String str8,
                                 @Field("kota_id") String str9,
                                 @Field("kelurahan") String str10,
                                 @Field("rt") String str11,
                                 @Field("rw") String str12,
                                 @Field("nohp") String str13,
                                 @Field("role") String str14);


    @FormUrlEncoded
    @POST("sawah")
    Call<Data_respon> sawah(@Field("luas_sawah") String str,
                            @Field("titik_koordinat") String str1,
                            @Field("alamat_id") String str2,
                            @Field("kelurahan") String str3,
                            @Field("kecamatan") String str4,
                            @Field("alamat_lengkap") String str5,
                            @Field("nama") String str6);

    @FormUrlEncoded
    @POST("edit-sawah/{id_sawah}")
    Call<Data_respon> Inputsawah(@Path("id_sawah") String str,
                                 @Field("luas_sawah") String str1,
                                 @Field("titik_koordinat") String str2,
                                 @Field("alamat_id") String str3,
                                 @Field("kelurahan") String str4,
                                 @Field("kecamatan") String str5,
                                 @Field("alamat_lengkap") String str6,
                                 @Field("nama") String str7);


    @FormUrlEncoded
    @POST("edit-user")
    Call<Data_respon>   EditUser(@Field("name") String str,
                                 @Field("jkel") String str1,
                                 @Field("tempat_lahir") String str2,
                                 @Field("tanggal_lahir") String str3,
                                 @Field("alamat_lengkap") String str4,
                                 @Field("kecamatan") String str5,
                                 @Field("kota_id") String str6,
                                 @Field("kelurahan") String str7,
                                 @Field("rt") String str8,
                                 @Field("rw") String str9,
                                 @Field("nohp") String str10,
                                 @Field("password_lama") String str11,
                                 @Field("password") String str12,
                                 @Field("password_confirmation") String str13);

    @FormUrlEncoded
    @POST("gabah-store/{id_gabah}")
    Call<Data_respon> JualGabah(@Path("id_gabah") String str,
                                 @Field("jumlah") String str1,
                                 @Field("alamat_lengkap") String str2,
                                 @Field("kecamatan") String str3,
                                 @Field("kelurahan") String str4,
                                 @Field("waktu_jemput") String str5,
                                 @Field("keterangan") String str6);

    @FormUrlEncoded
    @POST("gadai-sawah")
    Call<Data_respon> GadaiSawah(@Field("sawah_id") String str,
                                @Field("harga") String str1,
                                @Field("periode") String str2);


    @FormUrlEncoded
    @POST("modal-tanam")
    Call<Data_respon> ModalTanam(@Field("periode_tanam") String str,
                                 @Field("jenis_pupuk") String str1,
                                 @Field("jenis_bibit") String str2,
                                 @Field("sawah_id") String str3);


    @POST("delete-sawah/{sawah_id}")
    Call<Data_respon> DeteleSawah(@Path("sawah_id") String str);

    @FormUrlEncoded
    @POST("add-cart/{id_barang}")
    Call<Data_respon> TambahKeranjang(@Path("id_barang") String str,
                                      @Field("jumlah") String str1);


    @FormUrlEncoded
    @POST("checkout/{id_keranjang}")
    Call<Data_respon> Checkout(@Path("id_keranjang") String str,
                               @Field("nama_penerima") String str1,
                               @Field("nohp") String str2,
                               @Field("alamat_id") String str3,
                               @Field("alamat") String str4,
                               @Field("kecamatan") String str5,
                               @Field("kelurahan") String str6,
                               @Field("rt") String str7,
                               @Field("rw") String str8,
                               @Field("keterangan") String str9);


    @POST("delete-cart/{id_barang}")
    Call<Data_respon> HapusKeranjang(@Path("id_barang") String str);
}
