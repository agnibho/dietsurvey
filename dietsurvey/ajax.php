<?php
/**********************************************************************
 * Title: Diet-Survey
 * Description: Application for calculating nutrient intake
 * Author: Agnibho Mondal
 * Website: http://code.agnibho.com
 **********************************************************************
   Copyright (c) 2013-2015 Agnibho Mondal
   All rights reserved
 **********************************************************************
   This file is part of Diet-Survey.
   
   Diet-Survey is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   Diet-Survey is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with Diet-Survey.  If not, see <http://www.gnu.org/licenses/>.
 **********************************************************************/
?>
<?php
ini_set('session.cookie_lifetime', 60 * 60 * 24 * 30);
ini_set('session.gc_maxlifetime', 60 * 60 * 24 * 30);
session_start();
session_regenerate_id(true);
if(!isSet($_SESSION["custom_food"])){
    $_SESSION["custom_food"]=array();
}

define("HOST", "localhost");
define("USER", "root");
define("PASS", "data");
define("DTBS", "test");

if(isSet($_POST["subjects"]) && isSet($_POST["foods"])){
    $sql=new mysqli(HOST, USER, PASS, DTBS);
    $send=array();
    $send["rda"]=subject_stat($_POST["subjects"], $sql);
    $send["val"]=food_stat($_POST["foods"], $sql);
    $sql->close();
    echo json_encode($send);
}
else if(isSet($_POST["name"]) && isSet($_POST["energy"]) && isSet($_POST["protein"]) && isSet($_POST["iron"]) && isSet($_POST["vitA"]) && isSet($_POST["thiamine"]) && isSet($_POST["riboflavin"]) && isSet($_POST["vitC"])){
    $_SESSION["custom_food"][]=array("name"=>$_POST["name"], "energy"=>$_POST["energy"], "protein"=>$_POST["protein"], "iron"=>$_POST["iron"], "vitA"=>$_POST["vitA"], "thiamine"=>$_POST["thiamine"], "riboflavin"=>$_POST["riboflavin"], "vitC"=>$_POST["vitC"]);
    echo json_encode(["flag"=>true]);
}
else if(isSet($_POST["delete-custom-food"])){
    array_splice($_SESSION["custom_food"], $_POST["delete-custom-food"], 1);
    echo json_encode(["flag"=>true]);
}
else if(isSet($_GET["initdata"])){
    $data["list"]=get_food_list();
    $data["custom"]=get_custom_food();
    echo json_encode($data);
}
else{
    exit("Error");
}

function get_food_list(){
    $preset=array();
    $sql=new mysqli(HOST, USER, PASS, DTBS);
    $stmt=$sql->prepare("SELECT name FROM food");
    $stmt->execute();
    $stmt->bind_result($name);
    while($stmt->fetch()){
	$preset[]=$name;
    }
    $stmt->close();
    $sql->close();
    foreach($_SESSION["custom_food"] as $item){
	if(!in_array($item["name"], $preset)){
	    $preset[]=$item["name"];
	}
    }
    return $preset;
}

function get_custom_food(){
    return $_SESSION["custom_food"];
}

function subject_stat($subjects, $sql){
    $rda=array();
    for($i=0; $i<count($subjects); $i++){
	$stmt=$sql->prepare("SELECT energy, protein, iron, vitA, thiamine, riboflavin, vitC FROM rda WHERE subject=?");
	$stmt->bind_param("s", make_string($subjects[$i]));
	$stmt->execute();
	$stmt->bind_result($energy, $protein, $iron, $vitA, $thiamine, $riboflavin, $vitC);
	$stmt->fetch();
	$stmt->close();
	$rda[$i]["energy"]=(float)$energy;
	$rda[$i]["protein"]=(float)$protein;
	$rda[$i]["iron"]=(float)$iron;
	$rda[$i]["vitA"]=(float)$vitA;
	$rda[$i]["thiamine"]=(float)$thiamine;
	$rda[$i]["riboflavin"]=(float)$riboflavin;
	$rda[$i]["vitC"]=(float)$vitC;
	if($subjects[$i]["age"]>=18 && $subjects[$i]["age"]<=45 && $subjects[$i]["sex"]=="Female"){
	    if($subjects[$i]["state"]!="npnl"){
		$qs="";
		if($subjects[$i]["state"]=="preg"){
		    $qs="pregnant";
		}
		else if($subjects[$i]["state"]=="lactb6"){
		    $qs="lactating_below6";
		}
		else if($subjects[$i]["state"]=="lacto6"){
		    $qs="lactating_above6";
		}
		$stmt=$sql->prepare("SELECT energy, protein, iron, vitA, thiamine, riboflavin, vitC FROM rda WHERE subject=?");
		$stmt->bind_param("s", $qs);
		$stmt->execute();
		$stmt->bind_result($energy, $protein, $iron, $vitA, $thiamine, $riboflavin, $vitC);
		$stmt->fetch();
		$stmt->close();
		$rda[$i]["energy"]=$rda[$i]["energy"]+(float)$energy;
		$rda[$i]["protein"]=(float)$protein;
		$rda[$i]["iron"]=(float)$iron;
		$rda[$i]["vitA"]=(float)$vitA;
		$rda[$i]["thiamine"]=$rda[$i]["thiamine"]+(float)$thiamine;
		$rda[$i]["riboflavin"]=$rda[$i]["riboflavin"]+(float)$riboflavin;
		$rda[$i]["vitC"]=(float)$vitC;
	    }
	}
    }
    return $rda;
}

