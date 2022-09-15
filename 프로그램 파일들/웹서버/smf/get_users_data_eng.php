<?
	$conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");




			$sql = "SELECT * FROM (SELECT * FROM temphumid_v01 where month_time = 11 and year_time = 19 and day_time = 28 and r_lux > 0 ORDER BY `temphumid_id` DESC LIMIT 1000 ) AS A GROUP BY device_id";
			$result = mysqli_query($conn, $sql);
		     while($data = mysqli_fetch_array($result))
		    {
		    	if(is_null($data['temp'])){$data['temp']=0;}
		    	if(is_null($data['humid'])){$data['humid']=0;}
		    	if(is_null($data['co2'])){$data['co2']=0;}
		    	if(is_null($data['created_at'])){$data['created_at']=0;}	
		    	if(is_null($data['gen'])){$data['gen']=2;}	
		    	if(is_null($data['device_id'])){$data['device_id']=1;}	
	    	 	if(is_null($data['r_lux'])){$data['r_lux']=1;}	
		    	if(is_null($data['l_lux'])){$data['l_lux']=1;}	

		    	$id = $data['device_id'];	
		    	$temp = $data['temp'];
				$humid = $data['humid'];
				$co2 = $data['co2'];
				$time = $data['created_at'];
				$co2gen = $data['gen'];
				$mintemp = $data['mintemp'];
				$maxtemp = $data['maxtemp'];
				$minhumid = $data['minhumid'];
				$maxhumid = $data['maxhumid'];
				$minco2 = $data['minco2'];
				$maxco2 = $data['maxco2'];
				$lux= ( $data['r_lux']+ $data['l_lux'])/2;

				echo $id."/". $temp."/". $humid."/". $co2."/".$co2gen."/". $time.
		    	"/". $mintemp."/". $maxtemp."/". $minhumid."/". $maxhumid."/". 
		    	$minco2."/". $maxco2."/".$lux."\n";
		    }
		    /*
			$sql2 = "SELECT * FROM co2gen where device_id = '$userID' ORDER BY `id` DESC LIMIT 1";
			$result2 = mysqli_query($conn, $sql2);
 			while($data2 = mysqli_fetch_array($result2, MYSQL_NUM))
		    {
		    	//if(is_null($data2[2])){$data2[2]=0;}
				//$co2 = $data2[2];
				if(is_null($data2[3])){$data2[3]=2;}
				$co2gen = $data2[3];
		    }

				*/

		    	
		    	//echo $data[9] . "<br>\n";
		    
		
		
	


?>