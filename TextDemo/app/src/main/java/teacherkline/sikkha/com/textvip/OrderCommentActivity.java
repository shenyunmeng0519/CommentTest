package teacherkline.sikkha.com.textvip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import teacherkline.sikkha.com.textvip.adapter.CommentAdapter;
import teacherkline.sikkha.com.textvip.adapter.PostImageAdapter;
import teacherkline.sikkha.com.textvip.comm.StarBarView;
import teacherkline.sikkha.com.textvip.comm.StringUtils;


/**
 * Created by Administrator on 2018/8/1.
 */

public class OrderCommentActivity extends AppCompatActivity implements CommentAdapter.OnStatusListener{

    StarBarView sbv_starbar;

    private ArrayList<MemberEntityImpl> mList;
    private int star_Num = 0;
    private int editStart,editEnd;
    private PostImageAdapter adapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private String upToken = "", Uuid = "";
    final private List<LocalMedia> upSuccessList = new ArrayList<>();
    private int iaid;
    final List<LocalMedia> progressList = new ArrayList<>();
    private boolean isExit = false;
    private boolean flag = false;
    private List<LocalMedia> EndSuccessList = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private int item_pos = 0;
    private Button bt_tijiao;
    private ImageButton top_back;
    private TextView top_text_center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_comments);
        initView();
        initListen();
    }

    public void initView() {
        RecyclerView lvComment = (RecyclerView) findViewById(R.id.lv_comments);
        bt_tijiao = (Button)findViewById(R.id.bt_tijiao);
        top_back = (ImageButton)findViewById(R.id.top_back);
        top_text_center = (TextView)findViewById(R.id.top_text_center);
        top_text_center.setText("评价");

        mList = new ArrayList<MemberEntityImpl>();

        for(int i=0;i<3;i++){
            MemberEntityImpl commentsInfo = new MemberEntityImpl();
            List<LocalMedia> commentImgs = new ArrayList<>();
            commentsInfo.setCommentImgs(commentImgs);
            commentsInfo.setStar_num(5.0f);
            mList.add(commentsInfo);
        }

        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        //设置RecyclerView 布局
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        lvComment.setLayoutManager(layoutmanager);

        commentAdapter = new CommentAdapter(mList,this);
        commentAdapter.setOnStatusListener(this);
        lvComment.setAdapter(commentAdapter);
    }

    private void initListen() {
        top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_tijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MemberEntityImpl> A =commentAdapter.getList();
            }
        });

    }

    @Override
    public void onSetStatusListener(int pos) {
        addPicture(pos);
        this.item_pos = pos;
    }
    /**
     * 进入相册添加图片
     * */
    private void addPicture(int pos) {
        if (item_pos!=pos){
            selectList = new ArrayList<>();
        }
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(OrderCommentActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(6)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(3)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选
                .previewImage(true)// 是否可预览图片
                .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .isGif(true)// 是否显示gif图片
                .openClickSound(false)// 是否开启点击声音
                .selectionMedia(selectList)// 是否传入已选图片
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                //.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled() // 裁剪是否可旋转图片
                //.scaleEnabled()// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    public void onDeleteListener(int pos,int tagPos) {
        MemberEntityImpl commentsInfo = mList.get(pos);
        List<LocalMedia> commentImgs = commentsInfo.getCommentImgs();
        commentImgs.remove(tagPos);

        commentsInfo.setCommentImgs(commentImgs);
        mList.set(pos,commentsInfo);
        commentAdapter.notifyItemChanged(pos);
    }

    @Override
    public void onSetListener(int pos) {
        PictureSelector.create(OrderCommentActivity.this).externalPicturePreview(pos, selectList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    MemberEntityImpl commentsInfo = mList.get(item_pos);
                    commentsInfo.setCommentImgs(selectList);
                    mList.set(item_pos,commentsInfo);
                    /*commentAdapter.setList(selectList);
                    commentAdapter.setClick(false);*/
                    commentAdapter.notifyItemChanged(item_pos);

                    /*recyclerView.smoothScrollToPosition(selectList.size());
                    if (selectList.size() > 0) {
                        upFinish = false;
                        biaoshi = false;
                    } else {
                        upFinish = true;
                    }
                    checkProgress();
                    putPicture(0);*/

                    break;

            }
        }
    }
}
