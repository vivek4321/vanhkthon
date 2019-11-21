package com.gen.autoFillPDF.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.gen.autoFillPDF.model.DataModel;
import com.gen.autoFillPDF.model.PageOneModel;

public interface DataService {

	public void updateData(DataModel e);

	public void createData(List<DataModel> data);

	public Optional<DataModel> findDataById(int id);
	
	public Optional<PageOneModel> findPageOneById(int id);
}