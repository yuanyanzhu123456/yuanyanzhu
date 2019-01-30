package com.geo.rcs.modules.roster.controller;

import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.util.DateUtils;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.excel.ExportExcel;
import com.geo.rcs.modules.roster.service.BlackListService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("excel")
public class ExcelController {

    @Autowired
    private BlackListService blackListService;

    /**
     * 测试进度分析模块的产品统计的导出
     * @param response
     * @param request
     * @return
     */
    @SysLog("黑名单的导出")
    @RequestMapping(value = "/exportTestCustDatils", method = RequestMethod.POST)
    @RequiresPermissions("ros:bow:export")
    public Geo exportTestCustDatils(HttpServletResponse response, HttpServletRequest request){
        List<Map<String, Object>> mapList = blackListService.findAll();

       /* for (Map<String,Object> map:mapList) {
            map.put("status",configMap.get(map.get("status")));
        }*/

        String fileName = "产品统计_"+ DateUtils.format(new Date(),"yyyyMMddHHmmss")+".xlsx";

        String[] headers = new String[]{
                "区域","状态","新产品汇总","反欺诈评分","验真新接口","验真老三样","位置信息","信用评分","银行卡鉴权","金融老接口",
                "风险IP","关注名单","其他","反欺诈","多头","身份认证","车辆信息"
        };

        String[][] columns = new String[][]{
                {"华东","西区","华南","华北","其他"},
                {"测试未通过","测试中","搁置","交流中","接口对接中","签约中","商务谈判中","谈判失败","拓展中","消耗中","暂无合作","暂无需求","(空白)"}
        };

        String[] props = new String[]{"custCount"};

        ExportExcel ee = new ExportExcel("产品统计", headers).setDataList4(mapList,headers,columns,props);



        try {
            ee.write(response,fileName).dispose();
            return Geo.ok("导出成功！");
        } catch (IOException e) {
            e.printStackTrace();
            return Geo.ok("导出失败！");
        }
    }
    @RequestMapping(value="/import",method = RequestMethod.POST)
    @RequiresPermissions("ros:bow:import")
    public String  upload(@RequestParam(value="file",required = false)MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        String result = blackListService.readExcelFile(file);
        return result;
    }

}