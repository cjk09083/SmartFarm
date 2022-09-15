<?php
// header('Content-Type: text/html; charset=utf-8');
//header('Content-Type: bitmap; charset=utf-8');
// echo " 접근 확인 성공 " 

$upload_dir="/cjk09083/www/data/";
if(!is_dir($upload_dir)){
    if(mkdir($upload_dir,0777))
    {echo $upload_dir." 디렉토리 생성성공./"; }
    else{echo $upload_dir." 디렉토리 생성실패./"; }
} else{
    // echo $upload_dir." 디렉토리가 이미 있습니다./"; 
}

    $conn = new mysqli("localhost", "cjk09083", "wornr0908@", "cjk09083");


    $randomNum = mt_rand(0, 1000000);


    if(isset($_POST['host']))
    {
        $host= $_POST['host'];
        echo ", 유저 확인 성공:".$host; 
    }

    if(isset($_POST['farmNM']))
    {
        $farmNM= $_POST['farmNM'];
        echo ", 농장 확인 성공:".$farmNM; 
    }


    if(isset($_POST['title']))
    {
        $title= $_POST['title'];
        echo ", 제목 확인 성공:".$title; 
    }


    if(isset($_POST['memo']))
    {
        $memo=$_POST['memo'];
        echo ", 내용 확인 성공:"; 
    }

    if(isset($_FILES['image1']["name"])){
        $error1 = $_FILES['image1']['error'];
        if ( $error1 == UPLOAD_ERR_OK) {


            $name = $_FILES["image1"]["name"];
            $tmp_name1 = $_FILES["image1"]["tmp_name"];
            $size = $_FILES['image1']["size"];
            $name1 = date("Y-m-d_H:i:s")."_1_".$randomNum;
            $path1=$upload_dir.$name1.'.jpg';
                
                if(move_uploaded_file($tmp_name1, $path1)) 
                {
                    echo ", 사진1 저장 성공. ".$path1; 
                    $destination_sfile=$upload_dir."thum_".$name1.'.jpg';
                    if($size>40000){
                    make_thumbnail($path1, 200, 200, $destination_sfile);
                    }else{
                    copy($path1, $destination_sfile);
                    }
                    // echo ", 사진1 저장 경로 : http://cjk09083.cafe24.com/data/".$name1.'.jpg';     
                } else{
                    echo ", 사진1 저장 실패. ".$path1; 

                }   
        } else{
            echo ", 사진1 업로드 에러".$error1;
            switch( $error1 ) {
            case UPLOAD_ERR_INI_SIZE:
            case UPLOAD_ERR_FORM_SIZE:
                echo "파일이 너무 큽니다. ($error1)";
                break;
            case UPLOAD_ERR_NO_FILE:
                echo "파일이 첨부되지 않았습니다. ($error1)";
                break;
            default:
                echo "파일이 제대로 업로드되지 않았습니다. ($error1)";
            }
        }
    }else{
         $path1="null";
    }

    if(isset($_FILES['image2']["name"])){
        $error2 = $_FILES['image2']['error'];
        if ( $error2 == UPLOAD_ERR_OK) {
            $name = $_FILES["image2"]["name"];
            
            $tmp_name2 = $_FILES["image2"]["tmp_name"];
            $name2 = date("Y-m-d_H:i:s")."_2_".$randomNum;
            $path2=$upload_dir.$name2.'.jpg';
                if(move_uploaded_file($tmp_name2, $path2)) 
                {
                    echo ", 사진2 저장 성공. ".$path2; 

                } else{
                    echo ", 사진2 저장 실패. ".$path2; 

                }   
        } else{
            echo ", 사진2 업로드 에러".$error2;
            switch( $error2 ) {
            case UPLOAD_ERR_INI_SIZE:
            case UPLOAD_ERR_FORM_SIZE:
                echo "파일이 너무 큽니다. ($error2)";
                break;
            case UPLOAD_ERR_NO_FILE:
                echo "파일이 첨부되지 않았습니다. ($error2)";
                break;
            default:
                echo "파일이 제대로 업로드되지 않았습니다. ($error2)";
            }
        }
    } else{
         $path2="null";
    }

    if(isset($_FILES['image3']["name"])){
        $error3 = $_FILES['image3']['error'];
        if ( $error3 == UPLOAD_ERR_OK) {
            $name = $_FILES["image3"]["name"];
            $tmp_name3 = $_FILES["image3"]["tmp_name"];
            $name3 = date("Y-m-d_H:i:s")."_3_".$randomNum;
            $path3=$upload_dir.$name3.'.jpg';
                if(move_uploaded_file($tmp_name3, $path3)) 
                {
                    echo ", 사진3 저장 성공. ".$path3; 

                } else{
                    echo ", 사진3 저장 실패. ".$path3; 

                }   
        } else{
            echo ", 사진1 업로드 에러".$error3;
            switch( $error3 ) {
            case UPLOAD_ERR_INI_SIZE:
            case UPLOAD_ERR_FORM_SIZE:
                echo "파일이 너무 큽니다. ($error3)";
                break;
            case UPLOAD_ERR_NO_FILE:
                echo "파일이 첨부되지 않았습니다. ($error3)";
                break;
            default:
                echo "파일이 제대로 업로드되지 않았습니다. ($error3)";
            }
        }
    }else{
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

function make_thumbnail($source_path, $width, $height, $thumbnail_path){
    
    list($img_width,$img_height, $type) = getimagesize($source_path);
     echo "원본 사이즈  width : ".$img_width.", height : ".$img_height.", type : ".$type;

    if ($type!=1 && $type!=2 && $type!=3 && $type!=15) return;
    if ($type==1) $img_sour = imagecreatefromgif($source_path);
    else if ($type==2 ) $img_sour = imagecreatefromjpeg($source_path);
    else if ($type==3 ) $img_sour = imagecreatefrompng($source_path);
    else if ($type==15) $img_sour = imagecreatefromwbmp($source_path);
    if ($img_width > $img_height) {
        $w = round($height*$img_width/$img_height);
        $h = $height;
        $x_last = round(($w-$width)/2);
        $y_last = 0;
    } else {
        $w = $width;
        $h = round($width*$img_height/$img_width);
        $x_last = 0;
        $y_last = round(($h-$height)/2);
    }
    if ($img_width < $width && $img_height < $height) {
        $img_last = imagecreatetruecolor($width, $height); 
        $x_last = round(($width - $img_width)/2);
        $y_last = round(($height - $img_height)/2);

        imagecopy($img_last,$img_sour,$x_last,$y_last,0,0,$w,$h);
        imagedestroy($img_sour);
        $white = imagecolorallocate($img_last,255,255,255);
        imagefill($img_last, 0, 0, $white);
    } else {
        $img_dest = imagecreatetruecolor($w,$h); 
        imagecopyresampled($img_dest, $img_sour,0,0,0,0,$w,$h,$img_width,$img_height); 
        $img_last = imagecreatetruecolor($width,$height); 
        imagecopy($img_last,$img_dest,0,0,$x_last,$y_last,$w,$h);
        imagedestroy($img_dest);
    }
    if ($thumbnail_path) {
        if ($type==1) imagegif($img_last, $thumbnail_path, 100);
        else if ($type==2 ) imagejpeg($img_last, $thumbnail_path, 100);
        else if ($type==3 ) imagepng($img_last, $thumbnail_path, 100);
        else if ($type==15) imagebmp($img_last, $thumbnail_path, 100);
    } else {
        if ($type==1) imagegif($img_last);
        else if ($type==2 ) imagejpeg($img_last);
        else if ($type==3 ) imagepng($img_last);
        else if ($type==15) imagebmp($img_last);
    }
    imagedestroy($img_last);
}

function make_thumbnail2($source_path, $thumbnail_path){
    list($img_width,$img_height, $type) = getimagesize($source_path);
     echo "원본 사이즈  width : ".$img_width.", height : ".$img_height.", type : ".$type;

    if ($type!=1 && $type!=2 && $type!=3 && $type!=15) return;
    if ($type==1) $img_sour = imagecreatefromgif($source_path);
    else if ($type==2 ) $img_sour = imagecreatefromjpeg($source_path);
    else if ($type==3 ) $img_sour = imagecreatefrompng($source_path);
    else if ($type==15) $img_sour = imagecreatefromwbmp($source_path);
    
    if ($img_width < $width && $img_height < $height) {
        $img_last = imagecreatetruecolor($width, $height); 
        $x_last = round(($width - $img_width)/2);
        $y_last = round(($height - $img_height)/2);

        imagecopy($img_last,$img_sour,$x_last,$y_last,0,0,$w,$h);
        imagedestroy($img_sour);
        $white = imagecolorallocate($img_last,255,255,255);
        imagefill($img_last, 0, 0, $white);
    } else {
        $img_dest = imagecreatetruecolor($w,$h); 
        imagecopyresampled($img_dest, $img_sour,0,0,0,0,$w,$h,$img_width,$img_height); 
        $img_last = imagecreatetruecolor($width,$height); 
        imagecopy($img_last,$img_dest,0,0,$x_last,$y_last,$w,$h);
        imagedestroy($img_dest);
    }
    if ($thumbnail_path) {
        if ($type==1) imagegif($img_last, $thumbnail_path, 100);
        else if ($type==2 ) imagejpeg($img_last, $thumbnail_path, 100);
        else if ($type==3 ) imagepng($img_last, $thumbnail_path, 100);
        else if ($type==15) imagebmp($img_last, $thumbnail_path, 100);
    } else {
        if ($type==1) imagegif($img_last);
        else if ($type==2 ) imagejpeg($img_last);
        else if ($type==3 ) imagepng($img_last);
        else if ($type==15) imagebmp($img_last);
    }
    imagedestroy($img_last);
}
// echo " 접근 확인 성공?" 

?>


