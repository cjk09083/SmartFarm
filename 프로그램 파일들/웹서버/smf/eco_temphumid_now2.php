<?

    $conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");


    if(isset($_GET['device_id'])){

        $device_id = $_GET['device_id'];
        $day_time = $_GET['day_time'];
        $month_time = $_GET['month_time'];
        $year_time = $_GET['year_time'];
        $hour_time = 0;

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

               
        while($hour_time != 24)
        {
            $sql = "SELECT AVG(temp) AS temp, AVG(humid) AS humid, AVG(co2) AS co2,  
            AVG(dust) AS dust, created_at FROM temphumid2
                    WHERE month_time = '$month_time' and device_id = '$device_id' and year_time = '$year_time' and day_time = '$day_time' and hour_time = '$hour_time'";
                
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

                            $created_at = $row[3];

                            extract($row);
                    
                            array_push($data, 
                                array('dust'.($hour_time) =>$dust,
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

