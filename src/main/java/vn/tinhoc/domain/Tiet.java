package vn.tinhoc.domain;

import java.util.List;

import vn.lanhoang.ontology.annotation.Name;
import vn.lanhoang.ontology.annotation.OntologyObject;

@OntologyObject(uri = "Tiet")
public class Tiet {
    @Name
    private String id;

    private String NoiDungTiet;
    
    private String Link;
    
    private Integer STTTiet;

    private String TuKhoa;

    private Chuong ThuocChuong;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoiDungTiet() {
        return NoiDungTiet;
    }

    public void setNoiDungTiet(String noiDungTiet) {
        if (noiDungTiet != null) {
            NoiDungTiet = noiDungTiet.replace("\\\"", "'");
        } else {
            NoiDungTiet = null;
        }
    }
    
    public String getLink() {
		return Link;
	}

	public void setLink(String link) {
		Link = link;
	}

	public Integer getSTTTiet() {
		return STTTiet;
	}

	public void setSTTTiet(Integer sTTTiet) {
		STTTiet = sTTTiet;
	}
	
	public String getTuKhoa() {
        return TuKhoa;
    }

    public void setTuKhoa(String tuKhoa) {
        TuKhoa = tuKhoa;
    }

    public Chuong getThuocChuong() {
        return ThuocChuong;
    }

    public void setThuocChuong(Chuong thuocChuong) {
        ThuocChuong = thuocChuong;
    }
}