package vn.tinhoc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.tinhoc.domain.BaiGiang;
import vn.tinhoc.domain.KiemTra;
import vn.tinhoc.domain.request.KiemTraCreate;
import vn.tinhoc.repository.BaiGiangRepository;
import vn.tinhoc.repository.CauHoiRepository;
import vn.tinhoc.repository.DapAnRepository;
import vn.tinhoc.repository.KiemTraRepository;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    CauHoiRepository cauHoiRepository;

    @Autowired
    KiemTraRepository kiemTraRepository;

    @Autowired
    DapAnRepository dapAnRepository;

    @Autowired
    BaiGiangRepository baiGiangRepository;

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
}
