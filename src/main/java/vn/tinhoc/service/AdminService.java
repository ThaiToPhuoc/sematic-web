package vn.tinhoc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.lanhoang.ontology.configuration.OntologyVariables;
import vn.tinhoc.domain.BaiGiang;
import vn.tinhoc.domain.Chuong;
import vn.tinhoc.domain.KiemTra;
import vn.tinhoc.domain.request.KiemTraCreate;
import vn.tinhoc.repository.BaiGiangRepository;
import vn.tinhoc.repository.CauHoiRepository;
import vn.tinhoc.repository.ChuongRepository;
import vn.tinhoc.repository.DapAnRepository;
import vn.tinhoc.repository.KiemTraRepository;
import vn.tinhoc.repository.TietRepository;

import java.util.List;

@Service
public class AdminService {
    Logger log = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    CauHoiRepository cauHoiRepository;

    @Autowired
    KiemTraRepository kiemTraRepository;

    @Autowired
    DapAnRepository dapAnRepository;

    @Autowired
    BaiGiangRepository baiGiangRepository;

    @Autowired
    ChuongRepository chuongRepository;

    @Autowired
    TietRepository tietRepository;

    @Autowired
    OntologyVariables vars;

//    public KiemTraCreate createKiemTra(KiemTraCreate kiemTraCreate) {
//        KiemTra kiemTra = kiemTraCreate.getKiemTra();
//        String idKiemTra = "KT_" + kiemTra.getThuocBaiGiang().getId();
//
//        kiemTra.getGomCauHoi().forEach(cauHoi -> {-
//        });
//    }

    public List<BaiGiang> findAllBaiGiang() {
        return baiGiangRepository.find();
    }

    public HttpStatus getError(Chuong chuong) {
        if (chuong.getThuocBaiGiang() == null || !baiGiangRepository.exists(chuong.getThuocBaiGiang().getId())) {
            return HttpStatus.BAD_REQUEST;
        }

        return null;
    }

    private void updateTiet(Chuong chuong, String shortChuongId, Chuong chuongTemp) {
        chuong.getGomTiet().forEach(tiet -> {
            if (StringUtils.isBlank(tiet.getId())) {
                String tietId = vars.getBaseUri() + "Tiet" + tiet.getSTTTiet() + "_C" + shortChuongId;
                tiet.setId(tietId);
            }

            if (tiet.getThuocChuong() == null) {
                tiet.setThuocChuong(chuongTemp);
            }

            tietRepository.save(tiet);
        });
    }

    public Chuong create(Chuong chuong) {
        BaiGiang bg = baiGiangRepository.findByUriTag(chuong.getThuocBaiGiang().getId()).orElse(null);

        if (bg == null) {
            log.warn("BaiGiang NullPointer, BG id: {}", chuong.getThuocBaiGiang().getId());
            return null;
        }

        String shortChuongId = chuong.getSTTChuong() + "_Lop" + bg.getChuongTrinh() + "_" + bg.getHocKy();
        String chuongId = vars.getBaseUri() + "Chuong" + shortChuongId;

        if (chuongRepository.exists(chuongId)) {
            throw new IllegalArgumentException("Chuong id '" + chuongId + "' duplicate");
        }

        Chuong chuongTemp = new Chuong();
        chuongTemp.setId(chuongId);
        chuong.setId(chuongId);
        bg.getGomChuong().add(chuong);
        chuong.setThuocBaiGiang(bg);

        if (!CollectionUtils.isEmpty(chuong.getGomTiet())) {
            updateTiet(chuong, shortChuongId, chuongTemp);
        }
        baiGiangRepository.save(bg);
        chuong = chuongRepository.save(chuong);

        return chuong;
    }

    public Chuong update(Chuong chuong) {
        String chuongId = chuong.getId().substring(chuong.getId().indexOf("Chuong") + "Chuong".length());
        Chuong chuongExist = chuongRepository.findByUriTag(chuong.getId()).orElse(null);
        Chuong chuongTemp = new Chuong();
        chuongTemp.setId(chuong.getId());

        BaiGiang bg = baiGiangRepository.findByUriTag(chuong.getThuocBaiGiang().getId()).orElse(null);

        if (bg != null && chuongExist != null) {
            if (bg.getGomChuong().stream().noneMatch(c -> c.getId().equals(chuong.getId()))) {
                bg.getGomChuong().add(chuong);
                baiGiangRepository.save(bg);
            }
        } else {
            log.warn("BaiGiang NullPointer, BG id: {}", chuong.getThuocBaiGiang().getId());
            return null;
        }

        updateTiet(chuong, chuongId, chuongTemp);

        if (!CollectionUtils.isEmpty(chuongExist.getGomTiet())) {
            chuongExist.getGomTiet().forEach(tietExist -> {
                if (chuong.getGomTiet().stream().noneMatch(tiet -> tiet.getId().equals(tietExist.getId()))) {
                    tietRepository.remove(tietExist.getId());
                }
            });
        }

        return chuongRepository.save(chuong);
    }
}
