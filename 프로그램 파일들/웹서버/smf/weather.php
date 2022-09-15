<?
header("Content-Type:text/html;charset=utf-8");

class KmaWeather
{
	
	//해당 좌표의 동네 날씨를  가져온다.   해당좌표는 기상청사이트=>  http://www.kma.go.kr/weather/lifenindustry/sevice_rss.jsp  가서 확인해 보시면 됩니다.
	function area_weather_xml_parser($x,$y,$now)
	{
		$array = array(); 

		$url1="www.kma.go.kr";
		$url2="GET /";
		// $url2.="wid/queryDFS.jsp?gridx=".$x."&gridy=".$y;
		$url2.="wid/queryDFS.jsp?zone=1168070000";
		$url2.=" HTTP/1.0\r\nHost:www.kma.go.kr\r\n\r\n";

		$fp2 = fsockopen ($url1, 80, $errno, $errstr,30 );
		$time=0;
		$humid=0;
		$temp=0;
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
				$hour=$array[$i]['hour'];
				if((($hour-3)<$now)&&($hour>$now)){
				$array[$i]['now'] = $this-> iconv_UTF_8($now); //시간 
				$array[$i]['hours'] = $this-> iconv_UTF_8($hour); //시간 
				$time=$hour;
				// $array[$i]['hour-3'] = $this-> iconv_UTF_8($hour-3); //시간 
				}

				if(ereg("<temp>",$line)){
					$array[$i]['temp'] = $this-> iconv_UTF_8(trim(strip_tags($line))); //온도
					if((($hour-3)<$now)&&($hour>$now))$temp=$array[$i]['temp'];
				}

				if(ereg("<reh>",$line)){
					$array[$i]['reh'] = $this-> iconv_UTF_8(trim(strip_tags($line))); //습도
					if((($hour-3)<$now)&&($hour>$now))$humid=$array[$i]['reh'];
				}
				

				if(ereg("</data>",$line))$i++;
				
			}
		}
		fclose($fp2);

		// return $array;
		return $time."-".$temp."-".$humid;

	}

	function iconv_UTF_8($str)
	{
		 return iconv('UTF-8','EUC-KR',$str); 
	}
}

$obj = new KmaWeather;

//테스트용 코드   $obj->area_weather_xml_parser('69','106')  //한번 찍어보시고 취향에 맞게 변경해서 사용하시면 되겠지여  
// echo print_r($obj->weather_xml_parser());
$hour_time = date('G');
// echo "hour : ".$hour_time."\n";

 // echo print_r($obj->area_weather_xml_parser('37','127',$hour_time));
  // echo print_r($obj->area_weather_xml_parser('37','127',$hour_time));

 $data= ($obj->area_weather_xml_parser('37','127',$hour_time));

$name_array = explode("-", $data);
echo $name_array[0]."\n";
echo $name_array[1]."\n";
echo $name_array[2]."\n";


?>

