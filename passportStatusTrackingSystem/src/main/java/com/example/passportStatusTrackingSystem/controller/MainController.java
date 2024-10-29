package com.example.passportStatusTrackingSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.example.passportStatusTrackingSystem.model.Applicant_details;
import com.example.passportStatusTrackingSystem.model.Login_details;
import com.example.passportStatusTrackingSystem.model.Mail;
import com.example.passportStatusTrackingSystem.service.Applicant_detailsService;
import com.example.passportStatusTrackingSystem.service.Login_detailsService;
import com.example.passportStatusTrackingSystem.service.MailService;

@Controller
public class MainController {

	int randomPIN=0;
	@Autowired
    private Applicant_detailsService service;
	
	@Autowired
	private Login_detailsService service1;
	
	@Autowired
	private MailService mailService;
	
	public static long passport_number=910000000000L;

	String emailId,password;
	long applicationId;
	
//************************************************************************************
	
  @RequestMapping(value = "/login")
  public String homeScreen() {
      return "login";
  }
  
  @RequestMapping(value = "/loginAdmin")
   public String loginAdmin() {
       return "loginAdmin";
   }
  
  @RequestMapping(value = "/welcomeAdmin")
   public String welcomeAdmin() {
       return "welcomeAdmin";
   }
  
  @RequestMapping(value = "/viewApplicant")
   public String poApproval(Model model) {
	  List<Applicant_details> listApplicant = service.listStatus(1);
	    model.addAttribute("listApplicant", listApplicant);
	    String description=null;
	    model.addAttribute("description",description);
       return "viewApplicant";
   }
  
  @RequestMapping(value = "/viewApplicant2")
   public String policeApproval(Model model) {
	  List<Applicant_details> listApplicant = service.listStatus(2);
	    model.addAttribute("listApplicant", listApplicant);
	    model.addAttribute("number", 3);
	  
       return "viewApplicant2";
   }
  
  @RequestMapping(value = "/welcomeApplicant")
   public String welcomeApplicant() {
       return "welcomeApplicant";  
   }
  
  @RequestMapping(value = "/existingApplicantLogin")
   public String existingApplicationLogin(Model model) {
   	
	  Login_details login_details=new Login_details();
	  model.addAttribute("login_details", login_details);
	  
       return "existingApplicantLogin";
       
   }

  @PostMapping(value = "/trackStatusTable")
   public String trackStatusTable(Model model,@ModelAttribute("applicant_details") Applicant_details applicant_details) {
   	
	  Applicant_details applicant_details1=service.trackDetails(applicant_details.getApplication_id(),applicant_details.getDob());
	  int flag1 = applicant_details1.getFlag();
	  try
	  {		  
			 if(flag1 == 1)
			 {
				 flag1 = 2;
			 } else if(flag1 == 2)
				 {
					 flag1 = 3;
				 }
					 else if(flag1 == 3)
					  	  {
					  		  flag1=6;
					  	  }else if(flag1 == 4)
					  	  		{
					  		  		flag1 = 2;
					  	  		}else if(flag1 == 5)
					  	  			{
					  	  				flag1=3;
					  	  			}
		  		
		  	  applicant_details1.getApplication_id();
			  model.addAttribute("applicant_details", applicant_details1);
			  model.addAttribute("flag", flag1);
			  return "trackStatusTable";
		  
	  }catch(NullPointerException e)
	  {
		  System.out.println("Null Pointer exception");
		  return "redirect:/loginTrackStatus";
	  }
   }
  //th:href="@{/reject/{applicationId}/police(applicationId=${applicant_details.application_id})}"
  
  @RequestMapping(value = "/newapplicant")
   public String newapplicant(Model model) {
   	
	  Applicant_details applicant_details = new Applicant_details();
	    model.addAttribute("applicant_details", applicant_details);
       return "newapplicant";
       
   }
  
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public String saveProduct(Model model,@ModelAttribute("applicant_details") Applicant_details applicant_details, @ModelAttribute("login_details") Login_details login_details ) {
     String str=null;
	  Applicant_details ad1=service.findByAadhar(applicant_details.getAadhar_no());
	  try
	  {
		  ad1.getAadhar_no();
		  model.addAttribute("logError","logError");
		  str="newapplicant";
	  }
	  catch(NullPointerException e)
	  {
		 
		  applicant_details.setFlag(1);
		  service.save(applicant_details);
		  
		  Mail mail = new Mail();
	      mail.setMailFrom("projecttrainingpassport@gmail.com");
	      mail.setMailTo(applicant_details.getEmail_id());
	      mail.setMailSubject("User Details About Application");
	      mail.setMailContent("Application Id : " +applicant_details.getApplication_id() + "\nAppointment Date : "+ applicant_details.getAppointment_date());

	      mailService.sendEmail(mail);
	      
		  str="login";
		  System.out.println("Null Pointer exception");
	  }
	  
       
      return str;
  }
  
