package com.mapfre.mifel.vida.model.ul;

public class FieldRisk {
    private String fieldCode;
    private String fieldValue;

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public void convertDate(FieldRisk field){
        if(field.getFieldCode().equals("FEC_NACIMIENTO")){
            String newVal_campo= field.getFieldValue().replace("/", "");
            field.setFieldValue(newVal_campo);
        }
    }
}
