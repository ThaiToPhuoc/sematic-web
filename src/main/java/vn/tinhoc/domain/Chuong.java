package vn.tinhoc.domain;

import vn.lanhoang.ontology.annotation.Name;
import vn.lanhoang.ontology.annotation.OntologyObject;

import java.util.List;

@OntologyObject(uri = "Chuong")
public class Chuong {
    @Name
    private String id;

    private List<Tiet> GomTiet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Tiet> getGomTiet() {
        return GomTiet;
    }

    public void setGomTiet(List<Tiet> gomTiet) {
        GomTiet = gomTiet;
    }
}