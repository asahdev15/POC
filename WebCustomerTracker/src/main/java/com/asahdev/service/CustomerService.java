package com.asahdev.service;

import com.asahdev.entity.Customer;

import java.util.List;


public interface CustomerService {

	public Customer getCustomer(int theId);

	public List<Customer> getCustomers();

	public void saveCustomer(Customer theCustomer);

	public void deleteCustomer(int theId);

	public List<Customer> searchCustomers(String theSearchName);
}
