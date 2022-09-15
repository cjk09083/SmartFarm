<?

    $conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");


    if(isset($_GET['device_id']) && isset($_GET['year_time'])&& isset($_GET['month_time'])&& isset($_GET['day_time'])){

        $device_id = $_GET['device_id'];
        $day_time = $_GET['day_time'];
        $month_time = $_GET['month_time'];
        $year_time = $_GET['year_time'];

        $hour_time = 0;

            $data = array();        

        while($hour_time != 24)
        {
            $sql = "SELECT AVG(temp) AS temp, AVG(humid) AS humid, created_at FROM temphumid_v01
                    WHERE month_time = '$month_time' and device_id = '$device_id' and year_time = '$year_time' and day_time = '$day_time' and hour_time = '$hour_time'";
                
            $result = mysqli_query($conn, $sql);
            $row = mysqli_fetch_array($result);    
 
                $temp = (float)$row['temp'];
                $temp = round($temp,2);
                
                $humid = (float)$row['humid'];
                $humid = round($humid,2);

                            $created_at = $row[2];

                            extract($row);
                    
                            array_push($data, 
                                array('temp'.($hour_time) =>$temp,
                                    'humid'.($hour_time) =>$humid,
                                    'created_at'.$hour_time =>$created_at
                                ));
                            
                                // array_push($data2, 
                                // array('humid'.$month =>$humid
                                // ));                                
                       

                //echo  $temp."&".$humid."\n";                   
            
            $hour_time++;
        }
        header('Content-Type: application/json; charset=utf8');
               $json = json_encode(array("data"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
                
                 echo $json;
        //$results = print_r($arr, true); 
        //echo $results;
    }
?>

