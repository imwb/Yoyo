package wb.com.yoyo.Utils;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class BitmapUtils {
	private static final String Util_LOG = makeLogTag(BitmapUtils.class);

	public static String makeLogTag(Class<?> cls) {
		return cls.getName();
	}

	public static void showToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 检查是否存在SD卡
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 创建目录
	 * 
	 * @param context
	 * @param dirName
	 *            文件夹名称
	 * @return
	 */
	public static File createFileDir(Context context, String dirName) {
		String filePath;
		// 如SD卡已存在，则存储；反之存在data目录下
		if (hasSdcard()) {
			// SD卡路径
			filePath = Environment.getExternalStorageDirectory()
					+ File.separator + dirName;
		} else {
			filePath = context.getCacheDir().getPath() + File.separator
					+ dirName;
		}
		File destDir = new File(filePath);
		if (!destDir.exists()) {
			boolean isCreate = destDir.mkdirs();
			Log.i(Util_LOG, filePath + " has created. " + isCreate);
		}
		return destDir;
	}
	

	/**
	 * 删除文件（若为目录，则递归删除子目录和文件）
	 * 
	 * @param file
	 * @param delThisPath
	 *            true代表删除参数指定file，false代表保留参数指定file
	 */
	public static void delFile(File file, boolean delThisPath) {
		if (!file.exists()) {
			return;
		}
		if (file.isDirectory()) {
			File[] subFiles = file.listFiles();
			if (subFiles != null) {
				int num = subFiles.length;
				// 删除子目录和文件
				for (int i = 0; i < num; i++) {
					delFile(subFiles[i], true);
				}
			}
		}
		if (delThisPath) {
			file.delete();
		}
	}

	public static void delFile(String path) {
		File file=new File(path);

			file.delete();
		
	}
	/**
	 * 获取文件大小，单位为byte（若为目录，则包括所有子目录和文件）
	 * 
	 * @param file
	 * @return
	 */
	public static long getFileSize(File file) {
		long size = 0;
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] subFiles = file.listFiles();
				if (subFiles != null) {
					int num = subFiles.length;
					for (int i = 0; i < num; i++) {
						size += getFileSize(subFiles[i]);
					}
				}
			} else {
				size += file.length();
			}
		}
		return size;
	}

	/**
	 * 保存Bitmap到指定目录
	 * 
	 * @param dir
	 *            目录
	 * @param fileName
	 *            文件名
	 * @param bitmap
	 * @throws IOException
	 */
	public static void savaBitmap(File dir, String fileName, Bitmap bitmap) {
		if (bitmap == null) {
			return;
		}
		File file = new File(dir, fileName);
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static File getImagePath() {
		String EXTERN_PATH = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) == true) {
			String dbPath = "/yoyo/avatar";
			EXTERN_PATH = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + dbPath;
			File f = new File(EXTERN_PATH);
			if (!f.exists()) {
				f.mkdirs();
			}
			return new File(EXTERN_PATH );
		}else {
			return new File("/avatar");
		}

	}

	/**
	 * 判断某目录下文件是否存在
	 * 
	 * @param dir
	 *            目录
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static boolean isFileExists(File dir, String fileName) {
		return new File(dir, fileName).exists();
	}
	/**
	 * @param path 图片路径
	 * @param width 宽
	 * @param heigh 高
	 * @author wb
	 */
	/*
	    那是BitmapFactory.options类，主要会用到这个类的inSampleSize、inJustDecodeBounds、outHeight、outWidth参数。

      inSampleSize：缩放比例，这个参数需要是2的幂函数。

      inJustDecodeBounds：如果设置这个参数为ture，就不会给图片分配内存空间，但是可以获取到图片的大小等属性。
      outHeight：图片高，单位像素.

      outWidth：图片宽，单位像素.
	 */
	public static Bitmap getBitmapByOption(String path, int width,int heigh) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		//防止溢出
		options.inJustDecodeBounds=true;
		Bitmap bitmap=BitmapFactory.decodeFile(path,options);
		int xScale = options.outWidth / width;  
	    int yScale = options.outHeight / heigh;

		//缩放比例
	    options.inSampleSize = xScale > yScale ? xScale : yScale; 
		options.inJustDecodeBounds=false;

		/**
		编码格式
		        ALPHA_8     (1),
				RGB_565     (3),
				ARGB_4444   (4),
				ARGB_8888   (5);
		*/
		options.inPreferredConfig=Bitmap.Config.ARGB_4444;
		bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}
}