  @RequestMapping(value = "/save1", method = RequestMethod.POST)
  public String saveApplication(Model model,@ModelAttribute("applicant_details") Applicant_details applicant_details ) {
	  
	  Applicant_details ad1=service.findByPassportId(applicant_details.getPassport_id());
	
	
	  try
	  {
		  System.out.println(applicant_details.getPassport_id());
		  System.out.println(ad1.getPassport_id());
		  
		  ad1.setAddress(applicant_details.getAddress());
		  ad1.setEmail_id(applicant_details.getEmail_id());
		  ad1.setMobile_no(applicant_details.getMobile_no());
		  ad1.setAppointment_date(applicant_details.getAppointment_date());
		  ad1.setFlag(1);
		  
		  service.save(ad1);
		  Applicant_details ap=new Applicant_details();
		  model.addAttribute("applicant_details", ap);
	      return "login";
	  }
	  catch(NullPointerException e)
	  {
		  		System.out.println("null pointer exception");
		  		Applicant_details ap=new Applicant_details();
		  		model.addAttribute("applicant_details", ap);
		  		return "renewApplication";
	  }
  }
  
  @RequestMapping(value = "/loginTrackStatus")
   public String loginTrackStatus(Model model) {
	   Applicant_details applicant_details = new Applicant_details();
	   model.addAttribute("applicant_details", applicant_details);
       return "loginTrackStatus";
       
   }
  
  @RequestMapping(value = "/newUserRegister")
   public String newUserRegister(Model model) {
   	
	   Login_details login_details = new Login_details();
	   model.addAttribute("login_details", login_details);
       return "newUserRegister";
       
   }
  
  @RequestMapping(value="/sendPassword", method=RequestMethod.POST)
  public String sendPass(Model model,@ModelAttribute("login_details") Login_details login_details)
  {  
	  Login_details login_details1=service1.findByEmailId(login_details.getEmail_id());
	  try
	  {
		  login_details1.getApplication_id();
		  Mail mail = new Mail();
	      mail.setMailFrom("projecttrainingpassport@gmail.com");
	      mail.setMailTo(login_details.getEmail_id());
	      mail.setMailSubject("User Details About Password");
	      mail.setMailContent("Application Id : " +login_details1.getApplication_id() + "\nPassword : "+ login_details1.getPassword());

	      mailService.sendEmail(mail);
	     
	      Login_details login_details2=new Login_details();
	      model.addAttribute("login_details", login_details2);
	      return "existingApplicantLogin";
		  
	  }
	  catch(NullPointerException e)
	  {
		  System.out.println("Null Pointer Exception");
		  Applicant_details ap=new Applicant_details();
		  model.addAttribute("applicant_details", ap);
		  return "redirect:/emailForgot";
	  }
  }
  
  @RequestMapping(value="/sendOtp", method=RequestMethod.POST)
  public String sendOtp(Model model,@ModelAttribute("login_details") Login_details login_details)
  {  
	  Login_details login_details1=service1.findByEmailId(login_details.getEmail_id());
	  try
	  {
		  randomPIN = (int)(Math.random()*9000)+1000;
		  System.out.println(randomPIN);
		  emailId=login_details1.getEmail_id();
		  password=login_details1.getPassword();
		  applicationId=login_details1.getApplication_id();
		  //login_details1.getApplication_id();
		  Mail mail = new Mail();
	      mail.setMailFrom("projecttrainingpassport@gmail.com");
	      mail.setMailTo(login_details1.getEmail_id());
	      mail.setMailSubject("Account Verification - OTP");
	      mail.setMailContent("Please enter the otp for verification.....\n\n Your One Time Password is : "+randomPIN+" \n\nThank You.....");


	      mailService.sendEmail(mail);
	     int num=0;
	      Applicant_details applicant_details1=new Applicant_details();
	      
	      model.addAttribute("applicant_details", applicant_details1);
	      
	      return "otp2";
		  
	  }
	  catch(NullPointerException e)
	  {
		  System.out.println("Null Pointer Exception");
		  Applicant_details ap=new Applicant_details();
		  model.addAttribute("applicant_details", ap);
		  return "redirect:/emailForgot";
	  }
  }
  
 
  @RequestMapping(value="/renewApplicant", method=RequestMethod.POST)
  public String renewApp(Model model,@ModelAttribute("login_details") Login_details login_details1)
  {
	  Login_details login_details2=service1.findByEmailIdandPassword(login_details1.getEmail_id(),login_details1.getPassword());
	  try
	  {
		  login_details2.getEmail_id();
		  Applicant_details applicant_details=new Applicant_details();
		  model.addAttribute("applicant_details",applicant_details);
		  
		  return "renewApplication";
	  }
	  catch(NullPointerException e)
	  {
		  return "redirect:/existingApplicantLogin";
	  }
  }
  
  @RequestMapping(value="/emailForgot")
  public String forgot(Model model)
  {
		  Login_details ad=new Login_details();
		  model.addAttribute("login_details", ad);
		  return "emailForgot";
  }
  
