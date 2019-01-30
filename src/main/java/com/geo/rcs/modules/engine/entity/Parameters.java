package com.geo.rcs.modules.engine.entity;


import com.alibaba.fastjson.annotation.JSONField;

public class Parameters {


    private String cid;            // "13306328903" ;

    private String idNumber;       // "370404196212262212" ;

    private String realName;       // "赵玉柏" ;

    private String cid2;           // "13800138000"

    private String workLongitude;

    private String workLatitude;

    private String liveLongitude;

    private String liveLatitude;

    private String addressCode;

    private String cardNo;

    private String company;

    private String IP;

    private String cycle;      // "30"

    /**入参参数*/
    private String inputHomeAddress;
    private String inputRegisteredAddress;
    private String inputContactAddress;
    private String inputApplyProvince;
    private String inputApplyCity;
    private String inputMarriage;
    private String inputDiploma;
    private String inputLoanAmount;
    private String inputLoanPurpose;
    private String inputLoanTerm;
    private String inputLoanDate;
    private String inputOrganizationAddress;
    private String inputWorkPhone;
    private String inputCompanyType;
    private String inputApplyerType;
    private String inputIndustry;
    private String inputCareer;
    private String inputOccupation;
    private String inputWorkTime;
    private String inputEntryTime;
    private String inputMonthlyIncome;
    private String inputAnnualIncome;
    private String inputOtherIncome;
    private String inputFamilyMobile;
    private String inputWorkmateMobile;
    private String inputOthercontactMobile;
    private String inputHouseProperty;
    private String inputHouseLocation;
    private String inputHousePurchasingtime;
    private String inputHouseCondition;
    private String inputHouseAddress;
    private String inputHouseType;
    private String inputHouseArea;
    private String inputHouseEquity;
    private String inputCarProperty;
    private String inputCarProductiontime;
    private String inputRegisterDate;
    private String inputPledgeProperty;
    private String inputUsedCar;
    private String inputAllopatricCar;
    private String inputTransferTime;
    private String inputGraduateSchool;
    private String inputOrganization;
    private String inputFamilyName;
    private String inputFamilyRelation;
    private String inputWorkmateName;
    private String inputWorkmateRelation;
    private String inputOthercontactName;
    private String inputOthercontactRelation;
    private String inputHouseNumber;
    private String inputCarType;
    private String inputCarLicensenumber;
    private String inputCarVinnumber;
    private String inputManufacturerName;
    private String inputCarColour;
    private String inputBuyPrice;

    /**
     * 数据平台-MacAddr
     * */
    @JSONField(name = "MacAddr")
    private String MacAddr;
    @JSONField(name = "CustAcc")
    private String CustAcc;
    @JSONField(name = "PayeeAcc")
    private String PayeeAcc;
    @JSONField(name = "PayerAcc")
    private String PayerAcc;

    /**
     * 无锡银行
     *
     */
    private String tran_date;
    private String branch_id;
    private String  query_org;
    private String user_id;
    private String tran_type;
    private String global_type;
    private String global_id;
    private String customer_name;
    private String query_time_begein;
    private String query_time_end;
    private String query_reason;
    private String ver_type;
    private String idauth_flag;
    private String image_url;
    private String note;
    private String remark;
    private String spcode;
    private String ksrq;
    private String jsrq;
    private String sncode;
    private String  user_code;
    private String  name;
    private String  id_num;
    public Parameters() {
    }

    public String getTran_date() {
        return tran_date;
    }

