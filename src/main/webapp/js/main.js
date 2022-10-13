 
 
 // Depends on rsa.js,  jsbn.js,  prng4.js and rng.js
 
 
 
 	const BASE_URL = "http://a.com:8881";
 	
 	/*
 	 키생성 API
 	 @ param : n/a
 	 @ result : RSAModulus
                RSAExponent
 	 */
    const KEY_CREATE_API_URI = "/ss/step1";
 	/*
 	 복호화 건증
 	 @ param : encryptString
 	 @ result : decryptString
 	 */
    const AUTH_API_URI = "/ss/test_v";


    const axiosService = axios.create({
        baseURL: BASE_URL, // api base_url
        timeout: 5000, // request timeout
        //withCredentials: true,
        //crossDomain: true,
        headers : {
            'Content-Type': 'application/json;charset=UTF-8',
        }
	});

    
    //1.키생성 요청
    function getPubkey() {
        $("#reskeyinfoM").val("요청중...");
       
        axiosService.get(KEY_CREATE_API_URI)
            .then(function (response) {
                // handle success
                //키생성 결과정보
                $("#reskeyinfoM").val(response.data.RSAModulus);
                $("#reskeyinfoE").val(response.data.RSAExponent);
            })
            .catch(function (response) {
                // handle error
                console.log("getPubkey|error!!!"+JSON.stringify(response));
                $("#reskeyinfoM").val("통신오류!!");
            });
    }


    //2. 암호화
    function do_encrypt() {
        var reskeyinfoM = $("#reskeyinfoM").val();
        var reskeyinfoE = $("#reskeyinfoE").val();
        var PlanText = $("#PlanText").val(); 


        var rsa = new RSAKey();
        rsa.setPublic(reskeyinfoM, reskeyinfoE);
        
        
        //3. Ciphertext (hex): 암호화결과정보 
        var res = rsa.encrypt(PlanText);
        if(res) {
           $("#ciphertext").val(res);
        }
    }



    //4. 정보확인
    function validate() {
        //encryptString
        var reqData = {};
        reqData.encryptString = $("#ciphertext").val();


        axiosService.post(AUTH_API_URI, reqData)
            .then(function (response) {
                // handle success
                $("#resultPlanText").val(response.data.decryptString);
                
            })
            .catch(function (error) {
                // handle error
                console.log("validate|error!!!"+JSON.stringify(error));
                alert("error:"+JSON.stringify(error));
            })
    }