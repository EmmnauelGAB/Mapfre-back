package com.mapfre.mifel.vida.model.ul;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.Data;

@Data
public class ContractorData {
    private Optional<String> documCode = Optional.empty();//cod_docum
    private Optional<String> curp = Optional.empty();//curp
    private Optional<String> contractingFistName = Optional.empty();//nom_tercero
    private Optional<String> contractingMiddleName = Optional.empty();//ape1_tercero
    private Optional<String> contractingLastName = Optional.empty();//ape2_tercero
    private Optional<String> addressName1 = Optional.empty();//nom_domicilio1
    private Optional<String> addressName2 = Optional.empty();//nom_domicilio2
    private Optional<String> addressName3 = Optional.empty();//nom_domicilio3
    private Optional<String> postalCode = Optional.empty();//cod_postal
    private Optional<String> stateCode = Optional.empty();//cod_estado
    private Optional<String> provinceCode = Optional.empty();//cod_prov
    private Optional<String> countryCode = Optional.empty();//cod_pais
    private Optional<String> telephoneNumber = Optional.empty();//tlf_numero
    private String dateBirth;//fec_nacimiento
    private Optional<String> realAge = Optional.empty();//edad_real
    private String gender;//mca_sexo
    private Optional<String> maritalStatusCode = Optional.empty();//cod_est_civil
    private Optional<String> nationalityCode = Optional.empty();//cod_nacionalidad
    private String personType = "S";//mca_fisico
    private String email;//email
    private String beneficiaryType = "2";//tip_benef
    private Optional<String> professionCode = Optional.empty();//cod_profesion
    private Optional<String> occupationCode = Optional.empty();//cod_ocupacion
    private Optional<String> insuredObservations = Optional.empty();//obs_asegurado
    private Optional<String> corporateRegime = Optional.empty();//Regimen societario (catalogo)
    private Optional<String> taxZipCode = Optional.empty();//Codigo postal fiscal
    private Optional<String> taxSystemCode = Optional.empty();//Codigo de regimen fiscal (catalogo)

   
    private String[] getAllValues(ContractorData dto){
        String[] values= {
                dto.getDocumCode().orElse(""),
                dto.getCurp().orElse(""), 
                dto.getContractingFistName().orElse(""), 
                dto.getContractingMiddleName().orElse(""),
                dto.getContractingLastName().orElse(""),
                dto.getAddressName1().orElse(""), 
               	dto.getAddressName2().orElse(""),
                dto.getAddressName3().orElse(""), 
                dto.getPostalCode().orElse(""), 
                dto.getStateCode().orElse(""), 
                dto.getProvinceCode().orElse(""), 
                dto.getCountryCode().orElse(""), 
                dto.getTelephoneNumber().orElse(""), 
                dto.getDateBirth(),
                dto.getRealAge().orElse(""), 
                dto.getGender(), 
                dto.getMaritalStatusCode().orElse(""), 
                dto.getNationalityCode().orElse(""), 
                dto.getPersonType(), 
                dto.getEmail(), 
                dto.getBeneficiaryType(),
                dto.getProfessionCode().orElse(""), 
                dto.getOccupationCode().orElse(""), 
                dto.getInsuredObservations().orElse(""),
                dto.getCorporateRegime().orElse(""), 
                dto.getTaxZipCode().orElse(""), 
                dto.getTaxSystemCode().orElse("")
        };
        return values;
    }

    private String[] getColumnNames() {
        //List<Field> parameters= Arrays.asList(getClass().getDeclaredFields());
        Field[] fs = getClass().getDeclaredFields();
        List<String> ll= new ArrayList<>();
        String columnValue="";
        for (Field f : fs) {
            switch (f.getName()) {
                case "documCode":
                    columnValue = "COD_DOCUM";
                    break;
                case "curp":
                    columnValue = "CURP";
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
                case "realAge":
                    columnValue = "EDAD_REAL";
                    break;
                case "gender":
                    columnValue = "MCA_SEXO";
                    break;
                case "maritalStatusCode":
                    columnValue = "COD_EST_CIVIL";
                    break;
                case "nationalityCode":
                    columnValue = "COD_NACIONALIDAD";
                    break;
                case "personType":
                    columnValue = "MCA_FISICO";
                    break;
                case "email":
                    columnValue = "EMAIL";
                    break;
                case "beneficiaryType":
                    columnValue = "TIP_BENEF";
                    break;
                case "professionCode":
                    columnValue = "COD_PROFESION";
                    break;
                case "occupationCode":
                    columnValue = "COD_OCUPACION";
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

    public Map<String, String> dameLista(ContractorData listValue){
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
