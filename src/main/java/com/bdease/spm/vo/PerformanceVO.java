package com.bdease.spm.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import io.swagger.annotations.ApiModelProperty;

public class PerformanceVO {
	@ApiModelProperty(value = "当前月1号到当前日")
	private List<String> days;
	
	@ApiModelProperty(value = "店员绩效列表")
	private List<PerformanceDataVO> performances = new ArrayList<>();

	public PerformanceVO() {
		days = new ArrayList<String>();
		LocalDate today = LocalDate.now();
		for (int i = 1; i <= today.getDayOfMonth(); i++) {
			days.add(dayToString(i));
		}
	}

	public void addDailySale(Long userId, String userName, List<DailySaleVO> dailySales) {
		PerformanceDataVO performanceDataVO = new PerformanceDataVO();
		performanceDataVO.setSoldBy(userId);
		performanceDataVO.setSoldName(userName);
		List<BigDecimal> data = new ArrayList<BigDecimal>();
		for (int i = 0; i < days.size(); i++) {
			data.add(new BigDecimal(0));
		}
		for (DailySaleVO dailySale : dailySales) {
			data.set(dailySale.getOrderDate().getDayOfMonth() - 1, dailySale.getAmount());
		}
		performanceDataVO.setData(data);
		performances.add(performanceDataVO);
	}

	private String dayToString(int day) {
		if (day < 10)
			return "0" + day;
		else
			return String.valueOf(day);
	}

	public List<String> getDays() {
		return days;
	}

	public List<PerformanceDataVO> getPerformances() {
		return performances;
	}
	
	public static void main(String args[]) {
		PerformanceVO per = new PerformanceVO();
		
		List<DailySaleVO> sales = null;
		DailySaleVO dailySaleVO = null;
		
		sales = new ArrayList<DailySaleVO>();		
		dailySaleVO = new DailySaleVO();
		dailySaleVO.setAmount(new BigDecimal(100));
		dailySaleVO.setOrderDate(LocalDate.now());
		sales.add(dailySaleVO);		
			
		dailySaleVO = new DailySaleVO();
		dailySaleVO.setAmount(new BigDecimal(80));
		dailySaleVO.setOrderDate(LocalDate.now().minusDays(1));
		sales.add(dailySaleVO);
		
		per.addDailySale(1L, "ypzhuang", sales);
		
		
		dailySaleVO = new DailySaleVO();
		dailySaleVO.setAmount(new BigDecimal(60));
		dailySaleVO.setOrderDate(LocalDate.now().minusDays(2));
		sales.add(dailySaleVO);
		per.addDailySale(2L, "John", sales);
		
		Gson gson = new Gson();
		System.out.println(gson.toJson(per));
		
	}
}
