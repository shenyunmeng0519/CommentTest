package teacherkline.sikkha.com.textvip;

import com.luck.picture.lib.entity.LocalMedia;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/3.
 */

public class MemberEntityImpl implements Serializable {

    /**
     * 会员卡号
     */
    String vipno;
    /**
     * 加密会员卡号
     */
    String VipnoCrc;
    /**
     * 剩余积分
     * */
     String Rest;
    /**
     *  余额
     * */
    String Amt;
    float star_num;

    public float getStar_num() {
        return star_num;
    }

    public void setStar_num(float star_num) {
        this.star_num = star_num;
    }

    public String getVipno() {
        return vipno;
    }

    public void setVipno(String vipno) {
        this.vipno = vipno;
    }

    public String getVipnoCrc() {
        return VipnoCrc;
    }

    public void setVipnoCrc(String vipnoCrc) {
        VipnoCrc = vipnoCrc;
    }

    public String getRest() {
        return Rest;
    }

    public void setRest(String rest) {
        Rest = rest;
    }

    public String getAmt() {
        return Amt;
    }

    public void setAmt(String amt) {
        Amt = amt;
    }

    /**来店记录需要的字段开始*/
    /**
     * 消费日期
     */
    String vdate;
    /**
     * 消费金额
     */
    String Vamt;
    /**
     * 增加积分
     * */
    String Vcnt;
    /**
     *  门店编码
     * */
    String shpno;
    /**
     *  门店名称
     * */
    String shpname;
    /**
     *  购物小票号码
     * */
    String Vsno;
    /**
     *  备注
     * */
    String Vdsc;


    public String getVdate() {
        return vdate;
    }

    public void setVdate(String vdate) {
        this.vdate = vdate;
    }

    public String getVamt() {
        return Vamt;
    }

    public void setVamt(String Vamt) {
        Vamt = Vamt;
    }

    public String getVcnt() {
        return Vcnt;
    }

    public void setVcnt(String Vcnt) {
        Vcnt = Vcnt;
    }

    public String getShpno() {
        return shpno;
    }

    public void setShpno(String shpno) {
        shpno = shpno;
    }

    public String getShpname() {
        return shpname;
    }

    public void setShpname(String shpname) {
        shpname = shpname;
    }

    public String getVsno() {
        return Vsno;
    }

    public void setVsno(String Vsno) {
        Vsno = Vsno;
    }

    public String getVdsc() {
        return Vdsc;
    }

    public void setVdsc(String Vdsc) {
        Vdsc = Vdsc;
    }

    /**来店记录另外需要的字段结束*/

    /**积分兑换记录需要的字段开始*/
    /**
     * 兑换商品编码
     */
    String pluno;
    /**
     * 兑换商品名称
     */
    String pluname;
    /**
     * 兑换的券号
     * */
    String TicketNo;
    /**
     *  券名称
     * */
    String Ticketname;
    /**
     *  抵扣金额
     * */
    String Ticketamt;
    /**
     *  有效期
     * */
    String Ticketdate;
    /**
     *  已使用或未使用
     * */
    String Vstatus;


    public String getPluno() {
        return pluno;
    }

    public void setPluno(String pluno) {
        this.pluno = pluno;
    }

    public String getPluname() {
        return pluname;
    }

    public void setPluname(String pluname) {
        pluname = pluname;
    }

    public String getTicketNo() {
        return TicketNo;
    }

    public void setTicketNo(String TicketNo) {
        TicketNo = TicketNo;
    }

    public String getTicketname() {
        return Ticketname;
    }

    public void setTicketname(String Ticketname) {
        Ticketname = Ticketname;
    }

    public String getTicketamt() {
        return Ticketamt;
    }

    public void setTicketamt(String Ticketamt) {
        Ticketamt = Ticketamt;
    }

    public String getTicketdate() {
        return Ticketdate;
    }

    public void setTicketdate(String Ticketdate) {
        Ticketdate = Ticketdate;
    }

    public String getVstatus() {
        return Vstatus;
    }

    public void setVstatus(String Vstatus) {
        Vstatus = Vstatus;
    }

    

    /**积分兑换记录另外需要的字段结束*/
    private int ProductId;
    private String CommentContent;
    public List<LocalMedia> CommentImgs;
    private double StarCount;

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getCommentContent() {
        return CommentContent;
    }

    public void setCommentContent(String commentContent) {
        CommentContent = commentContent;
    }

    public List<LocalMedia> getCommentImgs() {
        return CommentImgs;
    }

    public void setCommentImgs(List<LocalMedia> commentImgs) {
        CommentImgs = commentImgs;
    }

    public double getStarCount() {
        return StarCount;
    }

    public void setStarCount(double starCount) {
        StarCount = starCount;
    }

}
