<?

if (isset($_POST['temp']) && isset($_POST['humid']) && isset($_POST['id']))
{ // check if both fields are set
	
	$temp = $_POST['temp'];
	$humid = $_POST['humid'];
	$co2 = $_POST['co2'];
	$dust=  $_POST['dust'];
	$water= $_POST['water'];
	$device_id = $_POST['id'];

	//$temphumid_id = $_POST['temphumid_id'];

	if($temp>100){$temp=0;}
	if($temp<(-100)){$temp=0;}
	if($humid>100){$humid=0;}
	if($humid<0){$humid=0;}
	if($co2<0){$co2=0;}
	if($co2>10000){$co2=0;}
	if($dust<0){$dust=0;}
	if($dust>100000){$dust=0;}

	$conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");


	$hour_time = date('G');
	$daily_time = date('D');
	$year_time = date('y');
	$month_time = date('m');
	$day_time = date('j');
		

	$sql  ="INSERT INTO temphumid2
		(			
			temp,
			humid,
			co2,
			dust,
			water,
			created_at, 
			year_time,
			month_time,
			day_time,
			hour_time, 
			device_id,
			daily_time
		)
		
		VALUES
		(			
			'$temp',
			'$humid',
			'$co2',
			'$dust',
			'$water',
			now(),
			'$year_time',
			'$month_time',
			'$day_time',			
			'$hour_time',
			'$device_id',
			'$daily_time'
		)";

	mysqli_query($conn, $sql);

	//echo "[HTTP] Success \n ";

	$sql2 = "SELECT * FROM temphumid2 where device_id = '$device_id' ORDER BY `temphumid_id` DESC LIMIT 1";
			$result = mysqli_query($conn, $sql2);
		    while($data = mysqli_fetch_array($result, MYSQL_NUM))
		    {
		    	$timestamp = strtotime("Now");
				echo "[HTTP] Success with temphumid_ID : ".$data[0]." & Time :".date("Y-m-d H:i:s", $timestamp)."\n";
		    }	

}

else
{
	echo "fail";
}
?>