package com.geo.rcs.modules.jobs.controller;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.geo.entity.GeoUser;
import com.geo.rcs.modules.jobs.entity.JobWorker;
import com.geo.rcs.modules.jobs.service.JobRegisterService;
import com.geo.rcs.modules.jobs.service.RegisterService;
import com.geo.rcs.modules.rabbitmq.consumer.Worker;
import com.geo.rcs.modules.rabbitmq.constant.MqConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件管理
 *
 * @author yongmingz
 * @email yongmingz@geotmt.com
 * @date 2018-07-08
 */
@RestController
@RequestMapping("/job/manage")
public class JobManageController extends BaseController {

	@Autowired
	private RegisterService registerService;

	@Value("${spring.rabbitmq.host}")
	private String host;
	@Value("${spring.rabbitmq.port}")
	private int port;
	@Value("${spring.rabbitmq.username}")

	private String username;
	@Value("${spring.rabbitmq.password}")
	private String password;
	@Autowired
	private JobRegisterService jobRegisterService;
	private HashMap<String, Object> mqConfigMap;
	@Autowired
	private Worker worker;

	private List<Worker> workerList = new ArrayList<>();

	/**
	 * worker列表
	 *
	 * @param map
	 * @return
	 */
	@PostMapping("/workers")
	public Geo list(@RequestBody Map<String, Object> map) {
		try {
			GeoUser user = getGeoUser();
			if (user == null) {
				return Geo.error(StatusCode.EXPIRED.getCode(), "用户信息已过期，请重新登录");
			}

			map.put("userId", user.getUniqueCode());
			List<JobWorker> workers = getAllworkers();

			Object json = JSON.toJSON(workers);
			return Geo.ok().put("data", json);
			// return Geo.ok().put("data", workers);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.error(" 任务注册管理 ", map.toString(), getUser().getName(), e);
			return Geo.error();

		}
	}

	/**
	 * 新建worker
	 *
	 * @param map
	 * @return
	 */
	@PostMapping("/createWorker")
	public Geo createWorker(@RequestBody Map<String, Object> map) {
		try {
			GeoUser user = getGeoUser();
			if (user == null) {
				return Geo.error(StatusCode.EXPIRED.getCode(), "用户信息已过期，请重新登录");
			}
			System.out.println("======");
			if (mqConfigMap == null) {
				mqConfigMap = new HashMap<>();
//				mqConfigMap.put(MqConstant.HOST, host);
//				mqConfigMap.put(MqConstant.PORT, port);
//				mqConfigMap.put(MqConstant.USERNAME, username);
//				mqConfigMap.put(MqConstant.PASSWORD, password);
			}
			ArrayList<String> list = (ArrayList<String>) map.get("queneNameList");
			String workerName = (String) map.get("workerName");
			String taskRole = (String) map.get("taskRole");
			Worker worker = new Worker();
			workerList.add(worker);
			System.out.println("新建worker成功:==" + workerList.toString());
			return Geo.ok();
			// return Geo.ok().put("data", workers);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.error(" 任务注册管理 ", map.toString(), getUser().getName(), e);
			return Geo.error();

		}
	}

	/**
	 * 修改worker状态
	 *
	 * @param status
	 *            0 删除worker, 1开启worker等待状态 , 2 暂停worker
	 * @return
	 */
	@PostMapping("/changeWorkerStatus")
	public Geo changeWorkerStatus(String id, int status) {
		boolean flag = true;
		try {
			if (status != 0 && status != 1 && status != 2) {
				return Geo.error("修改状态值非法!");
			}
			GeoUser user = getGeoUser();
			if (user == null) {
				return Geo.error(StatusCode.EXPIRED.getCode(), "用户信息已过期，请重新登录");
			}
			for (Worker worker : workerList) {
				// System.out.println("worker里的id:=="+worker.getRegister().getId());
				// System.out.println("参数id:========"+id);
				if (id.equals(worker.getRegister().getId())) {
					flag = false;
					if (MqConstant.WorkStatus.WORKER_DEAD.getCode() == status) {
//						worker.destroy();
					} else {
//						worker.workSwitch(status);
					}
				}
			}
			if (flag) {
				System.out.println("该worker已经注销,无法进行操作!");
				return Geo.error("该worker已经注销,无法进行操作!");
			}
			return Geo.ok();
			// return Geo.ok().put("data", workers);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// LogUtil.error(" 任务注册管理 ", map.toString(), getUser().getName(),
			// e);
			return Geo.error();

		}
	}

	/**
	 * 子方法: 获取所有
	 * @return
	 */
	private List<JobWorker> getAllworkers() {
		Map<String, String> _workers = registerService.getAllRegisterInfo();

		List<JobWorker> workers = new ArrayList<JobWorker>();

		for (Map.Entry<String, String> entry : _workers.entrySet()) {
			workers.add(JSON.parseObject(entry.getValue(), JobWorker.class));
		}
		return workers;
	}

}
