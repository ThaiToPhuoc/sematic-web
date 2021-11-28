package vn.tinhoc.domain;

import vn.lanhoang.ontology.annotation.Name;
import vn.lanhoang.ontology.annotation.OntologyObject;
import vn.tinhoc.domain.dto.User;

import java.util.List;

@OntologyObject(uri = "KQKiemTra")
public class KQKiemTra {
    @Name
    private String id;

    private User CuaHocSinh;

    private KiemTra CuaBaiKiemTra;

    private List<DapAn> GomDapAn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getCuaHocSinh() {
        return CuaHocSinh;
    }

    public void setCuaHocSinh(User cuaHocSinh) {
        CuaHocSinh = cuaHocSinh;
    }

    public KiemTra getCuaBaiKiemTra() {
        return CuaBaiKiemTra;
    }

    public void setCuaBaiKiemTra(KiemTra cuaBaiKiemTra) {
        CuaBaiKiemTra = cuaBaiKiemTra;
    }

    public List<DapAn> getGomDapAn() {
        return GomDapAn;
    }

    public void setGomDapAn(List<DapAn> gomDapAn) {
        GomDapAn = gomDapAn;
    }
}
