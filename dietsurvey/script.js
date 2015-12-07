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
var foodlist=[];
var customfood=[];
var family={};
family.members=[];
var foods=[];
var total={};


$(document).ready(function(){
    $("#js-check").hide();
    enum_date();
    $("#date-month").change(function(e){
	adjust_day($(e.target).val());
    });
    
    $("#final").hide();
    
    $("#block-wait").modal("show");
    $.get("ajax.php?initdata=true", function(data, state){
	$("#block-wait").modal("hide");
	if(state=="success"){
	    init=JSON.parse(data);
	    foodlist=init.list;
	    customfood=init.custom;
	    for(i=0; i<foodlist.length; i++){
		$("#food-name").append("<option>"+foodlist[i]+"</option>");
	    }
	}
	else{
	    $("#connect-fail").modal("show");
	}
    });
    
    $("#edit-member").on("show.bs.modal", function(){
	$("#member-name").val("");
	$("#member-age").val("");
	$("[name='member-sex']").removeProp("checked");
	$("#member-female-npnl").prop("checked", "true");
	$("#member-work-box").hide();
	$("#female-special").hide();
    });
    $("#member-age").change(function(){
	adjust_member();
    });
    $("[name='member-sex']").change(function(){
	adjust_member();
    });
    $("#save-member").click(function(){
	mem={};
	mem.name=$("#member-name").val();
	if(mem.name.length==0){
	    alert("Name can't be empty");
	    return;
	}
	mem.age=$("#member-age").val();
	if(mem.age=="" || isNaN(mem.age) || mem.age<0 || mem.age>100){
	    alert("Please enter correct age");
	    return;
	}
	if($("#member-sex-male").is(":checked")){
	    mem.sex="Male";
	}
	else if($("#member-sex-female").is(":checked")){
	    mem.sex="Female";
	}
	else{
	    alert("Please select sex");
	    return;
	}
	if(mem.age>=18){
	    mem.work=$("#member-work").val();
	    if(mem.age<=45 && mem.sex=="Female"){
		if($("#member-female-npnl").is(":checked")){
		    mem.state="npnl";
		}
		if($("#member-female-pregnant").is(":checked")){
		    mem.state="preg";
		}
		if($("#member-female-lactating-lt6").is(":checked")){
		    mem.state="lactb6";
		}
		if($("#member-female-lactating-gt6").is(":checked")){
		    mem.state="lacto6";
		}
	    }
	}
	family.members.push(mem);
	show_members();
	$("#edit-member").modal("hide");
    });
    $("#family-members").on("click", ".remove-member", function(e){
	family.members.splice($(e.target).data("id"), 1);
	show_members();
    });
    
    $("#edit-food").on("show.bs.modal", function(){
	$("#food-name").val("");
	$("#food-amount").val("");
    });
    
    $("#add-food").click(function(){
	item={};
	item.name=$("#food-name").val();
	item.amount=$("#food-amount").val();
	if(item.name==""){
	    alert("Please select a food");
	    return;
	}
	if(item.amount=="" || isNaN(item.amount) || item.amount<=0){
	    alert("Please enter correct amount");
	    return;
	}
	foods.push(item);
	show_foods();
	$("#edit-food").modal("hide");
    });
    $("#launch-custom").click(function(){
	$("#edit-food").modal("hide");
	$("#customize-food").modal("show");
    });
    $("#food-list").on("click", ".remove-food", function(e){
	foods.splice($(e.target).data("id"), 1);
	show_foods();
    });
    
    $("#customize-food").on("show.bs.modal", function(){
	show_custom_foods();
    });
    $("#custom-save").click(function(){
	save_custom_food();
    });
    $("#custom-existing").on("click", ".custom-delete", function(e){
	del_id=$(e.target).data("id");
	$("#customize-food :input, #customize-food :button").prop("disabled", "disabled");
	$.post("ajax.php", {"delete_custom_food":del_id}, function(data, state){
	    if(state=="success" && JSON.parse(data).flag){
		customfood.splice(del_id, 1);
		show_custom_foods();
	    }
	    else{
		alert("Server sync failed");
	    }
	    $("#customize-food :input, #customize-food :button").removeProp("disabled", "disabled");
	});
    });
    
    $("#get-result").click(function(){
	family.head=$("#family-head").val();
	family.date=[$("#date-day").val(), $("#date-month").val(), $("#date-year").val()];
	if(family.head.length==0){
	    alert("Please enter the name of the head of the family");
	    return;
	}
	else if(family.members.length==0){
	    alert("Please add family members");
	}
	else if(foods.length==0){
	    alert("Please add consumed foods");
	}
	retrieve();
    });
    
});

