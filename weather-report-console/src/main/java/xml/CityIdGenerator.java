package main.java.xml;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import main.java.props.CommonProperty;

/**
 * livedoor天気の地点定義xmlファイルを読み込みます。
 * 
 * @author shinjo
 *
 */
public class CityIdGenerator {
	
	/**
	 * XMLドキュメント
	 */
	private static Document doc;
	
	/**
	 * インスタンス化禁止
	 */
	private CityIdGenerator() {
		
	}
	
	/**
	 * staticイニシャライザ
	 * xmlファイルの読み込み
	 */
	static {
		final String xmlPath = CommonProperty.get("cityXmlFile");
		
		try (FileInputStream fs = new FileInputStream(Paths.get(xmlPath).toFile())) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(fs);
			
		} catch (IOException e) {
			System.err.println("xmlファイルの読み込みに失敗しました。");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("その他の例外");
			e.printStackTrace();
		}
	}
	
	/**
	 * 都道府県名、市区町村の情報から地点IDを生成します。
	 * 地点IDが生成できない場合は、nullを返します。
	 * 
	 * @param prefecture 都道府県名
	 * @param city 市区町村名
	 * @return 地点ID
	 */
	public static String generateCityId(String prefecture, String city) throws Exception {
		// 都道府県名、市区町村両方が揃わないと生成できない。
		if (prefecture == null || city == null) return null;
		
		final String xPathTemplate = "/pref[@title = '%s']/city[@title = '%s']";
		Element element = searchSingleByXPath(String.format(xPathTemplate, prefecture, city));
		
		// 検索失敗の場合もnullを返す。
		if (element == null) return null;
		
		return element.getAttribute("id");
		
	}
	
	/**
	 *  指定されたXPathでXMLドキュメント内を検索します。
	 *  検索結果が1件以上ある場合は例外をスローします。
	 *  検索結果が0件の場合、nullを返します。
	 * 
	 * @param xPathStr XPath文字列
	 * @return 検索結果
	 * @throws Exception 例外
	 */
	private static Element searchSingleByXPath(String xPathStr) throws Exception {
		XPathExpression expr = complieXPathExpression(xPathStr);
		Element elem = (Element)expr.evaluate(doc, XPathConstants.NODE);
		
		return elem;
	}
	
	/**
	 *  指定されたXPathでXMLドキュメント内を検索します。
	 *  検索結果が1件以上ある場合はこのメソッドを使用します。
	 *  
	 * @param xPathStr XPath文字列
	 * @return 検索結果
	 * @throws Exception 例外
	 */
	private static NodeList searchListByXPath(String xPathStr) throws Exception {
		XPathExpression expr = complieXPathExpression(xPathStr);
		NodeList nodelist = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		
		return nodelist;
	}
	
	/**
	 * XPath式をコンパイルします。
	 * 
	 * @param xPathStr XPath文字列
	 * @return XPathExpression
	 * @throws Exception 例外
	 */
	private static XPathExpression complieXPathExpression(String xPathStr) throws Exception {
		XPath xPath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xPath.compile(xPathStr);
		
		return expr;
	}
	
}
