package com.wizinno.livgo.app.data.live;

/**
 * Created by HP on 2017/8/25.
 */
public class LiveThumbnail {

    private String channel_id;

    private int create_time;

    private int event_type;

    private String pic_url;

    private String stream_id;

    private int t;

    private String sign;

   private String pic_full_url;

    public String getPic_full_url() {
        return pic_full_url;
    }

    public void setPic_full_url(String pic_full_url) {
        this.pic_full_url = pic_full_url;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public int getEvent_type() {
        return event_type;
    }

    public void setEvent_type(int event_type) {
        this.event_type = event_type;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
