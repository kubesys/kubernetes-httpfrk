/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.tools.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * @author wuheng@otcaix.iscas.ac.cn
 * @since  2019/11/16
 *
 *  
 */

public class FileUtils {

	public static void createFile(String dir, String name, String data) throws Exception {
		
		File fdir = new File(dir);
		
		// create directory
		if (!fdir.exists()) {
			fdir.mkdirs();
		}
		
		// delete existing file
		File file = new File(fdir, name);
		if (file.exists()) {
			file.delete();
		}
		
		// create new file
		file.createNewFile();
		FileWriter fileWritter = new FileWriter(file, true);
		BufferedWriter out = new BufferedWriter(fileWritter);
		out.write(data);
		out.flush();
		out.close();
		
	}
}
