# SmartFarm
<div>
<img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=Android&logoColor=white"/>
<img src="https://img.shields.io/badge/Arduino-00979D?style=for-the-badge&logo=Arduino&logoColor=white"/></a>
<img src="https://img.shields.io/badge/PHP-777BB4?style=for-the-badge&logo=PHP&logoColor=white"/>
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/>
<a href="https://ieeexplore.ieee.org/document/9268238" target="_blank">
<img src="https://img.shields.io/badge/관련논문-FF0000?style=for-the-badge&logo=Apache&logoColor=white"/>
</a>
</div>

## 스마트팜 자동제어 및 모니터링  

<img src="https://github.com/cjk09083/SmartFarm/blob/main/%EC%82%AC%EC%A7%84%20%EB%B0%8F%20%EC%98%81%EC%83%81/%EC%A1%B0%EA%B0%90%EB%8F%84/perspective%20view.jpg" width="100%"/>

## 목적
- 매 시간 최적의 광량을 농장에 공급해주는 자동제어 스마트팜 개발
- 스마트팜의 상태를 어플리케이션으로 모니터링하고 설치된 각종 기기를 원격 제어

## 담당
- 안드로이드 어플리케이션
- 웹서버 php, sql 
- 아두이노 NANO 와 센서, 모터 제어 (I2C, Serial, PWM)
- 아두이노 WEMOS 와 서버 통신 (Wifi) 
- 앱 <-> 서버 <-> 아두이노 통신 (HTTP, MQTT)

## 기능
### 1. 센서 정보 서버업로드 및 모니터링
 - 실내에서 측정한 각종 센서(온습도, 광량, 이산화탄소) 정보를 서버에 업로드
 - 전용 어플리케이션(Android)에서 이를 모니터링
<div align="center">
<img src="https://github.com/cjk09083/SmartFarm/blob/main/%EC%82%AC%EC%A7%84%20%EB%B0%8F%20%EC%98%81%EC%83%81/%EC%96%B4%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%20%EB%A9%94%EC%9D%B8%ED%99%94%EB%A9%B4.jpg" width="30%"/>
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://github.com/cjk09083/SmartFarm/blob/main/%EC%82%AC%EC%A7%84%20%EB%B0%8F%20%EC%98%81%EC%83%81/%EC%96%B4%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%20%EB%88%84%EC%A0%81%EA%B4%91%EB%9F%89%20%ED%99%94%EB%A9%B4.jpg" width="30%"/>
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://github.com/cjk09083/SmartFarm/blob/main/%EC%82%AC%EC%A7%84%20%EB%B0%8F%20%EC%98%81%EC%83%81/%EC%96%B4%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%20%EB%8D%B0%EC%9D%B4%ED%84%B0%20%EB%A6%AC%EC%8A%A4%ED%8A%B8%20%ED%99%94%EB%A9%B4.jpg" width="30%"/>
</div></br>

 - 측정한 데이터중 온습도 데이터는 차트로 확인
<div align="center">
<img src="https://github.com/cjk09083/S.O.S/blob/main/%EC%82%AC%EC%A7%84/Screenshot_20180926-122917.png" width="55%"/>
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://github.com/cjk09083/S.O.S/blob/main/%EC%82%AC%EC%A7%84/motion.jpg" width="35%"/>
</div></br>

## 관련논문
<a href="https://ieeexplore.ieee.org/document/9268238" target="_blank">
Light Control Smart Farm Monitoring System with Reflector Control
</a>
