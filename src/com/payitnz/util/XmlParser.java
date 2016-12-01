package com.payitnz.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.payitnz.config.DynamicPaymentConstant;
import com.payitnz.model.AlipayAPIResponse;

public class XmlParser {

    public static AlipayAPIResponse parseAlipayPaymentAPIResponse(String xmlInput) {
        AlipayAPIResponse alipayAPIResponse = new AlipayAPIResponse();

        try {
            System.out.println("---parseAlipayPaymentAPIResponse() - xmlInput=" + xmlInput);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new java.io.StringReader(xmlInput)));

            doc.getDocumentElement().normalize();

            Element ele = doc.getDocumentElement();
            NodeList nList = doc.getElementsByTagName("is_success");
            Node node = nList.item(0);
            String isSuccess = node.getFirstChild().getNodeValue();
            alipayAPIResponse.setPgIsSuccess(isSuccess);

            nList = doc.getElementsByTagName("request");

            for (int i = 0; i < nList.getLength(); i++) {
                node = nList.item(i);
                Element eElement = (Element) node;
                NodeList nList1 = eElement.getElementsByTagName("param");
                for (int j = 0; j < nList1.getLength(); j++) {
                    node = nList1.item(j);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement1 = (Element) node;
                        String paramName = ((Element) eElement1).getAttribute("name");
                        String paramValue = eElement1.getTextContent();
                        if ("trans_amount".equalsIgnoreCase(paramName)) {
                            alipayAPIResponse.setPgTransAmountCny(Double.parseDouble(paramValue));
                        }
                        if ("trans_name".equalsIgnoreCase(paramName)) {
                            alipayAPIResponse.setMcItemName(paramValue);
                        }
                        if ("partner_trans_id".equalsIgnoreCase(paramName)) {
                            alipayAPIResponse.setPgPartnerTransId(paramValue);
                        }
                    }
                }
            }
            nList = doc.getElementsByTagName("response");
            for (int i = 0; i < nList.getLength(); i++) {
                node = nList.item(i);
                Element eElement = (Element) node;
                NodeList nList1 = eElement.getElementsByTagName("alipay");
                Element eElement2 = (Element) nList1.item(0);
                if (isSuccess.equalsIgnoreCase("T")) {

                    NodeList nListSuccess = eElement2.getElementsByTagName("result_code");
                    String paramValue = nListSuccess.item(0).getTextContent();
                    alipayAPIResponse.setPgResultCode(paramValue);
                    // check if the result code is
                    if (paramValue != null && paramValue.equalsIgnoreCase(DynamicPaymentConstant.PG_UNKNOW_STATUS)) {
                        //Call the Alipay query API
                        return alipayAPIResponse;
                        
                    } else {
                        nListSuccess = eElement2.getElementsByTagName("alipay_buyer_login_id");
                        if (nListSuccess != null) {
                            if (nListSuccess.item(0) != null) {

                                paramValue = nListSuccess.item(0).getTextContent();
                                alipayAPIResponse.setPgAlipayBuyerLoginId(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("alipay_buyer_user_id");
                                paramValue = nListSuccess.item(0).getTextContent();
                                alipayAPIResponse.setPgAlipayBuyerUserId(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("alipay_pay_time");
                                paramValue = nListSuccess.item(0).getTextContent();
                                alipayAPIResponse.setPgAlipayPayTime(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("alipay_trans_id");
                                paramValue = nListSuccess.item(0).getTextContent();
                                alipayAPIResponse.setPgAlipayTransId(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("currency");
                                paramValue = nListSuccess.item(0).getTextContent();
                                alipayAPIResponse.setMcCurrency(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("exchange_rate");
                                paramValue = nListSuccess.item(0).getTextContent();
                                alipayAPIResponse.setPgExchangeRate(Double.parseDouble(paramValue));

                                nListSuccess = eElement2.getElementsByTagName("partner_trans_id");
                                paramValue = nListSuccess.item(0).getTextContent();
                                alipayAPIResponse.setPgPartnerTransId(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("trans_amount");
                                paramValue = nListSuccess.item(0).getTextContent();
                                alipayAPIResponse.setMcTransAmount(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("trans_amount_cny");
                                paramValue = nListSuccess.item(0).getTextContent();
                                alipayAPIResponse.setPgTransAmountCny(Double.parseDouble(paramValue));
                            } else {
                                NodeList nListFailure = eElement2.getElementsByTagName("error");
                                paramValue = nListFailure.item(0).getTextContent();
                                alipayAPIResponse.setPgError(paramValue);

                                nListFailure = eElement2.getElementsByTagName("result_code");
                                paramValue = nListFailure.item(0).getTextContent();
                                alipayAPIResponse.setPgResultCode(paramValue);
                            }
                        }

                    }

                }
                // request not accepted by Alipay and isSuccess = F
                else {
                    System.out.println("---Request not accepted by Alipay---");
                }

            } // end for loop

            /*nList = doc.getElementsByTagName("sign");
            String sign = nList.item(0).getTextContent();
            System.out.println("sign=" + sign);
            alipayAPIResponse.setPgSign(sign);
            
            nList = doc.getElementsByTagName("sign_type");
            String signType = nList.item(0).getTextContent();
            alipayAPIResponse.setPgSignType(signType);
            System.out.println("signType=" + signType);*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return alipayAPIResponse;

    }

    public static AlipayAPIResponse parseAlipayReversalAPIResponse(String xmlInput) {
        AlipayAPIResponse alipayAPIResponse = new AlipayAPIResponse();

        try {
            System.out.println("---parseAlipayReversalAPIResponse() - xmlInput=" + xmlInput);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new java.io.StringReader(xmlInput)));

            doc.getDocumentElement().normalize();

            Element ele = doc.getDocumentElement();
            NodeList nList = doc.getElementsByTagName("is_success");
            Node node = nList.item(0);
            String isSuccess = node.getFirstChild().getNodeValue();
            alipayAPIResponse.setPgIsSuccess(isSuccess);
            if (isSuccess != null && isSuccess.equalsIgnoreCase("T")) {
                nList = doc.getElementsByTagName("request");
                for (int i = 0; i < nList.getLength(); i++) {
                    node = nList.item(i);
                    Element eElement = (Element) node;
                    NodeList nList1 = eElement.getElementsByTagName("param");
                    for (int j = 0; j < nList1.getLength(); j++) {
                        node = nList1.item(j);

                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement1 = (Element) node;
                            String paramName = ((Element) eElement1).getAttribute("name");
                            String paramValue = eElement1.getTextContent();
                        }
                    }
                }
                System.out.println("----------------------------");
                nList = doc.getElementsByTagName("response");
                for (int i = 0; i < nList.getLength(); i++) {
                    node = nList.item(i);
                    Element eElement = (Element) node;
                    NodeList nList1 = eElement.getElementsByTagName("alipay");
                    Element eElement2 = (Element) nList1.item(0);
                    if (isSuccess.equalsIgnoreCase("T")) {
                        NodeList nListSuccess = eElement2.getElementsByTagName("alipay_trans_id");
                        if (nListSuccess != null) {
                            if (nListSuccess.item(0) != null) {

                                String paramValue = nListSuccess.item(0).getTextContent();
                                alipayAPIResponse.setPgAlipayTransId(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("partner_trans_id");
                                paramValue = nListSuccess.item(0).getTextContent();
                                alipayAPIResponse.setPgPartnerTransId(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("alipay_reverse_time");
                                paramValue = nListSuccess.item(0).getTextContent();
                                alipayAPIResponse.setPgAlipayReverseTime(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("result_code");
                                paramValue = nListSuccess.item(0).getTextContent();
                                alipayAPIResponse.setPgResultCode(paramValue);

                            }
                        }
                    }
                } // end for loop
                nList = doc.getElementsByTagName("sign");
                String sign = nList.item(0).getTextContent();
                alipayAPIResponse.setPgSign(sign);

                nList = doc.getElementsByTagName("sign_type");
                String signType = nList.item(0).getTextContent();
                alipayAPIResponse.setPgSignType(signType);
            } else {

                nList = doc.getElementsByTagName("error");
                String error = nList.item(0).getTextContent();
                alipayAPIResponse.setPgSign(error);

                nList = doc.getElementsByTagName("sign");
                String sign = nList.item(0).getTextContent();
                alipayAPIResponse.setPgSign(sign);

                nList = doc.getElementsByTagName("sign_type");
                String signType = nList.item(0).getTextContent();
                alipayAPIResponse.setPgSignType(signType);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return alipayAPIResponse;

    }

    public static AlipayAPIResponse parseAlipayCancelAPIResponse(String xmlInput) {
        AlipayAPIResponse alipayAPIResponse = new AlipayAPIResponse();

        try {
            System.out.println("---parseAlipayReversalAPIResponse() - xmlInput=" + xmlInput);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new java.io.StringReader(xmlInput)));

            doc.getDocumentElement().normalize();

            Element ele = doc.getDocumentElement();

            NodeList nList = doc.getElementsByTagName("is_success");
            Node node = nList.item(0);
            String isSuccess = node.getFirstChild().getNodeValue();
            alipayAPIResponse.setPgIsSuccess(isSuccess);

            if (isSuccess != null && isSuccess.equalsIgnoreCase("T")) {
                nList = doc.getElementsByTagName("request");

                for (int i = 0; i < nList.getLength(); i++) {
                    node = nList.item(i);
                    Element eElement = (Element) node;
                    NodeList nList1 = eElement.getElementsByTagName("param");
                    for (int j = 0; j < nList1.getLength(); j++) {
                        node = nList1.item(j);

                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement1 = (Element) node;
                            String paramName = ((Element) eElement1).getAttribute("name");
                            String paramValue = eElement1.getTextContent();

                        }
                    }
                }

                nList = doc.getElementsByTagName("response");
                for (int i = 0; i < nList.getLength(); i++) {
                    node = nList.item(i);
                    Element eElement = (Element) node;
                    NodeList nList1 = eElement.getElementsByTagName("alipay");
                    Element eElement2 = (Element) nList1.item(0);
                    if (isSuccess.equalsIgnoreCase("T")) {
                        NodeList nListSuccess = eElement2.getElementsByTagName("alipay_trans_id");
                        if (nListSuccess != null) {
                            if (nListSuccess.item(0) != null) {

                                String paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setPgAlipayTransId(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("partner_trans_id");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setPgPartnerTransId(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("alipay_cancel_time");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setPgAlipayCancelTime(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("result_code");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setPgResultCode(paramValue);

                            } else {
                                nList = doc.getElementsByTagName("result_code");
                                String resultCode = nList.item(0).getTextContent();

                                alipayAPIResponse.setPgResultCode(resultCode);

                                nList = doc.getElementsByTagName("error");
                                String errorCode = nList.item(0).getTextContent();

                                alipayAPIResponse.setPgError(errorCode);

                            }
                        }
                    }
                } // end for loop
                nList = doc.getElementsByTagName("sign");
                String sign = nList.item(0).getTextContent();

                alipayAPIResponse.setPgSign(sign);

                nList = doc.getElementsByTagName("sign_type");
                String signType = nList.item(0).getTextContent();
                alipayAPIResponse.setPgSignType(signType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alipayAPIResponse;

    }

    public static AlipayAPIResponse parseAlipayRefundAPIResponse(String xmlInput) {
        AlipayAPIResponse alipayAPIResponse = new AlipayAPIResponse();

        try {
            System.out.println("---parseAlipayRefundAPIResponse() - xmlInput=" + xmlInput);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new java.io.StringReader(xmlInput)));

            doc.getDocumentElement().normalize();

            Element ele = doc.getDocumentElement();

            NodeList nList = doc.getElementsByTagName("is_success");
            Node node = nList.item(0);
            String isSuccess = node.getFirstChild().getNodeValue();
            alipayAPIResponse.setPgIsSuccess(isSuccess);

            if (isSuccess != null && isSuccess.equalsIgnoreCase("T")) {
                nList = doc.getElementsByTagName("request");

                for (int i = 0; i < nList.getLength(); i++) {
                    node = nList.item(i);
                    Element eElement = (Element) node;
                    NodeList nList1 = eElement.getElementsByTagName("param");
                    for (int j = 0; j < nList1.getLength(); j++) {
                        node = nList1.item(j);

                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement1 = (Element) node;
                            String paramName = ((Element) eElement1).getAttribute("name");
                            String paramValue = eElement1.getTextContent();
                        }
                    }
                }

                nList = doc.getElementsByTagName("response");
                for (int i = 0; i < nList.getLength(); i++) {
                    node = nList.item(i);
                    Element eElement = (Element) node;
                    NodeList nList1 = eElement.getElementsByTagName("alipay");
                    Element eElement2 = (Element) nList1.item(0);
                    if (isSuccess.equalsIgnoreCase("T")) {
                        NodeList nListSuccess = eElement2.getElementsByTagName("alipay_trans_id");
                        if (nListSuccess != null) {
                            if (nListSuccess.item(0) != null) {

                                String paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setPgAlipayTransId(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("partner_trans_id");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setPgPartnerTransId(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("partner_refund_id");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setMerchantRefundId(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("refund_amount");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setMcTransAmount(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("currency");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setMcCurrency(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("exchange_rate");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setPgExchangeRate(Double.parseDouble(paramValue));

                                nListSuccess = eElement2.getElementsByTagName("refund_amount_cny");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setPgTransAmountCny(Double.parseDouble(paramValue));

                                nListSuccess = eElement2.getElementsByTagName("result_code");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setPgResultCode(paramValue);

                            } else {
                                nList = doc.getElementsByTagName("result_code");
                                String resultCode = nList.item(0).getTextContent();

                                alipayAPIResponse.setPgResultCode(resultCode);

                                nList = doc.getElementsByTagName("error");
                                String errorCode = nList.item(0).getTextContent();

                                alipayAPIResponse.setPgError(errorCode);

                            }
                        }
                    }
                } // end for loop
                nList = doc.getElementsByTagName("sign");
                String sign = nList.item(0).getTextContent();

                alipayAPIResponse.setPgSign(sign);

                nList = doc.getElementsByTagName("sign_type");
                String signType = nList.item(0).getTextContent();
                alipayAPIResponse.setPgSignType(signType);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return alipayAPIResponse;

    }

    public static AlipayAPIResponse parseAlipayQueryAPIResponse(String xmlInput) {
        AlipayAPIResponse alipayAPIResponse = new AlipayAPIResponse();

        try {
            System.out.println("---parseAlipayQueryAPIResponse() - xmlInput=" + xmlInput);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new java.io.StringReader(xmlInput)));

            doc.getDocumentElement().normalize();

            Element ele = doc.getDocumentElement();

            NodeList nList = doc.getElementsByTagName("is_success");
            Node node = nList.item(0);
            String isSuccess = node.getFirstChild().getNodeValue();
            alipayAPIResponse.setPgIsSuccess(isSuccess);

            if (isSuccess != null && isSuccess.equalsIgnoreCase("T")) {
                nList = doc.getElementsByTagName("request");

                for (int i = 0; i < nList.getLength(); i++) {
                    node = nList.item(i);
                    Element eElement = (Element) node;
                    NodeList nList1 = eElement.getElementsByTagName("param");
                    for (int j = 0; j < nList1.getLength(); j++) {
                        node = nList1.item(j);

                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement1 = (Element) node;
                            String paramName = ((Element) eElement1).getAttribute("name");
                            String paramValue = eElement1.getTextContent();
                        }
                    }
                }
                System.out.println("----------------------------");
                nList = doc.getElementsByTagName("response");
                for (int i = 0; i < nList.getLength(); i++) {
                    node = nList.item(i);
                    Element eElement = (Element) node;
                    NodeList nList1 = eElement.getElementsByTagName("alipay");
                    Element eElement2 = (Element) nList1.item(0);
                    if (isSuccess.equalsIgnoreCase("T")) {
                        NodeList nListSuccess = eElement2.getElementsByTagName("alipay_trans_id");
                        if (nListSuccess != null) {
                            if (nListSuccess.item(0) != null) {

                                String paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setPgAlipayTransId(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("partner_trans_id");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setPgPartnerTransId(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("currency");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setMcCurrency(paramValue);

                                nList = doc.getElementsByTagName("out_trade_no");
                                String outTradeNo = nList.item(0).getTextContent();

                                alipayAPIResponse.setPgOutTradeNo(outTradeNo);

                                nListSuccess = eElement2.getElementsByTagName("alipay_buyer_login_id");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setPgAlipayBuyerLoginId(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("alipay_buyer_user_id");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setPgAlipayBuyerUserId(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("alipay_pay_time");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setPgAlipayPayTime(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("currency");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setMcCurrency(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("exchange_rate");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setPgExchangeRate(Double.parseDouble(paramValue));

                                nListSuccess = eElement2.getElementsByTagName("trans_amount");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setMcTransAmount(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("trans_amount_cny");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setPgTransAmountCny(Double.parseDouble(paramValue));

                                nListSuccess = eElement2.getElementsByTagName("alipay_trans_status");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setPgAlipayTransStatus(paramValue);

                                nListSuccess = eElement2.getElementsByTagName("result_code");
                                paramValue = nListSuccess.item(0).getTextContent();

                                alipayAPIResponse.setPgResultCode(paramValue);

                            } else {

                                nList = doc.getElementsByTagName("result_code");
                                String resultCode = nList.item(0).getTextContent();

                                alipayAPIResponse.setPgResultCode(resultCode);

                                nList = doc.getElementsByTagName("detail_error_code");
                                String errorCode = nList.item(0).getTextContent();

                                alipayAPIResponse.setPgError(errorCode);

                                nList = doc.getElementsByTagName("detail_error_des");
                                String errorDetails = nList.item(0).getTextContent();

                                alipayAPIResponse.setPgErrorDescription(errorDetails);

                                nList = doc.getElementsByTagName("out_trade_no");
                                String outTradeNo = nList.item(0).getTextContent();

                                alipayAPIResponse.setPgOutTradeNo(outTradeNo);

                                nList = doc.getElementsByTagName("partner_trans_id");
                                String partnerTransId = nList.item(0).getTextContent();

                                alipayAPIResponse.setPgPartnerTransId(partnerTransId);
                            }
                        }
                    }
                } // end for loop

                nList = doc.getElementsByTagName("sign");
                String sign = nList.item(0).getTextContent();

                alipayAPIResponse.setPgSign(sign);

                nList = doc.getElementsByTagName("sign_type");
                String signType = nList.item(0).getTextContent();
                alipayAPIResponse.setPgSignType(signType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alipayAPIResponse;

    }
}
