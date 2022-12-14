<?

    $conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");

    $arr = array(array(), array());



	if(isset($_GET['device_id'])){

	    $device_id = $_GET['device_id'];

		$hour = 0;
		
		while($hour != 24)
		{
			$sql = "SELECT AVG(temp) AS temp, AVG(humid) AS humid FROM temphumid_v01
		 	 		WHERE hour_time = $hour and device_id = '$device_id'";
				
			$result = mysqli_query($conn, $sql);
			$row = mysqli_fetch_array($result);
			
			$temp = (float)$row['temp'];
			$temp = round($temp,2);
			
			$humid = (float)$row['humid'];
			$humid = round($humid,2);
			
			$arr[$hour][1] = $temp;
			$arr[$hour][2] = $humid;

			$hour++;
		}
	}
?>




<html>
  <head>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
    <script src="http://code.highcharts.com/highcharts.js"></script>
    
    <script>
        $(function () {
            $('#container').highcharts({
                chart: {
                      type: 'line'
                },
                title: {
                    text: 'Temp & Humid AVG Value',
                    style: {
                    	fontSize: '40px'
                    }
                },
                subtitle: {
                    text: '0~23hour',
                    style: {
                    	fontSize: '24px'
                    }
                },
                xAxis: {                    
                    categories: ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23'],
                    labels: {
						style: {
							fontSize: '20px'
						}
					}
                },
                yAxis: {
                    title: {
                        text: 'Temperature(`C)  &  Humidity(%)',
                        style: {
				            fontSize: '16px'
				        }				        
                    },
                    labels: {
						style: {
							fontSize: '20px'
						}
					}
                },
                plotOptions: {
                    line: {
                        dataLabels: {
                            enabled: true,
                            style: {
                                fontSize: '12px'
                            }
                        },
                        enableMouseTracking: true
                    }
                },
                series: [{
                    name: 'Temp',
                    data:  [<? echo $arr[0][1] ?>, <? echo $arr[1][1] ?>, <? echo $arr[2][1] ?>, <? echo $arr[3][1] ?>, <? echo $arr[4][1] ?>, <? echo $arr[5][1] ?>,
                            <? echo $arr[6][1] ?>, <? echo $arr[7][1] ?>, <? echo $arr[8][1] ?>, <? echo $arr[9][1] ?>, <? echo $arr[10][1] ?>, <? echo $arr[11][1] ?>,
                            <? echo $arr[12][1] ?>, <? echo $arr[13][1] ?>, <? echo $arr[14][1] ?>, <? echo $arr[15][1] ?>, <? echo $arr[16][1] ?>, <? echo $arr[17][1] ?>,
                            <? echo $arr[18][1] ?>, <? echo $arr[19][1] ?>, <? echo $arr[20][1] ?>, <? echo $arr[21][1] ?>, <? echo $arr[22][1] ?>, <? echo $arr[23][1] ?>]
                }, {
                    name: 'Humid',
                    data:  [<? echo $arr[0][2] ?>, <? echo $arr[1][2] ?>, <? echo $arr[2][2] ?>, <? echo $arr[3][2] ?>, <? echo $arr[4][2] ?>, <? echo $arr[5][2] ?>,
                            <? echo $arr[6][2] ?>, <? echo $arr[7][2] ?>, <? echo $arr[8][2] ?>, <? echo $arr[9][2] ?>, <? echo $arr[10][2] ?>, <? echo $arr[11][2] ?>,
                            <? echo $arr[12][2] ?>, <? echo $arr[13][2] ?>, <? echo $arr[14][2] ?>, <? echo $arr[15][2] ?>, <? echo $arr[16][2] ?>, <? echo $arr[17][2] ?>,
                            <? echo $arr[18][2] ?>, <? echo $arr[19][2] ?>, <? echo $arr[20][2] ?>, <? echo $arr[21][2] ?>, <? echo $arr[22][2] ?>, <? echo $arr[23][2] ?>]
                }]
            });
        });
    </script>
  </head>
  <body>
    <div id="container" style="min-width: 310px; height: 500px; margin: 0 auto"></div>
    
  </body>
</html>