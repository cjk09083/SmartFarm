<?

if (isset($_POST['temp']) && isset($_POST['humid']) && isset($_POST['id']))
{ // check if both fields are set
	
	$temp = $_POST['temp'];
	$humid = $_POST['humid'];
	$co2 = $_POST['co2'];
	$lux=  $_POST['lux'];
	$device_id = $_POST['id'];
	$co2gen = $_POST['co2gen'];
	$mintemp = $_POST['mintemp'];
	$maxtemp = $_POST['maxtemp'];
	$minhumid = $_POST['minhumid'];
	$maxhumid = $_POST['maxhumid'];
	$minco2 = $_POST['minco2'];
	$maxco2 = $_POST['maxco2'];

	//$temphumid_id = $_POST['temphumid_id'];

	if($temp>100){$temp=0;}
	if($temp<(-100)){$temp=0;}
	if($humid>100){$humid=0;}
	if($humid<0){$humid=0;}
	if($co2<0){$co2=0;}
	if($co2>10000){$co2=0;}
	if($lux<0){$lux=0;}
	if($lux>100000){$lux=0;}

	$conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");



	$hour_time = date('G');
	$daily_time = date('D');
	$year_time = date('y');
	$month_time = date('m');
	$day_time = date('j');
		

	$sql  ="INSERT INTO temphumid_v01
		(			
			temp,
			humid,
			co2,
			created_at, 
			gen,
			year_time,
			month_time,
			day_time,
			hour_time, 
			device_id,
			daily_time,
			mintemp,
			maxtemp,
			minhumid,
			maxhumid,
			minco2,
			maxco2,
			lux
		)
		
		VALUES
		(			
			'$temp',
			'$humid',
			'$co2',
			now(),
			'$co2gen',
			'$year_time',
			'$month_time',
			'$day_time',			
			'$hour_time',
			'$device_id',
			'$daily_time',
			'$mintemp',
			'$maxtemp',
			'$minhumid',
			'$maxhumid',
			'$minco2',
			'$maxco2',
			'$lux'
		)";

	mysqli_query($conn, $sql);

	//echo "[HTTP] Success \n ";

	$sql2 = "SELECT * FROM temphumid_v01 where device_id = '$device_id' ORDER BY `temphumid_id` DESC LIMIT 1";
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