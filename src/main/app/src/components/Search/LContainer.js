export interface LLabel {
    type: String;
    name: String;
    value: String;
}

export interface LContainer {
    type: String;
    label: String;
    labels: LLabel[]
}