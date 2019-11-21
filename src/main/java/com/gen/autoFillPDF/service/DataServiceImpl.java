package com.gen.autoFillPDF.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gen.autoFillPDF.service.DataService;
import com.gen.autoFillPDF.dao.DataDao;
import com.gen.autoFillPDF.dao.PageOneDao;
import com.gen.autoFillPDF.model.DataModel;
import com.gen.autoFillPDF.model.PageOneModel;

@Service
public class DataServiceImpl implements DataService {

	@Autowired
	DataDao dao;
	
	@Autowired	
	PageOneDao pageOneDao;


	@Override
	public void createData(List<DataModel> data) {
		dao.saveAll(data);
	}

	public Optional<DataModel> findDataById(int id) {
		return dao.findById(id);
	}

	public void updateData(DataModel emp) {
		dao.save(emp);
	}

	@Override
	public Optional<PageOneModel> findPageOneById(int id) {
		// TODO Auto-generated method stub
		return pageOneDao.findById(id);
	}

}