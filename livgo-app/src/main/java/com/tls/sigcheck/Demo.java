package com.tls.sigcheck;

import com.tls.sigcheck.tls_sigcheck;
import org.springframework.util.ResourceUtils;

import java.io.*;

// 由于生成 sig 和校验 sig 的接口使用方法类似，故此处只是演示了生成 sig 的接口调用

// 使用的编译命令是
// javac -encoding utf-8 Demo.java
// 使用的运行命令是
// java Demo

public class Demo { 

    public static void main(String args[]) throws Exception {
        
        tls_sigcheck demo = new tls_sigcheck();
        
        // 使用前请修改动态库的加载路径
        File FileDll = ResourceUtils.getFile("classpath:jnisigcheck.dll");
        String s1 = FileDll.toString();
        demo.loadJniLib(s1);
        //demo.loadJniLib("C:\\Windows\\System32\\jnisigcheck.so");
        
       // File priKeyFile = new File("private_key");
        File priKeyFile = ResourceUtils.getFile("classpath:private_key");
        StringBuilder strBuilder = new StringBuilder();
        String s = "";
        
        BufferedReader br = new BufferedReader(new FileReader(priKeyFile));
        while ((s = br.readLine()) != null) {
            strBuilder.append(s + '\n');
        }
        br.close();
        String priKey = strBuilder.toString();        
		int ret = demo.tls_gen_signature_ex2("1400033525", "sig_"+1, priKey);
        
        if (0 != ret) {
            System.out.println("ret " + ret + " " + demo.getErrMsg());
        }
        else
        {
            System.out.println("sig:\n" + demo.getSig());
        }      

        //File pubKeyFile = new File("public.pem");
        File pubKeyFile = ResourceUtils.getFile("classpath:public_key");
        br = new BufferedReader(new FileReader(pubKeyFile));
		strBuilder.setLength(0);
        while ((s = br.readLine()) != null) {
            strBuilder.append(s + '\n');
        }
        br.close();
        String pubKey = strBuilder.toString();        
		ret = demo.tls_check_signature_ex2(demo.getSig(), pubKey, "1400033525", "sig_"+1);
        if (0 != ret) {
            System.out.println("ret " + ret + " " + demo.getErrMsg());
        }
        else
        {
            System.out.println("--\nverify ok -- expire time " + demo.getExpireTime() + " -- init time " + demo.getInitTime());
        }      
    }
}