function retrieve(){
    compact={};
    compact.subjects=[];
    compact.foods=[];
    local=[];
    $.extend(true, compact.subjects, family.members);
    $.extend(true, compact.foods, foods);
    for(i=0; i<compact.subjects.length; i++){
	delete compact.subjects[i].name;
    }
    for(i=0; i<compact.foods.length; i++){
	delete compact.foods[i].amount;
    }
    $("#block-wait").modal("show");
    $.post("ajax.php", compact, function(string, status){
	$("#block-wait").modal("hide");
	if(status=="success"){
	    data=JSON.parse(string);
	    for(i=0; i<data["rda"].length; i++){
		family.members[i].rda=data["rda"][i];
	    }
	    for(i=0; i<foods.length; i++){
		foods[i].value=data["val"][foods[i].name];
	    }
	    calculate();
	    show_result();
	    draw_bar_diagram();
	}
	else{
	    $("#connect-fail").modal("show");
	}
    });
}

function calculate(){
    for(i=0; i<foods.length; i++){
	foods[i].intake={};
	foods[i].intake.energy=foods[i].value.energy*foods[i].amount/100;
	foods[i].intake.protein=foods[i].value.protein*foods[i].amount/100;
	foods[i].intake.iron=foods[i].value.iron*foods[i].amount/100;
	foods[i].intake.vitA=foods[i].value.vitA*foods[i].amount/100;
	foods[i].intake.thiamine=foods[i].value.thiamine*foods[i].amount/100;
	foods[i].intake.riboflavin=foods[i].value.riboflavin*foods[i].amount/100;
	foods[i].intake.vitC=foods[i].value.vitC*foods[i].amount/100;
    }
    total.intake={};
    total.intake.energy=0
    total.intake.protein=0
    total.intake.iron=0
    total.intake.vitA=0
    total.intake.thiamine=0
    total.intake.riboflavin=0
    total.intake.vitC=0
    for(i=0; i<foods.length; i++){
	total.intake.energy=total.intake.energy+foods[i].intake.energy;
	total.intake.protein=total.intake.protein+foods[i].intake.protein;
	total.intake.iron=total.intake.iron+foods[i].intake.iron;
	total.intake.vitA=total.intake.vitA+foods[i].intake.vitA;
	total.intake.thiamine=total.intake.thiamine+foods[i].intake.thiamine;
	total.intake.riboflavin=total.intake.riboflavin+foods[i].intake.riboflavin;
	total.intake.vitC=total.intake.vitC+foods[i].intake.vitC;
    }
    total.requirement={};
    total.requirement.energy=0
    total.requirement.protein=0
    total.requirement.iron=0
    total.requirement.vitA=0
    total.requirement.thiamine=0
    total.requirement.riboflavin=0
    total.requirement.vitC=0
    for(i=0; i<family.members.length; i++){
	total.requirement.energy=total.requirement.energy+family.members[i].rda.energy;
	total.requirement.protein=total.requirement.protein+family.members[i].rda.protein;
	total.requirement.iron=total.requirement.iron+family.members[i].rda.iron;
	total.requirement.vitA=total.requirement.vitA+family.members[i].rda.vitA;
	total.requirement.thiamine=total.requirement.thiamine+family.members[i].rda.thiamine;
	total.requirement.riboflavin=total.requirement.riboflavin+family.members[i].rda.riboflavin;
	total.requirement.vitC=total.requirement.vitC+family.members[i].rda.vitC;
    }
    total.diff={};
    total.diff.energy={};
    total.diff.protein={};
    total.diff.iron={};
    total.diff.vitA={};
    total.diff.thiamine={};
    total.diff.riboflavin={};
    total.diff.vitC={};
    if(total.requirement.energy>total.intake.energy){
	total.diff.energy.state="-";
	total.diff.energy.value=total.requirement.energy-total.intake.energy
    }
    else{
	total.diff.energy.state="+";
	total.diff.energy.value=total.intake.energy-total.requirement.energy
    }
    if(total.requirement.protein>total.intake.protein){
	total.diff.protein.state="-";
	total.diff.protein.value=total.requirement.protein-total.intake.protein
    }
    else{
	total.diff.protein.state="+";
	total.diff.protein.value=total.intake.protein-total.requirement.protein
    }
    if(total.requirement.iron>total.intake.iron){
	total.diff.iron.state="-";
	total.diff.iron.value=total.requirement.iron-total.intake.iron
    }
    else{
	total.diff.iron.state="+";
	total.diff.iron.value=total.intake.iron-total.requirement.iron
    }
    if(total.requirement.vitA>total.intake.vitA){
	total.diff.vitA.state="-";
	total.diff.vitA.value=total.requirement.vitA-total.intake.vitA
    }
    else{
	total.diff.vitA.state="+";
	total.diff.vitA.value=total.intake.vitA-total.requirement.vitA
    }
    if(total.requirement.thiamine>total.intake.thiamine){
	total.diff.thiamine.state="-";
	total.diff.thiamine.value=total.requirement.thiamine-total.intake.thiamine
    }
    else{
	total.diff.thiamine.state="+";
	total.diff.thiamine.value=total.intake.thiamine-total.requirement.thiamine
    }
    if(total.requirement.riboflavin>total.intake.riboflavin){
	total.diff.riboflavin.state="-";
	total.diff.riboflavin.value=total.requirement.riboflavin-total.intake.riboflavin
    }
    else{
	total.diff.riboflavin.state="+";
	total.diff.riboflavin.value=total.intake.riboflavin-total.requirement.riboflavin
    }
    if(total.requirement.vitC>total.intake.vitC){
	total.diff.vitC.state="-";
	total.diff.vitC.value=total.requirement.vitC-total.intake.vitC
    }
    else{
	total.diff.vitC.state="+";
	total.diff.vitC.value=total.intake.vitC-total.requirement.vitC
    }
}


