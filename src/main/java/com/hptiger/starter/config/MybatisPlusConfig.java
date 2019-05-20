/**
 * created Dec 30, 2018 by ypzhuang
 * 
 * MybatisPlus 配置
 */

package com.hptiger.starter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;


@Configuration
public class MybatisPlusConfig {

    @Bean
    public PerformanceInterceptor performanceInterceptor(){
        //启用性能分析插件
        return new PerformanceInterceptor();
    }
    
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();

//        List<ISqlParser> sqlParserList = new ArrayList<>();
//        TenantSqlParser tenantSqlParser = new TenantSqlParser();
//
//        tenantSqlParser.setTenantHandler(new TenantHandler() {
//
//            @Override
//            public Expression getTenantId() {
//                return new LongValue(99L);
//            }
//
//            @Override
//            public String getTenantIdColumn() {
//                return "shop_id";
//            }
//
//            @Override
//            public boolean doTableFilter(String tableName) {
//                // 这里可以判断是否过滤表
//                /*if ("user".equals(tableName)) {
//                    return true;
//                }*/
//                return false;
//            }
//        });
//        sqlParserList.add(tenantSqlParser);
//        paginationInterceptor.setSqlParserList(sqlParserList);


        return paginationInterceptor;

    }
    
    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }
}
