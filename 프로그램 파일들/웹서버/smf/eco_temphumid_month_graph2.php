<?

    $conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");


    if(isset($_GET['year_time']) && isset($_GET['month_time']) && isset($_GET['day_time']) )
    {
        //echo = "go";

        $device_id = $_GET['device_id'];
        $year_time_start = $_GET['year_time'];
        if(strlen($year_time_start)<2){$year_time_start="0".$year_time_start;}
        $month_time_start = $_GET['month_time'];
        if(strlen($month_time_start)<2){$month_time_start="0".$month_time_start;}
        $day_time_start = $_GET['day_time'];
        if(strlen($day_time_start)<2){$day_time_start="0".$day_time_start;}

        $date="20".$year_time_start."-".$month_time_start."-".$day_time_start;
        //echo $date."<br>";   //기준날짜
        //$day_time_end=date("d",strtotime($date.'-8 days')) ;       
        //echo date('Y-m-d',strtotime($date.'-7 days')); //7일전

        $day_time0 = '00:00:00';
        $day_time1 = '23:59:59';
        $time_start = date("Y-m-d",strtotime($date.'-1 months')).' '.$day_time0;
        //echo $time_start."\n"; 
        $time_end = $date.' '.$day_time1;
               // echo $time_end; 

        $index = 0;
        
        $sql = "SELECT AVG(temp) AS temp_avg, 
                      AVG(humid) AS humid_avg, 
                      AVG(co2) AS co2_avg, 
                       AVG(dust) AS dust_avg,                  
                       created_at FROM temphumid2 where device_id = '$device_id' and created_at between '$time_start' and '$time_end' GROUP BY date_format(created_at, '%Y%m%d')";
            
        $result = mysqli_query($conn, $sql);
        
        $array1 = array();    

        while($data = mysqli_fetch_array($result))
        {
            $temp_avg = round($data['temp_avg'],2);
            $humid_avg = round($data['humid_avg'],2);
            $co2_avg = round($data['co2_avg'],2);
            $dust_avg = round($data['dust_avg'],2);

            $created_at = $data['created_at'];

                            array_push($array1, 
                                array('dust'.$index =>$dust_avg,
                                    'temp'.$index =>$temp_avg,
                                    'humid'.$index =>$humid_avg,
                                    'co2'.$index =>$co2_avg,
                                    'created_at'.$index =>$created_at
                                ));
                            
            

            $index++;
        }       
         
$sql2 = "SELECT water FROM temphumid2
                    WHERE device_id = '$device_id'  ORDER BY `temphumid_id` DESC LIMIT 1";
                
            $result2 = mysqli_query($conn, $sql2);
            while($data2 = mysqli_fetch_array($result2, MYSQL_NUM))
            {
                            array_push($array1,  array('water' =>$data2[0]));
                           // echo "water:".$data2[0]."\n";
            }  
            
          header('Content-Type: application/json; charset=utf8');
               $json = json_encode(array("data"=>$array1), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
                echo $json;

             

    }
    else{
      echo "fail";
    }
?>
