package com.gen.autoFillPDF.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gen.autoFillPDF.model.DataModel;

@Repository
public interface DataDao extends MongoRepository<DataModel, Integer> {

}