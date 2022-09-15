<?

    $conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");


    if(isset($_GET['year_time_start']) && isset($_GET['month_time_start']) && isset($_GET['day_time_start']) && 
           isset($_GET['year_time_end']) && isset($_GET['month_time_end']) && isset($_GET['day_time_end']))
    {
        //echo = "go";

        $device_id = 1;
        $year_time_start = $_GET['year_time_start'];
        $month_time_start = $_GET['month_time_start'];
        $day_time_start = $_GET['day_time_start'];
        $year_time_end = $_GET['year_time_end'];
        $month_time_end = $_GET['month_time_end'];
        $day_time_end = $_GET['day_time_end'];
        $day_time0 = '00:00:00';
        $day_time1 = '23:59:59';
        $time_start = $year_start.$year_time_start.'-'.$month_time_start.'-'.$day_time_start.' '.$day_time0;
        $time_end = $year_start.$year_time_end.'-'.$month_time_end.'-'.$day_time_end.' '.$day_time1;
        // echo $time_start. "<br>\n";
        // echo $time_end. "<br>\n";
        $index = 0;
        
        $sql = "SELECT AVG(temp) AS temp_avg, 
                      AVG(humid) AS humid_avg, created_at
                FROM temphumid_v01 where device_id = '$device_id' and created_at between '$time_start' and '$time_end' GROUP BY date_format(created_at, '%Y%m%d')";
            
        $result = mysqli_query($conn, $sql);
        
        $array1 = array();    
        $array2 = array();

        $array3 = array();
        $temp_arr = array();
        $humid_arr = array();
        $time_arr = array();

        while($data = mysqli_fetch_array($result, MYSQL_NUM))
        {
            $temp_avg = round($data[0],2);
            $humid_avg = round($data[1],2);
            $created_at = $data[2];
            array_push($temp_arr,$temp_avg);
            array_push($humid_arr,$humid_avg);            
            array_push($time_arr,$created_at);


           //extract($data);                   
                            array_push($array1, 
                                array('temp'.$index =>$temp_avg,
                                    'humid'.$index =>$humid_avg,
                                    'created_at'.$index =>$created_at
                                ));
                            
            
            // echo $created_at . " : " . $temp_avg . ", " . $temp_max . ", " . $temp_min. "<br>\n";
            // echo $created_at . " = " . $humid_avg . ", " . $humid_max . ", " . $humid_min. "<br>\n";

            $index++;
        }

       
          if($index>19){
              array_push($array2, 
                array('temp0' =>$temp_arr[0],
                'humid0' =>$humid_arr[0],
                'created_at0' =>$time_arr[0]
                 ));
              while (20 > count($array3)) {
              $i = rand(1, $index-1);
              $array3[$i] = $i; 
              //echo $array1[$array3[$i]]."\n";

              }
              sort($array3);
              $flag=0;
               for($j=1;$j<19;$j++){
                $num = $array3[$j];                
                
                array_push($array2, 
                array('temp'.$j =>$temp_arr[$num],
                'humid'.$j =>$humid_arr[$num],
                'created_at'.$j =>$time_arr[$num]
                 ));                          
               }
               array_push($array2, 
                array('temp19' =>$temp_arr[$index-1],
                'humid19' =>$humid_arr[$index-1],
                'created_at19' =>$time_arr[$index-1]
                 ));

               header('Content-Type: application/json; charset=utf8');
               $json = json_encode(array("data"=>$array2), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
               echo $json; 

          } else{

          header('Content-Type: application/json; charset=utf8');
               $json = json_encode(array("data"=>$array1), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
                echo $json;

             }
                 //echo "go";

    }
    else{
      echo "fail";
    }
?>
