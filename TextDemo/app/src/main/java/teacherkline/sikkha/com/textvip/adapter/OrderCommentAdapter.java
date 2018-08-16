package teacherkline.sikkha.com.textvip.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import teacherkline.sikkha.com.textvip.MemberEntityImpl;
import teacherkline.sikkha.com.textvip.R;

/**
 * Created by sym on 2018/8/3.
 */

public class OrderCommentAdapter extends BaseAdapter {
    private final List<MemberEntityImpl> mList;
    /**
     * 存输入
     **/
    private Map<Integer, String> mMapContent = new HashMap<Integer, String>();
    /**
     * 存输入的长度0/500
     **/
    private Map<Integer, String> mMapContentOther = new HashMap<Integer, String>();
    private CharSequence temp;
    private Context mContext;
    private int index = -1;

    public OrderCommentAdapter(Context context, List<MemberEntityImpl> list) {
        mList = list;
        mContext = context;
    }
    @Override
    public int getCount() {
        return  mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHold holder = null;
        if (convertView == null) {
            convertView = LinearLayout.inflate(mContext, R.layout.order_comment_item, null);
            holder = new ViewHold(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHold) convertView.getTag();
        }
        holder.position = position;
        holder.editContent.setText(mMapContent.get(position));
        holder.tvShowTextNum.setText(mMapContentOther.get(position));
        holder.editContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    index = position;
                }
                return false;
            }
        });
        holder.editContent.clearFocus();
        if (index != -1 && index == position) {
            //强制加上焦点
            holder.editContent.requestFocus();
            //设置光标显示到编辑框尾部
            holder.editContent.setSelection(holder.editContent.getText().length());
            //重置
//            index = -1;
            setSelectionEnd(holder.editContent);
        }

        return convertView;
    }
    class ViewHold {
        /**
         * 评价描述
         **/
        private EditText editContent;
        /**
         * 还剩多少字数的文本
         **/
        private TextView tvShowTextNum;
        int position;

        public ViewHold(View v) {
            //编辑的内容
            editContent = v.findViewById(R.id.edt_content);
//            editContent.setFocusable(true);
//            editContent.setFocusableInTouchMode(true);

//            setSelectionEnd(editContent);
//            showKeyboard(editContent);
            //剩余的字数
            tvShowTextNum = v.findViewById(R.id.tv_content_count);
            editContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    temp = s;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    mMapContent.put(position, s.toString());
                    mMapContentOther.put(position, s.toString().length() + "/500");
                    if (temp.length() > 500) {
                        Toast.makeText(mContext, "评价内容最多输入500字", Toast.LENGTH_SHORT).show();
                        //截取输入的字符砖，只显示0-500位的字符
                        editContent.setText(s.toString().substring(0, 500));
                        //将光标追踪到内容的最后
                        editContent.setSelection(500);
                    }
                }
            });
        }
    }
    /**
     * 光标一直在后面
     **/
    public static void setSelectionEnd(EditText editText) {
        Editable b = editText.getText();
        editText.setSelection(b.length());
    }

    /**
     * 解决在dialog中无法自动弹出对话框的问题
     **/
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void showKeyboard(EditText editText) {
        if (editText != null) {
            //设置可获得焦点
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            //请求获得焦点
            editText.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(inputManager).showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

}
