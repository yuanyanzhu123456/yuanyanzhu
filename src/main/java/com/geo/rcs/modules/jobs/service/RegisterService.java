package com.geo.rcs.modules.jobs.service;

import java.util.Map;

public interface RegisterService {

    void setRegisterInfoByKey( String infoName, String infoContent);

    void updateRegisterInfoByKey( String infoName, String infoContent);

    String getRegisterInfoByKey( String infoName);

    Map getAllRegisterInfo();

    void deleteRegisterInfoByKey( String infoName);
}
