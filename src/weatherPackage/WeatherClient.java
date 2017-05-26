package weatherPackage;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.axis.AxisFault;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import gov.weather.graphical.xml.DWMLgen.wsdl.ndfdXML_wsdl.NdfdXMLLocator;
import gov.weather.graphical.xml.DWMLgen.wsdl.ndfdXML_wsdl.NdfdXMLPortType;
import gov.weather.graphical.xml.DWMLgen.wsdl.ndfdXML_wsdl.NdfdXMLPortTypeProxy;
import gov.weather.graphical.xml.DWMLgen.wsdl.ndfdXML_wsdl.WeatherParametersType;
/*
 * Name : Vyankatesh Manohar Kulkarni
 * Student Id: 1001409017.
 * Lab 3 : A Weather Client Using XML
 * */

//Client class 
public class WeatherClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NdfdXMLLocator locator = new NdfdXMLLocator();//Service locator object
		try {		
			QName ServiceName =locator.getServiceName();//Get service name from locator
			JOptionPane.showMessageDialog(null, "Service name is "+ ServiceName );//print service name on JPanel
			String WSDServiceName = locator.getndfdXMLPortWSDDServiceName();
			JOptionPane.showMessageDialog(null, "WSDServiceName is "+ WSDServiceName );
			NdfdXMLPortType port = locator.getndfdXMLPort();//Get XML port 
			JOptionPane.showMessageDialog(null, "Stub is "+ port );//print XML port
			NdfdXMLPortTypeProxy portProxy = new NdfdXMLPortTypeProxy();//Create proxy object
			NdfdXMLPortType portType = portProxy.getNdfdXMLPortType();//Create port type object
			JOptionPane.showMessageDialog(null, "Port type is "+ portType );//print port type on JPannel
			
	
			/*
			 * Get inputs Longitude and Latitude from user
			 * */
	    	Scanner scanner = new Scanner(System.in);     	
	    	System.out.print("Enter the value of Latitude:");
	    	BigDecimal latitude=(BigDecimal)scanner.nextBigDecimal();//Convert the input into BigDecimal
	    	System.out.print("Enter the value of Longitude:");
	    	BigDecimal longitude=(BigDecimal)scanner.nextBigDecimal();
	    	System.out.println("Latitude:"+latitude+" Longitude:"+longitude);//Convert the input into BigDecimal
	    	
	    	WeatherParametersType wtp= new WeatherParametersType();//Create weatherParameters object
	    	/*
	    	 * Set variables for getting weather details
	    	 * */
	    	wtp.setMaxt(true);//Set Maximum temperature true
			wtp.setMint(true);//Set Minimum temperature true
			wtp.setDew(true);//Set Dew Point temperature true
	    	wtp.setPop12(true);//Set Probability Of Precipitation true
			wtp.setWdir(true);//Set Wind Direction true
			
			String productType="time-series";//Set product type as Time Series
			String unit ="e";//Set unit type as e
			Calendar  time = new GregorianCalendar(2017,05,01);//Set Date
			time.setTime(new Date());
			
			//Call method NDFDgen from proxy class of the service with input variables Latitude Longitude Product type, Time, Unit, Weather Parameters.
			String xml = portProxy.NDFDgen(latitude, longitude, productType,time, time, unit , wtp);//Store the result in String variable 
	    	//System.out.println(xml);
	    	
			/*
			 * The javax.xml.Parsers.DocumentBuilderFactory class defines a factory API that enables applications to obtain a parser that produces DOM object trees from XML documents.
			 * */
	    	Document document = stringToDOM(xml);//Call to the method stringDOM with xml as input and store the returned value in Document object
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        factory.setNamespaceAware(true);//method specifies that the parsers created by this factory will provide support for XML namespaces
	      
	        /*
	         * The javax.xml.xpath.XPathFactory class instance can be used to create XPath objects
	         * */
            XPathFactory xpathFactory = XPathFactory.newInstance();//Create XPath factory object
            XPath path = xpathFactory.newXPath();//Set new path
           
            System.out.println("Weather Forcast is given below:");
            
            String maximumTemperature = getMaximumTemperature(document, path);//Call to get maximum temperature method
            System.out.println("Maximum temperature is:  " + maximumTemperature);//Print the maximum temperature
            
            String minimumTemperature = getMinimumTemperature(document, path);//Call to get minimum temperature method
            System.out.println("Minimum temperature is:  " + minimumTemperature);//Print the minimum temperature
            
            String dewPointTemperature = getDewPointTemperature(document, path);//Call to get dew point temperature method
            System.out.println("Dew Point Temperature is: " + dewPointTemperature);//Print the dew point temperature
            
            String probabilityOfPrecipitation = getProbabilityofPrecipitation(document, path);//Call to get probability of precipitation method
            System.out.println("12 Hour Probability of precepitation is: " + probabilityOfPrecipitation);//Print the probability of precipitation
            
            String waved = getwavedirection(document, path);//Call to get wave direction method
            System.out.println("Wave Direction is: " + waved);//Print the wave direction 
            
			/*
			NdfdXMLPortType port = locator.getndfdXMLPort();
			WeatherParametersType wtp= new WeatherParametersType();
			wtp.setTemp(true);
			BigInteger bi = BigInteger.valueOf(1);
			double lat=35.4 ;
			double lng=-97.6;
			BigDecimal latitude=new BigDecimal(lat);
			BigDecimal longitude=new BigDecimal(lng);
			@SuppressWarnings("deprecation")
			Date startDate= new Date(2017,01,10);
			int numofdays=6;
			BigInteger numOfDays = new BigInteger(String.valueOf(numofdays));
			String unit="e";
			String format="12 hourly";
			NdfdXMLPortTypeProxy portProxy = new NdfdXMLPortTypeProxy();
			String listLatLon = "35.4,-97.6";
			//String result = portProxy.NDFDgenByDay(latitude, longitude, startDate, numOfDays, unit, format);
			//System.out.println(result);*/	      	
		} catch (AxisFault e) {

			JOptionPane.showMessageDialog(null, "Axis Fault Exception " + e.getMessage());//Axis Fault Exception
		} catch (RemoteException e) {
					
			JOptionPane.showMessageDialog(null, "Remote Exception " + e.getMessage());//Remote Exception
		} catch (SAXException e) {
			
			JOptionPane.showMessageDialog(null, "SAX Exception " + e.getMessage());//SAX Exception
		} catch (IOException e) {
	
			JOptionPane.showMessageDialog(null, "IO Exception " + e.getMessage());//IO Exception
		} catch (ParserConfigurationException e) {

			JOptionPane.showMessageDialog(null, "Parser Exception " + e.getMessage());//Parser Exception
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(null, "Service Exception " + e.getMessage());//Service Exception
		} 
	}//end main
	
	
		//DOM parse method to convert string xml to Document object.Input type: String type Return Type: Document
		public static Document stringToDOM(String xml) throws SAXException, IOException, ParserConfigurationException 
		{
			DocumentBuilderFactory documentBuilderfactory = DocumentBuilderFactory.newInstance();// create object of DocumentBuilderFactory
			
			//The javax.xml.Parsers.DocumentBuilder class defines the API to obtain DOM Document instances from an XML document.
			DocumentBuilder docbuilder = documentBuilderfactory.newDocumentBuilder();// create object of document builder with Document builder giving documentBuilderfactory as input
			return docbuilder.parse(new InputSource(new StringReader(xml)));//give input string xml to parse function and return the parsed value
		}
		
		/*
		 * XPath uses a path expression to select node or a list of nodes from an XML document
		 * */
		//Method to convert maximum temperature value from Document in the xml file.
		private static String getMaximumTemperature(Document doc, XPath xpath) {
			String temp = null;
			try {
				//Execute the query to get maximum temperature with temperature tag in the xml with type maximum
	            XPathExpression maximumtemperature = xpath.compile("/dwml/data/parameters/temperature[@type='maximum']/value/text()");
	            //Store the value of string type result of evaluate method of XPathExpression
	            temp = (String) maximumtemperature.evaluate(doc, XPathConstants.STRING);
	        } catch (XPathExpressionException e) {
	            //catch XPath expression exception
	            JOptionPane.showMessageDialog(null, "XPathExpressionException Exception " + e.getMessage());//XPathExpressionException Exception
	        }

	        return temp;//return the maximum temperature value
	    }

		//Method to convert minimum temperature value from Document in the xml file.
	 	private static String getMinimumTemperature(Document doc, XPath xpath) {
	        String temp = null;
	        try {
	        	//Execute the query to get minimum temperature with temperature tag in the xml with type minimum
	            XPathExpression minimumtemperature = xpath.compile("/dwml/data/parameters/temperature[@type='minimum']/value/text()");
	            //Store the value of string type result of evaluate method of XPathExpression
	            temp = (String) minimumtemperature.evaluate(doc, XPathConstants.STRING);
	        } catch (XPathExpressionException e) {
	        	//catch XPath expression exception
	            JOptionPane.showMessageDialog(null, "XPathExpressionException Exception " + e.getMessage());//XPathExpressionException Exception
	        }

	        return temp;//return the minimum temperature value
	    }
	    
	 	//Method to convert dew point temperature value from Document in the xml file.
	    private static String getDewPointTemperature(Document doc, XPath xpath) {
	        String temp = null;
	        try {
	        	//Execute the query to get dew point temperature with temperature tag in the xml with type dew point
	            XPathExpression dewPointTemperature = xpath.compile("/dwml/data/parameters/temperature[@type='dew point']/value/text()");
	            //Store the value of string type result of evaluate method of XPathExpression
	            temp = (String) dewPointTemperature.evaluate(doc, XPathConstants.STRING);
	        } catch (XPathExpressionException e) {
	        	//catch XPath expression exception
	            JOptionPane.showMessageDialog(null, "XPathExpressionException Exception " + e.getMessage());//XPathExpressionException Exception
	        }

	        return temp;//return the dew point temperature value
	    }
	    
	    //Method to convert probability Of Precipitation value from Document in the xml file.
	    private static String getProbabilityofPrecipitation(Document doc, XPath xpath) {
		 	String temp = null;
	        try {
	        	//Execute the query to get dew point temperature with probability-of-precipitation tag in the xml
	            XPathExpression probabilityOfPrecipitation = xpath.compile("/dwml/data/parameters/probability-of-precipitation/value/text()");
	          //Store the value of string type result of evaluate method of XPathExpression
	            temp = (String) probabilityOfPrecipitation.evaluate(doc, XPathConstants.STRING);
	        } catch (XPathExpressionException e) {
	        	//catch XPath expression exception
	            JOptionPane.showMessageDialog(null, "XPathExpressionException Exception " + e.getMessage());//XPathExpressionException Exception
	        }

	        return temp;//return the probability of precipitation value
	    }
	    
	  //Method to convert wave direction value from Document in the xml file.
	    private static String getwavedirection(Document doc, XPath xpath) {
	        String temp = null;
	        try {
	        	//Execute the query to get dew point temperature with direction tag in the xml
	            XPathExpression wavedirection = xpath.compile("/dwml/data/parameters/direction/value/text()");
	            //Store the value of string type result of evaluate method of XPathExpression
	            temp = (String) wavedirection.evaluate(doc, XPathConstants.STRING);
	        } catch (XPathExpressionException e) {
	        	//catch XPath expression exception
	            JOptionPane.showMessageDialog(null, "XPathExpressionException Exception " + e.getMessage());//XPathExpressionException Exception
	        }

	        return temp;//return the wave direction value
	    }	
}//end Class