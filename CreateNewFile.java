package tanksMenu;

import java.io.File;
import java.io.IOException;
/**
 * Класс создания нового имени для сохранения
 * @author zork
 *
 */
public class CreateNewFile {
	/**
	 * Метод генерации нового имени для последнего сохранения
	 * @return
	 */
	public static String findLastFileName() {
		String fileName = null;
		File[] fileList;
		File directory = new File("saves/");
		fileList = directory.listFiles();
		int count = fileList.length;
		if (count > 999) {
			fileName = new String("saves/Save" + count + ".txt");
		} else if (count > 99) {
			fileName = new String("saves/Save0" + count + ".txt");
		} else if (count > 9) {
			fileName = new String("saves/Save00" + count + ".txt");
		} else {
			fileName = new String("saves/Save000" + count + ".txt");
		}
		createNewFile(fileName);
		return fileName;
	}
/**
 * Метод создания файла с именем, созданным в предыдущем методе
 * @param path
 */
	private static void createNewFile(String path) {
		File newFile = new File(path);
		newFile.getParentFile().mkdirs();
		try {
			newFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
