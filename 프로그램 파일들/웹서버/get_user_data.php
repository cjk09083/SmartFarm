<?
	$conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");


  	if(isset($_GET['userID']))
	{
		$userID = $_GET['userID'];

		//echo "id=". $userID;
		
			$sql = "SELECT * FROM temphumid_v01 where device_id = '$userID' ORDER BY `temphumid_id` DESC LIMIT 1000";
			$result = mysqli_query($conn, $sql);
		    while($data = mysqli_fetch_array($result, MYSQL_NUM))
		    {
		    	if(is_null($data[1])){$data[1]=0;}
		    	if(is_null($data[2])){$data[2]=0;}
		    	if(is_null($data[3])){$data[3]=0;}
		    	if(is_null($data[4])){$data[4]=0;}

		    	echo $data[0]."/". $data[1]."/". $data[2]."/". $data[3]."/". $data[4]."\n";
		    	//echo $data[9] . "<br>\n";
		    }
		
		
	}


else
{
	echo "fail";
}
?>