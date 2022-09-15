 <?php


if(!isset($_SESSION)) {
    session_start();    
}    

if(isset($_SESSION['userID'])&&isset($_SESSION['userPW'])) {     
        //echo "success";
           $userID = (string) $_SESSION['userID']; 
           $userPW =(string)   $_SESSION['userPW'];
           $name = (string)$_SESSION['userNM'] ; 
           $ip = (string)  $_SESSION['ip']; 
           $ua =  (string) $_SESSION['ua']; 
           $deviceID = (string) $_SESSION['deviceID'];
           $state = (int)$_SESSION['state'];
           $conn=new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");

        $sql = "INSERT INTO login
        (
            userID, 
            userPW,
            name,
            ip,
            ua,
            deviceID,
            state,
            login_at            
        )
        
        VALUES
        (
            '$userID',
            '$userPW',
            '$name',
            '$ip',
            '$ua',
            '$deviceID',
            '$state',
            now()            
        )";

         $result = mysqli_query($conn,$sql); //성공

         // echo $userID." ";
         // echo $userPW." ";
         // echo $name." ";
         // echo $ip." ";
         // echo $ua." ";
         // echo $deviceID." ";
         // echo $state."\n";

        //session_destroy(); 
        echo 1;
        echo $name;
        //echo $name;."(자동)";
    

} else {
    echo('no data2');
    //echo $_SESSION['userID'];
}
?>

