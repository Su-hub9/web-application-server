# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
* InputStream , OutputStream은 Byte 단위라서 영어나 숫자 등은 잘 출력되는데, 단위가 2Byte인 한글은 깨져서 출력된다.  
  그래서 char 단위인 InputStreamReader를 쓴다. 또한 한글자씩 받아와야 하는 상황이 아니면 버퍼에 저장하여 한꺼번에 받는 방식을 많이 사용한다.
  ```
  InputStream in = connection.getInputStream();
  BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
  ```
* BufferedReader.readLine()은 개행문자가 포함되어야 내부 blocking이 풀리면서 wihle문이 실행한다.
* 자바에서의 개행문자는 "\n" 이지만, 스트림에서의 개행문자는 "\r\n"이 개행문자이다.

### 요구사항 2 - get 방식으로 회원가입
* GET 방식 사용 시 문자열 길이 제한 : 

### 요구사항 3 - post 방식으로 회원가입
* 

### 요구사항 4 - 302 status code 적용
* 

### 요구사항 5 - 로그인하기
* 

### 요구사항 6 - 사용자 목록 출력
* 

### 요구사항 7 - CSS 지원하기
* 