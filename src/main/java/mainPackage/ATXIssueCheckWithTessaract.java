package mainPackage;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import net.sourceforge.tess4j.Tesseract;

public class ATXIssueCheckWithTessaract {

	public static void main(String[] args) throws Exception 
	{
		File newFile = new File ("F:\\Tessaract Files\\Lease_1023_1024_1300_W_Barrett_St_ATX_Wells (1).pdf");
		pdfScreenShot(newFile);
	}
		public static String pdfScreenShot(File newFile) throws Exception 
		{
			
			try
			{
			//File newFile = new File ("C:\\SantoshMurthyP\\Lease Audit Automation\\Lease_923_924_619_W_Sabine_ATX_Cloteaux.pdf");
			 //File newFile = RunnerClass.getLastModified();
			 PDDocument pdfDocument = PDDocument.load(newFile);
			 PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
			 String targetText1 = "x Option 1: Waive Renters Insurance"; //Tenant will pay Landlord monthly rent in the amount of";
			 String targetText1_2 = "X] Option 1: Waive Renters Insurance";
			 String targetText2 = "x Option 2: Purchase a Renters Insurance Policy";
			
			 String targetText1_NotSelectedWith0 = "O] Option 1: Waive Renters Insurance";
			 String targetText2_NotSelectedWith0 = "O] Option 1: Waive Renters Insurance";
			 
			// String targetText2 = "(X)) monthly installments,"; //on or before the 1° day of each month, in the amount";
			 //Rectangle textCoordinates = textStripper.getTextBounds("monthly installments, Tenant will pay Landlord monthly rent in the amount of");
			
			 for (int page = 15; page < pdfDocument.getNumberOfPages(); ++page) {
					 BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
			         // Crop the image based on the specified coordinates
			        // BufferedImage croppedImage = bim.getSubimage(x, y, width, height);
			         File outputFile = new File("C:\\SantoshMurthyP\\Tessaract Images\\Image.jpeg");
			         ImageIO.write(bim, "jpeg", outputFile);
			        // System.out.println( "Image has been extracted successfully");
					  
				     Tesseract tesseract = new Tesseract();

					 tesseract.setDatapath("F:\\Eclipse Workspace\\Gopi\\Lease-Close-Outs-2.0\\tessdata");

					 //image.setLanguage(“eng”);
					 try {
					   String text= tesseract.doOCR(new File(AppConfig.pdfImage+"Image.jpeg"));
					   System.out.print(text);
					   String text2 = text.toString();
					   //text2 = text2.replaceAll(System.lineSeparator(), " ");
					   text2 = text2.replaceAll("[\\t\\n\\r]+"," ");
					   boolean checkIfOption2IsSelectedWhenXMarkIsSomewhereBetweenOption1AndOption2 = false;
					   if(text2.contains("Option 2:"))
					   {
						   String textBetweenOption1AndOption2[] = text2.substring(0,text.indexOf("Option 2:")).split(" ");
						   for(int i=125;i<textBetweenOption1AndOption2.length;i++) //String a : textBetweenOption1AndOption2
						   {
							   String a = textBetweenOption1AndOption2[i];
							   System.out.println(textBetweenOption1AndOption2[i]);
							   if(a.trim().equals("x")||a.trim().equals("X"))
							   {
								   checkIfOption2IsSelectedWhenXMarkIsSomewhereBetweenOption1AndOption2 = true;
								   break;
							   }
						   }
						   
					   }
					   String lines[] = text.split("\\r?\\n");
					   for(int i=0;i<lines.length;i++)
					   {
						   String textBeforeOption1 ="";
						   String textBeforeOption2 ="";
						   String line = lines[i].trim();
						   if(line.contains("Option 1:"))
						   textBeforeOption1 = line.trim().substring(0, line.trim().indexOf("Option 1:"));
						   else if(line.contains("Option 2:"))
						  textBeforeOption2 = line.trim().substring(0, line.trim().indexOf("Option 2:"));
						   else continue;
						   //Checking Option 1 is selected 
						  if(line.contains("Option 1:")&&!line.startsWith("Option 1:")&&(textBeforeOption1.contains("X")||textBeforeOption1.contains("x")))
                          {
							  System.out.println("Option 1 is selected");
							   return "Option 1";
                          }
						  if(line.contains("Option 2:")&&!line.startsWith("Option 2:")&&(!textBeforeOption2.contains("X")||!textBeforeOption2.contains("x")))
                          {
							  System.out.println("Option 1 is selected");
							   return "Option 1";
                          }
						//Checking Option 2 is selected 
						  if(line.contains("Option 2:")&&!line.startsWith("Option 2:")&&(textBeforeOption2.contains("X")||textBeforeOption2.contains("x")))
                          {
							  System.out.println("Option 2 is selected");
							   return "Option 2";
                          }
						  if(line.contains("Option 1:")&&!line.startsWith("Option 1:")&&(!textBeforeOption1.contains("X")||!textBeforeOption1.contains("x")))
                          {
							  System.out.println("Option 2 is selected");
							   return "Option 2";
                          }
						  if(checkIfOption2IsSelectedWhenXMarkIsSomewhereBetweenOption1AndOption2==true)
						  {
							  System.out.println("Option 2 is selected");
							   return "Option 2";
						  }
						  
						   /*
						   if((line.trim().contains("Option 1:")&&!line.trim().startsWith("Option 1:")&&(!textBeforeOption1.contains("X")||textBeforeOption1.contains("x")))||(textBeforeOption2.contains("x")||textBeforeOption2.contains("X"))||checkIfOption2IsSelectedWhenXMarkIsSomewhereBetweenOption1AndOption2==true)
						   {
							   System.out.println("Option 2 is selected");
							   return "Option 2";
						   }
						   if(line.trim().contains("Option 2:")&&!line.trim().startsWith("Option 2:")&&(!textBeforeOption2.contains("X")||textBeforeOption2.contains("x"))||(textBeforeOption1.contains("x")||textBeforeOption1.contains("X")))
						   {
							   System.out.println("Option 1 is selected");
							   return "Option 1";
						   }
						   
						   */
					   }
					   /*
					   if(text.contains(targetText1)||text.contains(targetText1_2)||text.contains(targetText2_NotSelectedWith0)) { //|| text.contains(targetText2)
						   System.out.println("Option 1 is selected");
						   return "Option 1";
					   }
					   else
						   if(text.contains(targetText2)||text.contains(targetText1_NotSelectedWith0)) { //|| text.contains(targetText2)
							   System.out.println("Option 2 is selected");
							   return "Option 2";
						   }
					   }*/
					 }
					 catch(Exception e) 
					 {
						 return "Error";
					    //System.out.println("Exception "+e);
					   }
					      
		        }
			 // Closing the PDF document
		        pdfDocument.close();
			}
			catch(Exception e)
			{
				return "Error";
			}
			return "Error";
			
			       
		 }


}