  @RequestMapping(value = "/save2", method = RequestMethod.POST)
  public String saveNewUser(Model model,@ModelAttribute("login_details") Login_details login_details ) {
	  
		  String str=null;
		  int flag=0;
		  Login_details ld1=service1.findEmail(login_details.getEmail_id());
		  try
		  {
			  ld1.getEmail_id();
			  Login_details ld=new Login_details();
			  model.addAttribute("login_details", ld);
			  str="newUserRegister";
		  }catch(NullPointerException e)
		  {
			  flag=1;
			  System.out.println("Null Pointer exception");
		  }
		  if(flag==1)
		  {
			  randomPIN = (int)(Math.random()*9000)+1000;
			  System.out.println(randomPIN);
			  Mail mail = new Mail();
		      mail.setMailFrom("projecttrainingpassport@gmail.com");
		      mail.setMailTo(login_details.getEmail_id());
		      mail.setMailSubject("Account Verification - OTP");
		      mail.setMailContent("Please enter the otp for verification.....\n\n Your One Time Password is : "+randomPIN+" \n\nThank You.....");
	
		      int status=mailService.sendEmail(mail);
		      if(status==1)
		      {
			      
		    	  service1.save(login_details);
				  Applicant_details ap=new Applicant_details();
				  model.addAttribute("applicant_details", ap);
				  str="otp1";
		      }
		      else {
		    	  Login_details ld=new Login_details();
				  model.addAttribute("login_details", ld);
				  model.addAttribute("logError","logError");
		    	  str="newUserRegister";
		      }
		  }
		  return str;
  }
  
  @PostMapping(value="/compareOTP")
  public String compareOTP(Model model,@ModelAttribute("applicant_details") Applicant_details applicant_details)
  {
		  if(applicant_details.getOtp()==randomPIN)
		  {
			  Applicant_details ad=new Applicant_details();
			  model.addAttribute("applicant_details", ad);
			  return "/newApplicant";
		  }
		  else
		  {
			  Applicant_details ad=new Applicant_details();
			  model.addAttribute("applicant_details", ad);
			  model.addAttribute("logError","logError");
			  return "otp1";
		  }
  }
  
  @PostMapping(value="/compareOTPForgot")
  public String compareOTPForgot(Model model,@ModelAttribute("login_details") Login_details login_details,@ModelAttribute("applicant_details") Applicant_details applicant_details)
  {
		  if(applicant_details.getOtp()==randomPIN)
		  {
			  Mail mail = new Mail();
		      mail.setMailFrom("projecttrainingpassport@gmail.com");
		      mail.setMailTo(emailId);
		      mail.setMailSubject("User Details About Password");
		      mail.setMailContent("Application Id : " +applicationId + "\nPassword : "+ password);

		      mailService.sendEmail(mail);
			  
			  
			  Login_details ad=new Login_details();
			  model.addAttribute("login_details", ad);
			  return "/existingApplicantLogin";
		  }
		  else
		  {
			  Login_details ad=new Login_details();
			  model.addAttribute("login_details", ad);
			  model.addAttribute("logError","logError");
			  return "otp2";
		  }
  }
  
  @GetMapping(value="/approve/{applicationId}/po")
  public String approveRequestPO(Model model,@PathVariable long applicationId,@ModelAttribute("applicant_details") Applicant_details applicant_details)
  {
		  
	  	  Applicant_details ap= service.update(applicationId);
		  Mail mail = new Mail();
	      mail.setMailFrom("projecttrainingpassport@gmail.com");
	      mail.setMailTo(ap.getEmail_id());
	      mail.setMailSubject("Approve Request by Passport Officer");
	      mail.setMailContent("Your Application Approved by Passport Officer.\n\n Application is going to the Police department. \n\nThank You.....");
	      mailService.sendEmail(mail);
	      
		  return "redirect:/viewApplicant";
  }

  @RequestMapping(value={"/reject/{applicationId}/po"})
  public String RejectRequestPORequest(Model model,@PathVariable long applicationId,@ModelAttribute("applicant_details") Applicant_details applicant_details)
  {
		  service.deletePoRequest(applicationId);
		  return "redirect:/viewApplicant";
  }
  
  @GetMapping(value="/approve/{applicationId}/police")
  public String approveRequestPolice(Model model,@PathVariable long applicationId,@ModelAttribute("applicant_details") Applicant_details applicant_details)
  {  
		  Applicant_details ap=service.updatePolice(applicationId);
		  service.pdfCreation(applicationId);
		  Mail mail = new Mail();
	      mail.setMailFrom("projecttrainingpassport@gmail.com");
	      mail.setMailTo(ap.getEmail_id());
	      mail.setMailSubject("Approve Request by Police and E-Passport");
	      mail.setMailContent("Your Application Approved by Police Officer.. \n\nThank You.....");
	      mailService.sendEmail(mail,ap.getApplication_id());
	      
	      return "redirect:/viewApplicant2";
  }
  
  @RequestMapping(value={"/reject/{applicationId}/police"})
  public String RejectRequestPoliceRequest(Model model,@PathVariable long applicationId,@ModelAttribute("applicant_details") Applicant_details applicant_details)
  {
	  service.deletePoliceRequest(applicationId);
	  return "redirect:/viewApplicant2";
  }

}
