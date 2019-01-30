package com.geo.rcs.modules.event.customerEvent.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.util.DateUtils;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.util.ObjectUtils;
import com.geo.rcs.common.util.excel.ExportExcel;
import com.geo.rcs.common.util.pdf.PdfUtil;
import com.geo.rcs.modules.event.customerEvent.entity.CustomerEventEntry;
import com.geo.rcs.modules.event.customerEvent.service.CustomerEventService;
import com.geo.rcs.modules.event.vo.EventReport;
import com.geo.rcs.modules.geo.entity.GeoUser;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.github.pagehelper.PageInfo;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import sun.misc.BASE64Decoder;

/**
 * 客户事件统计
 * 
 * @author qiaoShengLong
 * @email qiaoshenglong@geotmt.com
 * @date 2018/4/8 16:43
 */
@RestController
@RequestMapping("/Customer/EventStatistics")
public class CustomerEventStatistics extends BaseController {

	@Autowired
	private CustomerEventService customerEventService;

	/**
	 * 进件统计列表 （模糊，分页）
	 * 
	 * @param map
	 * @return
	 */
	@PostMapping("/list")
	@RequiresPermissions("api:customerEvestat:list") // 权限管理
	public void list(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> map) {

		try {
			// 获取当前登录用户
			GeoUser user = getGeoUser();
			if (user == null) {
				this.sendError(request, response, "用户信息已过期，请重新登录！");
			}

			this.sendData(request, response, customerEventService.findByPage(map, 1));
			this.sendOK(request, response);
		} catch (Exception e) {
			LogUtil.error("进件统计列表 （模糊，分页）", map.toString(), getGeoUser().getName(), e);
			this.sendError(request, response, StatusCode.SERVER_ERROR.getMessage());
		}
	}

	/**
	 * 进件列表展示最后一行 （模糊，分页）
	 * 
	 * @param map
	 * @return
	 */
	@PostMapping("/total")
	@RequiresPermissions("api:customerEvestat:list") // 权限管理
	public void getTotal(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> map) {

		try {
			// 获取当前登录用户
			GeoUser user = getGeoUser();
			if (user == null) {
				this.sendError(request, response, "用户信息已过期，请重新登录！");
			}
			CustomerEventEntry total = customerEventService.getTotal(map);
			total.getEventStatEntryList().clear();
			this.sendData(request, response, total);
			this.sendOK(request, response);
		} catch (Exception e) {
			LogUtil.error("进件列表展示最后一行 （模糊，分页）", map.toString(), getGeoUser().getName(), e);
			this.sendError(request, response, StatusCode.SERVER_ERROR.getMessage());
		}
	}

	/**
	 * 图表
	 * 
	 * @param map
	 * @return
	 */
	@PostMapping("/chartList")
	@RequiresPermissions("api:customerEvestat:list") // 权限管理
	public void chartList(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> map) {

		try {

			PageInfo<CustomerEventEntry> pageInfo = customerEventService.findByPage(map, 0);
			List<CustomerEventEntry> list = pageInfo.getList();
			HashMap<Object, Object> hashMap = new HashMap<>();
			int size = list.size();
			String[] dateArr = new String[size];
			String[] passArr = new String[size];
			String[] mannualArr = new String[size];
			String[] notpassArr = new String[size];
			for (int i = 0; i < size; i++) {

				dateArr[i] = list.get(i).getDate();
				passArr[i] = String.valueOf(list.get(i).getPass());
				mannualArr[i] = String.valueOf(list.get(i).getMannual());
				notpassArr[i] = String.valueOf(list.get(i).getNotpass());
			}
			hashMap.put("date", dateArr);
			hashMap.put("pass", passArr);
			hashMap.put("mannual", mannualArr);
			hashMap.put("notpass", notpassArr);
			ArrayList<Object> listmap = new ArrayList<>();
			listmap.add(hashMap);
			this.sendData(request, response, listmap);
			this.sendOK(request, response);
		} catch (Exception e) {
			LogUtil.error("图表", map.toString(), getGeoUser().getName(), e);

			this.sendError(request, response, StatusCode.SERVER_ERROR.getMessage());
		}
	}

	public String addDays(int x) {
		x--;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, -x);
		date = c.getTime();
		String dateStr = f.format(date);
		return dateStr;
	}

	/**
	 * 导出风险事件统计数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/export")
	@RequiresPermissions("api:customerEvestat:list") // 权限管理
	public Geo export(HttpServletRequest request, HttpServletResponse response) {
		// 将请求参数转换成map
		try {
			Map<String, Object> map = ObjectUtils.requestToMap(request);

			// SysUser user = getUser();
			// if(user == null) {
			// return Geo.error(StatusCode.EXPIRED.getCode(), "用户信息已过期，请重新登录");
			// }
			// 获取当前登录用户
			GeoUser user = getGeoUser();
			if (user == null) {
				return Geo.error(StatusCode.EXPIRED.getCode(), "用户信息已过期，请重新登录");
			}

			map.put("userId", user.getUniqueCode());
			CustomerEventEntry total = customerEventService.getTotal(map);
			List<Map<String, Object>> mapList = new ArrayList<>();
			for (CustomerEventEntry entry : total.getEventStatEntryList()) {
				Map<String, Object> map1 = JSONUtil.beanToMap(entry);
				mapList.add(map1);
			}
			Map<String, Object> map2 = JSONUtil.beanToMap(total);
			map2.put("total", "总计");
			mapList.add(map2);
			String fileName = "客户事件统计_" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			String[] headers = new String[] { "时间", "客户名称", "通过事件数", "通过事件率", "拒绝事件数", "拒绝事件率", "人工审核数", "人工审核率" };
			String[] props = new String[] { "date", "name", "pass", "passpercent", "notpass", "notpasspercent", "mannual",
					"mannualpercent" };

			ExportExcel excel = new ExportExcel("客户事件统计", headers, true).setDataList(mapList, props);

			String svg = request.getParameter("svg");

			try {
				if (svg != null && svg != "") {
					BASE64Decoder decoder = new BASE64Decoder();
					byte[] bt = decoder.decodeBuffer(svg);
					excel.setPictuce(bt, excel.getRowsIdx());
				}
				excel.write(response, fileName).dispose();
				return Geo.ok(StatusCode.SUCCESS.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.error("导出风险事件统计数据", "", getGeoUser().getName(), e);

			e.printStackTrace();
			return Geo.error();
		}

	}

	/**
	 * 获取客户名称列表
	 */
	@RequestMapping("/getCustomerType")
	@RequiresPermissions("api:customerEvestat:list") // 权限管理
	public void getCustomerType(HttpServletRequest request, HttpServletResponse response) {
		try {
			List typeList = new ArrayList();

			List<SysUser> busType = customerEventService.getCustomerType();
			typeList.add(busType);
			this.sendData(request, response, typeList);
		} catch (Exception e) {
			LogUtil.error("获取客户名称列表", "", getGeoUser().getName(), e);

			this.sendError(request, response, "获取业务类型列表失败！");
		}
	}

}
