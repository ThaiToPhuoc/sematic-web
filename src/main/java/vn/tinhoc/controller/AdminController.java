package vn.tinhoc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.tinhoc.domain.BaiGiang;
import vn.tinhoc.repository.BaiGiangRepository;
import vn.tinhoc.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    BaiGiangRepository baiGiangRepository;

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
}
