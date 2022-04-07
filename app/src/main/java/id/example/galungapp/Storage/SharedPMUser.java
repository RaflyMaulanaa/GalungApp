package id.example.galungapp.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import id.example.galungapp.Api.Model.DataUser;
import id.example.galungapp.Api.Model.DataUserLogin;


public class SharedPMUser {
    private static final String SHARED_PREF_NAME = "shared_pref_user";
    private static SharedPMUser mInstance;
    private Context mContext;

    private SharedPMUser(Context mContext2) {
        this.mContext = mContext2;
    }

    public static synchronized SharedPMUser getmInstance(Context mContext2) {
        SharedPMUser sharedPMKuesioner;
        synchronized (SharedPMUser.class) {
            if (mInstance == null) {
                mInstance = new SharedPMUser(mContext2);
            }
            sharedPMKuesioner= mInstance;
        }
        return sharedPMKuesioner;
    }

    public void saveUser(DataUser dataUser, DataUserLogin dataUserLogin) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SHARED_PREF_NAME, 0).edit();
        editor.putInt("id", dataUser.getId());
        editor.putString("name", dataUser.getName());
        editor.putString("email", dataUser.getEmail());
        editor.putString("tempat_lahir", dataUser.getTempat_lahir());
        editor.putString("tanggal_lahir", dataUser.getTanggal_lahir());
        editor.putString("alamat_id", dataUser.getAlamat_id());
        editor.putString("alamat", dataUser.getAlamat());
        editor.putString("kecamatan", dataUser.getKecamatan());
        editor.putString("kelurahan", dataUser.getKelurahan());
        editor.putString("nohp", dataUser.getNohp());
        editor.putString("petani_verified", dataUser.getPetani_verified());
        editor.putString("jkel", dataUser.getJkel());
        editor.putString("rt", dataUser.getRt());
        editor.putString("rw", dataUser.getRw());
        editor.putString("role", dataUser.getRole());
        if(dataUserLogin != null) {
            editor.putString("token", dataUserLogin.getToken());
        }
        editor.apply();
    }

    public boolean isUserReady() {
        if (this.mContext.getSharedPreferences(SHARED_PREF_NAME, 0).getString("token", null) != null) {
            return true;
        }
        return false;
    }

    public DataUser getUser() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(SHARED_PREF_NAME, 0);
        DataUser dataUser = new DataUser(
                sharedPreferences.getInt("id", 0),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("tempat_lahir", null),
                sharedPreferences.getString("tanggal_lahir", null),
                sharedPreferences.getString("alamat_id", null),
                sharedPreferences.getString("alamat", null),
                sharedPreferences.getString("kecamatan", null),
                sharedPreferences.getString("kelurahan", null),
                sharedPreferences.getString("nohp", null),
                sharedPreferences.getString("petani_verified", null),
                sharedPreferences.getString("jkel", null),
                sharedPreferences.getString("rt", null),
                sharedPreferences.getString("rw", null),
                sharedPreferences.getString("role", null));
        return dataUser;
    }

    public DataUserLogin getTokenUser() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(SHARED_PREF_NAME, 0);
        DataUserLogin dataUserLogin = new DataUserLogin(
                sharedPreferences.getString("token", null));
        return dataUserLogin;
    }

    public void clear() {
        SharedPreferences.Editor sharedPreferences = this.mContext.getSharedPreferences(SHARED_PREF_NAME, 0).edit();
        sharedPreferences.clear();
        sharedPreferences.apply();
    }
}
