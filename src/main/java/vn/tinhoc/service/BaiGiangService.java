package vn.tinhoc.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.tinhoc.config.StreamProperty;
import vn.tinhoc.domain.BaiGiang;
import vn.tinhoc.domain.CauHoi;
import vn.tinhoc.domain.Chuong;
import vn.tinhoc.domain.DapAn;
import vn.tinhoc.domain.KQKiemTra;
import vn.tinhoc.domain.KiemTra;
import vn.tinhoc.domain.Tiet;
import vn.tinhoc.repository.BaiGiangRepository;
import vn.tinhoc.repository.CauHoiRepository;
import vn.tinhoc.repository.ChuongRepository;
import vn.tinhoc.repository.DapAnRepository;
import vn.tinhoc.repository.KQKiemTraRepository;
import vn.tinhoc.repository.KiemTraRepository;
import vn.tinhoc.repository.TietRepository;

import java.nio.file.Path;
import java.util.Optional;

@Service
public class BaiGiangService {
    @Autowired
    BaiGiangRepository baiGiangRepository;

    @Autowired
    ChuongRepository chuongRepository;

    @Autowired
    TietRepository tietRepository;

    @Autowired
    KiemTraRepository kiemTraRepository;

    @Autowired
    CauHoiRepository cauHoiRepository;

    @Autowired
    DapAnRepository dapAnRepository;

    @Autowired
    KQKiemTraRepository kqKiemTraRepository;

    @Autowired
    StreamProperty streamProperty;

    public void delete(BaiGiang baiGiang) {
        baiGiang = existOrThow(baiGiangRepository.findByUriTag(baiGiang.getId()));
        if (!CollectionUtils.isEmpty(baiGiang.getGomChuong())) {
            baiGiang.getGomChuong().forEach(this::delete);
        }
        kiemTraRepository.query(
                "SELECT ?s WHERE { " +
                        " ?s thoc:ThuocBaiGiang " +
                        " <" + baiGiang.getId() + "> " +
                        " }"
        ).forEach(this::delete);
        baiGiangRepository.remove(baiGiang.getId());
    }

    public void delete(Chuong chuong) {
        chuong = existOrThow(chuongRepository.findByUriTag(chuong.getId()));
        if (StringUtils.isNotEmpty(chuong.getVideo())) {
            FileUtils.deleteQuietly(Path.of(streamProperty.getMediaPath()).resolve(chuong.getVideo()).toFile());
        }
        if (!CollectionUtils.isEmpty(chuong.getGomTiet())) {
            chuong.getGomTiet().forEach(this::delete);
        }
        Chuong finalChuong = chuong;
        chuong.getThuocBaiGiang().getGomChuong().removeIf(c -> finalChuong.getId().equals(c.getId()));
        baiGiangRepository.save(chuong.getThuocBaiGiang());
        chuongRepository.remove(chuong.getId());
    }

    public void delete(Tiet tiet) {
        tiet = existOrThow(tietRepository.findByUriTag(tiet.getId()));
        tietRepository.remove(tiet.getId());
    }

    public void delete(KiemTra kiemTra) {
        kiemTra = existOrThow(kiemTraRepository.findByUriTag(kiemTra.getId()));
        kqKiemTraRepository.query(
                "SELECT ?s WHERE { " +
                        " ?s thoc:CuaBaiKiemTra " +
                        " <" + kiemTra.getId() + "> " +
                        " }"
        ).forEach(this::delete);
        if (!CollectionUtils.isEmpty(kiemTra.getGomCauHoi())) {
            kiemTra.getGomCauHoi().forEach(this::delete);
        }
        kiemTraRepository.remove(kiemTra.getId());
    }

    public void delete(CauHoi cauHoi) {
        cauHoi = existOrThow(cauHoiRepository.findByUriTag(cauHoi.getId()));
        dapAnRepository.query(
            "SELECT ?s WHERE { " +
                    " ?s thoc:ThuocCauHoi " +
                    " <" + cauHoi.getId() + "> " +
                    " }"
        ).forEach(this::delete);
        cauHoiRepository.remove(cauHoi.getId());
    }

    public void delete(DapAn dapAn) {
        dapAn = existOrThow(dapAnRepository.findByUriTag(dapAn.getId()));
        dapAnRepository.remove(dapAn.getId());
    }

    public void delete(KQKiemTra kqKiemTra) {
        kqKiemTraRepository.remove(kqKiemTra.getId());
    }

    private <T> T existOrThow(Optional<T> op) {
        return op.orElseThrow(IllegalArgumentException::new);
    }
}
