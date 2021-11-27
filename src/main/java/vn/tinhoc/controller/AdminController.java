package vn.tinhoc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.tinhoc.domain.BaiGiang;
import vn.tinhoc.domain.Chuong;
import vn.tinhoc.domain.request.KiemTraWrite;
import vn.tinhoc.repository.BaiGiangRepository;
import vn.tinhoc.repository.ChuongRepository;
import vn.tinhoc.service.AdminService;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    BaiGiangRepository baiGiangRepository;

    @Autowired
    ChuongRepository chuongRepository;

    @GetMapping("/bai-giang")
    public ResponseEntity<?> findAllBaiGiang() {
        return ResponseEntity.ok(adminService.findAllBaiGiang());
    }

    @PostMapping("/bai-giang")
    public ResponseEntity<?> createBaiGiang(@RequestBody BaiGiang baiGiang) {
        StringBuilder sb = new StringBuilder();
        sb.append("CTLop").append(baiGiang.getChuongTrinh());
        sb.append("_").append(baiGiang.getHocKy());

        String id = sb.toString();

        if(baiGiangRepository.exists(id)) {
            return new ResponseEntity<>(
                String.format("Bài giảng %s đã tồn tại!", id),
                HttpStatus.BAD_REQUEST);
        }

        baiGiang.setId(id);

        return new ResponseEntity<>(baiGiangRepository.save(baiGiang), HttpStatus.OK);
    }

    @GetMapping("/bai-giang/{id}/kiem-tra")
    public ResponseEntity<?> findKiemTraFromBaiGiang(@PathVariable String id) {
        return ResponseEntity.ok(adminService.findKiemTraByBaiGiang(id));
    }

    @PostMapping("/kiem-tra")
    public ResponseEntity<?> createKiemTra(@RequestBody KiemTraWrite kiemTraWrite) {

        return ResponseEntity.ok(adminService.create(kiemTraWrite));
    }

    @PutMapping("/kiem-tra")
    public ResponseEntity<?> updateKiemTra(@RequestBody KiemTraWrite kiemTraWrite) {

        return ResponseEntity.ok(adminService.update(kiemTraWrite));
    }

    @PostMapping("chuong")
    public ResponseEntity<?> createChuong(@RequestBody Chuong chuong) {
        HttpStatus errorStatus = adminService.getError(chuong);
        if (errorStatus != null) {
            return new ResponseEntity<>(errorStatus);
        } else {
            chuong = adminService.create(chuong);
            if (chuong == null) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(chuong, HttpStatus.OK);
    }

    @PutMapping("/chuong")
    public ResponseEntity<?> updateChuong(@RequestBody Chuong chuong) {
        if (!chuongRepository.exists(chuong.getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (chuong.getThuocBaiGiang() == null || !baiGiangRepository.exists(chuong.getThuocBaiGiang().getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            chuong = adminService.update(chuong);
            if (chuong == null) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(chuong, HttpStatus.OK);
    }
}
