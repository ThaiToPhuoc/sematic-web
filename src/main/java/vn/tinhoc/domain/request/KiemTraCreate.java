package vn.tinhoc.domain.request;

import vn.tinhoc.domain.DapAn;
import vn.tinhoc.domain.KiemTra;

import java.util.List;

public class KiemTraCreate {
    KiemTra kiemTra;

    List<DapAn> dapAns;

    public KiemTra getKiemTra() {
        return kiemTra;
    }

    public void setKiemTra(KiemTra kiemTra) {
        this.kiemTra = kiemTra;
    }

    public List<DapAn> getDapAns() {
        return dapAns;
    }

    public void setDapAns(List<DapAn> dapAns) {
        this.dapAns = dapAns;
    }
}
