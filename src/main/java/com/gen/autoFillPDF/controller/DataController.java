package com.gen.autoFillPDF.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gen.autoFillPDF.model.DataModel;
import com.gen.autoFillPDF.service.DataService;

@RestController
@RequestMapping(value= "/api/mongo/data")
public class DataController {

	@Autowired
	DataService serv;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Method to save data in the db.
	 * @param data
	 * @return
	 */
	@PostMapping(value= "/create")
	public String create(@RequestBody List<DataModel> data) {
		logger.debug("Saving data.");
		serv.createData(data);
		return "data records created.";
	}


	/**
	 * Method to fetch data by id.
	 * @param id
	 * @return
	 * @throws IOException 
	 */
	@GetMapping(value= "/getbyid/{data-id}")
	public Optional<DataModel> getById(@PathVariable(value= "data-id") int id) throws IOException {
		logger.debug("Getting data with data-id= {}.", id);
		GetFieldsList gfl = new GetFieldsList();

		Optional<DataModel> dm = serv.findDataById(id);
		gfl.generatePdf(dm, id);
		return serv.findDataById(id);
	}
	
	@RequestMapping(value = "/get-pdf/{data-id}", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity<InputStreamResource> downloadPDFFile(@PathVariable(value= "data-id") int id)
	        throws IOException {
		Optional<DataModel> dm = serv.findDataById(id);
		GetFieldsList gfl = new GetFieldsList();
		
		ByteArrayInputStream in = gfl.generatePdf(dm, id);

	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	    headers.add("Pragma", "no-cache");
	    headers.add("Expires", "0");

	    return ResponseEntity
	            .ok()
	            .headers(headers)
	            .contentType(MediaType.parseMediaType("application/pdf"))
	            .body(new InputStreamResource(in));
	}

	/**
	 * Method to update data by id.
	 * @param id
	 * @param e
	 * @return
	 */
	@PutMapping(value= "/update/{data-id}")
	public String update(@PathVariable(value= "data-id") int id, @RequestBody DataModel e) {
		logger.debug("Updating data with data-id= {}.", id);
		e.setId(id);
		serv.updateData(e);
		return "data record for data-id= " + id + " updated.";
	}

}