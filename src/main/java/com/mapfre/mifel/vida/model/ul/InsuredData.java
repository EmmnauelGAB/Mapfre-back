package com.mapfre.mifel.vida.model.ul;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InsuredData {
    /**
     *          "COD_DOCUM": "RARH790421QJ0" ,
     *         "NOM_TERCERO":"HECTOR" ,
     *         "APE1_TERCERO":"RAMIREZ" ,
     *         "APE2_TERCERO":"RAMIREZ" ,
     *         "NOM_DOMICILIO1":"JAZMINEZ 13" ,
     *         "NOM_DOMICILIO2":"13" ,
     *         "NOM_DOMICILIO3":"CALIFORNIA" ,
     *         "COD_POSTAL":"23154" ,
     *         "COD_ESTADO":"14" ,
     *         "COD_PROV":"14007" ,
     *         "COD_PAIS":"MEX" ,
     *         "TLF_NUMERO":"58711248" ,
     *         "FEC_NACIMIENTO":"21/04/1979" ,
     *         "MCA_SEXO":"1"  ,
     *         "COD_EST_CIVIL":"S" ,
     *         "MCA_FISICO":"S" ,
     *         "COD_OCUPACION":"39" ,
     *         "TIP_BENEF":"2" ,
     *         "OBS_ASEGURADO":"1.75,60,25"
     */
    private String documCode;//cod_docum
    private String contractingFistName;//nom_tercero
    private String contractingMiddleName;//ape1_tercero
    private String contractingLastName;//ape2_tercero
    private String addressName1;//nom_domicilio1
    private String addressName2;//nom_domicilio2
    private String addressName3;//nom_domicilio3
    private String postalCode;//cod_postal
    private String stateCode;//cod_estado
    private String provinceCode;//cod_prov
    private String countryCode;//cod_pais
    private String telephoneNumber;//tlf_numero
    private String dateBirth;//fec_nacimiento
    private String gender;//mca_sexo
    private String maritalStatusCode;//cod_est_civil
    private String personType;//mca_fisico
    private String occupationCode;//cod_ocupacion
    private String beneficiaryType;//tip_benef
    private String insuredObservations;//obs_asegurado

    public String getDocumCode() {
        return documCode;
    }

    public void setDocumCode(String documCode) {
        this.documCode = documCode;
    }

    public String getContractingFistName() {
        return contractingFistName;
    }

    public void setContractingFistName(String contractingFistName) {
        this.contractingFistName = contractingFistName;
    }

    public String getContractingMiddleName() {
        return contractingMiddleName;
    }

    public void setContractingMiddleName(String contractingMiddleName) {
        this.contractingMiddleName = contractingMiddleName;
    }

    public String getContractingLastName() {
        return contractingLastName;
    }

    public void setContractingLastName(String contractingLastName) {
        this.contractingLastName = contractingLastName;
    }

    public String getAddressName1() {
        return addressName1;
    }

    public void setAddressName1(String addressName1) {
        this.addressName1 = addressName1;
    }

    public String getAddressName2() {
        return addressName2;
    }

    public void setAddressName2(String addressName2) {
        this.addressName2 = addressName2;
    }

    public String getAddressName3() {
        return addressName3;
    }

    public void setAddressName3(String addressName3) {
        this.addressName3 = addressName3;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatusCode() {
        return maritalStatusCode;
    }

    public void setMaritalStatusCode(String maritalStatusCode) {
        this.maritalStatusCode = maritalStatusCode;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getOccupationCode() {
        return occupationCode;
    }

    public void setOccupationCode(String occupationCode) {
        this.occupationCode = occupationCode;
    }

    public String getBeneficiaryType() {
        return beneficiaryType;
    }

    public void setBeneficiaryType(String beneficiaryType) {
        this.beneficiaryType = beneficiaryType;
    }

    public String getInsuredObservations() {
        return insuredObservations;
    }

    public void setInsuredObservations(String insuredObservations) {
        this.insuredObservations = insuredObservations;
    }

    private String[] getAllValues(InsuredData dto){
        String[] values= {
                dto.getDocumCode(), dto.getContractingFistName(), dto.getContractingMiddleName(), dto.getContractingLastName(),dto.getAddressName1(), dto.getAddressName2(),
                dto.getAddressName3(), dto.getPostalCode(), dto.getStateCode(), dto.getProvinceCode(), dto.getCountryCode(), dto.getTelephoneNumber(), dto.getDateBirth(),
                dto.getGender(), dto.getMaritalStatusCode(), dto.getPersonType(), dto.getOccupationCode(), dto.getBeneficiaryType(),
                dto.getInsuredObservations()
        };
        return values;
    }

    private String[] getColumnNames(){
        Field[] fs = getClass().getDeclaredFields();
        List<String> ll= new ArrayList<>();
        String columnValue="";
        for (Field f : fs) {
            switch (f.getName()) {
                case "documCode":
                    columnValue = "COD_DOCUM";
                    break;
                case "contractingFistName":
                    columnValue = "NOM_TERCERO";
                    break;
                case "contractingMiddleName":
                    columnValue = "APE1_TERCERO";
                    break;
                case "contractingLastName":
                    columnValue = "APE2_TERCERO";
                    break;
                case "addressName1":
                    columnValue = "NOM_DOMICILIO1";
                    break;
                case "addressName2":
                    columnValue = "NOM_DOMICILIO2";
                    break;
                case "addressName3":
                    columnValue = "NOM_DOMICILIO3";
                    break;
                case "postalCode":
                    columnValue = "COD_POSTAL";
                    break;
                case "stateCode":
                    columnValue = "COD_ESTADO";
                    break;
                case "provinceCode":
                    columnValue = "COD_PROV";
                    break;
                case "countryCode":
                    columnValue = "COD_PAIS";
                    break;
                case "telephoneNumber":
                    columnValue = "TLF_NUMERO";
                    break;
                case "dateBirth":
                    columnValue = "FEC_NACIMIENTO";
                    break;
                case "gender":
                    columnValue = "MCA_SEXO";
                    break;
                case "maritalStatusCode":
                    columnValue = "COD_EST_CIVIL";
                    break;
                case "personType":
                    columnValue = "MCA_FISICO";
                    break;
                case "occupationCode":
                    columnValue = "COD_OCUPACION";
                    break;
                case "beneficiaryType":
                    columnValue = "TIP_BENEF";
                    break;
                case "insuredObservations":
                    columnValue = "OBS_ASEGURADO";
                    break;
                default:
                    columnValue = "INVALID_VALUE";
                    break;
            }
            //System.out.println("field:"+f.getName());
            ll.add(columnValue);
        }
        String[] names= new String[ll.size()];
        names=ll.toArray(names);
        return names;
    }

    public Map<String, String> getList(InsuredData listValue){
        String[] names= listValue.getColumnNames();
        String[] values= listValue.getAllValues(listValue);

        Map<String, String> myMap = new LinkedHashMap<>();
        for (int i = 0; i < names.length; i++) {
            for (String value : values) {
                String aux= values[i] !=null ? values[i]: "";
                if(aux.length() != 0){
                    myMap.put(names[i], values[i]);
                }
            }
        }
        return myMap;
    }
}
