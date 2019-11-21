package com.gen.autoFillPDF.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gen.autoFillPDF.model.DataModel;
import com.gen.autoFillPDF.model.PageOneModel;

@Repository
public interface PageOneDao extends MongoRepository<PageOneModel, Integer> {

}