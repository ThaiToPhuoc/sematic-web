package vn.tinhoc.domain.request;

import vn.tinhoc.domain.KiemTra;
import vn.tinhoc.domain.dto.CauHoiDTO;

import java.util.List;

public class KiemTraWrite {
    KiemTra kiemTra;

    List<CauHoiDTO> cauHoi;

    public KiemTra getKiemTra() {
        return kiemTra;
    }

    public void setKiemTra(KiemTra kiemTra) {
        this.kiemTra = kiemTra;
    }

    public List<CauHoiDTO> getCauHoi() {
        return cauHoi;
    }

    public void setCauHoi(List<CauHoiDTO> cauHoi) {
        this.cauHoi = cauHoi;
    }
}
