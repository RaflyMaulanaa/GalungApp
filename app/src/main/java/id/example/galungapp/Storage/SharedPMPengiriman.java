package id.example.galungapp.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import id.example.galungapp.Api.Model.DataPengiriman;
import id.example.galungapp.Api.Model.DataUser;
import id.example.galungapp.Api.Model.DataUserLogin;


public class SharedPMPengiriman {
    private static final String SHARED_PREF_NAME = "shared_pref_pengiriman";
    private static SharedPMPengiriman mInstance;
    private Context mContext;

    private SharedPMPengiriman(Context mContext2) {
        this.mContext = mContext2;
    }

    public static synchronized SharedPMPengiriman getmInstance(Context mContext2) {
        SharedPMPengiriman sharedPMKuesioner;
        synchronized (SharedPMPengiriman.class) {
            if (mInstance == null) {
                mInstance = new SharedPMPengiriman(mContext2);
            }
            sharedPMKuesioner= mInstance;
        }
        return sharedPMKuesioner;
    }

    public void saveAlamat(DataPengiriman dataPengiriman) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SHARED_PREF_NAME, 0).edit();
        editor.putBoolean("data_available", dataPengiriman.getData_available());
        editor.putString("nama", dataPengiriman.getNama_penerima());
        editor.putString("nomortlp", dataPengiriman.getNohp());
        editor.putString("alamat", dataPengiriman.getAlamat());
        editor.putString("kabupaten_kota", dataPengiriman.getKabupatenkota());
        editor.putString("kecamatan", dataPengiriman.getKecamatan());
        editor.putString("kelurahan", dataPengiriman.getKelurahan());
        editor.putString("rt", dataPengiriman.getRt());
        editor.putString("rw", dataPengiriman.getRw());
        editor.putString("keterangan", dataPengiriman.getKeterangan());
        editor.apply();
    }

    public boolean isDataReady() {
        if (this.mContext.getSharedPreferences(SHARED_PREF_NAME, 0).getBoolean("data_available", false) != false) {
            return true;
        }
        return false;
    }

    public DataPengiriman getDataPengiriman() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(SHARED_PREF_NAME, 0);
        DataPengiriman dataPengiriman = new DataPengiriman(
                sharedPreferences.getBoolean("data_available", false),
                sharedPreferences.getString("nama", null),
                sharedPreferences.getString("nomortlp", null),
                sharedPreferences.getString("alamat", null),
                sharedPreferences.getString("kabupaten_kota", null),
                sharedPreferences.getString("kecamatan", null),
                sharedPreferences.getString("kelurahan", null),
                sharedPreferences.getString("rt", null),
                sharedPreferences.getString("rw", null),
                sharedPreferences.getString("keterangan", null));
        return dataPengiriman;
    }

    public void clear() {
        SharedPreferences.Editor sharedPreferences = this.mContext.getSharedPreferences(SHARED_PREF_NAME, 0).edit();
        sharedPreferences.clear();
        sharedPreferences.apply();
    }
}
