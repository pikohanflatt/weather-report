package main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import main.java.props.CommonProperty;
import main.java.xml.CityIdGenerator;

public class Main {

	public static void main(String[] args) {
		final String inputFile = CommonProperty.get("inputFile");
		
		try (Stream<String> stream = Files.lines(Paths.get(inputFile))) {
			stream.forEach(line -> {
				// カンマ区切りで読み込み
				String[] fields = line.split(",");
				String prefecture = fields[0];
				String city = fields[1];
				
				try {
					String cityId = CityIdGenerator.generateCityId(prefecture, city);
					System.out.println(cityId);
				} catch (Exception e) {
					System.err.println("システムエラー");
					e.printStackTrace();
					
					return;
				}
			});
		} catch (IOException e) {
			System.err.println("入力ファイルの読み取りに失敗しました。");
			e.printStackTrace();
		}
	}

}