function adjust_member(){
    age=$("#member-age").val();
    fem=false;
    if($("#member-sex-female").is(":checked")){
	fem=true;
    }
    if(age>=18){
	$("#member-work-box").show();
	if(fem && age<=45){
	    $("#female-special").show();
	}
	else{
	    $("#female-special").hide();
	}
    }
    else{
	$("#member-work-box").hide();
	$("#female-special").hide();
    }
}

function show_members(){
    $("#family-members").html("");
    for(i=0; i<family.members.length; i++){
	$("#family-members").append(display_member(family.members[i], i));
    }
}

function show_foods(){
    $("#food-list").html('<tr><th class="col-xs-6">Food Name</th><th class="col-xs-5">Amount (gm or ml)</th><th class="col-xs-1">Remove</th></tr>');
    for(i=0; i<foods.length; i++){
	$("#food-list").append(display_food(foods[i], i));
    }
}

function show_custom_foods(){
    $("#custom-existing").html("<tr><th>Name</th><th>Energy</th><th>Protein</th><th>Iron</th><th>Vitamin A</th><th>Thiamine</th><th>Riboflavin</th><th>Vitamin C</th><th>Delete</th></tr>");
    for(i=0; i<customfood.length; i++){
	$("#custom-existing").append('<tr><td>'+customfood[i].name+'</td><td>'+customfood[i].energy+'</td><td>'+customfood[i].protein+'</td><td>'+customfood[i].iron+'</td><td>'+customfood[i].vitA+'</td><td>'+customfood[i].thiamine+'</td><td>'+customfood[i].riboflavin+'</td><td>'+customfood[i].vitC+'</td><td class="text-center"><a href="#custom-existing" data-id="'+i+'" class="text-danger"><span class="glyphicon glyphicon-remove custom-delete" data-id="'+i+'"></span></a></td></tr>');
    }
}
function save_custom_food(){
    sv={};
    sv.name=$("#custom-name").val();
    sv.energy=$("#custom-energy").val();
    sv.protein=$("#custom-protein").val();
    sv.iron=$("#custom-iron").val();
    sv.vitA=$("#custom-vitA").val();
    sv.thiamine=$("#custom-thiamine").val();
    sv.riboflavin=$("#custom-riboflavin").val();
    sv.vitC=$("#custom-vitC").val();
    if(sv.name==""){
	alert("Please enter name");
	return;
    }
    else if(!foodlist.indexOf(sv.name)==-1){
	alert("The food is already present");
	return;
    }
    else if(!(check(sv.energy) && check(sv.protein) && check(sv.iron) && check(sv.vitA) && check(sv.thiamine) && check(sv.riboflavin) && check(sv.vitC))){
	alert("Please enter proper nutritional values");
	return;
    }
    $("#customize-food :input, #customize-food :button").prop("disabled", "disabled");
    $.post("ajax.php", sv, function(data, state){
	if(state=="success" && JSON.parse(data).flag){
	    customfood.push(sv);
	    foodlist.push(sv.name);
	    show_custom_foods();
	    for(i=0; i<foodlist.length; i++){
		$("#food-name").append("<option>"+foodlist[i]+"</option>");
	    }
	}
	else{
	    alert("Server sync failed");
	}
	$("#customize-food :input, #customize-food :button").removeProp("disabled");
    });
    
    function check(val){
	if(val=="" || isNaN(val) || val<0){
	    return false;
	}
	else{
	    return true;
	}
    }
}

