package vn.tinhoc.domain.dto;

import java.util.List;

import vn.lanhoang.ontology.annotation.Name;
import vn.lanhoang.ontology.annotation.OntologyObject;
import vn.tinhoc.domain.CauHoi;
import vn.tinhoc.domain.Tiet;

@OntologyObject(uri = "Tiet")
public class TietDTO {
    @Name
    private Tiet tiet;

    private List<CauHoi> cauHois;

	public TietDTO(Tiet tiet, List<CauHoi> cauHois) {
		super();
		this.tiet = tiet;
		this.cauHois = cauHois;
	}

	public Tiet getTiet() {
		return tiet;
	}

	public void setTiet(Tiet tiet) {
		this.tiet = tiet;
	}

	public List<CauHoi> getCauHois() {
		return cauHois;
	}

	public void setCauHois(List<CauHoi> cauHois) {
		this.cauHois = cauHois;
	}
    
    
}