<?
	$conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");



	if(isset($_GET['device_id'])&&isset($_GET['year_time']) && isset($_GET['month_time']) && isset($_GET['day_time']) )
    {
        // echo = "go";

        $device_id = $_GET['device_id'];
        $year_time_start = $_GET['year_time'];
        if(strlen($year_time_start)<2){$year_time_start="0".$year_time_start;}
        $month_time_start = $_GET['month_time'];
        if(strlen($month_time_start)<2){$month_time_start="0".$month_time_start;}
        $day_time_start = $_GET['day_time'];
        if(strlen($day_time_start)<2){$day_time_start="0".$day_time_start;}

        $date="20".$year_time_start."-".$month_time_start."-".$day_time_start;
        //echo $date."<br>";   //기준날짜
        //$day_time_end=date("d",strtotime($date.'-8 days')) ;       
        //echo date('Y-m-d',strtotime($date.'-7 days')); //7일전

        $day_time0 = '00:00:00';
        $day_time1 = '23:59:59';
        $time_start_day = $date.' '.$day_time0;
        $time_start_week = date("Y-m-d",strtotime($date.'-7 days')).' '.$day_time0;
        $time_start_month = date("Y-m-d",strtotime($date.'-1 months')).' '.$day_time0;

        $time_end = $date.' '.$day_time1;
		// echo $time_start_week."\n"; 
  //       echo $time_start_month."\n"; 
  //       echo $time_end."\n"; 

		$sql = "SELECT *FROM 
				(SELECT SUM(r_lux_avg_day) AS r_lux_sum_day, SUM(l_lux_avg_day) AS l_lux_sum_day 
			   	FROM (SELECT device_id AS id, hour_time, created_at, 
				AVG(r_lux) AS r_lux_avg_day, AVG(l_lux) AS l_lux_avg_day            
	         	FROM temphumid_v01 where device_id = '$device_id' and created_at 
				BETWEEN '$time_start_day' AND '$time_end'
				group by date_format(created_at, '%Y%m%d'), hour_time) AS DAY) AS DAY2
				
				JOIN (SELECT SUM(r_lux_avg_week) AS r_lux_sum_week, SUM(l_lux_avg_week) AS l_lux_sum_week FROM (SELECT hour_time, created_at, AVG(r_lux) AS r_lux_avg_week, AVG(l_lux) AS l_lux_avg_week            
	        	FROM temphumid_v01 where device_id = '$device_id' and created_at 
				BETWEEN '$time_start_week' AND '$time_end'
				group by date_format(created_at, '%Y%m%d'), hour_time) AS WEEK) AS WEEK2 
				
				JOIN (SELECT SUM(r_lux_avg_month) AS r_lux_sum_month, SUM(l_lux_avg_month) AS l_lux_sum_month FROM (SELECT hour_time, created_at, AVG(r_lux) AS r_lux_avg_month, AVG(l_lux) AS l_lux_avg_month            
	         	FROM temphumid_v01 where device_id = '$device_id' and created_at 
				BETWEEN '$time_start_month' AND '$time_end'
				group by date_format(created_at, '%Y%m%d'), hour_time) AS MONTH) AS MONTH2";


		// "SELECT * FROM (SELECT device_id AS id, SUM(r_lux) AS r_lux_sum_day, SUM(l_lux) 		AS l_lux_sum_day            
  //              FROM temphumid_v01 where device_id = '$device_id' and created_at BETWEEN '$time_start_day' and '$time_end') AS DAY
		// 		JOIN (SELECT SUM(r_lux) AS r_lux_sum_week, SUM(l_lux) AS l_lux_sum_week            
  //              FROM temphumid_v01 where device_id = '$device_id' and created_at BETWEEN '$time_start_week' and '$time_end') AS WEEK 
  //              JOIN (SELECT SUM(r_lux) AS r_lux_sum_month, SUM(l_lux) AS l_lux_sum_month            
  //              FROM temphumid_v01 where device_id = '$device_id' and created_at BETWEEN '$time_start_month' and '$time_end') AS MONTH";


               

			$result = mysqli_query($conn, $sql);
		    while($data = mysqli_fetch_array($result))
		    {
		    	if(is_null($data['r_lux_sum_day'])){$data['r_lux_sum_day']=0;}
		    	if(is_null($data['l_lux_sum_day'])){$data['l_lux_sum_day']=0;}
		    	if(is_null($data['r_lux_sum_week'])){$data['r_lux_sum_week']=0;}
		    	if(is_null($data['l_lux_sum_week'])){$data['l_lux_sum_week']=0;}
		    	if(is_null($data['r_lux_sum_month'])){$data['r_lux_sum_month']=0;}
		    	if(is_null($data['l_lux_sum_month'])){$data['l_lux_sum_month']=0;}
			    	
			    // echo $data['r_lux_sum_week']."\n";

		    	$lux_day = ((int)$data['r_lux_sum_day']+ (int)$data['l_lux_sum_day'])/2;
		    	$lux_week = ((int)$data['r_lux_sum_week']+ (int)$data['l_lux_sum_week'])/2;
		    	$lux_month = ((int)$data['r_lux_sum_month']+ (int)$data['l_lux_sum_month'])/2;



		    	echo number_format($lux_day)."/".number_format($lux_week)."/". number_format($lux_month)."\n";
		    	//echo $data[9] . "<br>\n";
		    }
		
		
	}


else
{
	echo "fail";
}
?>