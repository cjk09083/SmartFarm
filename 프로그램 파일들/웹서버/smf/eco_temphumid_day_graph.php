<?

    $conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");


    if(isset($_GET['device_id']) && isset($_GET['year_time'])&& isset($_GET['month_time'])&& isset($_GET['day_time'])){

        $device_id = $_GET['device_id'];
        $day_time = $_GET['day_time'];
        $month_time = $_GET['month_time'];
        $year_time = $_GET['year_time'];

        $hour_time = 0;
        $out_temp=0;
        $out_humid=0;
        $out_dust=0;
        $data = array();        

        while($hour_time != 24)
        {
            $sql = "SELECT AVG(temp) AS temp, AVG(humid) AS humid, AVG(co2) AS co2,  
            AVG(dust) AS dust, AVG(out_temp) AS out_temp, AVG(out_humid) AS out_humid, AVG(out_dust) AS out_dust, created_at AS created_at FROM temphumid2 WHERE month_time = '$month_time' and device_id = '$device_id' and year_time = '$year_time' and day_time = '$day_time' and hour_time = '$hour_time'";
                
            $result = mysqli_query($conn, $sql);
            $row = mysqli_fetch_array($result);    
 
                $temp = (float)$row['temp'];
                $temp = round($temp,2);
                
                $humid = (float)$row['humid'];
                $humid = round($humid,2);

                $co2 = (float)$row['co2'];
                $co2 = round($co2,2);

                $dust = (float)$row['dust'];
                $dust = round($dust,2);

                $out_temp = (float)$row['out_temp'];
                $out_temp = round($out_temp,2);

                $out_humid = (float)$row['out_humid'];
                $out_humid = round($out_humid,2);

                $out_dust = (float)$row['out_dust'];
                $out_dust = round($out_dust,2);

                            $created_at = $row['created_at'];

                            extract($row);
                    
                            array_push($data, 
                                array('dust'.($hour_time) =>$dust,
                                    'temp'.($hour_time) =>$temp,
                                    'humid'.($hour_time) =>$humid,
                                    'co2'.($hour_time) =>$co2,
                                    'out_temp'.($hour_time) =>$out_temp,
                                    'out_humid'.($hour_time) =>$out_humid,
                                    'out_dust'.($hour_time) =>$out_dust,

                                    'created_at'.$hour_time =>$created_at
                                ));
                            
                                // array_push($data2, 
                                // array('humid'.$month =>$humid
                                // ));                                
                       

                //echo  $temp."&".$humid."\n";                   
            
            $hour_time++;
        }

            $sql2 = "SELECT water FROM temphumid2
                    WHERE device_id = '$device_id'  ORDER BY `temphumid_id` DESC LIMIT 1";
                
            $result2 = mysqli_query($conn, $sql2);
            while($data2 = mysqli_fetch_array($result2, MYSQL_NUM))
            {
                            array_push($data,  array('water' =>$data2[0]));
                           // echo "water:".$data2[0]."\n";
            }   


        header('Content-Type: application/json; charset=utf8');
               $json = json_encode(array("data"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
                
                 echo $json;
        //$results = print_r($arr, true); 
        //echo $results;
    }
?>

