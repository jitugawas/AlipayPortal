package com.dynamicpayment.paymentexpress;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class DPSRequestBean {
    Integer id = 0;

    String pxPayUserId = "";
    
    String merchantId = "";
   
    String requestTime = "";

	String ipAddress = "";  

    String softDelete = "N";
  
    String pxPayKey = "";
    
    String amountInput = "";
    
    String billingId = "";
    
    String currencyInput = "";
    
    String emailAddress = "";
    
    Integer enableAddBillCard = 0;
    
    String merchantReference = "";

    String opt = "";
    
    String txnData1 = "";
    
    String txnData2 = "";

    String txnData3 = "";
    
    String txnId = "";

    String txnType = "";

    String urlFail = "";

    String urlSuccess = "";
    
    String Xml;
	  
    public DPSRequestBean() {
		  
	  }
    
	  private void buildXml()
	  {
		  
		  try
			  {
			  
			  DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			  Document doc = docBuilder.newDocument();
	
			  Element root = doc.createElement("GenerateRequest");
			  doc.appendChild(root);
			  
	
			  	Element child;
			  	Text text;
			  	
			  	child = doc.createElement("PxPayUserId");
			  	root.appendChild(child);	  	
			  	text = doc.createTextNode(this.getPxPayUserId());
			  	child.appendChild(text);	  		  	
			  	
			  	System.out.println(this.getPxPayUserId());
			  	
			  	child = doc.createElement("PxPayKey");
			  	root.appendChild(child);	  	
			  	text = doc.createTextNode(this.getPxPayKey());
			  	child.appendChild(text);	  	
		  	
				System.out.println(this.getPxPayKey());
				
			  	child = doc.createElement("AmountInput");
			  	root.appendChild(child);	  	
			  	text = doc.createTextNode(this.getAmountInput());
			  	child.appendChild(text);
	
				System.out.println(this.getAmountInput());
				
			  	child = doc.createElement("BillingId");
			  	root.appendChild(child);		  	
			  	text = doc.createTextNode(this.getBillingId());
			  	child.appendChild(text);	  	
	
				System.out.println(this.getBillingId());
				
			  	child = doc.createElement("CurrencyInput");
			  	root.appendChild(child);		  	
			  	text = doc.createTextNode(this.getCurrencyInput());
			  	child.appendChild(text);	  	
	
				System.out.println(this.getCurrencyInput());
				
			  	child = doc.createElement("EmailAddress");	
			  	root.appendChild(child);		  	
			  	text = doc.createTextNode(this.getEmailAddress());
			  	child.appendChild(text);	  	
				System.out.println(this.getEmailAddress());
				
			  	child = doc.createElement("EnableAddBillCard");	
			  	root.appendChild(child);		  	
			  	text = doc.createTextNode(""+this.getEnableAddBillCard());
			  	child.appendChild(text);	  	
				System.out.println(this.getEnableAddBillCard());
				
			  	child = doc.createElement("MerchantReference");
			  	root.appendChild(child);		  	
			  	text = doc.createTextNode(this.getMerchantReference());
			  	child.appendChild(text);	
				System.out.println(this.getMerchantReference());
				
			  	child = doc.createElement("Opt");
			  	root.appendChild(child);		  	
			  	text = doc.createTextNode(this.getOpt());
			  	child.appendChild(text);	
				System.out.println(this.getOpt());
				
			  	child = doc.createElement("TxnData1");
			  	root.appendChild(child);		  	
			  	text = doc.createTextNode(this.getTxnData1());
			  	child.appendChild(text);	
				System.out.println(this.getTxnData1());
				
			  	child = doc.createElement("TxnData2");
			  	root.appendChild(child);		  	
			  	text = doc.createTextNode(this.getTxnData2());
			  	child.appendChild(text);	
				System.out.println(this.getTxnData2());
				
			  	child = doc.createElement("TxnData3");
			  	root.appendChild(child);		  	
			  	text = doc.createTextNode(this.getTxnData3());
			  	child.appendChild(text);	
				System.out.println(this.getTxnData3());
				
			  	child = doc.createElement("TxnId");
			  	root.appendChild(child);		  	
			  	text = doc.createTextNode(this.getTxnId());
			  	child.appendChild(text);	
				System.out.println(this.getTxnId());
				
			  	child = doc.createElement("TxnType");
			  	root.appendChild(child);		  	
			  	text = doc.createTextNode(this.getTxnType());
			  	child.appendChild(text);
				System.out.println(this.getTxnType());
				
			  	child = doc.createElement("UrlFail");
			  	root.appendChild(child);		  	
			  	text = doc.createTextNode(this.getUrlFail());
			  	child.appendChild(text);
				System.out.println(this.getUrlFail());
				
			  	child = doc.createElement("UrlSuccess");
			  	root.appendChild(child);		  	
			  	text = doc.createTextNode(this.getUrlSuccess());
			  	child.appendChild(text);	
				System.out.println(this.getUrlSuccess());
				
	
	
			  /////////////////
			  //Output the XML
	
			  //set up a transformer
			  TransformerFactory transfac = TransformerFactory.newInstance();
			  Transformer trans = transfac.newTransformer();
			  trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			  trans.setOutputProperty(OutputKeys.INDENT, "yes");
	
			  //create string from xml tree
			  StringWriter sw = new StringWriter();
			  StreamResult result = new StreamResult(sw);
			  DOMSource source = new DOMSource(doc);
			  System.out.println("generated doc:"+doc.toString());
			  trans.transform(source, result);
			  
			  System.out.println("generated srintg:"+sw.toString());
			  
			  this.setXml(sw.toString());
		  }
		  catch (Exception e)
		  {
			  e.printStackTrace();
		  }
	}
	  
    public String getPxPayUserId() {
		return pxPayUserId;
	}

	public void setPxPayUserId(String pxPayUserId) {
		this.pxPayUserId = pxPayUserId;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getSoftDelete() {
		return softDelete;
	}

	public void setSoftDelete(String softDelete) {
		this.softDelete = softDelete;
	}

	public String getPxPayKey() {
		return pxPayKey;
	}

	public void setPxPayKey(String pxPayKey) {
		this.pxPayKey = pxPayKey;
	}

	public String getAmountInput() {
		return amountInput;
	}

	public void setAmountInput(String amountInput) {
		this.amountInput = amountInput;
	}

	public String getBillingId() {
		return billingId;
	}

	public void setBillingId(String billingId) {
		this.billingId = billingId;
	}

	public String getCurrencyInput() {
		return currencyInput;
	}

	public void setCurrencyInput(String currencyInput) {
		this.currencyInput = currencyInput;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Integer getEnableAddBillCard() {
		return enableAddBillCard;
	}

	public void setEnableAddBillCard(Integer enableAddBillCard) {
		this.enableAddBillCard = enableAddBillCard;
	}

	public String getMerchantReference() {
		return merchantReference;
	}

	public void setMerchantReference(String merchantReference) {
		this.merchantReference = merchantReference;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	public String getTxnData1() {
		return txnData1;
	}

	public void setTxnData1(String txnData1) {
		this.txnData1 = txnData1;
	}

	public String getTxnData2() {
		return txnData2;
	}

	public void setTxnData2(String txnData2) {
		this.txnData2 = txnData2;
	}

	public String getTxnData3() {
		return txnData3;
	}

	public void setTxnData3(String txnData3) {
		this.txnData3 = txnData3;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getUrlFail() {
		return urlFail;
	}

	public void setUrlFail(String urlFail) {
		this.urlFail = urlFail;
	}

	public String getUrlSuccess() {
		return urlSuccess;
	}

	public void setUrlSuccess(String urlSuccess) {
		this.urlSuccess = urlSuccess;
	}

	public String getXml() {
		this.buildXml();
		return this.Xml;
	}

	public void setXml(String xml) {
		this.Xml = xml;
	}

   

}
