# RSA
1. 키생성
  client -> 서버요청 : /ss/step1
2. public키로 암호화
  CLIENT에서 암호화진행
3. 복호화 요청
  client -> 서버요청 : /ss/test_v
  
  
## Install
/rsa-sample/src/main/resources/application.properties
LISTEN port 변경후
```
server.port=8881
```


## Run
```
Run as > String Boot App
```

### Advanced usage
가나다라마바사
- ** AAAAAAA settings**
  - ```timeout=5000``` 통신 타임아웃시간설정
  - ** BBBBBBB settings**

## 참고사이트
[stanford.edu](http://www-cs-students.stanford.edu/~tjw/jsbn/)
