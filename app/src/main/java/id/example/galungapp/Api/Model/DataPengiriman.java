package id.example.galungapp.Api.Model;

public class DataPengiriman {

    private Boolean data_available;
    private String nama_penerima;
    private String nohp;
    private String alamat;
    private String kabupatenkota;
    private String kecamatan;
    private String kelurahan;
    private String rt;
    private String rw;
    private String keterangan;

    public DataPengiriman(Boolean data_available, String nama_penerima, String nohp, String alamat, String kabupatenkota, String kecamatan, String kelurahan, String rt, String rw, String keterangan) {
        this.data_available = data_available;
        this.nama_penerima = nama_penerima;
        this.nohp = nohp;
        this.alamat = alamat;
        this.kabupatenkota = kabupatenkota;
        this.kecamatan = kecamatan;
        this.kelurahan = kelurahan;
        this.rt = rt;
        this.rw = rw;
        this.keterangan = keterangan;
    }

    public Boolean getData_available() {
        return data_available;
    }

    public String getNama_penerima() {
        return nama_penerima;
    }

    public String getNohp() {
        return nohp;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getKabupatenkota() {
        return kabupatenkota;
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

    public String getKeterangan() {
        return keterangan;
    }
}