function show_result(){
    $("#initial").hide();
    $("#final").slideDown();
    
    $("#show-hof").text(family.head);
    $("#show-date").text(family.date[0]+"-"+family.date[1]+"-"+family.date[2]);
    
    $("#show-members").html("<tr><th>Name</th><th>Age</th><th>Sex</th></tr>");
    $("#show-member-points").html("");
    for(i=0; i<family.members.length; i++){
	$("#show-members").append("<tr><td>"+family.members[i].name+"</td><td>"+family.members[i].age+" years</td><td>"+family.members[i].sex+"</td></tr>");
	if(family.members[i].age>=18){
	    $("#show-member-points").append('<li class="list-group-item">'+family.members[i].name+' is a '+family.members[i].work+' worker</li>');
	    if(family.members[i].age<=45 && family.members[i].sex=="Female"){
		if(family.members[i].state=="pregnant"){
		    $("#show-member-points").append('<li class="list-group-item">'+family.members[i].name+' is pregnant</li>');
		}
		else if(family.members[i].state=="lactb6"){
		    $("#show-member-points").append('<li class="list-group-item">'+family.members[i].name+' is lactating (below 6 months)</li>');
		}
		if(family.members[i].state=="lacto6"){
		    $("#show-member-points").append('<li class="list-group-item">'+family.members[i].name+' is lactating (over 6 months)</li>');
		}
	    }
	}
    }
    
    $("#show-foods").html("<tr><th>Name</th><th>Amount (mg or ml)</th></tr>");
    for(i=0; i<foods.length; i++){
	$("#show-foods").append("<tr><td>"+foods[i].name+"</td><td>"+foods[i].amount+"</td></tr>");
    }
    
    $("#show-requirement").html("<tr><th>Member</th><th>Energy</th><th>Protein</th><th>Iron</th><th>Vitamin A</th><th>Thiamine</th><th>Riboflavin</th><th>Vitamin C</th></tr>");
    for(i=0; i<family.members.length; i++){
	$("#show-requirement").append("<tr><td>"+family.members[i].name+"</td><td>"+family.members[i].rda.energy.toFixed(2)+" kcal</td><td>"+family.members[i].rda.protein.toFixed(2)+" gm</td><td>"+family.members[i].rda.iron.toFixed(2)+" mg</td><td>"+family.members[i].rda.vitA.toFixed(2)+" &mu;g</td><td>"+family.members[i].rda.thiamine.toFixed(2)+" mg</td><td>"+family.members[i].rda.riboflavin.toFixed(2)+" mg</td><td>"+family.members[i].rda.vitC.toFixed(2)+" mg</td></tr>");
    }
    $("#show-requirement").append("<tr><th>Total</th><th>"+total.requirement.energy.toFixed(2)+" kcal</th><th>"+total.requirement.protein.toFixed(2)+" gm</th><th>"+total.requirement.iron.toFixed(2)+" mg</th><th>"+total.requirement.vitA.toFixed(2)+" &mu;g</th><th>"+total.requirement.thiamine.toFixed(2)+" mg</th><th>"+total.requirement.riboflavin.toFixed(2)+" mg</th><th>"+total.requirement.vitC.toFixed(2)+" mg</th></tr>");
    
    $("#show-value").html("<tr><th>Food</th><th>Energy</th><th>Protein</th><th>Iron</th><th>Vitamin A</th><th>Thiamine</th><th>Riboflavin</th><th>Vitamin C</th></tr>");
    for(i=0; i<foods.length; i++){
	$("#show-value").append("<tr><td>"+foods[i].name+"</td><td>"+foods[i].intake.energy.toFixed(2)+"</td><td>"+foods[i].intake.protein.toFixed(2)+"</td><td>"+foods[i].intake.iron.toFixed(2)+"</td><td>"+foods[i].intake.vitA.toFixed(2)+"</td><td>"+foods[i].intake.thiamine.toFixed(2)+"</td><td>"+foods[i].intake.riboflavin.toFixed(2)+"</td><td>"+foods[i].intake.vitC.toFixed(2)+"</td></tr>");
    }
    $("#show-value").append("<tr><th>Total</th><th>"+total.intake.energy.toFixed(2)+"</th><th>"+total.intake.protein.toFixed(2)+"</th><th>"+total.intake.iron.toFixed(2)+"</th><th>"+total.intake.vitA.toFixed(2)+"</th><th>"+total.intake.thiamine.toFixed(2)+"</th><th>"+total.intake.riboflavin.toFixed(2)+"</th><th>"+total.intake.vitC.toFixed(2)+"</th></tr>");
    
    $("#show-compare").html("<tr><th>Nutrient</th><th>Requirement</th><th>Intake</th><th>Deficeiency / Excess</th></tr>");
    $("#show-summary").html("");
    if(total.diff.energy.state=="+"){
	$("#show-compare").append("<tr><td>Energy</td><td>"+total.requirement.energy.toFixed(2)+"</td><td>"+total.intake.energy.toFixed(2)+"</td><td>"+total.diff.energy.value.toFixed(2)+" excess</td></tr>");
	$("#show-summary").append("<li class='list-group-item list-group-item-success'>Energy consumption is excess by "+((total.diff.energy.value*100)/total.requirement.energy).toFixed(2)+"%</li>");
    }
    else{
	$("#show-compare").append("<tr><td>Energy</td><td>"+total.requirement.energy.toFixed(2)+"</td><td>"+total.intake.energy.toFixed(2)+"</td><td>"+total.diff.energy.value.toFixed(2)+" deficient</td></tr>");
	$("#show-summary").append("<li class='list-group-item list-group-item-danger'>Energy consumption is deficient by "+((total.diff.energy.value*100)/total.requirement.energy).toFixed(2)+"%</li>");
    }
    if(total.diff.protein.state=="+"){
	$("#show-compare").append("<tr><td>Protein</td><td>"+total.requirement.protein.toFixed(2)+"</td><td>"+total.intake.protein.toFixed(2)+"</td><td>"+total.diff.protein.value.toFixed(2)+" excess</td></tr>");
	$("#show-summary").append("<li class='list-group-item list-group-item-success'>Protein consumption is excess by "+((total.diff.protein.value*100)/total.requirement.protein).toFixed(2)+"%</li>");
    }
    else{
	$("#show-compare").append("<tr><td>Protein</td><td>"+total.requirement.protein.toFixed(2)+"</td><td>"+total.intake.protein.toFixed(2)+"</td><td>"+total.diff.protein.value.toFixed(2)+" deficient</td></tr>");
	$("#show-summary").append("<li class='list-group-item list-group-item-danger'>Protein consumption is deficient by "+((total.diff.protein.value*100)/total.requirement.protein).toFixed(2)+"%</li>");
    }
    if(total.diff.iron.state=="+"){
	$("#show-compare").append("<tr><td>Iron</td><td>"+total.requirement.iron.toFixed(2)+"</td><td>"+total.intake.iron.toFixed(2)+"</td><td>"+total.diff.iron.value.toFixed(2)+" excess</td></tr>");
	$("#show-summary").append("<li class='list-group-item list-group-item-success'>Iron consumption is excess by "+((total.diff.iron.value*100)/total.requirement.iron).toFixed(2)+"%</li>");
    }
    else{
	$("#show-compare").append("<tr><td>Iron</td><td>"+total.requirement.iron.toFixed(2)+"</td><td>"+total.intake.iron.toFixed(2)+"</td><td>"+total.diff.iron.value.toFixed(2)+" deficient</td></tr>");
	$("#show-summary").append("<li class='list-group-item list-group-item-danger'>Iron consumption is deficient by "+((total.diff.iron.value*100)/total.requirement.iron).toFixed(2)+"%</li>");
    }
    if(total.diff.vitA.state=="+"){
	$("#show-compare").append("<tr><td>Vitamin A</td><td>"+total.requirement.vitA.toFixed(2)+"</td><td>"+total.intake.vitA.toFixed(2)+"</td><td>"+total.diff.vitA.value.toFixed(2)+" excess</td></tr>");
	$("#show-summary").append("<li class='list-group-item list-group-item-success'>Vitamin A consumption is excess by "+((total.diff.vitA.value*100)/total.requirement.vitA).toFixed(2)+"%</li>");
    }
    else{
	$("#show-compare").append("<tr><td>Vitamin A</td><td>"+total.requirement.vitA.toFixed(2)+"</td><td>"+total.intake.vitA.toFixed(2)+"</td><td>"+total.diff.vitA.value.toFixed(2)+" deficient</td></tr>");
	$("#show-summary").append("<li class='list-group-item list-group-item-danger'>Vitamin A consumption is deficient by "+((total.diff.vitA.value*100)/total.requirement.vitA).toFixed(2)+"%</li>");
    }
    if(total.diff.thiamine.state=="+"){
	$("#show-compare").append("<tr><td>Thiamine</td><td>"+total.requirement.thiamine.toFixed(2)+"</td><td>"+total.intake.thiamine.toFixed(2)+"</td><td>"+total.diff.thiamine.value.toFixed(2)+" excess</td></tr>");
	$("#show-summary").append("<li class='list-group-item list-group-item-success'>Thiamine consumption is excess by "+((total.diff.thiamine.value*100)/total.requirement.thiamine).toFixed(2)+"%</li>");
    }
    else{
	$("#show-compare").append("<tr><td>Thiamine</td><td>"+total.requirement.thiamine.toFixed(2)+"</td><td>"+total.intake.thiamine.toFixed(2)+"</td><td>"+total.diff.thiamine.value.toFixed(2)+" deficient</td></tr>");
	$("#show-summary").append("<li class='list-group-item list-group-item-danger'>Thiamine consumption is deficient by "+((total.diff.thiamine.value*100)/total.requirement.thiamine).toFixed(2)+"%</li>");
    }
    if(total.diff.riboflavin.state=="+"){
	$("#show-compare").append("<tr><td>Riboflavin</td><td>"+total.requirement.riboflavin.toFixed(2)+"</td><td>"+total.intake.riboflavin.toFixed(2)+"</td><td>"+total.diff.riboflavin.value.toFixed(2)+" excess</td></tr>");
	$("#show-summary").append("<li class='list-group-item list-group-item-success'>Riboflavin consumption is excess by "+((total.diff.riboflavin.value*100)/total.requirement.riboflavin).toFixed(2)+"%</li>");
    }
    else{
	$("#show-compare").append("<tr><td>Riboflavin</td><td>"+total.requirement.riboflavin.toFixed(2)+"</td><td>"+total.intake.riboflavin.toFixed(2)+"</td><td>"+total.diff.riboflavin.value.toFixed(2)+" deficient</td></tr>");
	$("#show-summary").append("<li class='list-group-item list-group-item-danger'>Riboflavin consumption is deficient by "+((total.diff.riboflavin.value*100)/total.requirement.riboflavin).toFixed(2)+"%</li>");
    }
    if(total.diff.vitC.state=="+"){
	$("#show-compare").append("<tr><td>Vitamin C</td><td>"+total.requirement.vitC.toFixed(2)+"</td><td>"+total.intake.vitC.toFixed(2)+"</td><td>"+total.diff.vitC.value.toFixed(2)+" excess</td></tr>");
	$("#show-summary").append("<li class='list-group-item list-group-item-success'>Vitamin C consumption is excess by "+((total.diff.vitC.value*100)/total.requirement.vitC).toFixed(2)+"%</li>");
    }
    else{
	$("#show-compare").append("<tr><td>Vitamin C</td><td>"+total.requirement.vitC.toFixed(2)+"</td><td>"+total.intake.vitC.toFixed(2)+"</td><td>"+total.diff.vitC.value.toFixed(2)+" deficient</td></tr>");
	$("#show-summary").append("<li class='list-group-item list-group-item-danger'>Vitamin C consumption is deficient by "+((total.diff.vitC.value*100)/total.requirement.vitC).toFixed(2)+"%</li>");
    }
}

