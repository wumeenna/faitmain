
package com.faitmain.domain.customer.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.faitmain.domain.customer.domain.BanPeriod;
import com.faitmain.domain.customer.domain.Customer;
import com.faitmain.global.common.Image;

@Mapper
public interface CustomerMapper {

	//CustomerBoard
	
	//INSERT 
	public int addCustomerBoard(Customer customer) throws Exception;
	
	//INSERT
	public void addCustomerBoardImage(Image image) throws Exception;
	
	//UPDATE
	public int updateCustomerBoard(Customer customer) throws Exception;
	
	//UPDATE
	public void updateCustomerBoardImage(Image image) throws Exception;
	
	//SELECT Detail
	public Customer getCustomerBoard(int boardNumber) throws Exception;
	
	//SELECT
	public Image getImage(int boardNumber) throws Exception;
	
	//SELECT List
	public List<Customer> getCustomerBoardList() throws Exception;
	
	//SELECT
	public int deleteCustomerBoard(int boardNumber) throws Exception;
	
	//DELETE
	public int DeleteCustomerBoard(Customer customer) throws Exception;
	
	

	//BanPeriod
	public int processBanPeriod(int reportNumber) throws Exception;
}

