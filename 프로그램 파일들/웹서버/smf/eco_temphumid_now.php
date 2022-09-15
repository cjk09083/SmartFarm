<?

    $conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");


    if(isset($_GET['device_id'])){

        $device_id = $_GET['device_id'];
            $data = array();        

       $sql2 = "SELECT * FROM temphumid2
                    WHERE device_id = '$device_id'  ORDER BY `temphumid_id` DESC LIMIT 1";
                
            $result2 = mysqli_query($conn, $sql2);
            while($data2 = mysqli_fetch_array($result2, MYSQL_NUM))
            {
                        array_push($data, 
                            array(
                                    'temp' =>$data2[1],
                                    'humid' =>$data2[2],
                                    'co2' =>$data2[3],
                                    'dust'=>$data2[4],
                                    'water' =>$data2[5],
                                    'created_at' =>$data2[6]
                                ));
            }

               


        header('Content-Type: application/json; charset=utf8');
               $json = json_encode(array("data"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
                
                 echo $json;
        //$results = print_r($arr, true); 
        //echo $results;
    }
?>

