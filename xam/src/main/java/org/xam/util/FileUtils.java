package org.xam.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class FileUtils {

	private static final char UNIX_SEPARATOR = '/';

	private static final char WINDOWS_SEPARATOR = '\\';

	public static String getFilename(final String filename) {
		if (filename == null) {
			return null;
		}
		final int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
		final int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
		final int index = Math.max(lastUnixPos, lastWindowsPos);
		return filename.substring(index + 1);
	}

	public static String getFilenameExtension(final String filename) {
		final int index = filename.lastIndexOf('.');
		if (-1 == index) {
			return "";
		} else {
			return filename.substring(index + 1);
		}
	}
	
	/**
	 * 读取文件并以String类型返回文件内容，换行符"\n"
	 */
	public static String getFileToString(String filePath){
		FileInputStream inputstream;
		StringBuffer buffer = new StringBuffer();
		String line;
		try {
			inputstream = new FileInputStream(new File(filePath));
			BufferedReader bufferreader = new BufferedReader(new InputStreamReader(
					inputstream));
			line = bufferreader.readLine(); // 读取第一行
			while (line != null) { // 如果 line 为空说明读完了
				buffer.append(line); // 将读到的内容添加到 buffer 中
				buffer.append("\n"); // 添加换行符
				line = bufferreader.readLine(); // 读取下一行
			}
			inputstream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return buffer.toString();
	}

	public static String stripFilenameExtension(final String path) {
		if (path == null) {
			return null;
		}
		final int sepIndex = path.lastIndexOf(".");
		return (sepIndex != -1 ? path.substring(0, sepIndex) : path);
	}

	public static boolean createDirectoryRecursively(File directory) {
		if (directory == null) {
			return false;
		} else if (directory.exists()) {
			return !directory.isFile();
		} else if (!directory.isAbsolute()) {
			directory = new File(directory.getAbsolutePath());
		}
		final String parent = directory.getParent();
		if ((parent == null) || !createDirectoryRecursively(new File(parent))) {
			return false;
		}
		directory.mkdir();
		return directory.exists();
	}

	public static File createFile(final File file) throws IOException {
		if (!file.exists()) {
			createDirectoryRecursively(file.getParentFile());
			file.createNewFile();
		}
		return file;
	}

	@SuppressWarnings("unused")
	private static long kb = 1024, mb = kb * 1024, gb = mb * 1024;

	public static long sizeOfDirectory(final File directory) {
		if (!directory.exists()) {
			return 0l;
		}

		if (!directory.isDirectory()) {
			return directory.length();
		}

		long size = 0;
		final File[] files = directory.listFiles();
		if (files == null) {
			return 0;
		}
		for (int i = 0; i < files.length; i++) {
			final File file = files[i];
			if (file.isDirectory()) {
				size += sizeOfDirectory(file);
			} else {
				size += file.length();
			}
		}
		return size;
	}

	public static void deleteAll(final File dir) throws IOException {
		deleteAll(dir, true);
	}

	public static void deleteAll(final File dir, final boolean self) throws IOException {
		if (!dir.exists()) {
			return;
		}
		if (!dir.isDirectory()) {
			dir.delete();
			return;
		}
		final String[] list = dir.list();
		if (list != null) {
			for (final String element : list) {
				final File child = new File(dir, element);
				deleteAll(child);
			}
		}
		if (self) {
			dir.delete();
		}
	}
	
	/**
	 * 解压jar包中的文件
	 * @param jarFileName
	 * @param target
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static void unCompressFromJar(String jarFileName,String target) throws IOException{
		JarFile jfInst = new JarFile(jarFileName);
		Enumeration<JarEntry> enumEntry = jfInst.entries(); 
		while(enumEntry.hasMoreElements()){
			JarEntry jarEntry = enumEntry.nextElement();
			File tarFile = new File(target, jarEntry.getName());
			makeFile(jarEntry, tarFile);
			if (jarEntry.isDirectory()) {  
                continue;  
            }  
            //构造输出流  
            FileChannel fileChannel = new FileOutputStream(tarFile).getChannel();  
            //取输入流  
            InputStream ins = jfInst.getInputStream(jarEntry);  
            transferStream(ins, fileChannel);  
		}
	}
	
    /** 
     * 流交换 
     * @param ins 输入流 
     * @param targetChannel 输出流 
     */  
    private static void transferStream(InputStream ins, FileChannel targetChannel) {  
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 10);  
        ReadableByteChannel rbcInst = Channels.newChannel(ins);  
        try {  
            while (-1 != (rbcInst.read(byteBuffer))) {  
                byteBuffer.flip();  
                targetChannel.write(byteBuffer);  
                byteBuffer.clear();  
            }  
        } catch (IOException ioe) {  
            ioe.printStackTrace();  
        } finally {  
            if (null != rbcInst) {  
                try {  
                    rbcInst.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            if (null != targetChannel) {  
                try {  
                    targetChannel.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
	
    /** 
     * 创建文件 
     * @param jarEntry jar实体 
     * @param fileInst 文件实体 
     * @throws IOException 抛出异常 
     */  
    public static void makeFile(JarEntry jarEntry, File fileInst) {  
        if (!fileInst.exists()) {  
            if (jarEntry.isDirectory()) {  
                fileInst.mkdirs();  
            } else {  
                try {  
                    fileInst.createNewFile();  
                } catch (IOException e) {  
                    System.out.println("创建文件失败>>".concat(fileInst.getPath()));  
                }  
            }  
        }  
    }
}
