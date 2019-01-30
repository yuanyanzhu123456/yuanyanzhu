## 定制账户

共2部分修改:

INSERT INTO `sys_user` (`id`, `contact`, `create_time`, `creater`, `email`, `mobilephone`, `name`, `password`, `role_id`, `salt`, `status`, `username`, `login_ip`, `login_date`, `unique_code`, `company`)
VALUES
	(1,'上海TCL金控','2018-04-20 00:00:00','上海TCL金控','www.tcl.com','13800138000','上海TCL金控','a4953ba122f33201e62ba2f5530367c0',2,NULL,1,'tcl-admin','0:0:0:0:0:0:0:1','2018-04-20 00:00:00',1,'上海TCL金控');


INSERT INTO `geo_user` (`id`, `contact`, `create_time`, `creater`, `email`, `mobilephone`, `name`, `password`, `role_id`, `salt`, `status`, `username`, `login_ip`, `login_date`, `role_type`, `unique_code`, `company`)
VALUES
  (1,'上海TCL金控','2018-04-20 10:54:00','上海TCL金控','www.tcl.com','','上海TCL金控','013267422ded9e24219b83c1ec9b6caf',1,NULL,1,'superuser',NULL,NULL,NULL,1,'上海TCL金控');
