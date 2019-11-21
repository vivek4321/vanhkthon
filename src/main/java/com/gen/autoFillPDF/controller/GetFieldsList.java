package com.gen.autoFillPDF.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDNonTerminalField;
import org.apache.pdfbox.pdmodel.interactive.form.PDRadioButton;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.springframework.core.io.ClassPathResource;

import com.gen.autoFillPDF.model.DataModel;

import net.minidev.json.JSONObject;

/**
 * This will take a PDF document and return all the fields from the file.
 * 
 * @author vivsh
 * 
 */
public class GetFieldsList {

	/**
	 * This will print all the fields from the document.
	 * 
	 * @param pdfDocument The PDF to get the fields from.
	 * 
	 * @throws IOException If there is an error getting the fields.
	 */

	private static List<String> processField(PDField field, String sParent, List<String> fieldNames)
			throws IOException {
		String partialName = field.getPartialName();

		if (field instanceof PDNonTerminalField) {
			if (!sParent.equals(field.getPartialName())) {
				if (partialName != null) {
					sParent = sParent + "." + partialName;
				}
			}
			System.out.println(sParent);

			for (PDField child : ((PDNonTerminalField) field).getChildren()) {
//				processField(child, "|  " + sLevel, sParent, fieldNames);
				processField(child, sParent, fieldNames);
			}
		} else {
			String fieldValue = field.getValueAsString();
			StringBuilder outputString = new StringBuilder(" ");
			outputString.append(sParent);
			if (partialName != null) {
				outputString.append(".").append(partialName);
				outputString.append("").append(field.getClass().getName()
						.replace("org.apache.pdfbox.pdmodel.interactive.form.", ""));
				fieldNames.add(outputString.toString().trim());
			}
			outputString.append(" = ").append(fieldValue);
			outputString.append(",  type=").append(field.getClass().getName());
		System.out.println(outputString);
		}
		return fieldNames;
	}

	public static List<String> getFieldsList() {
		ClassPathResource pdfFile = new ClassPathResource("1.pdf");
		List<String> fieldNames = new ArrayList<>();
		try (PDDocument pdfDocument = PDDocument.load(pdfFile.getFile())) {

			PDDocumentCatalog docCatalog = pdfDocument.getDocumentCatalog();
			PDAcroForm acroForm = docCatalog.getAcroForm();
			List<PDField> fields = acroForm.getFields();

			System.out.println(fields.size() + " top-level fields were found on the form");

			PDField field = fields.get(0);
			fieldNames = processField(field, field.getPartialName(), fieldNames);

		} catch (Exception e) {
			System.out.println(e);
		}
		return fieldNames;
	}

	public ByteArrayInputStream generatePdf(Optional<DataModel> dm, int id) throws IOException {
		dm.get().getEMP5624_E0Page10txtF_first_name0PDTextField();
		
//		List<String> fieldNames = getFieldsList();
//		for (String fieldname : fieldNames) {
//			System.out.println(fieldname + "");
//		}
//		String formTemplate = "C:\\Users/vivsh/Downloads/1.pdf";
		ClassPathResource pdfFile = new ClassPathResource("1.pdf");
		

		try (PDDocument pdfDocument = PDDocument.load(pdfFile.getInputStream())) {
			PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();
			List<Object> EMP5624_E = new ArrayList<>();
			List<Object> pages = new ArrayList<>();
			List<String> fields = new ArrayList<String>();
					DataModel dataM = new DataModel();
					
					JSONObject js = dm.get().getFirstPageJson();
					for (String i: js.keySet()) {
					System.out.println(i);
					System.out.println(js.getAsString(i));
					PDField field = acroForm.getField(i);
					System.out.println(field.getFieldType().toString());
					if(field.toString().contains("PDTextField")) {
						field.setValue(js.getAsString(i));
					}else if(field.toString().contains("PDCheckBox")) {
						((PDCheckBox) field).check();
					}else if(field.toString().contains("PDRadioButton")) {
						field.setValue(js.getAsString(i));
					}
				}
			
				
//			if (acroForm != null) {
//				for (String fieldname : fieldNames) {
////					System.out.println("public string"+fieldname.replace("[", "").replace(".", "")
////							.replace("]", "") + "");
//					if(fieldname.contains("PDTextField")) {
//						String pdfKey = fieldname.replace("PDTextField", "");
//					PDTextField field = (PDTextField) acroForm.getField(fieldname.replace("PDTextField", ""));
////					String str = fieldname.replace("[", "").replace(".", "").replace("]", "").replace("PDTextField", "");
//					System.out.println(field.toString()); 
//					field.setValue(js.getAsString(pdfKey));
//					} else if(fieldname.contains("PDCheckBox")) {
//						PDCheckBox field = (PDCheckBox) acroForm.getField(fieldname.replace("PDCheckBox", ""));
////						PDCheckBox field = (PDCheckBox) acroForm.getField(fieldname);
//						System.out.println("public boolean "+fieldname.replace("[", "").replace(".", "")
//								.replace("]", "") + ";");
////						
//					} else if(fieldname.contains("PDRadioButton")) {
////						PDRadioButton field = (PDRadioButton) acroForm.getField(fieldname);
////						field.setValue(fieldname);
//						System.out.println("public string "+fieldname.replace("[", "").replace(".", "")
//								.replace("]", "") + ";");
//					}
//				}
//			}

			// Save and close the filled out form.
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			pdfDocument.setAllSecurityToBeRemoved(true);
			pdfDocument.save(out);
			ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

//			pdfDocument.save("target/FillFormField.pdf");
			return in;
//			pdfDocument.save("C:\\Users/vivsh/Downloads/45.pdf");
		
	}
}
}