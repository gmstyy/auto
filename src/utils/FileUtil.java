
package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileUtil {

	public static String defaultPath = "C:\\Users\\Administrator\\Desktop";

	public static File genFile(String path, Object content) {
		File file = new File(defaultPath+"\\"+path);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			fw.write(content.toString());
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	public static String readFile(String path) {
		File file = new File(defaultPath+"\\"+path);
		String rt = "";
		try {
			BufferedReader reader  = new BufferedReader(new FileReader(file));
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			String tempString = "";
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				rt+=tempString;
				line++;
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rt;
	}

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
}
