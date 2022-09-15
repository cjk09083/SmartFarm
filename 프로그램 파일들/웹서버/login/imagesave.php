<?php
header('Content-Type: text/html; charset=utf-8');
//header('Content-Type: bitmap; charset=utf-8');

$upload_dir="/cjk09083/www/data";
if(!is_dir($upload_dir)){
    if(mkdir($upload_dir,0777))
    {echo $upload_dir." 디렉토리 생성성공./"; }
    else{echo $upload_dir." 디렉토리 생성실패./"; }
} else{
    echo $upload_dir." 디렉토리가 이미 있습니다./"; 
}

    $conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");

if(isset($_REQUEST['host']))
{
$host= $_REQUEST['host'];
echo " 유저 확인 성공:".$host; 
}

if(isset($_REQUEST['farmNM']))
{
$farmNM= $_REQUEST['farmNM'];
echo " 농장 확인 성공:".$farmNM; 
}


if(isset($_REQUEST['title']))
{
$title= $_REQUEST['title'];
echo " 제목 확인 성공:".$title; 
}


if(isset($_REQUEST['memo']))
{
$memo=$_REQUEST['memo'];
echo " 내용 확인 성공:".$memo; 
}

if(isset($_REQUEST['image1']))
{
$base=$_REQUEST['image1'];
$binary=base64_decode($base);
$name1 = date("Y-m-d H:i:s")."_1";
$file = fopen($upload_dir.'/'.$name1.'.jpg', 'wb');
$path1=$upload_dir.'/'.$name1.'.jpg';
fwrite($file, $binary);
fclose($file);
//echo '<img src=test.jpg>';
echo " 사진1 저장 성공. "; 
} else{

    $path1="null";
}

if(isset($_REQUEST['image2']))
{
$base2=$_REQUEST['image2'];
$binary2=base64_decode($base2);
$name2 = date("Y-m-d H:i:s")."_2";
$file2 = fopen($upload_dir.'/'.$name2.'.jpg', 'wb');
$path2=$upload_dir.'/'.$name2.'.jpg';
fwrite($file2, $binary2);
fclose($file2);
//echo '<img src=test.jpg>';
echo " 사진2 저장 성공. ";     
} else {
   $path2="null"; 
}


if(isset($_REQUEST['image3']))
{
$base3=$_REQUEST['image3'];
$binary3=base64_decode($base3);
$name3 = date("Y-m-d H:i:s")."_3";
$file3 = fopen($upload_dir.'/'.$name3.'.jpg', 'wb');
$path3=$upload_dir.'/'.$name3.'.jpg';
fwrite($file3, $binary3);
fclose($file3);
//echo '<img src=test.jpg>';
echo " 사진3 저장 성공. ";     
} else {
    $path3="null";
}


    $sql  ="INSERT INTO image_data
        (           
            host,
            farmNM,
            title,
            memo,
            path1,
            path2, 
            path3,
            created_at
        )
        
        VALUES
        (           
            '$host',
            '$farmNM',
            '$title',
            '$memo',            
            '$path1',
            '$path2',
            '$path3',
            now()
        )";

    mysqli_query($conn, $sql);

?>