function food_stat($foods, $sql){
    $val=array();
    if(count($foods)>0){
	$qs="";
	for($i=0; $i<count($foods); $i++){
	    if(has_custom($foods[$i]["name"])){
		$val[$foods[$i]["name"]]["energy"]=(float)get_custom_by_name($foods[$i]["name"])["energy"];
		$val[$foods[$i]["name"]]["protein"]=(float)get_custom_by_name($foods[$i]["name"])["protein"];
		$val[$foods[$i]["name"]]["iron"]=(float)get_custom_by_name($foods[$i]["name"])["iron"];
		$val[$foods[$i]["name"]]["vitA"]=(float)get_custom_by_name($foods[$i]["name"])["vitA"];
		$val[$foods[$i]["name"]]["thiamine"]=(float)get_custom_by_name($foods[$i]["name"])["thiamine"];
		$val[$foods[$i]["name"]]["riboflavin"]=(float)get_custom_by_name($foods[$i]["name"])["riboflavin"];
		$val[$foods[$i]["name"]]["vitC"]=(float)get_custom_by_name($foods[$i]["name"])["vitC"];
	    }
	    else{
		if($qs==""){
		    $qs="name='".$foods[0]["name"]."'";
		}
		else{
		    $qs=$qs." OR name='".$foods[$i]["name"]."'";
		}
	    }
	}
	if($qs!=""){
	    $stmt=$sql->prepare("SELECT name, energy, protein, iron, vitA, thiamine, riboflavin, vitC FROM food WHERE ".$qs);
	    $stmt->execute();
	    $stmt->bind_result($name, $energy, $protein, $iron, $vitA, $thiamine, $riboflavin, $vitC);
	    while($stmt->fetch()){
		$val[$name]["energy"]=(float)$energy;
		$val[$name]["protein"]=(float)$protein;
		$val[$name]["iron"]=(float)$iron;
		$val[$name]["vitA"]=(float)$vitA;
		$val[$name]["thiamine"]=(float)$thiamine;
		$val[$name]["riboflavin"]=(float)$riboflavin;
		$val[$name]["vitC"]=(float)$vitC;
	    }
	    $stmt->close();
	}
    }
    return $val;
}

function make_string($sub){
    $idx="";
    if($sub["age"]==0){
	$idx="infant_9m-12m";
    }
    else if($sub["age"]<18){
	if($sub["sex"]=="Male"){
	    $idx="male_".$sub["age"]."y";
	}
	else if($sub["sex"]=="Female"){
	    $idx="female_".$sub["age"]."y";
	}
    }
    else{
	if($sub["sex"]=="Male"){
	    if($sub["work"]=="Sedentary"){
		$idx="male_sedentary";
	    }
	    else if($sub["work"]=="Moderate"){
		$idx="male_moderate";
	    }
	    if($sub["work"]=="Heavy"){
		$idx="male_heavy";
	    }
	}
	else if($sub["sex"]=="Female"){
	    if($sub["work"]=="Sedentary"){
		$idx="female_sedentary";
	    }
	    else if($sub["work"]=="Moderate"){
		$idx="female_moderate";
	    }
	    if($sub["work"]=="Heavy"){
		$idx="female_heavy";
	    }
	}
    }
    return $idx;
}

function has_custom($value){
    $flag=false;
    foreach($_SESSION["custom_food"] as $item){
	if($item["name"]==$value){
	    $flag=true;
	    break;
	}
    }
    return $flag;
}
function get_custom_by_name($name){
    $obj=false;
    foreach($_SESSION["custom_food"] as $item){
	if($item["name"]==$name){
	    $obj=$item;
	    break;
	}
    }
    return $obj;
}
?> 
