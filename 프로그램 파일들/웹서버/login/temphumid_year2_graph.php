<?

    $conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");


    if(isset($_GET['device_id']) && isset($_GET['year_time'])){

        $device_id = $_GET['device_id'];
        $year_time = $_GET['year_time'];
        $month = 1;

            $data = array();        
            $data2 = array();    

        while($month != 13)
        {
            $sql = "SELECT AVG(temp) AS temp, AVG(humid) AS humid FROM temphumid_v01
                    WHERE month_time = $month and device_id = '$device_id' and year_time = '$year_time'";
                
            $result = mysqli_query($conn, $sql);
            $row = mysqli_fetch_array($result);    
 
                $temp = (float)$row['temp'];
                $temp = round($temp,2);
                
                $humid = (float)$row['humid'];
                $humid = round($humid,2);
                            extract($row);
                    
                            array_push($data, 
                                array('temp'.($month-1) =>$temp,
                                    'humid'.($month-1) =>$humid
                                ));
                            
                                // array_push($data2, 
                                // array('humid'.$month =>$humid
                                // ));                                
                       

                //echo  $temp."&".$humid."\n";                   
            
            $month++;
        }
        header('Content-Type: application/json; charset=utf8');
               $json = json_encode(array("data"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
                
                 echo $json;
        //$results = print_r($arr, true); 
        //echo $results;
    }
?>

