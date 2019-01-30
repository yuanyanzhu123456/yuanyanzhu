package com.geo.rcs.modules.event.handler;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class CsvFileHandler {

    /**
     * 读取Csv文件
     * @param filePath 文件路径
     * @param encode 编码格式："UTF-8" "GBK"
     * @return 文件列表
     */
    public static List<String[]> read(String filePath, String encode){

        List<String[]> readContent = new ArrayList();

        try {
            // 创建CSV读对象
            CsvReader csvReader = new CsvReader(filePath, ',', Charset.forName(encode));
            // 读表头
            csvReader.readHeaders();

            while (csvReader.readRecord()){
                // 读一整行
                String content = csvReader.getRawRecord();
                System.out.println(content);
                // 读这行的某一列
                // System.out.println(csvReader.get("Link"));
                readContent.add(content.split(","));
                // csvWriter.writeRecord(content.split(","));
            }
            csvReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return readContent;
    }

    /**
     * 写Csv文件
     * @param Headers 文件表头
     * @param Content 文件内容
     * @param filePath 文件路径
     * @param encode 编码格式
     */
    public static void write(String[] Headers, List<String[]> Content, String filePath, String encode ) {

        try {
            // 创建CSV写对象
            CsvWriter csvWriter = new CsvWriter(filePath,',', Charset.forName(encode));

            // 写表头
            csvWriter.writeRecord(Headers);

            for(String[] line: Content){
                csvWriter.writeRecord(line);
            }
            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
