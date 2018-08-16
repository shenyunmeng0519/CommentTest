package teacherkline.sikkha.com.textvip.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.luck.picture.lib.entity.LocalMedia;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import teacherkline.sikkha.com.textvip.MemberEntityImpl;
import teacherkline.sikkha.com.textvip.R;
import teacherkline.sikkha.com.textvip.comm.StarBarView;
import teacherkline.sikkha.com.textvip.comm.StringUtils;
import teacherkline.sikkha.com.textvip.flowtaglayout.FlowTagLayout;
import teacherkline.sikkha.com.textvip.flowtaglayout.OnTagSelectListener;

/**
 * @Company:
 * @Created: 2018/6/6
 * 作者：wy
 * @Description:
 * @version: 1.0
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> implements TagAdapter.OnclickListener {
    private ArrayList<MemberEntityImpl> list;
    private Context context;
    private OnStatusListener onStatusListener;
    public CommentAdapter(ArrayList<MemberEntityImpl> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnStatusListener(OnStatusListener onStatusListener) {
        this.onStatusListener = onStatusListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, viewGroup, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.flComment
                .setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        TagAdapter<LocalMedia> mSizeTagAdapter1 = new TagAdapter<>(context);
        mSizeTagAdapter1.setOnStatusClickListener(this);

        viewHolder.flComment.setAdapter(mSizeTagAdapter1);
        mSizeTagAdapter1.onlyAddAll(list.get(i).getCommentImgs());
        viewHolder.tv_shop_name.setText(list.get(i).getTicketname());
        viewHolder.sbv_starbar.setStarRating(list.get(i).getStar_num());
        setStarName(viewHolder.tv_wenzi,list.get(i).getStar_num());

        ImageLoader.getInstance().displayImage(list.get(i).getShpname(), viewHolder.iv_shop_image);
        viewHolder.sbv_starbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(i).setStar_num(viewHolder.sbv_starbar.getStarRating());
                setStarName(viewHolder.tv_wenzi,list.get(i).getStar_num());
            }
        });

        viewHolder.flComment.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, View view, List<Integer> selectedList) {
                switch (view.getId()){
                    case R.id.ll_del:
                        onStatusListener.onDeleteListener(i,selectedList.get(0));
                        break;
                    case R.id.iv_comment_image:
                        if (list.get(i).getCommentImgs().size()>0) {
                            if (list.get(i).getCommentImgs().size()==selectedList.get(0)){
                                onStatusListener.onSetStatusListener(i);
                            }else {
                                onStatusListener.onSetListener(selectedList.get(0));
                            }
                        }else {
                            onStatusListener.onSetStatusListener(i);
                        }
                        break;
                }
            }
        });
    }
    /**
     * 设置星星文字
     * */
    private void setStarName(TextView tv_wenzi, float star_num) {
        if (star_num==5.0f){
            tv_wenzi.setText("非常好");
        }else if (star_num==4.0f){
            tv_wenzi.setText("很好");
        }else if (star_num==3.0f){
            tv_wenzi.setText("一般");
        }else if (star_num==2.0f){
            tv_wenzi.setText("很差");
        }else if (star_num==1.0f){
            tv_wenzi.setText("非常差");
        }

    }
    public ArrayList<MemberEntityImpl> getList(){
        return list;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onSetClickListener(int pos) {

    }

    @Override
    public void onSetClickStatusListener(int pos) {

    }

    @Override
    public void onDeleteClickListener(int pos, int tagPos) {
        onStatusListener.onDeleteListener(pos,pos);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        FlowTagLayout flComment;
        ImageView iv_shop_image,iv_xuanze;
        TextView tv_shop_name,tv_wenzi,tv_content_count,tv_nimingtishi,tv_num;
        StarBarView sbv_starbar;
        EditText edt_content;

        public ViewHolder(View itemView) {
            super(itemView);
            flComment = itemView.findViewById(R.id.fl_comment);
            iv_shop_image = itemView.findViewById(R.id.iv_shop_image);
            tv_shop_name = itemView.findViewById(R.id.tv_shop_name);
            sbv_starbar = itemView.findViewById(R.id.sbv_starbar);
            tv_wenzi = itemView.findViewById(R.id.tv_wenzi);
            edt_content = itemView.findViewById(R.id.edt_content);
            tv_content_count = itemView.findViewById(R.id.tv_content_count);
            iv_xuanze = itemView.findViewById(R.id.iv_xuanze);
            tv_nimingtishi = itemView.findViewById(R.id.tv_nimingtishi);
            tv_num = itemView.findViewById(R.id.tv_num);

        }
    }

    public interface OnStatusListener {
        void onSetStatusListener(int pos);
        void onDeleteListener(int pos, int tagPos);
        void onSetListener(int pos);

    }

}
