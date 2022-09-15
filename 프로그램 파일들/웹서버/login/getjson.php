<?php 

    //error_reporting(E_ALL); 
    //ini_set('display_errors',1); 

    $conn=new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");
    $sql = "SELECT * from members";

    if ($result = mysqli_query($conn,$sql))
    {
        $data = array(); 

        //while($row=$result->fetch(PDO::FETCH_ASSOC))
        while($row = mysqli_fetch_array($result, MYSQL_NUM))
        {
            extract($row);
    
            array_push($data, 
                array('id'=>$row[0],
                'userID'=>$row[1],
                'userNM'=>$row[2],
                'userPW'=>$row[3]
            ));
        }

        header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("members"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
        echo $json;
    }

?>