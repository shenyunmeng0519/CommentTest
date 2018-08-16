package teacherkline.sikkha.com.textvip.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.DebugUtil;
import com.luck.picture.lib.tools.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import teacherkline.sikkha.com.textvip.OrderCommentActivity;
import teacherkline.sikkha.com.textvip.R;
import teacherkline.sikkha.com.textvip.comm.ProcessImageView;

public class PostImageAdapter extends
        RecyclerView.Adapter<PostImageAdapter.ViewHolder> {
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    private LayoutInflater mInflater;
    private List<LocalMedia> list = new ArrayList<>();
    private List<LocalMedia> SuccesList = new ArrayList<>();
    private List<LocalMedia> progressList = new ArrayList<>();
    private String adapterType = "";

    private int selectMax = 9,progress = 0;
    private Context context;
    /**
     * 点击添加图片跳转
     */
    private onAddPicClickListener mOnAddPicClickListener;
    private boolean biaoshi = true,checkClick = true,checkFail = false,againUp;

    public interface onAddPicClickListener {
        void onAddPicClick();
    }

    public PostImageAdapter(Context context, onAddPicClickListener mOnAddPicClickListener) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mOnAddPicClickListener = mOnAddPicClickListener;
    }
    public void setSelectMax(int selectMax) {
        this.selectMax = selectMax;
    }
    public boolean getBiaoshi() {
        return biaoshi;
    }
    public void setClick(boolean checkClick) {
        this.checkClick = checkClick;
    }
    public void setFail(boolean checkFail) {
        this.checkFail = checkFail;
    }
    public void setList(List<LocalMedia> list) {
        this.list = list;
    }
    public void setProgressList(List<LocalMedia> progressList) {
        this.progressList = progressList;
    }
    public void setSuccesList(List<LocalMedia> SuccesList) {
        this.SuccesList = SuccesList;
    }
    public void setAdapterType(String adapterType){
        this.adapterType = adapterType;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImg;
        LinearLayout ll_del;
        TextView tv_duration,tv_wenzi;
        ProcessImageView process_image;
        RelativeLayout rl_wenzi;

        public ViewHolder(View view) {
            super(view);
            mImg = (ImageView) view.findViewById(R.id.fiv);
            ll_del = (LinearLayout) view.findViewById(R.id.ll_del);
            tv_duration = (TextView) view.findViewById(R.id.tv_duration);
            process_image = (ProcessImageView) view.findViewById(R.id.process_image);
            tv_wenzi = (TextView) view.findViewById(R.id.tv_wenzi);
            rl_wenzi = (RelativeLayout) view.findViewById(R.id.rl_wenzi);
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() < selectMax) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.gv_filter_image,
                viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private boolean isShowAddItem(int position) {
        int size = list.size() == 0 ? 0 : list.size();
        return position == size;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        //少于8张，显示继续添加的图标
        if (getItemViewType(position) == TYPE_CAMERA) {
            viewHolder.rl_wenzi.setVisibility(View.GONE);
            viewHolder.mImg.setImageResource(R.mipmap.add_pic);
            viewHolder.ll_del.setVisibility(View.INVISIBLE);
            viewHolder.process_image.setVisibility(View.GONE);
            viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkClick) {
                        mOnAddPicClickListener.onAddPicClick();
                    }else {
                        if (checkFail){
                            Log.e("aaa","发帖过于频繁，请稍后再试");
                        }else {
                            Log.e("aaa","图片上传中，请稍后...");
                        }
                    }

                }
            });
        } else {
            viewHolder.ll_del.setVisibility(View.VISIBLE);
            viewHolder.process_image.setVisibility(View.VISIBLE);
            viewHolder.ll_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = viewHolder.getAdapterPosition();
                    // 这里有时会返回-1造成数据下标越界,具体可参考getAdapterPosition()源码，
                    // 通过源码分析应该是bindViewHolder()暂未绘制完成导致，知道原因的也可联系我~感谢
                    if (index != RecyclerView.NO_POSITION) {
                        biaoshi = false;
                        for (int i = 0; i < SuccesList.size(); i++) {
                            if (SuccesList.get(i).getPath().equals(list.get(index).getPath())) {
                                //SuccesList.remove(index);
                            }
                        }
                        if (progressList.get(index).getProgress()==-1&&progressList.size()-1>0) {
                            LocalMedia LocalMedia = new LocalMedia();
                            LocalMedia.setProgress(-1);
                            progressList.set(index + 1, LocalMedia);
                        }
                        progressList.remove(index);
                        list.remove(index);
                        notifyItemRemoved(index);
                        notifyItemRangeChanged(index, list.size());
                        DebugUtil.i("delete position:", index + "--->remove after:" + list.size());
                    }
                }
            });
            LocalMedia media = list.get(position);
            int mimeType = media.getMimeType();
            String path = "";
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                path = media.getCutPath();
            } else if (media.isCompressed() && !media.getPath().endsWith(".gif") || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = media.getCompressPath();
            } else {
                // 原图
                path = media.getPath();
            }
            // 图片
            if (media.isCompressed()) {
                Log.i("compress image result:", new File(media.getCompressPath()).length() / 1024 + "k");
                Log.i("压缩地址::", media.getCompressPath());
            }

            // Log.i("原图地址::", media.getPath());
            int pictureType = PictureMimeType.isPictureType(media.getPictureType());
            if (media.isCut()) {
                //Log.i("裁剪地址::", media.getCutPath());
            }
            long duration = media.getDuration();
            viewHolder.tv_duration.setVisibility(pictureType == PictureConfig.TYPE_VIDEO
                    ? View.VISIBLE : View.GONE);
            if (mimeType == PictureMimeType.ofAudio()) {
                viewHolder.tv_duration.setVisibility(View.VISIBLE);
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.picture_audio);
                StringUtils.modifyTextViewDrawable(viewHolder.tv_duration, drawable, 0);
            } else {
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.video_icon);
                StringUtils.modifyTextViewDrawable(viewHolder.tv_duration, drawable, 0);
            }
            viewHolder.tv_duration.setText(DateUtils.timeParse(duration));
            if (mimeType == PictureMimeType.ofAudio()) {
                viewHolder.mImg.setImageResource(R.drawable.audio_placeholder);
            } else {
                Glide.with(viewHolder.itemView.getContext())
                        .load(path)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(viewHolder.mImg);
            }
            //itemView 的点击事件
            if (mItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int adapterPosition = viewHolder.getAdapterPosition();
                        mItemClickListener.onItemClick(adapterPosition, v);
                    }
                });
            }
                if (progressList != null && progressList.size() > 0) {
                    viewHolder.rl_wenzi.setVisibility(View.VISIBLE);
                    if (progressList.get(position).getProgress() == -1) {
                        viewHolder.rl_wenzi.setEnabled(true);
                        viewHolder.tv_wenzi.setText("点击重新上传");
                        viewHolder.rl_wenzi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(adapterType.equals("CircleDetail")){

                                }else if (adapterType.equals("PL")){
                                    OrderCommentActivity mContext = (OrderCommentActivity) context;
                                    //mContext.putPicture(position);
                                }else {
                                    OrderCommentActivity mContext = (OrderCommentActivity) context;
                                    //mContext.putPicture(position);
                                }
                            }
                        });
                    }else if (progressList.get(position).getProgress() == 100) {
                        viewHolder.process_image.setVisibility(View.GONE);
                        viewHolder.rl_wenzi.setVisibility(View.GONE);
                    } else {
                        viewHolder.rl_wenzi.setEnabled(false);
                        viewHolder.tv_wenzi.setText("图片上传中");
                        viewHolder.process_image.setVisibility(View.VISIBLE);
                        viewHolder.process_image.setProgress(progressList.get(position).getProgress());
                    }
            }
        }
    }

    protected OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}
