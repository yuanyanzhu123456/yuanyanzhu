package com.geo.rcs.modules.engine.handler;

import java.io.*;

public class RulesFileReader {

    /**
     * 使用BufferedWriter类写文本文件(内容很多，就应该使用更为高效的缓冲器流类BufferedWriter)
     */
    public static void write(String fileName, String content)
    {
        String filePath = String.format("/tmp/%s.drl", fileName);
        try
        {
            BufferedWriter out=new BufferedWriter(new FileWriter(filePath));
            out.write(content);
            out.newLine();  //注意\n不一定在各种计算机上都能产生换行的效果
            out.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 使用BufferedReader类读文本文件
     */
    public static String read(String fileName)
    {
        String line="";
        String content = "";
        fileName = String.format("/tmp/%s.drl", fileName);
        try
        {
            BufferedReader in=new BufferedReader(new FileReader(fileName));
            line=in.readLine();
            while (line!=null)
            {
                line += "\n";
                content +=  line;
                line=in.readLine();
            }
            in.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return content;
    }
}


