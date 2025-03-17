package me.zoulei;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import me.zoulei.frame.SafeProperties;

/**
 * 
* @author zoulei 
* @date 2023年10月23日 下午6:22:11 
* @description 初始化数据库配置，默认生成2个数据库文件，用于保存数据库配置信息。可以手动增加数据库配置。
 */
@Log4j
public class Config {
	public Config(){
		
		try {
			log.info(System.getProperty("user.dir"));
			log.info("检查配置信息...");
			
	        String baseDir = System.getProperty("user.dir")+"/dsProp";
	        File f = new File(baseDir);
	        if(f.exists()&&f.isDirectory()) {
	        	log.info("dsProp目录已存在，用于放数据源配置文件");
	        }else {
	        	log.info("dsProp目录不存在，初始化数据源配置文件...");
	        	f.mkdir();
		        //达梦 oracle mysql三种类型
	        	//返回读取指定资源的输入流  /hfree/src/me/zoulei/ui/components/dsProp/1.properties
		        InputStream is=this.getClass().getClassLoader().getResourceAsStream("me/zoulei/ui/components/dsProp/001.properties");   
		        File p1 = new File(baseDir+"/001.properties");
		        this.copyInputStreamToFile(is, p1);
		        is.close();
		        
		        is=this.getClass().getClassLoader().getResourceAsStream("me/zoulei/ui/components/dsProp/002.properties");  
		        File p2 = new File(baseDir+"/002.properties");
		        this.copyInputStreamToFile(is, p2);
		        is.close();
		        
		        is=this.getClass().getClassLoader().getResourceAsStream("me/zoulei/ui/components/dsProp/003.properties");  
		        File p3 = new File(baseDir+"/003.properties");
		        this.copyInputStreamToFile(is, p3);
		        is.close();
		        log.info("初始化数据源配置文件成功");
	        }
	        
	        
	        //初始化文件
	        String inifilep = System.getProperty("user.dir")+"/hfree.ini";
	        File inifile = new File(inifilep);
	        if(inifile.exists()&&inifile.isFile()) {
	        	log.info("读取hfree.ini配置");
	        	//读取配置
	        	SafeProperties p = new SafeProperties();
	        	p.load(inifile);
	        	Constants.DATA_URL = p.getProperty("DATA_URL");
	        	Constants.ATTR_NOT_NEW_LINE = Boolean.valueOf(p.getProperty("ATTR_NOT_NEW_LINE"));
	        	Constants.AUTHOR = p.getProperty("AUTHOR");
	        	Constants.PACKAGE_OUTPATH = p.getProperty("PACKAGE_OUTPATH");
	        	Constants.CODE_VALUE_SQL = p.getProperty("CODE_VALUE_SQL");
	        	Constants.OUTPUT_PACKAGE = p.getProperty("OUTPUT_PACKAGE");
	        }else {
	        	log.info("第一次使用，初始化hfree.ini文件");
	        	//生成配置
	        	SafeProperties p = new SafeProperties();
	        	p.setProperty("DATA_URL", Constants.DATA_URL,"接口路径 RequestMapping。 已废弃 接口默认拼接表名，生成后自己改");
	        	
	        	p.setProperty("ATTR_NOT_NEW_LINE", Constants.ATTR_NOT_NEW_LINE.toString(),"属性是否换行");
	        	
	        	p.setProperty("AUTHOR", Constants.AUTHOR,"作者");
	        	
	        	p.setProperty("PACKAGE_OUTPATH", Constants.PACKAGE_OUTPATH,"实体类包名。已废弃，放到dao下面了");
	        	
	        	p.setProperty("OUTPUT_PACKAGE", Constants.OUTPUT_PACKAGE,"导出类的包名，填写到controller前一个目录");
	        	
	        	p.setProperty("CODE_VALUE_SQL", Constants.CODE_VALUE_SQL,"获取codetype的sql");
	        	
	        	OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(inifile), "utf-8");
				p.store(outputStreamWriter);
				outputStreamWriter.close();
	        }
	       
	        //检测公共资源目录是否存在
			String sourceDir = System.getProperty("user.dir")+"/source";
	        File sf = new File(sourceDir);
	        if(sf.exists()&&sf.isDirectory()) {
	        	log.info("资源目录已经存在。资源包括图标和4个公共类。");
	        }else {
	        	sf.mkdir();
	        	log.info("资源目录不存在，复制资源，资源包括图标和4个公共类。");
	        	///hfree/src/me/zoulei/backend/templete/dependency/source.zip
	        	InputStream is=this.getClass().getClassLoader().getResourceAsStream("me/zoulei/backend/templete/dependency/source.zip");   
		        File p1 = new File(sourceDir+"/source.zip");
		        this.copyInputStreamToFile(is, p1);
		        is.close();
	        }
	        
	        
	        log.info("检测配置完成。");
		} catch (Exception e) {
			log.info("检测配置失败。");
			e.printStackTrace();
		}
		
		
		
	}
	
	
	/**
     * 流输出到文件的方法
     */
    private void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {

        try (FileOutputStream outputStream = new FileOutputStream(file)) {

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.close();
            inputStream.close();
        }
    }
}
