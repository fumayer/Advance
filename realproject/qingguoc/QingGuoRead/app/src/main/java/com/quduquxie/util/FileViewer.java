package com.quduquxie.util;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import com.quduquxie.Constants;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.database.helper.BookDaoHelper;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class FileViewer {
	static String TAG = "FileViewer";
	// ==================================================================
	// fields
	// ====================================================================
	final long maxSize = 100 * 1024 * 1024; // TODO 100MB
	String rootPath = Constants.APP_PATH_BOOK;
	private List<String> delFailPath = new ArrayList<String>();// 删除失败的目录
	private List<String> delSuccessPath = new ArrayList<String>();// 删除失败的目录
	// 反查数据库的Book对象
	private ArrayList<Integer> bookIDList = new ArrayList<Integer>();
	private boolean isDelFileSizeOver;

	public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
	public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
	public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
	public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值
	public final static String FILE_EXTENSION_SEPARATOR = ".";
	public final static String FILE_EXTENSION_SUFFIX = "delete";
	Context context;
	Handler mHandler = null;
	FileOverCallback overCallback;
	FileDeleteFailedCallback deleteFailedCallback;
	FileDeleteSuccessCallback deleteSuccessCallback;
	FileBookIDCallback fileBookIDCallback;

	// ==================================================================
	// constructor
	// ====================================================================
	public FileViewer() {
	}

	public FileViewer(Context context) {
		this.context = context;
	}

	public FileViewer(Context context, Handler handler) {
		this.context = context;
		this.mHandler = handler;
	}

	// ==================================================================
	// public method
	// ====================================================================

	/**
	 * 比对.delete文件大小
	 */
	public void doCheckSize() {

		HandlerThread thread = new HandlerThread("doCheckSize");
		// thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();
		if (mHandler != null) {
			mHandler.postDelayed(checkFileTask, 2000);
		} else {
			mHandler = new Handler(thread.getLooper());
			mHandler.postDelayed(checkFileTask, 2000);
		}
	}

	/**
	 * 删除
	 */
	public void doDelete() {
		HandlerThread thread = new HandlerThread("doDelete");
		// thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		QGLog.d(TAG, "doDeleteThread.start " + thread.getName());
		thread.start();

		if (mHandler != null) {
			mHandler.post(deleteFileTask);
		} else {
			mHandler = new Handler(thread.getLooper());
			mHandler.post(deleteFileTask);
		}
	}

	public void setDeleteFailedCallback(FileDeleteFailedCallback callback) {
		this.deleteFailedCallback = callback;
	}

	public void removeCheckSizeCall() {
		if (mHandler != null && null != checkFileTask) {
			mHandler.removeCallbacks(checkFileTask);
		}
	}

	public void removeDeleteCall() {
		if (mHandler != null && null != deleteFileTask) {
			mHandler.removeCallbacks(deleteFileTask);
		}
	}

	public void removeCheckBookCall() {
		if (mHandler != null) {
			mHandler.removeCallbacks(checkBookIDTask);
		}
	}

	// ==================================================================
	// callback method
	// ====================================================================
	public interface FileOverCallback {
		void isFileSizeOver(boolean isOver);
	}

	public interface FileDeleteFailedCallback {
		void getDeleteFailedPath(ArrayList<String> pathList);

	}

	public interface FileDeleteSuccessCallback {
		void getDeleteSuccessPath(ArrayList<String> pathList);
	}

	public interface FileBookIDCallback {
		void getBookIDs(ArrayList<Integer> bookIDList);
	}

	// ==================================================================
	// Runnable method ,do not touch
	// ====================================================================
	Runnable checkFileTask = new Runnable() {

		@Override
		public void run() {

			isDelFileSizeOver = isFileSizeOver(rootPath);
			if (overCallback != null) {
				overCallback.isFileSizeOver(isDelFileSizeOver);
			}
			QGLog.d(TAG, "isDelFileSizeOver " + isDelFileSizeOver);
		}
	};

	Runnable deleteFileTask = new Runnable() {

		@Override
		public void run() {
			String failedPath = null;
			String successPath = null;
			ArrayList<String> faileds = new ArrayList<>();
			faileds.addAll(delFailPath);
			ArrayList<String> successs = new ArrayList<>();
			successs.addAll(delSuccessPath);
//			ArrayList<String> failedpaths = (ArrayList<String>) delFailPath;
//			ArrayList<String> successpaths = (ArrayList<String>) delSuccessPath;
			QGLog.d(TAG, "deleteFileTask " + deleteFileTask.hashCode());
			doDeleteFile(rootPath);// 删除目录文件

			if (faileds.size() > 0) {
				for (String path : faileds) {
					doDeleteFile(path);
					failedPath = path;
					QGLog.d(TAG, "delete failed file path" + failedPath);
				}
			}
			if (deleteFailedCallback != null && faileds.size() > 0) {
				deleteFailedCallback.getDeleteFailedPath(faileds);
			}

			if (successs.size() > 0) {
				for (String path : successs) {
					successPath = path;
					QGLog.d(TAG, "delete success file path" + successPath);
				}
			}
			if (deleteSuccessCallback != null && successs.size() > 0) {
				deleteSuccessCallback.getDeleteSuccessPath(successs);
			}

		}
	};

	Runnable checkBookIDTask = new Runnable() {

		@Override
		public void run() {
			// 检测整个目录，.delete文件与book表匹配
			bookIDList = getAllBookIDs(rootPath);
			if (fileBookIDCallback != null && bookIDList.size() > 0) {
				fileBookIDCallback.getBookIDs(bookIDList);
			}
		}
	};

	// ==================================================================
	// operate method,do not touch
	// ====================================================================

	private boolean isFileSizeOver(String root) {
		long size = getRootFilesSize(root);
		QGLog.d(TAG, "del size > maxSize?  " + (size > maxSize) + "size " + FormetFileSize(size));

		return size >= maxSize;
	}

	private void doDeleteFile(String root) {
		File f = new File(root);// 一级目录
		if (f != null && f.exists()) {

			File[] files = f.listFiles(); // 子目录文件
			if (files != null) {

				for (int i = 0; i < files.length; i++) {
					String singlePath = files[i].getAbsolutePath();
					QGLog.d(TAG, " 删除时，检查的路径 " + files[i].getAbsolutePath());
					if (singlePath != null && isContented(singlePath)) {
						QGLog.d(TAG, " 删除的路径 " + files[i].getAbsolutePath());
						deleteFiles(files[i]);// 执行删除
					}
				}
			}
		}
	}

	protected boolean isContented(String singlePath) { // 最终路径是否满足条件
		return getFileExtension(singlePath).equals(FILE_EXTENSION_SUFFIX);
	}

	protected long getRootFilesSize(String root) {
		long size = 0;
		File f = new File(root);// 一级目录
		if (f != null && f.exists()) {

			File[] files = f.listFiles(); // 子目录文件

			for (int i = 0; i < files.length; i++) {
				String singlePath = files[i].getAbsolutePath();
				QGLog.d(TAG, " 计算大小时，检查的所有路径 " + files[i].getAbsolutePath());
				if (singlePath != null && isContented(singlePath)) {
					QGLog.d(TAG, " 计算大小的路径 " + files[i].getAbsolutePath());
					size = size + getFileSizes(files[i]);// 文件大小累加
				}
			}
		}
		QGLog.d(TAG, "size大小  " + FormetFileSize(size));
		return size;
	}

	// =====================================================
	// 与文件操作无关的方法，预留
	// =================================================
	/**
	 * 根据book_id得到Book对象,存为list对象
	 */
	protected ArrayList<Book> getBooksFromDB(String book) {
		ArrayList<Book> books = new ArrayList<Book>();
		Book isbook = getBook(book);
		if (isbook != null) {
			QGLog.d(TAG, "isbook " + isbook.name);
			books.add(isbook);
		}
		return books;
	}

	protected Book getBook(String book_id) {
		Book book = BookDaoHelper.getInstance(context).loadBook(book_id, 0);
		return book;
	}

	/**
	 * 遍历全部文件路径，得到全部book_id
	 */
	private ArrayList<Integer> getAllBookIDs(String root) {
		ArrayList<Integer> book_ids = new ArrayList<Integer>();
		File f = new File(root);// 一级目录
		if (f != null && f.exists()) {

			File[] files = f.listFiles(); // 子目录文件

			for (int i = 0; i < files.length; i++) {
				String singlePath = files[i].getAbsolutePath();
				QGLog.d(TAG, " 获取book_id时，检查的所有路径 " + files[i].getAbsolutePath());
				String td = getSingleBookID(singlePath);
				book_ids.add(Integer.valueOf(td));
			}
		}
		QGLog.d(TAG, "book_ids " + book_ids.toString());
		return book_ids;
	}

	/**
	 * 获取单个book_id
	 */
	protected String getSingleBookID(String singlePath) {

		if (singlePath != null && isContented(singlePath)) {
			QGLog.d(TAG, " 获取book_id的路径 " + singlePath);
			int last = singlePath.indexOf(FILE_EXTENSION_SEPARATOR);
			int start = singlePath.lastIndexOf("/") + 1;
			String book_id = singlePath.substring(start, last);
			QGLog.d(TAG, "book_id " + book_id);
			return book_id;
		}
		return null;
	}

	// ==================================================================
	// File method,do not touch
	// ====================================================================

	/**
	 * 获取指定文件大小
	 * 
	 * f
	 * Exception
	 */
	private long getFileSize(File file) {
		long size = 0;
		if (file.exists() && file.isFile()) {
			size = file.length();
		}
		return size;
	}

	/**
	 * 获取指定文件夹大小
	 * 
	 * f
	 * Exception
	 */
	private long getFileSizes(File f) {
		long size = 0;
		if (f.exists()) {
			File flist[] = f.listFiles();
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					size = size + getFileSizes(flist[i]);
				} else {
					size = size + getFileSize(flist[i]);
				}
			}
		}
		QGLog.d(TAG, "delete file size " + FormetFileSize(size));
		return size;
	}

	/**
	 * 递归删除文件
	 * 
	 * singlePath
	 */
	private void deleteFiles(File singleFile) { 
		String path =null;

		if (singleFile == null || !singleFile.exists()) {
			return;
		}

//		if (!singleFile.isDirectory()) {//FIXME nullpointer
//			return;
//		}
		if(singleFile.listFiles()==null){
			return ;
		}
		try {
			for (File f : singleFile.listFiles()) {
				if (f!=null&&f.isFile()) {
					path = f.getAbsolutePath();
					if(path==null){
						return;
					}
					if (f.delete()) {
						delSuccessPath.add(path);
					} else {
						
						delFailPath.add(path);
						QGLog.e(TAG, "file.delete() failed ");
					}
				} else if (f!=null&&f.isDirectory()) {
					deleteFiles(f);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		path = singleFile.getAbsolutePath();
		if(path==null){
			return;
		}
		if (singleFile.delete()) {
			delSuccessPath.add(path);

		} else {
			delFailPath.add(path);
			QGLog.e(TAG, "file.delete() failed ");

		}
	}

	// ============================================================
	// converter method, do not touch
	// ============================================================
	/**
	 * 调用此方法自动计算指定文件或指定文件夹的大小
	 * 
	 * filePath
	 *            文件路径
	 * @return 计算好的带B、KB、MB、GB的字符串
	 */
	protected String getAutoFileOrFilesSize(String filePath) {
		File file = new File(filePath);
		long blockSize = 0;
		try {
			if (file.isDirectory()) {
				blockSize = getFileSizes(file);
			} else {
				blockSize = getFileSize(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			QGLog.e(TAG, "获取失败!");
		}
		return FormetFileSize(blockSize);
	}

	/**
	 * 转换文件大小
	 * 
	 * fileS
	 */
	protected String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		String wrongSize = "0B";
		if (fileS == 0) {
			return wrongSize;
		}
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "GB";
		}
		return fileSizeString;
	}

	/**
	 * 转换文件大小,指定转换的类型
	 * 
	 * fileS
	 * sizeType
	 */
	protected static double FormetFileSize(long fileS, int sizeType) {
		DecimalFormat df = new DecimalFormat("#.00");
		double fileSizeLong = 0;
		switch (sizeType) {
		case SIZETYPE_B:
			fileSizeLong = Double.valueOf(df.format((double) fileS));
			break;
		case SIZETYPE_KB:
			fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
			break;
		case SIZETYPE_MB:
			fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
			break;
		case SIZETYPE_GB:
			fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
			break;
		default:
			break;
		}
		return fileSizeLong;
	}

	/**
	 * get file name extension from path, not include suffix
	 * 
	 * filePath
	 * @return
	 */
	public static String getFileExtension(String filePath) {
		if (isBlank(filePath)) {
			return filePath;
		}

		int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
		int filePosi = filePath.lastIndexOf(File.separator);
		if (extenPosi == -1) {
			return "";
		}
		return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
	}

	public static boolean isBlank(String str) {
		return (str == null || str.trim().length() == 0);
	}
}
