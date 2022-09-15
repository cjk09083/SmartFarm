<?

    $conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");


    if(isset($_GET['device_id']) && isset($_GET['year_time'])&& isset($_GET['month_time'])&& isset($_GET['day_time'])){

        $device_id = $_GET['device_id'];
        $day_time = $_GET['day_time'];
        $month_time = $_GET['month_time'];
        $year_time = $_GET['year_time'];

        $hour_time = 0;

            $data = array();        

        while($hour_time != 16)
        {
            $sql = "SELECT AVG(temp) AS temp, AVG(humid) AS humid, AVG(co2) AS co2,  
            AVG(r_lux) AS r_lux, AVG(l_lux) AS l_lux, created_at FROM temphumid_v01
                    WHERE month_time = '$month_time' and device_id = '$device_id' and year_time = '$year_time' and day_time = '$day_time' and hour_time = '$hour_time'";
                
            $result = mysqli_query($conn, $sql);
            $row = mysqli_fetch_array($result);    
 
                $temp = (float)$row['temp'];
                $temp = round($temp,2);
                
                $humid = (float)$row['humid'];
                $humid = round($humid,2);

                $co2 = (float)$row['co2'];
                $co2 = round($co2,2);

                $r_lux = (float)$row['r_lux'];
                $r_lux = round($r_lux,2);

                $l_lux = (float)$row['l_lux'];
                $l_lux = round($l_lux,2);

                $lux=($l_lux+$r_lux)/2;

                            $created_at = $row[3];

                            extract($row);
                    
                            array_push($data, 
                                array('lux'.($hour_time) =>$lux,
                                    'temp'.($hour_time) =>$temp,
                                    'humid'.($hour_time) =>$humid,
                                    'co2'.($hour_time) =>$co2,
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

