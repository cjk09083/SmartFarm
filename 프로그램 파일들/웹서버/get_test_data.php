<?
	$conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");


		
			$sql = "SELECT * FROM temp  ORDER BY `id` DESC LIMIT 1000";
			$result = mysqli_query($conn, $sql);
		    while($data = mysqli_fetch_array($result, MYSQL_NUM))
		    {
		    	if(is_null($data[1])){$data[1]=0;}
		    	if(is_null($data[2])){$data[2]=0;}

		    	echo $data[0]."/". $data[1]."/". $data[2]."<br>\n";
		    	//echo $data[9] . "<br>\n";
		    }
		
		
	


?>