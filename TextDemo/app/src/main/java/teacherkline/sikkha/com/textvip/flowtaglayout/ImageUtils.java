package teacherkline.sikkha.com.textvip.flowtaglayout;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/8/15.
 */

public class ImageUtils {
    /**
     * 从本地path中获取bitmap，压缩后保存小图片到本地
     *
     * @param context
     * @param path    图片存放的路径
     * @return 返回压缩后图片的存放路径
     */
    public static String saveBitmap(Context context, String path) {
        String compressdPicPath = "";

//      ★★★★★★★★★★★★★★重点★★★★★★★★★★★★★
      /*  //★如果不压缩直接从path获取bitmap，这个bitmap会很大，下面在压缩文件到100kb时，会循环很多次，
        // ★而且会因为迟迟达不到100k，options一直在递减为负数，直接报错
        //★ 即使原图不是太大，options不会递减为负数，也会循环多次，UI会卡顿，所以不推荐不经过压缩，直接获取到bitmap
        Bitmap bitmap=BitmapFactory.decodeFile(path);*/
//      ★★★★★★★★★★★★★★重点★★★★★★★★★★★★★

//        建议先将图片压缩到控件所显示的尺寸大小后，再压缩图片质量
//        首先得到手机屏幕的高宽，根据此来压缩图片，当然想要获取跟精确的控件显示的高宽（更加节约内存）,可以使用getImageViewSize();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;  // 屏幕宽度（像素）
        int height = displayMetrics.heightPixels;  // 屏幕高度（像素）
//        获取按照屏幕高宽压缩比压缩后的bitmap
        Bitmap bitmap = decodeSampledBitmapFromPath(path, width, height);

        String oldName = path.substring(path.lastIndexOf("/"), path.lastIndexOf("."));
        String name = oldName + System.currentTimeMillis()+ "_compress.jpg";//★很奇怪oldName之前不能拼接字符串，只能拼接在后面，否则图片保存失败
        String saveDir = Environment.getExternalStorageDirectory()
                + "/compress";
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        // 保存入sdCard
        File file = new File(saveDir, name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

/* options表示 如果不压缩是100，表示压缩率为0。如果是70，就表示压缩率是70，表示压缩30%; */
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        while (baos.toByteArray().length / 1024 > 500) {
// 循环判断如果压缩后图片是否大于500kb继续压缩

            baos.reset();
            options -= 10;
            if (options <= 0) {
                //为了防止图片大小一直达不到500kb，options一直在递减，当options<0时，下面的方法会报错
                // 也就是说即使达不到500kb，也就压缩到10了
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                break;
            }
// 这里压缩options%，把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(baos.toByteArray());
            out.flush();
            out.close();
            compressdPicPath = file.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return compressdPicPath;
    }

    /**
     * 根据ImageView获取适当的压缩的宽和高
     * 尽可能得到ImageView的精确的宽高
     *
     * @param imageView
     * @return
     */
    public static ImageSize getImageViewSize(ImageView imageView) {

//      得到屏幕的宽度
        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();


        ImageSize imageSize = new ImageSize();
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();

//      ------------------------------------尽可能得到ImageView的精确的宽高-------------------------------------------------------------
//      得到imageView的实际宽度（为了压缩图片，这里一定要得到imageview的宽高）必须压缩！，否则OOM！！！！！！！！！！
        int width = imageView.getWidth();

        if (width <= 0) {//可能imageView刚new出来还没有添加到容器当中，width可能为0
            width = lp.width;//获取imageView在layout中声明的宽度
        }
        if (width <= 0) {//如果imageView设置的是WRAP_CONTENT=-1或者MATHC_PARENT=-2，得到的width还是0
            // TODO: 2016/3/19 此方法在API16以上才可以使用，为了兼容低版本，一会用反射获取,已解决
//            width = imageView.getMaxWidth();//检查最大值（此方法在API16以上才可以使用，为了兼容低版本，一会用反射获取）
            width = getImageViewFieldValue(imageView, "mMaxWidth");//检查最大值（此方法在API16以上才可以使用，为了兼容低版本，一会用反射获取）
        }
        if (width <= 0) {//如果还是得不到width，就设置为屏幕的宽度
            width = displayMetrics.widthPixels;
        }

//                                        ----------------------------------------

        //      得到imageView的实际高度（为了压缩图片，这里一定要得到imageview的宽高）必须压缩！，否则OOM！！！！！！！！！！
        int height = imageView.getHeight();

        if (height <= 0) {//可能imageView刚new出来还没有添加到容器当中，width可能为0
            height = lp.height;//获取imageView在layout中声明的高度
        }
        if (height <= 0) {//如果imageView设置的是WRAP_CONTENT=-1或者MATHC_PARENT=-2，得到的width还是0
            // TODO: 2016/3/19 此方法在API16以上才可以使用，为了兼容低版本，一会用反射获取，已解决
//            height = imageView.getMaxHeight();//检查最大值（此方法在API16以上才可以使用，为了兼容低版本，一会用反射获取）
            height = getImageViewFieldValue(imageView, "mMaxHeight");//检查最大值（此方法在API16以上才可以使用，为了兼容低版本，一会用反射获取）
        }
        if (height <= 0) {//如果还是得不到width，就设置为屏幕的高度
            height = displayMetrics.heightPixels;
        }
//       --------------------------------尽可能得到ImageView的精确的宽高------------------------------------------------------------------

        imageSize.width = width;
        imageSize.height = height;
        return imageSize;
    }

    /**
     * 通过反射获取imageview的某个属性值（imageView.getMaxWidth()这个方法在api16以上才可以用，所以使用反射得到这个width属性值）
     *
     * @param object
     * @param fieldName
     * @return
     */
    private static int getImageViewFieldValue(Object object, String fieldName) {

        int value = 0;

        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return value;
    }

    /**
     * 根据图片要显示的宽和高，对图片进行压缩，避免OOM
     *
     * @param path
     * @param width  要显示的imageview的宽度
     * @param height 要显示的imageview的高度
     * @return
     */
    private static Bitmap decodeSampledBitmapFromPath(String path, int width, int height) {

//      获取图片的宽和高，并不把他加载到内存当中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = caculateInSampleSize(options, width, height);
//      使用获取到的inSampleSize再次解析图片(此时options里已经含有压缩比 options.inSampleSize，再次解析会得到压缩后的图片，不会oom了 )
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;

    }

    /**
     * 根据需求的宽和高以及图片实际的宽和高计算SampleSize
     *
     * @param options
     * @param reqWidth  要显示的imageview的宽度
     * @param reqHeight 要显示的imageview的高度
     * @return
     * @compressExpand 这个值是为了像预览图片这样的需求，他要比所要显示的imageview高宽要大一点，放大才能清晰
     */
    private static int caculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;

        if (width >= reqWidth || height >= reqHeight) {

            int widthRadio = Math.round(width * 1.0f / reqWidth);
            int heightRadio = Math.round(width * 1.0f / reqHeight);

            inSampleSize = Math.max(widthRadio, heightRadio);

        }

        return inSampleSize;
    }


    /**
     * This method is used to get real path of file from from uri<br/>
     * http://stackoverflow.com/questions/11591825/how-to-get-image-path-just-
     * captured-from-camera
     * 这个方法是可以使用的（网上搜了一堆好多没用的代码）
     *
     * @param contentUri
     * @return String
     */
    public static String getRealPathFromURI(Activity activity, Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            // Do not call Cursor.close() on a cursor obtained using this
            // method,
            // because the activity will do that for you at the appropriate time
            Cursor cursor = activity.managedQuery(contentUri, proj, null, null,
                    null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }


    public static String getBase64String(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = fis.read(buffer)) >= 0) {
                baos.write(buffer, 0, count);
            }
            String uploadBuffer = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT)); // 进行Base64编码
            fis.close();//这两行原来没有
            baos.flush();
            return uploadBuffer;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //将bitmap转换为File文件
    public static File saveBitmapFile(Bitmap bitmap, File path) {
        File file = new File(path.getPath()+"_bitmap.jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
