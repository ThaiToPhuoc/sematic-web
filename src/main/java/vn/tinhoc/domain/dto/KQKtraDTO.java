package vn.tinhoc.domain.dto;

import vn.tinhoc.domain.KQKiemTra;

import java.util.List;

public class KQKtraDTO {
    KQKiemTra kqKiemTra;
    List<KetQuaDTO> ketQuaDTOS;
    List<CauHoiDTO> cauHoiDTOS;

    public KQKiemTra getKqKiemTra() {
        return kqKiemTra;
    }

    public void setKqKiemTra(KQKiemTra kqKiemTra) {
        this.kqKiemTra = kqKiemTra;
    }

    public List<KetQuaDTO> getKetQuaDTOS() {
        return ketQuaDTOS;
    }

    public void setKetQuaDTOS(List<KetQuaDTO> ketQuaDTOS) {
        this.ketQuaDTOS = ketQuaDTOS;
    }

    public List<CauHoiDTO> getCauHoiDTOS() {
        return cauHoiDTOS;
    }

    public void setCauHoiDTOS(List<CauHoiDTO> cauHoiDTOS) {
        this.cauHoiDTOS = cauHoiDTOS;
    }
}
