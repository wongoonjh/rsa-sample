### SOA(Same Origin Architecture) 정책 해결방법  
- 도메인 출처가 다를 경우 보안정책상 자격증명은 서버로 전송이 안됨.
- 브라우저에서 요청시 쿠키값은 무시된다.  



 ```
 (1) 첫번째 방법 : 어노테이션으로 CORS 적용  

 아래 예제는 '모든 도메인'이 아닌 '특정 도메인'만 허용하는 예제이다.  
    @CrossOrigin 어노테이션은 여러 Properties를 가지고 있다. 그 중, origins는 허용할 도메인을 나타낸다.  
       복수개일 경우 콤마로 구분하여 넣어주면 된다.  
    @CrossOrigin(origins = "http://domain1.com, http://domain2.com")  
    @CrossOrigin --> 모든 도메인의 요청 허용   
    @CrossOrigin(origins = "http://a.com:5500")  --> 특정 도메인의 요청 허용  
```


```
 (2) 두번째 방법 : response 헤더에 정책 설정하여 리턴 
	response.setHeader("Access-Control-Allow-Origin","http://a.com:5500");
	response.setHeader("Access-Control-Allow-Credentials","true");
```


```
 (3) 세번째 방법 : WebMvcConfigurer 에 글로벌하게 설정  
    @Override
    public void addCorsMappings(CorsRegistry registry) {
      registry.addMapping("/**")
              .allowedOrigins("http://a.com:5500")
              .allowedMethods("GET","POST","DELETE","PUT")
              .allowCredentials(true);
  }
```

