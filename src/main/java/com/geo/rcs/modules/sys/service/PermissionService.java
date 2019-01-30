package com.geo.rcs.modules.sys.service;

import com.geo.rcs.modules.sys.dao.PermissionMapper;
import com.geo.rcs.modules.sys.entity.SysPermission;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionService {

    @Autowired
    PermissionMapper entryDao;

    public ArrayList<JSONObject> applist(){
        ArrayList<SysPermission> appList = entryDao.getAppPermission();
        ArrayList<JSONObject> appnodes = new ArrayList<>();
        for (SysPermission app:appList) {
            JSONObject app_node = new JSONObject();
            app_node.put("id",app.getId());
            app_node.put("label",app.getPermission());
            //app_node.put("url",app.getUrl());
            //app_node.put("node",app);
            //app_node.put("children",channellist(app.getId()));
            appnodes.add(app_node);
        }

        return appnodes;
    }

    /*public ArrayList<JSONObject> channellist(long appid){
        ArrayList<SysPermission> channelList = entryDao.getChannelByApp(appid);
        ArrayList<JSONObject> channelnodes = new ArrayList<>();
        for(SysPermission channel:channelList){
            JSONObject channel_node = new JSONObject();
            channel_node.put("id",channel.getId());
            channel_node.put("label",channel.getPermission());
            //channel_node.put("url",channel.getUrl());
            //channel_node.put("node",channel);
            channel_node.put("children",viewlist(channel.getId()));
            channelnodes.add(channel_node);
        }
        return channelnodes;
    }


    public ArrayList<JSONObject> viewlist(long channelid){
        ArrayList<SysPermission> viewList = entryDao.getViewByChannel(channelid);
        ArrayList<JSONObject> viewnodes = new ArrayList<>();
        for(SysPermission view:viewList){
            JSONObject view_node = new JSONObject();
            view_node.put("id",view.getId());
            view_node.put("label",view.getPermission());
            //view_node.put("url",view.getUrl());
            //view_node.put("node",view);
            view_node.put("children",subviewlist(view.getId()));
            viewnodes.add(view_node);
        }
        return viewnodes;
    }

    public ArrayList<JSONObject> subviewlist(long viewid){
        ArrayList<SysPermission> subviewList = entryDao.getSubviewByView(viewid);
        ArrayList<JSONObject> subviewnodes = new ArrayList<>();
        for(SysPermission subview:subviewList){
            JSONObject subview_node = new JSONObject();
            subview_node.put("id",subview.getId());
            subview_node.put("label",subview.getPermission());
            //subview_node.put("url",subview.getUrl());
            //subview_node.put("node",subview);
            subview_node.put("children",null);
            subviewnodes.add(subview_node);
        }
        return subviewnodes;
    }*/

    public ArrayList<SysPermission> tree(){
        ArrayList<SysPermission> items = entryDao.tree();
        return items;
    }

    public List<SysPermission> findByUserId(Long roleId) {
        return entryDao.findByUserId(roleId);
    }

    public List<SysPermission> list() {
        return entryDao.list();
    }
}
