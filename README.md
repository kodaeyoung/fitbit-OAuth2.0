# JAVA-SpringBoot-Fitbit OAuth2.0

### SpringSecurity 없이 WebClient를 사용하여 프로토콜을 직접 구현
 
### 1.PKCEUtil -> XSS , MITM 공격을 방지하기 위한 보안 기능

#### - PKCE 흐름

1. Code Verifier 생성: 클라이언트는 generateCodeVerifier()를 사용해 난수를 생성하여 Code Verifier를 만듬
2. Code Challenge 생성: 클라이언트는 generateCodeChallenge(codeVerifier)를 사용하여 Code Verifier로부터 Code Challenge를 만듬
3. Authorization Request: 클라이언트는 Code Challenge를 포함하여 Authorization Server에 인증 요청을 보냄
4. Authorization Code 발급: 서버는 Code Challenge와 관련된 Authorization Code를 반환
5. Token Request: 클라이언트는 받은 Authorization Code와 함께 Code Verifier를 서버에 보냄
6. Code Verifier 검증: 서버는 Code Verifier와 Code Challenge를 비교하여 일치하는지 확인하고, 검증이 성공하면 Access Token을 발급

### 2.WebClientConfig - 비동기 적으로 http요청을 처리하기 위한 webClient설정

Http 요청을 위한 HttpClient를 생성 

대부분의 OAuth2.0의 Redirection URL은 Https를 요구하는 경우가 많기 때문에, 임시 SSL을 설정 

### 3. FitbitController - 실제 OAuth2.0 프로토콜을 구현한 클래스

#### 주요 흐름

#### /authorize:

1. 사용자가 Fitbit OAuth 인증을 시작하는 authorize 요청을 처리
2. PKCE를 사용하여 code_verifier와 code_challenge를 생성한 후, 사용자에게 Fitbit 로그인 페이지로 리다이렉션
3. code_verifier는 세션에 저장되어 나중에 토큰 요청에 사용

#### /callback:

1. 사용자가 Fitbit의 인증 화면에서 로그인 후 authorization code를 리다이렉트 URI로 반환
2. 이 코드가 /callback 엔드포인트로 전달되며, code_verifier를 세션에서가져와서 함께 사용하여 access_token을 요청
3. 액세스 토큰을 받으면 세션에 저장

### 4. FitbitRestController - 인증 이후 Access Key를 사용하여 사용자의 데이터에 접근하는 클래스 
