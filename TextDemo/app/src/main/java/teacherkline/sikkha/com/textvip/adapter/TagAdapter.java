package teacherkline.sikkha.com.textvip.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.luck.picture.lib.entity.LocalMedia;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import teacherkline.sikkha.com.textvip.R;
import teacherkline.sikkha.com.textvip.comm.StringUtils;
import teacherkline.sikkha.com.textvip.flowtaglayout.OnInitSelectedPosition;

/**
 * Created by HanHailong on 15/10/19.
 */
public class TagAdapter<T> extends BaseAdapter implements OnInitSelectedPosition {

    private final Context mContext;
    private final List<T> mDataList;
    String path;

    public TagAdapter(Context context) {
        this.mContext = context;
        mDataList = new ArrayList<T>();
    }

    @Override
    public int getCount() {
       if (mDataList.size() < 6) {
            return mDataList.size() + 1;
        } else {
            return mDataList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.tag_item2, null);

        ImageView ivImage = (ImageView) view.findViewById(R.id.iv_comment_image);
        LinearLayout ll_del= (LinearLayout)view.findViewById(R.id.ll_del);
        if (mDataList.size()>0&&position!=mDataList.size()) {
            T t = mDataList.get(position);

            if (t instanceof LocalMedia) {
                if (StringUtils.NullToStr(((LocalMedia) t).getCompressPath()).equals("")) {
                    ll_del.setVisibility(View.GONE);
                    ivImage.setBackground(mContext.getResources().getDrawable(R.mipmap.add_pic));
                } else {
                    ll_del.setVisibility(View.VISIBLE);
                    if (((LocalMedia) t).isCompressed() && !((LocalMedia) t).getPath().endsWith(".gif") || (((LocalMedia) t).isCut() && ((LocalMedia) t).isCompressed())) {
                        // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                        path = ((LocalMedia) t).getCompressPath();
                    } else {
                        // 原图
                        path = ((LocalMedia) t).getPath();
                    }
                    Glide.with(mContext)
                            .load(path)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ivImage);
                }
            }
        }else {
            ll_del.setVisibility(View.GONE);
            ivImage.setBackground(mContext.getResources().getDrawable(R.mipmap.add_pic));
        }
        return view;
    }


    public void onlyAddAll(List<T> datas) {
        mDataList.clear();
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    public void clearAndAddAll(List<T> datas) {
        mDataList.clear();
        onlyAddAll(datas);
    }

    @Override
    public boolean isSelectedPosition(int position) {
        if (position % 2 == 0) {
            return true;
        }
        return false;
    }
    private OnclickListener onclickListener;
    public void setOnStatusClickListener(OnclickListener onclickListener) {
        this.onclickListener = onclickListener;
    }


    public interface OnclickListener {
        void onSetClickListener(int pos);
        void onSetClickStatusListener(int pos);
        void onDeleteClickListener(int pos, int tagPos);
    }
}
