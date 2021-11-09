package vn.tinhoc.domain.dto;

import vn.lanhoang.ontology.annotation.Name;
import vn.lanhoang.ontology.annotation.OntologyObject;

@OntologyObject(uri = "Tiet")
public class TietDTO {
    @Name
    private String id;

    private String ThuocChuong;
}
