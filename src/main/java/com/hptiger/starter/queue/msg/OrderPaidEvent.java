package com.hptiger.starter.queue.msg;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor   
public class OrderPaidEvent implements Serializable {	
	private static final long serialVersionUID = 7637763176735386970L;
	
	public OrderPaidEvent() {			
	}
    private String orderId;        
    private BigDecimal paidMoney;
}