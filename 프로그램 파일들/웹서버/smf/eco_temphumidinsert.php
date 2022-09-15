<?

class KmaWeather
{
	
	#
	//해당 좌표의 동네 날씨를  가져온다.   해당좌표는 기상청사이트=>  http://www.kma.go.kr/weather/lifenindustry/sevice_rss.jsp  가서 확인해 보시면 됩니다.
	function area_weather_xml_parser($x,$y,$now)
	{
		$array = array(); 

		$url1="www.kma.go.kr";
		$url2="GET /";
		// $url2.="wid/queryDFS.jsp?gridx=".$x."&gridy=".$y;

		$url2.="wid/queryDFS.jsp?zone=".$x;
		$url2.=" HTTP/1.0\r\nHost:www.kma.go.kr\r\n\r\n";
		$time=0;
		$humid=0;
		$temp=0;
		$fp2 = fsockopen ($url1, 80, $errno, $errstr,30 );
		if (!$fp2) echo "?   $errstr ($errno)<br />n";
		else
		{
			fputs ($fp2, $url2);
			$i = 0; $j=0;
			while (!feof($fp2))
			{
				$line=fgets ($fp2,512);
				if(ereg("<tm>",$line))$array[date]= $this->iconv_UTF_8(trim(strip_tags($line))); 
				if($i==$j++ && ereg("<data",$line))
				{
					$area=preg_split("/\"/",$line);
					$area=preg_split("/\"/",$area[1]);
					$number = iconv_UTF_8($area[0]);// iconv('UTF-8','EUC-KR',$area[0]); 

					$array[$i]= $number; 
				}

				if(ereg("<hour>",$line))$array[$i]['hour'] = $this-> iconv_UTF_8(trim(strip_tags($line))); //시간 
				// $hour=$array[$i]['hour'];
				// if((($hour-3)<$now)&&($hour>=$now)){
				// $array[$i]['now'] = $this-> iconv_UTF_8($now); //시간 
				// $array[$i]['hours'] = $this-> iconv_UTF_8($hour); //시간 
				// $time=$hour;
				// // $array[$i]['hour-3'] = $this-> iconv_UTF_8($hour-3); //시간 
				// }

				if(ereg("<temp>",$line)){
					$array[$i]['temp'] = $this-> iconv_UTF_8(trim(strip_tags($line))); //온도
					// if((($hour-3)<$now)&&($hour>=$now))$temp=$array[$i]['temp'];
				}

				if(ereg("<reh>",$line)){
					$array[$i]['reh'] = $this-> iconv_UTF_8(trim(strip_tags($line))); //습도
					// if((($hour-3)<$now)&&($hour>=$now))$humid=$array[$i]['reh'];
				}

				if(ereg("</data>",$line))$i++;

				if($i==1){
				$time=$array[$i]['hour'];
				$temp=$array[$i]['temp'];
				$humid=$array[$i]['reh'];
				}
			}
		}
		fclose($fp2);

		return $time."-".$temp."-".$humid;

	}

	function iconv_UTF_8($str)
	{
		 return iconv('UTF-8','EUC-KR',$str); 
	}
}




///////////////////////////////////////////////////////////////////////////////




if (isset($_POST['temp']) && isset($_POST['humid']) && isset($_POST['id']))
{ // check if both fields are set
	
	$temp = $_POST['temp'];
	$humid = $_POST['humid'];
	$co2 = $_POST['co2'];
	$dust=  $_POST['dust'];
	$water= $_POST['water'];
	$device_id = $_POST['id'];

	//$temphumid_id = $_POST['temphumid_id'];
	$co2o=$co2;
	if($temp>100){$temp=0;}
	if($temp<(-100)){$temp=0;}
	if($humid>100){$humid=0;}
	if($humid<0){$humid=0;}
	if($co2<0){$co2=0;}
	// while($co2>2000){$co2=$co2/2;}
	// if($co2<100){$co2=100;}
	// $co2=$co2*3000/40000;
	// if($co2>100000){$co2=$co2/100;}
	// if($co2>10000){$co2=$co2/10;}
	if($co2>1500){$co2=1500+$co2*15/400;}
	if($co2>2900){$co2=$co2-($temp+$humid+$dust);}
	if($co2<10){$co2=$temp+$humid+$dust;}
	if($dust<0){$dust=0;}
	if($dust>100000){$dust=0;}
	$dust = ($dust *5)/1024;
	$dust = ($dust-0.06)*200; // 100 ~ 200 
	$dust= round($dust,2);
	if($dust<0){$dust=0;}
	if($device_id==2){$temp=$temp-1;}

	$conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");


	$hour_time = date('G');
	$daily_time = date('D');
	$year_time = date('y');
	$month_time = date('m');
	$day_time = date('j');

	$out_temp=0;
	$out_humid=0;
	$out_dust=0;


	$before_hour=0;
	$sql2 = "SELECT * FROM temphumid2 where device_id = '$device_id' ORDER BY `temphumid_id` DESC LIMIT 1";
			$result = mysqli_query($conn, $sql2);
		    while($data = mysqli_fetch_array($result, MYSQL_NUM))
		    {
		    	$timestamp = strtotime("Now");
				echo "[HTTP] Success with temphumid_ID : ".$data[0]." & Time :".$data[10]."\n";
				$before_hour=$data[10];
				$out_temp=$data[14];
				$out_humid=$data[15];
				$out_dust=$data[16];
		    }

    if($before_hour!=$hour_time){
		$obj = new KmaWeather;
				if($device_id==1){$code="1168070000";}
		else{$code="3611036000";}
		$data= ($obj->area_weather_xml_parser($code,'127',$hour_time));
		$name_array = explode("-", $data);
		$out_temp=$name_array[1];
		$out_humid= $name_array[2];

		$url = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?stationName=%EB%B3%B5%EC%A0%95%EB%8F%99&dataTerm=DAILY&pageNo=1&numOfRows=1&ServiceKey=LZck3ZNnB4PvXcYA9XYedYRPv8YCY4ljhroCyF75fIJw%2Bd0uwV%2B4owgyEvdjtUQY9bbKFCg30%2FxQqSlRNrUjNw%3D%3D&ver=1.3";
		$curl_handle = curl_init();
		curl_setopt($curl_handle, CURLOPT_URL, $url);
		curl_setopt($curl_handle, CURLOPT_CONNECTTIMEOUT, 2);
		curl_setopt($curl_handle, CURLOPT_RETURNTRANSFER, 1);
		curl_setopt($curl_handle, CURLOPT_USERAGENT, '   `http://mrkn.tistory.com`  ');

		$response = curl_exec($curl_handle);
		curl_close($curl_handle);
 		$xml=simplexml_load_string($response);
		$out_dust = $xml->body[0]->items[0]->item[0]->pm10Value[0];	
	}

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
			daily_time,
			co2o,
			out_temp,
			out_humid,
			out_dust
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
			'$daily_time',
			'$co2o',
			'$out_temp',
			'$out_humid',
			'$out_dust'
		)";

	mysqli_query($conn, $sql);


}

else
{
	echo "fail";
}
?>