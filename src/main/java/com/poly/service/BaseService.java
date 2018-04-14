package com.poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BaseService {
	
	@Autowired
	public JdbcTemplate jdbc;
}
