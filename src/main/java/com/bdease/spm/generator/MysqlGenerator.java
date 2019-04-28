package com.bdease.spm.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MysqlGenerator {
	public static final String PROJECT_PATH = "/Users/yinping/work/projects/ave/sipimo_service/";
	public static final String DB_HOST = "localhost";
	public static final int DB_PORT = 3306;
	public static final String DB_USER = "root";
	public static final String DB_PASSWORD = "12345678";
	public static final String BASE_BACKAGE = "com.bdease.spm";
	
    public static String scanner(String tip) {
    	try (Scanner scanner = new Scanner(System.in)) { 
    		  StringBuilder help = new StringBuilder();
    	        help.append("请输入" + tip + "：");
    	        System.out.println(help.toString());
    	        if (scanner.hasNext()) {
    	            String ipt = scanner.next();
    	            if (StringUtils.isNotEmpty(ipt)) {
    	                return ipt;
    	            }
    	        }
    	        throw new MybatisPlusException("请输入正确的" + tip + "！");
    	}      
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
       
        gc.setOutputDir(PROJECT_PATH + "src/main/java");
        gc.setAuthor("John Zhuang");
        gc.setOpen(false);
        gc.setSwagger2(true);       
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(String.format("jdbc:mysql://%s:%d/spm?useUnicode=true&useSSL=false&characterEncoding=utf8", DB_HOST, DB_PORT));
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername(DB_USER);
        dsc.setPassword(DB_PASSWORD);       
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        //pc.setModuleName(scanner("模块名"));
        pc.setModuleName(null);
        pc.setParent(BASE_BACKAGE);
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return PROJECT_PATH + "src/main/resources/mapper/"   + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass(BASE_BACKAGE + ".entity.BaseEntity");
        strategy.setEntityLombokModel(true);
        strategy.setSuperControllerClass(BASE_BACKAGE + ".controller.BaseController");
        strategy.setInclude(scanner("表名"));
        strategy.setSuperEntityColumns("id","create_time","update_time","created_by","updated_by");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_" + "");
        strategy.setRestControllerStyle(true);
        mpg.setStrategy(strategy);
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}