function enum_date(){
    for(i=2010; i<=2020; i++){
	if(i==2014){
	    $("#date-year").append("<option selected>"+i+"</option>");
	}
	else{
	    $("#date-year").append("<option>"+i+"</option>");
	}
    }
    for(i=1; i<=12; i++){
	if(i==10){
	    $("#date-month").append("<option selected>"+i+"</option>");
	}
	else{
	    $("#date-month").append("<option>"+i+"</option>");
	}
    }
    adjust_day(10);
}

function adjust_day(month){
    lim=31;
    if(month==2){
	lim=28;
    }
    else if(month==4||month==6||month==9||month==11){
	lim=30;
    }
    day=$("#date-day").val();
    $("#date-day").html("");
    if(day>lim){
	day=lim;
    }
    for(i=1; i<=lim; i++){
	if(i==day){
	    $("#date-day").append("<option selected>"+i+"</option>");
	}
	else{
	    $("#date-day").append("<option>"+i+"</option>");
	}
    }
}

function display_member(mem, i){
    html='<div class="panel panel-success">'
	+'<div class="panel-heading">'
	+'<div class="row">'
	+'<div class="col-xs-offset-11 col-xs-1"><span data-id="'+i+'" class="point remove-member text-warning"><span data-id="'+i+'" class="glyphicon glyphicon-remove"></span></span></div>'
	+'</div>'
	+'</div>'
	+'<div class="panel-body">'
	+'<form class="form-horizontal" role="form">'
	+'<div class="form-group">'
	+'<label class="col-sm-3 control-label">Name:</label>'
	+'<div class="col-sm-9">'
	+'<p class="form-control-static">'+mem.name+'</p>'
	+'</div>'
	+'</div>'
	+'<div class="form-group">'
	+'<label class="col-sm-3 control-label">Age:</label>'
	+'<div class="col-sm-9">'
	+'<p class="form-control-static">'+mem.age+'</p>'
	+'</div>'
	+'</div>'
	+'<div class="form-group">'
	+'<label class="col-sm-3 control-label">Sex:</label>'
	+'<div class="col-sm-9">'
	+'<p class="form-control-static">'+mem.sex+'</p>'
	+'</div>'
	+'</div>';
    if(mem.age>=18){
	html=html+'<div class="form-group">'
	    +'<label class="col-sm-3 control-label">Work Type:</label>'
	    +'<div class="col-sm-9">'
	    +'<p class="form-control-static">'+mem.work+'</p>'
	    +'</div>'
	    +'</div>'
    }
    html=html+'</form>'
	+'</div>';
    if(mem.age>=18 && mem.age<=45 && mem.sex=="Female"){
	if(mem.state=="npnl"){
	    state="";
	}
	else if(mem.state=="preg"){
	    state="Pregnant";
	}
	else if(mem.state=="lactb6"){
	    state="Lactating, below 6 months";
	}
	else if(mem.state=="lacto6"){
	    state="Lactating, over 6 months";
	}
	if(state!=""){
	    html=html+'<div class="panel-footer">'
		+'<div class="text-center">'
		+'<p class="form-control-static">'+state+'</p>'
		+'</div>'
		+'</div>';
	}
    }
    html=html+'</div>';
    return html;
}

