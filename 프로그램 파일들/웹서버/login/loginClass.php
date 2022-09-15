<?php
class LoginClass {
    function MobileUserAuthCheck($u,$p,$k) {
        if(!isset($u) || !isset($p) || empty($u) || empty($p)) {
            return 0;
        } else {
            global $dbconn; // global 키워드를 사용하여 변수에 선언할 경우 함수 밖의 변수를 참조할 수 있다
            $u = preg_replace("/[\s\t\'\;\"\=\--]+/","", $u); // 공백이나 탭 제거(사용자 실수 방지)
            $p = preg_replace("/[\s\t\'\;\"\=\--]+/","", $p); // 공백이나 탭 제거, 특수문자 제거
            // SQL injection 검사
            $u = mysqli_real_escape_string($dbconn,$u); // <script>documnet.cookie();</script> 공격 방지
            $p = mysqli_real_escape_string($dbconn,$p); // 
            if(preg_match('/[\/\\\\]/', $u)) $this->popup('비정상적 접근입니다'); // no slashes
            //if(preg_match('/(and|null|where|limit)/i', $u)) { // i는 대소문자 구별하지 말라
            //    $this->popup('비정상적 접근입니다');
            //} // 회원아이디가 일치하는 경우가 생겨서 이 구문은 사용을 자제해야 할 듯해서 주석처리함.
            if(!preg_match('/^[0-9a-zA-Z\~\!\@\#\$\%\^\&\*\(\)]{7,}$/',$p)) { // 최소7자리 이상 허용 문자만 통과
                $this->popup('정보가 올바르지 않습니다');
                // 같은 클래스안에 있는 다른 함수를 사용할 때 $this 로 기술
            }

            $sql = "select userNM ";
            $sql.= "from member where userPW=md5('".$p."') and userID= '".$u."' ";
            if($result = mysql_query($sql,$dbconn)) { //성공
                $row = mysql_fetch_array($result);
                if($row == NULL) return 0;
                return $row;
            } else {
                return '-1';
            }
        }
    }

    // 로그인함수 경고메시지
    function popup($msg) {
        echo "<script>alert('".$msg."');history.go(-1);</script>";
    }
} // End of LoginClass
?>

