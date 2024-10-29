package com.example.passportStatusTrackingSystem.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.awt.Color;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.example.passportStatusTrackingSystem.model.Applicant_details;
import com.example.passportStatusTrackingSystem.repository.Applicant_detailsRepository;

@Service
@Transactional
public class Applicant_detailsService {
	 @Autowired
	    private Applicant_detailsRepository repo;
	     
	    public List<Applicant_details> listAll() {
	        return repo.findAll();
	    }
	     
	    public void save(Applicant_details applicant_details) {
	        repo.save(applicant_details);
	    }
	    
	    public List<Applicant_details> listStatus(int status) {
	        return repo.findByCurrentDateAndStatus(status);
	    }
	     
	    public Applicant_details get(long application_id) {
	        return repo.findById(application_id).get();
	    }
	     
	    public void delete(long application_id) {
	        repo.deleteById(application_id);
	    }
	    
	    public Applicant_details update(long applicationId)
	    {
	    	Applicant_details applicant_details=repo.findById(applicationId).get();
	    	applicant_details.setFlag(2);
	    	repo.save(applicant_details);
	    	return applicant_details;
	    	
	    }
	    
	    public Applicant_details updatePolice(long applicationId)
	    {
	    	long millis=System.currentTimeMillis();  
	    	java.sql.Date  currentDate=new java.sql.Date(millis);
	    	
	    	LocalDate localDate = currentDate.toLocalDate();
	    	int day=localDate.getDayOfMonth();
	    	int months=localDate.getMonthValue();
	    	int years=localDate.getYear()+10;
	    	
	    	java.sql.Date endDate = java.sql.Date.valueOf( years+"-"+months+"-"+day );
	    	
	    	Applicant_details applicant_details=repo.findById(applicationId).get();
	    	applicant_details.setFlag(3);
	    	applicant_details.setStart_date(new java.sql.Date(millis));
	    	applicant_details.setEnd_date(endDate);
	    	applicant_details.setPassport_id(applicant_details.getApplication_id()+100);
	    	
	    	repo.save(applicant_details);
	    	return applicant_details;
	    }
	    
	    public void deletePoRequest(long applicationId)
	    {
	    	Applicant_details applicant_details=repo.findById(applicationId).get();
	    	applicant_details.setFlag(4);
	    	//applicant_details.setDescription(description);
	    	repo.save(applicant_details);
	    }
	    
	    public void deletePoliceRequest(long applicationId)
	    {
	    	Applicant_details applicant_details=repo.findById(applicationId).get();
	    	applicant_details.setFlag(5);
	    	//applicant_details.setDescription(description);
	    	repo.save(applicant_details);
	    }
	    
	    public Applicant_details trackDetails(long applicationId,Date dob)
	    {
	    	Applicant_details applicant_details=repo.findByIdAndDob(applicationId,dob);
	    	return applicant_details;
	    }
	    
	    public Applicant_details findByEmailId(String emailId)
	    {
	    	return repo.findByEmailId(emailId);
	    }
	    
	    public Applicant_details findByAadhar(long aadhar_id)
	    {
	    	return repo.findByAadharId(aadhar_id);
	    }
	    
	    public Applicant_details findByPassportId(long passportId)
	    {
	    	return repo.findByPassportId(passportId);
	    }
	    
	    public void pdfCreation(long applicantId)
		{
			  String filepath="C:\\Deveopment_avecto\\"+applicantId+"simple.pdf";
			  Applicant_details applicant_details_pdf=repo.findById(applicantId).get();
			  String passportID=Long.toString(applicant_details_pdf.getPassport_id());
			  String F_Name=applicant_details_pdf.getFirst_name(); 
			  String  L_name=applicant_details_pdf.getLast_name(); 
			  Date  DOB=applicant_details_pdf.getDob(); 
			  SimpleDateFormat formatter = new  SimpleDateFormat("MM/dd/yyyy");
			  String Dob = formatter.format(DOB); 
			  String  Gender=applicant_details_pdf.getGender(); 
			  String  Address=applicant_details_pdf.getAddress(); 
			  Date   Strt_Date=applicant_details_pdf.getStart_date(); 
			  String Start_date =  formatter.format(Strt_Date); 
			  Date End_Date=applicant_details_pdf.getEnd_date(); 
			  String end_date = formatter.format(End_Date);
			  try {
				PDDocument document = new PDDocument();
				PDPage page = new PDPage();
				document.addPage(page);
				PDPageContentStream contentStream = new PDPageContentStream(document, page);
			    contentStream.beginText();  
			    contentStream.setFont(PDType1Font.TIMES_BOLD_ITALIC, 14);  
			    contentStream.setLeading(14.5f); 
			    contentStream.setNonStrokingColor(Color.BLUE);
			    contentStream.newLineAtOffset(150, 700);  
			    contentStream.showText("REPUBLIC OF INDIA"); 
			    contentStream.newLine();   
		        contentStream.newLine();
		        contentStream.showText("PASSPORT");
		        contentStream.setFont(PDType1Font.TIMES_ROMAN, 11);  
				contentStream.setLeading(14.5f); 
				contentStream.setNonStrokingColor(Color.BLACK);
				contentStream.newLine();
				contentStream.newLine();
		        contentStream.showText("Passport NO: "+passportID);  
		        contentStream.newLine();  
		        contentStream.newLine();  
		        contentStream.showText("First Name: "+F_Name+"        Last Name: "+L_name);
		        contentStream.newLine(); 
		        contentStream.newLine();  
		        contentStream.showText("Date Of Birth: "+Dob+"        Gender: "+Gender);
		        contentStream.newLine();  
		        contentStream.newLine();  
		        contentStream.showText("Address: "+Address);
		        contentStream.newLine();  
		        contentStream.newLine();  
		        contentStream.showText("Date Of Issue: "+Start_date+"        Date Of Expiry: "+end_date);  
		        contentStream.endText();  
		          
		        System.out.println("Multiple Text Content is added in the PDF Document.");  
				contentStream.close();

				document.save(filepath);
				document.close();
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}	


}
