<?

// // First, include Requests
// require_once 'Requests.php';

// // Next, make sure Requests can load internal classes
// Requests::register_autoloader();

// // // Now let's make a request!
// // $request = Requests::get('http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?stationName=%EC%A2%85%EB%A1%9C%EA%B5%AC&dataTerm=DAILY&pageNo=1&numOfRows=10&ServiceKey=LZck3ZNnB4PvXcYA9XYedYRPv8YCY4ljhroCyF75fIJw%2Bd0uwV%2B4owgyEvdjtUQY9bbKFCg30%2FxQqSlRNrUjNw%3D%3D&ver=1.3', array('Accept' => 'application/json'));

// $request = Requests::get('http://cjk09083.cafe24.com/get_test_data.php', array('Accept' => 'application/json'));


// // Check what we received  http://cjk09083.cafe24.com/get_test_data.php
// var_dump($request);



$url = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?stationName=%EB%B3%B5%EC%A0%95%EB%8F%99&dataTerm=DAILY&pageNo=1&numOfRows=1&ServiceKey=LZck3ZNnB4PvXcYA9XYedYRPv8YCY4ljhroCyF75fIJw%2Bd0uwV%2B4owgyEvdjtUQY9bbKFCg30%2FxQqSlRNrUjNw%3D%3D&ver=1.3";


$curl_handle = curl_init();

curl_setopt($curl_handle, CURLOPT_URL, $url);

// curl_setopt($curl_handle, CURLOPT_CONNECTTIMEOUT, 15);

curl_setopt($curl_handle, CURLOPT_RETURNTRANSFER, 1);

curl_setopt($curl_handle, CURLOPT_USERAGENT, '   `http://mrkn.tistory.com`  ');

$response = curl_exec($curl_handle);
curl_close($curl_handle);

// $pm10value=$dust_array[1];
// $xml=simplexml_load_string($response);
$xml=simplexml_load_string($response);
$pm10dust = $xml->body[0]->items[0]->item[0]->pm10Value[0];

echo "dust\n";


echo $pm10dust;

echo "response\n";

print_r ($response);

echo "xml\n";

echo $xml;

// echo $dust_array[0]."<br>";
// echo $dust_array[1]."<br>";
// echo $dust_array[2]."<br>";
// echo $dust_array[3]."<br>";
// echo $dust_array[4]."<br>";
// echo $dust_array[5]."<br>";
// echo $dust_array[6]."<br>";
// echo $dust_array[7]."<br>";
// echo $dust_array[8]."<br>";
// echo $dust_array[9]."<br>";
// echo $dust_array[10]."<br>";
// echo $dust_array[11]."<br>";




?>