function display_food(item, i){
    html='<tr><td class="col-xs-6">'+item.name+'</td><td class="col-xs-5">'+item.amount+'</td><td class="col-xs-1 text-center"><span class="point remove-food text-warning" data-id="'+i+'"><span class="glyphicon glyphicon-remove" data-id="'+i+'"></span></span></td></tr>';
    return html;
}

function draw_bar_diagram(){
    data={
	labels: ["Energy", "Protein", "Iron", "Vitamine A", "Thiamine", "Riboflavin", "Vitamin C"],
	datasets: [
	    {
		label: "dataset",
		fillColor: "rgba(151,187,205,0.5)",
		strokeColor: "rgba(151,187,205,0.8)",
		highlightFill: "rgba(151,187,205,0.75)",
		highlightStroke: "rgba(151,187,205,1)",
		data: [parseFloat(((total.intake.energy*100)/total.requirement.energy).toFixed(2)), parseFloat(((total.intake.protein*100)/total.requirement.protein).toFixed(2)), parseFloat(((total.intake.iron*100)/total.requirement.iron).toFixed(2)), parseFloat(((total.intake.vitA*100)/total.requirement.vitA).toFixed(2)), parseFloat(((total.intake.thiamine*100)/total.requirement.thiamine).toFixed(2)), parseFloat(((total.intake.riboflavin*100)/total.requirement.riboflavin).toFixed(2)), parseFloat(((total.intake.vitC*100)/total.requirement.vitC).toFixed(2))]
	    }
	]
    };

    
    var ctx=$("#bar-diagram").get(0).getContext("2d");
    var diagram=new Chart(ctx).Bar(data);
}
