package com.geo.rcs.modules.decision.entity;

/**
 * @Author ZhengXingWang
 * @Email zhengxingwang@geotmt.com
 * @Date 2018/11/7  15:47
 **/

public class ImgBaseForApi {

    public ImgBaseForApi(Long id, String imgBase) {
        this.id = id;
        this.imgBase = imgBase;
    }

    public ImgBaseForApi() {
    }

    private Long id;

    public ImgBaseForApi(Long id) {
        this.id = id;
    }

    public ImgBaseForApi(String imgBase) {
        this.imgBase = imgBase;
    }

    private String imgBase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgBase() {
        return imgBase;
    }

    public void setImgBase(String imgBase) {
        this.imgBase = imgBase;
    }

    @Override
    public String toString() {
        return "ImgBaseForApi{" +
                "id=" + id +
                ", imgBase='" + imgBase + '\'' +
                '}';
    }
}