    public void setTran_date(String tran_date) {
        this.tran_date = tran_date;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTran_type() {
        return tran_type;
    }

    public void setTran_type(String tran_type) {
        this.tran_type = tran_type;
    }

    public String getGlobal_type() {
        return global_type;
    }

    public void setGlobal_type(String global_type) {
        this.global_type = global_type;
    }

    public String getGlobal_id() {
        return global_id;
    }

    public void setGlobal_id(String global_id) {
        this.global_id = global_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getQuery_time_begein() {
        return query_time_begein;
    }

    public void setQuery_time_begein(String query_time_begein) {
        this.query_time_begein = query_time_begein;
    }

    public String getQuery_time_end() {
        return query_time_end;
    }

    public void setQuery_time_end(String query_time_end) {
        this.query_time_end = query_time_end;
    }

    public String getQuery_reason() {
        return query_reason;
    }

    public void setQuery_reason(String query_reason) {
        this.query_reason = query_reason;
    }

    public String getVer_type() {
        return ver_type;
    }

    public void setVer_type(String ver_type) {
        this.ver_type = ver_type;
    }

    public String getIdauth_flag() {
        return idauth_flag;
    }

    public void setIdauth_flag(String idauth_flag) {
        this.idauth_flag = idauth_flag;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getQuery_org() {
        return query_org;
    }

    public void setQuery_org(String query_org) {
        this.query_org = query_org;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddr() {
        return MacAddr;
    }

    public void setMacAddr(String macAddr) {
        MacAddr = macAddr;
    }

    public String getCustAcc() {
        return CustAcc;
    }

    public void setCustAcc(String custAcc) {
        CustAcc = custAcc;
    }

    public String getPayeeAcc() {
        return PayeeAcc;
    }

    public void setPayeeAcc(String payeeAcc) {
        PayeeAcc = payeeAcc;
    }

    public String getPayerAcc() {
        return PayerAcc;
    }

    public void setPayerAcc(String payerAcc) {
        PayerAcc = payerAcc;
    }

    public String getInputHomeAddress() {
        return inputHomeAddress;
    }

    public void setInputHomeAddress(String inputHomeAddress) {
        this.inputHomeAddress = inputHomeAddress;
    }

    public String getInputRegisteredAddress() {
        return inputRegisteredAddress;
    }

    public void setInputRegisteredAddress(String inputRegisteredAddress) {
        this.inputRegisteredAddress = inputRegisteredAddress;
    }

    public String getInputContactAddress() {
        return inputContactAddress;
    }

    public void setInputContactAddress(String inputContactAddress) {
        this.inputContactAddress = inputContactAddress;
    }

    public String getInputApplyProvince() {
        return inputApplyProvince;
    }

    public void setInputApplyProvince(String inputApplyProvince) {
        this.inputApplyProvince = inputApplyProvince;
    }

    public String getInputApplyCity() {
        return inputApplyCity;
    }

    public void setInputApplyCity(String inputApplyCity) {
        this.inputApplyCity = inputApplyCity;
    }

    public String getInputMarriage() {
        return inputMarriage;
    }

    public void setInputMarriage(String inputMarriage) {
        this.inputMarriage = inputMarriage;
    }

    public String getInputDiploma() {
        return inputDiploma;
    }

    public void setInputDiploma(String inputDiploma) {
        this.inputDiploma = inputDiploma;
    }

    public String getInputLoanAmount() {
        return inputLoanAmount;
    }

    public void setInputLoanAmount(String inputLoanAmount) {
        this.inputLoanAmount = inputLoanAmount;
    }

    public String getInputLoanPurpose() {
        return inputLoanPurpose;
    }

    public void setInputLoanPurpose(String inputLoanPurpose) {
        this.inputLoanPurpose = inputLoanPurpose;
    }

    public String getInputLoanTerm() {
        return inputLoanTerm;
    }

    public void setInputLoanTerm(String inputLoanTerm) {
        this.inputLoanTerm = inputLoanTerm;
    }

    public String getInputLoanDate() {
        return inputLoanDate;
    }

    public void setInputLoanDate(String inputLoanDate) {
        this.inputLoanDate = inputLoanDate;
    }

    public String getInputOrganizationAddress() {
        return inputOrganizationAddress;
    }

    public void setInputOrganizationAddress(String inputOrganizationAddress) {
        this.inputOrganizationAddress = inputOrganizationAddress;
    }

    public String getInputWorkPhone() {
        return inputWorkPhone;
    }

    public void setInputWorkPhone(String inputWorkPhone) {
        this.inputWorkPhone = inputWorkPhone;
    }

    public String getInputCompanyType() {
        return inputCompanyType;
    }

    public void setInputCompanyType(String inputCompanyType) {
        this.inputCompanyType = inputCompanyType;
    }

    public String getInputApplyerType() {
        return inputApplyerType;
    }

    public void setInputApplyerType(String inputApplyerType) {
        this.inputApplyerType = inputApplyerType;
    }

    public String getInputIndustry() {
        return inputIndustry;
    }

    public void setInputIndustry(String inputIndustry) {
        this.inputIndustry = inputIndustry;
    }

    public String getInputCareer() {
        return inputCareer;
    }

    public void setInputCareer(String inputCareer) {
        this.inputCareer = inputCareer;
    }

    public String getInputOccupation() {
        return inputOccupation;
    }

    public void setInputOccupation(String inputOccupation) {
        this.inputOccupation = inputOccupation;
    }

    public String getInputWorkTime() {
        return inputWorkTime;
    }

    public void setInputWorkTime(String inputWorkTime) {
        this.inputWorkTime = inputWorkTime;
    }

    public String getInputEntryTime() {
        return inputEntryTime;
    }

    public void setInputEntryTime(String inputEntryTime) {
        this.inputEntryTime = inputEntryTime;
    }

    public String getInputMonthlyIncome() {
        return inputMonthlyIncome;
    }

    public void setInputMonthlyIncome(String inputMonthlyIncome) {
        this.inputMonthlyIncome = inputMonthlyIncome;
    }

    public String getInputAnnualIncome() {
        return inputAnnualIncome;
    }

    public void setInputAnnualIncome(String inputAnnualIncome) {
        this.inputAnnualIncome = inputAnnualIncome;
    }

    public String getInputOtherIncome() {
        return inputOtherIncome;
    }

    public void setInputOtherIncome(String inputOtherIncome) {
        this.inputOtherIncome = inputOtherIncome;
    }

    public String getInputFamilyMobile() {
        return inputFamilyMobile;
    }

    public void setInputFamilyMobile(String inputFamilyMobile) {
        this.inputFamilyMobile = inputFamilyMobile;
    }

    public String getInputWorkmateMobile() {
        return inputWorkmateMobile;
    }

    public void setInputWorkmateMobile(String inputWorkmateMobile) {
        this.inputWorkmateMobile = inputWorkmateMobile;
    }

    public String getInputOthercontactMobile() {
        return inputOthercontactMobile;
    }

    public void setInputOthercontactMobile(String inputOthercontactMobile) {
        this.inputOthercontactMobile = inputOthercontactMobile;
    }

    public String getInputHouseProperty() {
        return inputHouseProperty;
    }

    public void setInputHouseProperty(String inputHouseProperty) {
        this.inputHouseProperty = inputHouseProperty;
    }

    public String getInputHouseLocation() {
        return inputHouseLocation;
    }

    public void setInputHouseLocation(String inputHouseLocation) {
        this.inputHouseLocation = inputHouseLocation;
    }

    public String getInputHousePurchasingtime() {
        return inputHousePurchasingtime;
    }

    public void setInputHousePurchasingtime(String inputHousePurchasingtime) {
        this.inputHousePurchasingtime = inputHousePurchasingtime;
    }

    public String getInputHouseCondition() {
        return inputHouseCondition;
    }

    public void setInputHouseCondition(String inputHouseCondition) {
        this.inputHouseCondition = inputHouseCondition;
    }

    public String getInputHouseAddress() {
        return inputHouseAddress;
    }

    public void setInputHouseAddress(String inputHouseAddress) {
        this.inputHouseAddress = inputHouseAddress;
    }

    public String getInputHouseType() {
        return inputHouseType;
    }

    public void setInputHouseType(String inputHouseType) {
        this.inputHouseType = inputHouseType;
    }

    public String getInputHouseArea() {
        return inputHouseArea;
    }

    public void setInputHouseArea(String inputHouseArea) {
        this.inputHouseArea = inputHouseArea;
    }

    public String getInputHouseEquity() {
        return inputHouseEquity;
    }

    public void setInputHouseEquity(String inputHouseEquity) {
        this.inputHouseEquity = inputHouseEquity;
    }

    public String getInputCarProperty() {
        return inputCarProperty;
    }

    public void setInputCarProperty(String inputCarProperty) {
        this.inputCarProperty = inputCarProperty;
    }

    public String getInputCarProductiontime() {
        return inputCarProductiontime;
    }

    public void setInputCarProductiontime(String inputCarProductiontime) {
        this.inputCarProductiontime = inputCarProductiontime;
    }

    public String getInputRegisterDate() {
        return inputRegisterDate;
    }

    public void setInputRegisterDate(String inputRegisterDate) {
        this.inputRegisterDate = inputRegisterDate;
    }

    public String getInputPledgeProperty() {
        return inputPledgeProperty;
    }

    public void setInputPledgeProperty(String inputPledgeProperty) {
        this.inputPledgeProperty = inputPledgeProperty;
    }

    public String getInputUsedCar() {
        return inputUsedCar;
    }

    public void setInputUsedCar(String inputUsedCar) {
        this.inputUsedCar = inputUsedCar;
    }

    public String getInputAllopatricCar() {
        return inputAllopatricCar;
    }

    public void setInputAllopatricCar(String inputAllopatricCar) {
        this.inputAllopatricCar = inputAllopatricCar;
    }

    public String getInputTransferTime() {
        return inputTransferTime;
    }

    public void setInputTransferTime(String inputTransferTime) {
        this.inputTransferTime = inputTransferTime;
    }

    public String getInputGraduateSchool() {
        return inputGraduateSchool;
    }

    public void setInputGraduateSchool(String inputGraduateSchool) {
        this.inputGraduateSchool = inputGraduateSchool;
    }

    public String getInputOrganization() {
        return inputOrganization;
    }

    public void setInputOrganization(String inputOrganization) {
        this.inputOrganization = inputOrganization;
    }

    public String getInputFamilyName() {
        return inputFamilyName;
    }

    public void setInputFamilyName(String inputFamilyName) {
        this.inputFamilyName = inputFamilyName;
    }

    public String getInputFamilyRelation() {
        return inputFamilyRelation;
    }

    public void setInputFamilyRelation(String inputFamilyRelation) {
        this.inputFamilyRelation = inputFamilyRelation;
    }

    public String getInputWorkmateName() {
        return inputWorkmateName;
    }

    public void setInputWorkmateName(String inputWorkmateName) {
        this.inputWorkmateName = inputWorkmateName;
    }

    public String getInputWorkmateRelation() {
        return inputWorkmateRelation;
    }

    public void setInputWorkmateRelation(String inputWorkmateRelation) {
        this.inputWorkmateRelation = inputWorkmateRelation;
    }

    public String getInputOthercontactName() {
        return inputOthercontactName;
    }

    public void setInputOthercontactName(String inputOthercontactName) {
        this.inputOthercontactName = inputOthercontactName;
    }

    public String getInputOthercontactRelation() {
        return inputOthercontactRelation;
    }

    public void setInputOthercontactRelation(String inputOthercontactRelation) {
        this.inputOthercontactRelation = inputOthercontactRelation;
    }

    public String getInputHouseNumber() {
        return inputHouseNumber;
    }

    public void setInputHouseNumber(String inputHouseNumber) {
        this.inputHouseNumber = inputHouseNumber;
    }

    public String getInputCarType() {
        return inputCarType;
    }

    public void setInputCarType(String inputCarType) {
        this.inputCarType = inputCarType;
    }

    public String getInputCarLicensenumber() {
        return inputCarLicensenumber;
    }

    public void setInputCarLicensenumber(String inputCarLicensenumber) {
        this.inputCarLicensenumber = inputCarLicensenumber;
    }

    public String getInputCarVinnumber() {
        return inputCarVinnumber;
    }

    public void setInputCarVinnumber(String inputCarVinnumber) {
        this.inputCarVinnumber = inputCarVinnumber;
    }

    public String getInputManufacturerName() {
        return inputManufacturerName;
    }

    public void setInputManufacturerName(String inputManufacturerName) {
        this.inputManufacturerName = inputManufacturerName;
    }

    public String getInputCarColour() {
        return inputCarColour;
    }

    public void setInputCarColour(String inputCarColour) {
        this.inputCarColour = inputCarColour;
    }

    public String getInputBuyPrice() {
        return inputBuyPrice;
    }

    public void setInputBuyPrice(String inputBuyPrice) {
        this.inputBuyPrice = inputBuyPrice;
    }


    public String getCid2() {
        return cid2;
    }

    public void setCid2(String cid2) {
        this.cid2 = cid2;
    }

    public String getWorkLongitude() {
        return workLongitude;
    }

    public void setWorkLongitude(String workLongitude) {
        this.workLongitude = workLongitude;
    }

    public String getWorkLatitude() {
        return workLatitude;
    }

    public void setWorkLatitude(String workLatitude) {
        this.workLatitude = workLatitude;
    }

    public String getLiveLongitude() {
        return liveLongitude;
    }

    public void setLiveLongitude(String liveLongitude) {
        this.liveLongitude = liveLongitude;
    }

    public String getLiveLatitude() {
        return liveLatitude;
    }

    public void setLiveLatitude(String liveLatitude) {
        this.liveLatitude = liveLatitude;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getId_num() {
        return id_num;
    }

    public void setId_num(String id_num) {
        this.id_num = id_num;
    }

    public String getSpcode() {
        return spcode;
    }

    public void setSpcode(String spcode) {
        this.spcode = spcode;
    }


    public String getJsrq() {
        return jsrq;
    }

    public void setJsrq(String jsrq) {
        this.jsrq = jsrq;
    }

    public String getSncode() {
        return sncode;
    }

    public void setSncode(String sncode) {
        this.sncode = sncode;
    }

    public String getKsrq() {
        return ksrq;
    }

    public void setKsrq(String ksrq) {
        this.ksrq = ksrq;
    }
}
