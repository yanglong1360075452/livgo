package com.wizinno.livgo.app.utils;

import com.wizinno.livgo.app.document.Constant;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/17.
 */
public class Url {
    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public  static String getPushUrl(Long liveId){
        String bizid= Constant.BIZID;
        String securityKey=Constant.HONGJING_PUSH_ANTI_THEFT_CHAIN_KEY;
        StringBuilder url = new StringBuilder();

        long insertTime = (long) (System.currentTimeMillis() / 1000);
        long outTime = insertTime + 60*60*24*7;//24天
        url.append("rtmp://").append(bizid).append(Constant.URL_TENCENT_COMMON_PUSH).append(bizid).append("_");
        url.append(liveId).append("?").append("bizid=").append(bizid).append("&")
                .append(getSafeUrl(securityKey, bizid+"_"+liveId, outTime));

        return  url.toString();
    }
    public static Map getPlayUrl(Long liveId){
        String bizid= Constant.BIZID;
        String securityKey=Constant.HONGJING_PUSH_ANTI_THEFT_CHAIN_KEY;
        //生成播放的url
        StringBuilder url = new StringBuilder();
        url.append("rtmp://").append(bizid).append(Constant.URL_TENCENT_COMMON_PLAY).append(bizid).append("_");
        url.append(liveId);
        StringBuilder url1 = new StringBuilder();

        url1.append("http://").append(bizid).append(Constant.URL_TENCENT_COMMON_PLAY).append(bizid).append("_");
        url1.append(liveId).append(".flv");

        StringBuilder url2 = new StringBuilder();

        url2.append("http://").append(bizid).append(Constant.URL_TENCENT_COMMON_PLAY).append(bizid).append("_");
        url2.append(liveId).append(".m3u8");


        System.out.println(url.toString());
        System.out.println(url1.toString());
        System.out.println(url2.toString());

        String rtmpUrl = url.toString();
        String flvUrl = url1.toString();
        String hlsUrl = url2.toString();

        Map<String, Object> map = new HashMap<>();
        map.put("RTMP",rtmpUrl);
        map.put("FLV",flvUrl);
        map.put("HLS",hlsUrl);
        return map;
    }
    private static String getSafeUrl(String key, String streamId, long txTime) {
        String input = new StringBuilder().
                append(key).
                append(streamId).
                append(Long.toHexString(txTime).toUpperCase()).toString();

        String txSecret = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            txSecret  = byteArrayToHexString(
                    messageDigest.digest(input.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return txSecret == null ? "" :
                new StringBuilder().
                        append("txSecret=").
                        append(txSecret).
                        append("&").
                        append("txTime=").
                        append(Long.toHexString(txTime).toUpperCase()).
                        toString();
    }

    private static String byteArrayToHexString(byte[] data) {
        char[] out = new char[data.length << 1];

        for (int i = 0, j = 0; i < data.length; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }

}
