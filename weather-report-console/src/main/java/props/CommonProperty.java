/**
 * 
 */
package main.java.props;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Propertyファイルの読み取りを提供します。
 * 
 * @author shinjo
 * @since 2023/08/14
 *
 */
public class CommonProperty {
	
	/**
	 * プロパティファイルのパス
	 */
	private static final String PROPS_FILE_PATH = "src/main/resources/common.properties";
	
	/**
	 * プロパティ
	 */
	private static final Properties props;
	
	/**
	 * コンストラクタ
	 * インスタンス化禁止
	 * 
	 */
	private CommonProperty() {
		
	}
	
	/**
	 * staticイニシャライザ
	 * プロパティファイルを読み込みます。
	 */
	static {
		props = new Properties();
		
		try {
			props.load(Files.newBufferedReader(Paths.get(PROPS_FILE_PATH), StandardCharsets.UTF_8));
		} catch (IOException e) {
			System.err.println("プロパティファイルの読み取りに失敗しました。");
			e.printStackTrace();
		}
	}
	
	/**
	 * プロパティファイルからキーに対応した値を取得します。
	 * keyの定義がない場合は、nullが返されます。
	 * 
	 * @param key キー
	 * @return keyに対応した値
	 */
	public static String get(final String key) {
		return props.getProperty(key);
	}
	
}
