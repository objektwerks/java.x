package xml.processing;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathEvaluator {
    private DocumentBuilder documentBuilder;
    private XPath xpath;

    public XPathEvaluator() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(false);
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
        XPathFactory xpathFactory = XPathFactory.newInstance();
        xpath = xpathFactory.newXPath();
    }

    public DocumentBuilder getDocumentBuilder() {
        return documentBuilder;
    }

    public XPath getXpath() {
        return xpath;
    }

    public boolean evaluateToBoolean(String expression, Document document) throws XPathException {
        return (Boolean) xpath.compile(expression).evaluate(document, XPathConstants.BOOLEAN);
    }

    public Node evaluateToNode(String expression, Document document) throws XPathException {
        return (Node) xpath.compile(expression).evaluate(document, XPathConstants.NODE);
    }

    public NodeList evaluateToNodeList(String expression, Document document) throws XPathException {
        return (NodeList) xpath.compile(expression).evaluate(document, XPathConstants.NODESET);
    }

    public int evaluateToNumber(String expression, Document document) throws XPathException {
        Double number = (Double) xpath.compile(expression).evaluate(document, XPathConstants.NUMBER);
        return number.intValue();
    }

    public String evaluateToString(String expression, Document document) throws XPathException {
        return (String) xpath.compile(expression).evaluate(document, XPathConstants.STRING);
    }
}