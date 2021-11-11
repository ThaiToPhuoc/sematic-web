package vn.tinhoc.domain.dto;

import java.util.List;

import vn.lanhoang.ontology.annotation.Name;
import vn.lanhoang.ontology.annotation.OntologyObject;
import vn.tinhoc.domain.Chuong;
import vn.tinhoc.domain.Tiet;

@OntologyObject(uri = "Chuong")
public class ChuongDTO {
    @Name
    private Chuong chuong;

    private List<Tiet> tiets;
    
    public ChuongDTO(Chuong chuong, List<Tiet> tiets) {
		this.chuong = chuong;
		this.tiets = tiets;
	}

	public Chuong getChuong() {
		return chuong;
	}

	public void setChuong(Chuong chuong) {
		this.chuong = chuong;
	}

	public List<Tiet> getTiets() {
		return tiets;
	}

	public void setTiets(List<Tiet> tiets) {
		this.tiets = tiets;
	}
	
	
}