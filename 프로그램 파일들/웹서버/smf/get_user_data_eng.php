<?
	$conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");


  	if(isset($_GET['userID']))
	{
		$userID = $_GET['userID'];

		//echo "id=". $userID;
		
			$sql = "SELECT * FROM temphumid_v01 where device_id = '$userID' and month_time = 11 and year_time = 19 and day_time = 28 and r_lux > 0 ORDER BY `temphumid_id` DESC LIMIT 1000";
			$result = mysqli_query($conn, $sql);
		    while($data = mysqli_fetch_array($result))
		    {
		    	
		    	if(is_null($data['device_id'])){$data['device_id']=1;}	
		    	if(is_null($data['mintemp'])){$data['mintemp']=0;}	
		    	if(is_null($data['maxtemp'])){$data['maxtemp']=50;}	
		    	if(is_null($data['minhumid'])){$data['minhumid']=0;}	
		    	if(is_null($data['maxhumid'])){$data['maxhumid']=100;}	
		    	if(is_null($data['minco2'])){$data['minco2']=0;}	
		    	if(is_null($data['maxco2'])){$data['maxco2']=1000;}	

				if(is_null($data['temp'])){$data['temp']=0;}
		    	if(is_null($data['humid'])){$data['humid']=0;}
		    	if(is_null($data['co2'])){$data['co2']=0;}
		    	if(is_null($data['created_at'])){$data['created_at']=0;}	
		    	if(is_null($data['gen'])){$data['gen']=2;}	
		    	if(is_null($data['device_id'])){$data['device_id']=1;}	
	    	 	if(is_null($data['r_lux'])){$data['r_lux']=1;}	
		    	if(is_null($data['l_lux'])){$data['l_lux']=1;}	


		    	$db_id=$data['temphumid_id'];	
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
				$r_lux= $data['r_lux'];
				$l_lux= $data['l_lux'];


		    	echo $db_id."/". $temp."/". $humid."/". $co2."/". $time."/". $mintemp."/". $maxtemp."/". $minhumid."/". $maxhumid."/". $minco2."/". $maxco2."/". $r_lux."/". $l_lux."\n";
		    	//echo $data[9] . "<br>\n";
		    }
		
		
	}


else
{
	echo "fail";
}
?>