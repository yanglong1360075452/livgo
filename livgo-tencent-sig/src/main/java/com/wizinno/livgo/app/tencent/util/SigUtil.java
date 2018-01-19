package com.wizinno.livgo.app.tencent.util;

import com.tls.sigcheck.tls_sigcheck;

import java.io.*;

/**
 * Created by HP on 2017/6/20.
 */
public class SigUtil {

    private static String path = "/tmp/hsperfdata_www/";

    public static String generateSig(long userId){

        tls_sigcheck demo = new tls_sigcheck();

        // 使用前请修改动态库的加载路径
//        String sigPath = SigUtil.class.getClassLoader().getResource(File.separator+"jnisigcheck.dll").getPath();
        String sigPath = path+"jnisigcheck.so";
        String privatePath = path+"private_key";
        String publicPath = path+"public_key";

        demo.loadJniLib(sigPath);
        //demo.loadJniLib("C:\\Windows\\System32\\jnisigcheck.so");

        // File priKeyFile = new File("private_key");
        File priKeyFile = new File( privatePath);

        StringBuilder strBuilder = new StringBuilder();
        String s = "";

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(priKeyFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            while ((s = br.readLine()) != null) {
                strBuilder.append(s + '\n');
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String priKey = strBuilder.toString();
        int ret = demo.tls_gen_signature_ex2("1400033525", "sig_"+userId, priKey);

        if (0 != ret) {
            System.out.println("ret " + ret + " " + demo.getErrMsg());
        }
        else
        {
            System.out.println("sig:\n" + demo.getSig());
        }
        File pubKeyFile = new File(publicPath);
        try {
            br = new BufferedReader(new FileReader(pubKeyFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        strBuilder.setLength(0);
        try {
            while ((s = br.readLine()) != null) {
                strBuilder.append(s + '\n');
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String pubKey = strBuilder.toString();
        ret = demo.tls_check_signature_ex2(demo.getSig(), pubKey, "1400033525", "sig_"+userId);
        if (0 != ret) {
            System.out.println("ret " + ret + " " + demo.getErrMsg());
        }
        else
        {
            System.out.println("--\nverify ok -- expire time " + demo.getExpireTime() + " -- init time " + demo.getInitTime());
        }

        return demo.getSig();
    }

    public static void main(String[] args){

//        String filePath = SigUtil.class.getResource("/jnisigcheck.so").getPath();
//        File file = new File("jnisigcheck.so");
        String property = System.getProperty("jnisigcheck.so");
        File directory = new File("jnisigcheck.so");
        String path = directory.getPath();
        String a = directory.getAbsolutePath();
        generateSig(1);
    }

}
