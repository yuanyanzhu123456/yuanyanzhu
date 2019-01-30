package com.geo.rcs.modules.kafka;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;


/**
 * 客户端去操作hdfs时，是有一个用户身份的
 * 默认情况下，hdfs客户端api会从jvm中获取一个参数来作为自己的用户身份：-DHADOOP_USER_NAME=hadoop
 * <p>
 * 也可以在构造客户端fs对象时，通过参数传递进去
 *
 * @author
 */
/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 19:23 2018/11/7
 */
@Component
public class HdfsClient {
    FileSystem fs;
    Configuration conf = null;

    @Value("${spring.kafka.hdfsIpPortList}")
    private String hdfsIpPort;

    public void getFileSystem() {
        if (fs == null) {
            conf = new Configuration();
            hdfsIpPort = hdfsIpPort == null ? "hdfs://hadoop2.rcs.com:8020" : hdfsIpPort;
            conf.set("fs.defaultFS", hdfsIpPort);
            //拿到一个文件系统操作的客户端实例对象
        /*fs = FileSystem.get(conf);*/
            //可以直接传入 uri和用户身份
            try {
                fs = FileSystem.get(new URI(hdfsIpPort), conf, "hdfs"); //最后一个参数为用户名
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //	@Test
    public boolean appendLine(String tableName, String appendLine) throws Exception {
        try {
            getFileSystem();
            //先判断今天的日志文件文件是否存在,如果不存在就创建
            String filName = DateFormatUtils.format(new Date(), "yyyy-MM-dd") + ".log";
            String dirPath = HadoopConsts.PROJECT_SPACE+HadoopConsts.PROJECT_RCS2_SERVER+"/"+tableName + "/";
            String filePath = dirPath + filName;
            if (fs.exists(new Path(filePath))) {
                return appendFileByLock(appendLine, filePath);
            } else {
                synchronized ("createFile") {
                    if (!fs.exists(new Path(filePath))) {
                        boolean newFile = fs.createNewFile(new Path(filePath));
                    }
                    return appendFileByLock(appendLine, filePath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    public boolean appendFileByLock(String appendLine, String filePath) throws IOException {
        try {
            getFileSystem();
            synchronized (HdfsClient.class) {
                FSDataOutputStream append = fs.append(new Path(filePath));
                append.write((appendLine + HadoopConsts.LINE_SPLITE).getBytes());
                append.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean Download(String hdfsPath, String localPath) throws Exception {
        try {
            getFileSystem();
            fs.copyToLocalFile(new Path(hdfsPath), new Path(localPath));
            fs.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void printConf() {
        getFileSystem();
        Iterator<Entry<String, String>> iterator = conf.iterator();
        while (iterator.hasNext()) {
            Entry<String, String> entry = iterator.next();
            System.out.println(entry.getValue() + "--" + entry.getValue());//conf加载的内容
        }
    }

    /**
     * 创建目录或文件
     */
    public boolean makdirOrFile(String type, String path) throws Exception {
        getFileSystem();
        if ("file".equalsIgnoreCase(type)) {
            return fs.createNewFile(new Path(path));
        } else {
            return fs.mkdirs(new Path(path));
        }
    }

    /**
     * 删除
     */
    public boolean deleteDirOrFile(String path) throws Exception {
        getFileSystem();
        return fs.delete(new Path(path), true);//true， 递归删除
    }

    public void printFileList() throws Exception {
        getFileSystem();
        FileStatus[] listStatus = fs.listStatus(new Path("/"));
        for (FileStatus fileStatus : listStatus) {
            System.err.println(fileStatus.getPath() + "=================" + fileStatus.toString());
        }
        //会递归找到所有的文件
        RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);
        while (listFiles.hasNext()) {
            LocatedFileStatus next = listFiles.next();
            String name = next.getPath().getName();
            Path path = next.getPath();
            System.out.println(name + "---" + path.toString());
        }
    }

    public static void main(String[] args) throws Exception {
        HdfsClient hdfsClient = new HdfsClient();
////        hdfsClient.printFileList();
//        boolean b1 = hdfsClient.deleteDirOrFile("/Rcs/rcs2-server/api_event_entry/event.2018-11-07.log");
//        System.out.println(b);
//        boolean file = hdfsClient.makdirOrFile("file", "/Rcs/rcs2-server/api_event_entry/event.2018-11-07.log");
//        System.out.println(file);

//        boolean result = hdfsClient.appendLine("asdfasdfasdf10000");
//        boolean result1 = hdfsClient.appendLine("asdfasdfasdf10000");
//        boolean result2 = hdfsClient.appendLine("asdfasdfasdf10000");

//        boolean b = hdfsClient.deleteDirOrFile("/Rcs/rcs2-server/api_event_entry/2018-11-07.log");
        hdfsClient.Download("/Rcs/rcs2-server/api_event_entry/2018-11-08.log", "E:/");
//        System.out.println(result);
    }


}
