package vn.tinhoc.utils.model;

import java.util.List;

public class LContainer {
    private String type;
    private String label;
    private List<LLabel> labels;

    public LContainer(String type, String label, List<LLabel> labels) {
        this.type = type;
        this.label = label;
        this.labels = labels;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<LLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<LLabel> labels) {
        this.labels = labels;
    }

    public static class LLabel {
        private String type;
        private String name;
        private String value;

        public LLabel(String type, String name, String value) {
            this.type = type;
            this.name = name;
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
