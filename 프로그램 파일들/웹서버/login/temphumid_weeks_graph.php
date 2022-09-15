<?

    $conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");


    if(isset($_GET['year_time']) && isset($_GET['month_time']) && isset($_GET['day_time']) )
    {
        //echo = "go";

        $device_id = 1;
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
        $time_start = date("Y-m-d",strtotime($date.'-7 days')).' '.$day_time0;
        //echo $time_start."\n"; 
        $time_end = $date.' '.$day_time1;
               // echo $time_end; 

        $index = 0;
        
        $sql = "SELECT AVG(temp) AS temp_avg, 
                      AVG(humid) AS humid_avg, created_at
                FROM temphumid_v01 where device_id = '$device_id' and created_at between '$time_start' and '$time_end' GROUP BY date_format(created_at, '%Y%m%d')";
            
        $result = mysqli_query($conn, $sql);
        
        $array1 = array();    

        while($data = mysqli_fetch_array($result, MYSQL_NUM))
        {
            $temp_avg = round($data[0],2);
            $humid_avg = round($data[1],2);
            $created_at = $data[2];

                            array_push($array1, 
                                array('temp'.$index =>$temp_avg,
                                    'humid'.$index =>$humid_avg,
                                    'created_at'.$index =>$created_at
                                ));
                            
            

            $index++;
        }       
         

          header('Content-Type: application/json; charset=utf8');
               $json = json_encode(array("data"=>$array1), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
                echo $json;

             

    }
    else{
      echo "fail";
    }
?>
