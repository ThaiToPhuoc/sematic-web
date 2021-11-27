package vn.tinhoc.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.lanhoang.ontology.configuration.OntologyVariables;
import vn.tinhoc.domain.BaiGiang;
import vn.tinhoc.domain.CauHoi;
import vn.tinhoc.domain.Chuong;
import vn.tinhoc.domain.DapAn;
import vn.tinhoc.domain.KiemTra;
import vn.tinhoc.domain.dto.CauHoiDTO;
import vn.tinhoc.domain.request.KiemTraWrite;
import vn.tinhoc.repository.BaiGiangRepository;
import vn.tinhoc.repository.CauHoiRepository;
import vn.tinhoc.repository.ChuongRepository;
import vn.tinhoc.repository.DapAnRepository;
import vn.tinhoc.repository.KiemTraRepository;
import vn.tinhoc.repository.TietRepository;
import vn.tinhoc.utils.DataUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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

    public List<KiemTraWrite> findKiemTraByBaiGiang(String id) {
        Optional<BaiGiang> bgOp = baiGiangRepository.findByUriTag(id);
        if (bgOp.isEmpty()) {
            throw new IllegalArgumentException("BaiGiang id '" + id + "' not found!");
        }
        List<KiemTra> kiemTras = kiemTraRepository
                .query("SELECT ?subject " +
                        "WHERE { " +
                        "?subject rdf:type thoc:KiemTra ." +
                        "?subject thoc:ThuocBaiGiang thoc:" + id +
                        " }");

        return kiemTras.stream()
                .map(this::mapCauHoiDTOs)
                .collect(Collectors.toList());
    }

    public KiemTraWrite findKiemTraById(String id) {
        Optional<KiemTra> optional = kiemTraRepository.findByUriTag(id);
        return mapCauHoiDTOs(optional.orElse(null));
    }

    private KiemTraWrite mapCauHoiDTOs(KiemTra kiemTra) {
        String dapAnQuery = "SELECT ?s " +
                "WHERE { " +
                "   ?s rdf:type thoc:DapAn ." +
                "   ?s thoc:ThuocCauHoi <%s>" +
                " }";

        final KiemTraWrite kiemTraWrite = new KiemTraWrite();
        List<CauHoiDTO> cauHoiDTOS = new ArrayList<>();
        kiemTraWrite.setKiemTra(kiemTra);

        if (!CollectionUtils.isEmpty(kiemTra.getGomCauHoi())) {
            kiemTra.getGomCauHoi().forEach(cauHoi -> {
                cauHoiDTOS.add(new CauHoiDTO(
                    cauHoi, dapAnRepository.query(String.format(dapAnQuery, cauHoi.getId()))
                ));
            });
        }

        kiemTraWrite.setCauHoi(cauHoiDTOS);
        return kiemTraWrite;
    }

    public KiemTraWrite create(KiemTraWrite kiemTraWrite) throws NullPointerException {
        BaiGiang bg = kiemTraWrite.getKiemTra().getThuocBaiGiang();
        if (bg == null) {
            throw new NullPointerException("BaiGiang ID không hợp lệ");
        }

        String bgId = bg.getId();
        String suffix = bgId.substring(bgId.indexOf("Lop"));
        AtomicReference<String> ktra = new AtomicReference<>("");
        List<KiemTraWrite> kiemTraWrites = findKiemTraByBaiGiang(DataUtils.removeSharp(bgId));

        if (!CollectionUtils.isEmpty(kiemTraWrites)){
            Integer[] numbers = kiemTraWrites
                .stream()
                .map(write -> {
                    KiemTra kiemTra = write.getKiemTra();
                    if (StringUtils.isNotBlank(kiemTra.getId())) {
                        String id = kiemTra.getId();
                        int indexKTC = id.indexOf("KTC");
                        int indexUnderscore = id.indexOf("_");

                        id = id.substring(indexKTC + "KTC".length(), indexUnderscore);
                        return Integer.parseInt(id);
                    }

                    return 0;
                }).toArray(Integer[]::new);

            ktra.set("KTC" + DataUtils.smallestMissingNumber(numbers) + "_" + suffix);
        } else {
            ktra.set("KTC1_" + suffix);
        }

        kiemTraWrite.getKiemTra().setId(vars.getBaseUri() + ktra.get());

        KiemTraWrite write = write(kiemTraWrite);
        save(write);
        return write;
    }

    public KiemTraWrite update(KiemTraWrite kiemTraWrite) throws NullPointerException, IllegalArgumentException {
        BaiGiang bg = kiemTraWrite.getKiemTra().getThuocBaiGiang();
        if (bg == null) {
            throw new NullPointerException("BaiGiang ID không hợp lệ");
        }

        Optional<KiemTra> kiemTraOp = kiemTraRepository.findByUriTag(kiemTraWrite.getKiemTra().getId());

        if (kiemTraOp.isEmpty()) {
            throw new IllegalArgumentException("ID " + kiemTraWrite.getKiemTra().getId() + "không hợp lệ/tồn tại");
        }

        KiemTra kiemTra = kiemTraOp.get();
        KiemTraWrite kiemTraExist = findKiemTraById(kiemTra.getId());
        KiemTraWrite write = write(kiemTraWrite);

        if (!CollectionUtils.isEmpty(kiemTraExist.getCauHoi())) {
            List<CauHoiDTO> cauHoiCanXoa = new ArrayList<>();
            List<DapAn> dapAnCanXoa = new ArrayList<>();
            kiemTraExist.getCauHoi().forEach(cauHoiDTOExist -> {
                Optional<CauHoiDTO> chOp = write.getCauHoi().stream()
                    .filter(chdto -> {
                        String existId = DataUtils.removeSharp(cauHoiDTOExist.getCauHoi().getId());
                        String requestID = DataUtils.removeSharp(chdto.getCauHoi().getId());

                        return existId.equalsIgnoreCase(requestID);
                    }).findFirst();

                if (chOp.isEmpty()) {
                    cauHoiCanXoa.add(cauHoiDTOExist);
                } else {
                    CauHoiDTO cauHoiDTO = chOp.get();
                    cauHoiDTOExist.getDapAns().forEach(dapAnExist -> {
                        String existId = DataUtils.removeSharp(dapAnExist.getId());
                        boolean noneMatch = cauHoiDTO.getDapAns()
                                .stream()
                                .noneMatch(dapAn -> {
                            String requestID = DataUtils.removeSharp(dapAn.getId());

                            return existId.equalsIgnoreCase(requestID);
                        });

                        if (noneMatch) {
                            dapAnCanXoa.add(dapAnExist);
                        }
                    });
                }
            });

            if (!CollectionUtils.isEmpty(cauHoiCanXoa)) {
                cauHoiCanXoa.forEach(cauHoiDTO -> delete(kiemTra, cauHoiDTO));
            }

            if (!CollectionUtils.isEmpty(dapAnCanXoa)) {
                delete(dapAnCanXoa);
            }
        }
        save(write);
        return write;
    }

    public KiemTraWrite write(KiemTraWrite kiemTraWrite) {
        final String ktraId = DataUtils.removeSharp(kiemTraWrite.getKiemTra().getId());

        kiemTraWrite.getCauHoi().forEach(cauHoiDTO -> {

            final AtomicInteger newIndex = new AtomicInteger(-1);
            String chId = "Cau" + cauHoiDTO.getCauHoi().getSTTCauHoi() + "_" + ktraId;
            cauHoiDTO.getCauHoi().setId(vars.getBaseUri() + chId);

            Integer[] numbers = cauHoiDTO.getDapAns()
                .stream()
                .filter(dapAn -> StringUtils.isNotBlank(dapAn.getId()))
                .map(dapAn -> {
                    if (StringUtils.isNotBlank(dapAn.getId())) {
                        String dapAnId = dapAn.getId();
                        int indexDAn = dapAnId.indexOf("DapAn");
                        int indexUnderscore = dapAnId.indexOf("_");

                        dapAnId = dapAnId.substring(indexDAn + "DapAn".length(), indexUnderscore);
                        return Integer.parseInt(dapAnId);
                    }

                    return 0;
                }).toArray(Integer[]::new);

            cauHoiDTO.getDapAns().forEach(dapAn -> {
                if (StringUtils.isBlank(dapAn.getId())) {
                    if (newIndex.get() == -1) {
                        newIndex.set(DataUtils.smallestMissingNumber(numbers));
                    } else {
                        newIndex.incrementAndGet();
                    }

                    dapAn.setId(vars.getBaseUri() + "DapAn" + newIndex.get() + "_" + chId);
                    dapAn.setThuocCauHoi(cauHoiDTO.getCauHoi());
                }
            });

        });
        return kiemTraWrite;
    }

    public void save(KiemTraWrite kiemTraWrite) {
        kiemTraWrite.getCauHoi().forEach(cauHoiDTO -> {
            cauHoiRepository.save(cauHoiDTO.getCauHoi());
            cauHoiDTO.getDapAns().forEach(dapAnRepository::save);
        });

        kiemTraWrite.getKiemTra().setGomCauHoi(
                kiemTraWrite.getCauHoi().stream()
                        .map(CauHoiDTO::getCauHoi)
                        .collect(Collectors.toList())
        );
        kiemTraRepository.save(kiemTraWrite.getKiemTra());
    }

    public void delete(KiemTra ktra, CauHoiDTO cauHoiDTO) {
        ktra.getGomCauHoi().removeIf(cauHoi -> {
            String thisId = DataUtils.removeSharp(cauHoi.getId());
            String thatId = DataUtils.removeSharp(cauHoiDTO.getCauHoi().getId());

            return thisId.equalsIgnoreCase(thatId);
        });
        cauHoiDTO.getDapAns().forEach(dapAnRepository::remove);
        cauHoiRepository.remove(cauHoiDTO.getCauHoi());
        kiemTraRepository.save(ktra);
    }

    public void delete(List<DapAn> dapAns) {
        dapAns.forEach(dapAnRepository::remove);
    }
}
