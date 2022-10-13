package com.example.demo;

import java.lang.reflect.Type;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



@RestController
@RequestMapping("/ss")
public class LoginController {
 
    private static String RSA_WEB_KEY = "_RSA_WEB_Key_"; // 개인키 session key
    private static String RSA_INSTANCE = "RSA"; // rsa transformation
 
    
    /**
     * 키생성요청
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/step1") 
    public @ResponseBody String get(HttpServletRequest request, HttpServletResponse response) {
    	
		initRsa(request);
		
		String RSAModulus = (String) request.getAttribute("RSAModulus");
		String RSAExponent = (String) request.getAttribute("RSAExponent");
		
		HashMap<String, String> resMapData = new HashMap<String, String>();
		resMapData.put("RSAModulus", RSAModulus);
		resMapData.put("RSAExponent", RSAExponent);
				
		Gson resGs = new Gson();
		return resGs.toJson(resMapData);
	}

    /**
     * 복호화처리
     * @param request
     * @param response
     * @param body
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	@RequestMapping("/test_v") 
    public @ResponseBody String test_v(HttpServletRequest request, HttpServletResponse response , @RequestBody String body) throws Exception {
    	
    	Gson reqGs = new Gson();
    	Type type = new TypeToken<HashMap<String,String>>(){}.getType();
		HashMap<String,String> reqMapData = (HashMap<String,String>)reqGs.fromJson(body, type);
    	String encryptString =  reqMapData.get("encryptString");

    	System.out.println("Ciphertext");
    	System.out.println("["+encryptString+"]");
		System.out.println("\n\n");
    	
        HttpSession session = request.getSession();
        PrivateKey privateKey = (PrivateKey) session.getAttribute(LoginController.RSA_WEB_KEY);
 
        // 복호화
		String id = decryptRsa(privateKey, encryptString);
        
		
		HashMap<String, String> resMapData = new HashMap<String, String>();
		resMapData.put("decryptString", id);
		System.out.println("복호화데이터");
		System.out.println("["+id+"]");
		System.out.println("=========================================================");
		
		
		
		
    	Gson resGs = new Gson();
		return resGs.toJson(resMapData);
	}
    
    
    
    
    
    
    
    
    
    
    /**
     * 복호화
     * 
     * @param privateKey
     * @param securedValue
     * @return
     * @throws Exception
     */
    private String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
        Cipher cipher = Cipher.getInstance(LoginController.RSA_INSTANCE);
        byte[] encryptedBytes = hexToByteArray(securedValue);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
        return decryptedValue;
    }
 
    /**
     * 16진 문자열을 byte 배열로 변환한다.
     * 
     * @param hex
     * @return
     */
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) { return new byte[] {}; }
 
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }
 
    /**
     * rsa 공개키, 개인키 생성
     * 
     * @param request
     */
    public void initRsa(HttpServletRequest request) {
        HttpSession session = request.getSession();
 
        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance(LoginController.RSA_INSTANCE);
            generator.initialize(2048);
 
            KeyPair keyPair = generator.genKeyPair();
            KeyFactory keyFactory = KeyFactory.getInstance(LoginController.RSA_INSTANCE);
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
 
            session.setAttribute(LoginController.RSA_WEB_KEY, privateKey); // session에 RSA 개인키를 세션에 저장
 
            RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
            String publicKeyModulus = publicSpec.getModulus().toString(16);
            String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
 
            request.setAttribute("RSAModulus", publicKeyModulus); // rsa modulus 를 request 에 추가
            request.setAttribute("RSAExponent", publicKeyExponent); // rsa exponent 를 request 에 추가
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
