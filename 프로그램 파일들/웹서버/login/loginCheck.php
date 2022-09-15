 <?php

if(!isset($_SESSION)) {
    session_start();
    //echo session_id();
    //echo('session start');
} 

    
  //if(isset($COOKIE['PHPSESSID'])){echo $COOKIE['PHPSESSID'];}

if(isset($_SESSION['isLogin']) && $_SESSION['isLogin'] == 'Y'){
 //echo 1;
 //echo $_SESSION['userNM']."(자동)";
 $_SESSION['state']= 2;
 header("Location:loginResult.php");

}
else{
    @extract($_POST); // POST 전송으로 전달받은 값 처리
    if(isset($loginID) && !empty($loginID) && isset($loginPW) && !empty($loginPW)){

        $deviceID = $deviceID ? $deviceID : '';

        if(isset($deviceID) && !empty($deviceID)){ // 모바일 접속이면
            //require_once '/dbClass.php';
            $conn=new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");
            //require_once 'loginClass.php';
            //$c=new LoginClass();
            $sql = "SELECT * from members where userPW='$loginPW' and userID= '$loginID' ";
                if($result = mysqli_query($conn,$sql)) { //성공
                    $row = mysqli_fetch_array($result, MYSQL_NUM);
                    //echo $row[0];
                    if(empty($row[0])) {                    
                        $result= 0;
                    }
                    else {
                        $result= 1;
                        $data = array();        
                         // while($row = mysqli_fetch_array($result, MYSQL_NUM)
                        // {
                            extract($row);
                            
                            $name=$row[2];
                            array_push($data, 
                                array('id'=>$row[0],
                                'userID'=>$row[1],
                                'userNM'=>$row[2],
                                'userPW'=>md5($row[3])
                            ));
                        // }                                           
                        
                        header('Content-Type: application/json; charset=utf8');
                        $json = json_encode(array("members"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);                        //echo $result;
                    }
                } else {
                    $result= '-1';
                }

            if($result > 0 ) {
                 session_save_path('./_tmp/session');
                 $_SESSION['isLogin'] = 'Y';
                 $_SESSION['userID'] = $loginID;
                 $_SESSION['userPW'] = md5($loginPW);
                 $_SESSION['userNM'] = $name;
                 $_SESSION['ip'] = $_SERVER['REMOTE_ADDR'];
                 $_SESSION['ua'] = $_SERVER['HTTP_USER_AGENT'];
                 $_SESSION['deviceID']= $deviceID;
                 $_SESSION['state']= 1;
                echo $result; // 로그인 성공이면 1을 반환
               
                echo $json;
                //echo $name;
                //header("Location:loginResult.php");

            } else if($result == 0) {
                //echo "ID = ".$loginID.", PW = ".$loginPW."\n";
                echo 0; // 로그인 정보 틀림
                $_SESSION['isLogin'] = 'N';
            } else {
                echo '-1'; // Phone mismatch
                $_SESSION['isLogin'] = 'N';
            }

        }

    } else {
        echo('no data');
    }

 }
?>

