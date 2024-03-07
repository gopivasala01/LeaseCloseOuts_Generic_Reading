package DataReader;

import java.io.File;
import java.io.FileInputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.RunnerClass;

public class ReadingLeaseAgreements {
	
	public static String text="";
	public static String commencementDate ="";
	public static String expirationDate ="";
	public static String proratedRentDate="";

	public static void dataRead() throws Exception{
		
		File file = RunnerClass.getLastModified();
		//File file = new File("C:\\SantoshMurthyP\\Lease Audit Automation\\Lease_02.22_02.23_200_Doc_Johns_Dr_ATX_Smith (3).pdf");
		FileInputStream fis = new FileInputStream(file);
		PDDocument document = PDDocument.load(fis);
	    text = new PDFTextStripper().getText(document);
	    text = text.replaceAll(System.lineSeparator(), " ");
	    text = text.trim().replaceAll(" +", " ");
	    text = text.toLowerCase();
	    //System.out.println(text);
	    System.out.println("------------------------------------------------------------------");
		
		commencementDate = dataExtractionClass.getDatesModified(text,"term:^shall commence on@term:^commencement date:@term^commences on");
	    
		//commencementDate = LeaseAgreementDownloadandGetData.getDates(getDataOf("commencementDateFromPDF"));
	   System.out.println("Start date = "+ commencementDate);
	   expirationDate = dataExtractionClass.getDatesModified(text,"term:^location of the premises\\) on@term:^expiration date:@term^expires on");
	   System.out.println("End date = "+ expirationDate);
	   proratedRentDate = dataExtractionClass.getDatesModified(text,"rent:^prorated rent\\, on or before@rent:^Prorated Rent: On or before");
	   System.out.println("Prorated Rent Date = "+ proratedRentDate);
	}
	
}
