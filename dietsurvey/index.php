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
<!DOCTYPE html>
<html>
    <head>
	<title>Diet Survey</title>
	<meta charset="UTF-8"/>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
	<link href="res/bootstrap-3.3.6-dist/css/bootstrap.css" rel="stylesheet">
	<link href="res/custom.css" rel="stylesheet">
	<script src="res/jquery-1.11.3.min.js"></script>
	<script src="res/bootstrap-3.3.6-dist/js/bootstrap.js"></script>
	<script src="res/Chart.min.js"></script>
	<script src="script.js"></script>
    </head>
    <body>
	<div class="container">
	    <div class="text-primary">
		<div class="jumbotron">
		    <h1>Diet Survey</h1>
		    <h2>Family diet survey based on 24 hours recall...</h2>
		</div>
	    </div>
	    
	    <div id="js-check">
		<div class="alert alert-danger">
		    <h3>It looks like you don't have JavaScript enabled. Please enable JavaScript to use Diet-Survey.</h3>
		</div>
	    </div>
	    
	    <div id="initial" class="row"><!--Initial-->
		
		<div id="family-info" class="col-sm-6"><!--Family Info-->
		    <div class="panel panel-success">
			<div class="panel-heading">
			    <h3>Family Information</h3>
			</div>
			<div class="panel-body">
			    <form role="form">
				<div class="form-group">
				    <label for="">Head of the Family:</label>
				    <input class="form-control" type="text" id="family-head" placeholder="Enter Name"/>
				</div>
				<hr>
			    </form>
			    
			    <button class="btn btn-success btn-block" type="button" data-toggle="modal" data-target="#edit-member">Add Family Member</button>
			    <hr>
			    
			    <div id="family-members">
			    </div>
			    
			    
			    <div class="modal fade" id="edit-member" tabindex="-1" role="dialog" aria-hidden="true">
				<div class="modal-dialog">
				    <div class="modal-content">
					<div class="modal-header">
					    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					    <h4>Add a member:</h4>
					</div>
					<div class="modal-body">
					    <form class="form-horizontal" role="form">
						<div class="form-group">
						    <label for="member-name" class="control-label col-sm-3">Name:</label>
						    <div class="col-sm-9">
							<input class="form-control" type="text" id="member-name" placeholder="Enter name"/>
						    </div>
						</div>
						<div class="form-group">
						    <label for="member-age" class="control-label col-sm-3">Age:</label>
						    <div class="col-sm-9">
							<input class="form-control" type="text" id="member-age" placeholder="Enter age in years"/>
						    </div>
						</div>
						<div class="form-group">
						    <div class="col-sm-offset-3 col-sm-2">
							<div class="radio">
							    <label>
								<input type="radio" id="member-sex-male" name="member-sex"/>Male
							    </label>
							</div>
						    </div>
						    <div class="col-sm-2">
							<div class="radio">
							    <label>
								<input type="radio" id="member-sex-female" name="member-sex"/>Female
							    </label>
							</div>
						    </div>
						</div>
						<div class="form-group" id="member-work-box">
						    <label for="member-work" class="control-label col-sm-3">Work Type:</label>
						    <div class="col-sm-9">
							<select class="form-control" id="member-work">
							    <option value="Sedentary">Sedentary</option>
							    <option value="Moderate">Moderate</option>
							    <option value="Heavy">Heavy</option>
							</select>
						    </div>
						</div>
						<div class="well" id="female-special">
						    <div class="form-group">
							<div class="col-sm-offset-4">
							    <div class="radio">
								<label>
								    <input type="radio" id="member-female-npnl" name="member-female" checked/>Non-Pregnant / Non-Lactating
								</label>
							    </div>
							</div>
						    </div>
						    <div class="form-group">
							<div class="col-sm-offset-4">
							    <div class="radio">
								<label>
								    <input type="radio" id="member-female-pregnant" name="member-female"/>Pregnant
								</label>
							    </div>
							</div>
						    </div>
						    <div class="form-group">
							<div class="col-sm-offset-4">
							    <div class="radio">
								<label>
								    <input type="radio" id="member-female-lactating-lt6" name="member-female"/>Lactating, below 6 months
								</label>
							    </div>
							</div>
						    </div>
						    <div class="form-group">
							<div class="col-sm-offset-4">
							    <div class="radio">
								<label>
								    <input type="radio" id="member-female-lactating-gt6" name="member-female"/>Lactating, over 6 months
								</label>
							    </div>
							</div>
						    </div>
						</div>
					    </form>
					</div>
					<div class="modal-footer">
					    <button type="button" class="btn btn-primary" id="save-member">Save Member</button>
					    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				    </div>
				</div>
			    </div>
			    
			</div>
		    </div>
		</div><!--Family-info-->
		
		<div id="food-info" class="col-sm-6"><!--Food Info-->
		    <div class="panel panel-success">
			<div class="panel-heading">
			    <h3>Food Consumption in 24 hours</h3>
			</div>
			<div class="panel-body">
			    <form role="form">
				<div class="form-group">
				    <label for="date-day">Date:</label>
				    <div class="row">
					<div class="col-sm-4">
					    <select class="form-control" id="date-day">
					    </select>
					</div>
					<div class="col-sm-4">
					    <select class="form-control" id="date-month">
					    </select>
					</div>
					<div class="col-sm-4">
					    <select class="form-control" id="date-year">
					    </select>
					</div>
				    </div>
				</div>
				<hr>
			    </form>
			    
			    <button class="btn btn-success btn-block" type="button" data-toggle="modal" data-target="#edit-food">Add Food</button>
			    <hr>
			    
			    <table class="table table-hover" id="food-list">
			    </table>
			    
			    <div class="modal fade" id="edit-food" tabindex="-1" role="dialog" aria-hidden="true">
				<div class="modal-dialog">
				    <div class="modal-content">
					<div class="modal-header">
					    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					    <h4>Add a food:</h4>
					</div>
					<div class="modal-body">
					    <form class="form-horizontal" role="form">
						<div class="form-group">
						    <label for="food-name" class="control-label col-sm-3">Food Name:</label>
						    <div class="col-sm-9">
							<select class="form-control" id="food-name">
							    <option value="">--Select a food--</option>
							</select>
						    </div>
						</div>
						<div class="form-group">
						    <label for="food-amount" class="control-label col-sm-3">Amount:</label>
						    <div class="col-sm-9">
							<input class="form-control" type="text" id="food-amount" placeholder="Enter amount in gm or ml"/>
						    </div>
						</div>
					    </form>
					</div>
					<div class="modal-footer">
					    <button type="button" class="btn btn-primary" id="add-food">Add Food</button>
					    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					    <button type="button" class="btn btn-default" id="launch-custom">New Food</button>
					</div>
				    </div>
				</div>
			    </div>
			    
			</div>
		    </div>
		</div><!--Food Info-->
		
		<hr>
		<div class="row">
		    <div class="col-sm-offset-4 col-sm-4">
			<button type="button" class="btn btn-primary btn-lg btn-block" id="get-result">Get Result</button>
		    </div>
		</div>
		
	    </div><!--Initial-->
	    
	    <div class="row" id="final"><!--Final-->
		<div class="panel panel-info">
		    <div class="panel-heading">
			<h3>Family Information</h3>
		    </div>
		    <div class="panel-body">
			<form class="form-horizontal" role="form">
			    <div class="form-group">
				<label class="col-sm-6 control-label">Head of the Family:</label>
				<div class="col-sm-6">
				    <p class="form-control-static" id="show-hof"></p>
				</div>
				<label class="col-sm-6 control-label">Date of Survey:</label>
				<div class="col-sm-6">
				    <p class="form-control-static" id="show-date"></p>
				</div>
			    </div>
			</form>
			<div class="panel panel-default">
			    <div class="panel-heading">
				<h5>Family Members</h5>
			    </div>
			    <div class="body">
				<div class="row">
				    <div class="col-md-offset-2 col-md-8">
					<table class="table table-bordered table-hover" id="show-members">
					    <tr><th>Name</th><th>Age</th><th>Sex</th></tr>
					</table>
					<ul class="list-group" id="show-member-points">
					</ul>
				    </div>
				</div>
			    </div>
			</div>
		    </div>
		</div>
		<div class="panel panel-info">
		    <div class="panel-heading">
			<h3>Foods consumed in last 24 hours</h3>
		    </div>
		    <div class="body">
			<div class="row">
			    <div class="col-md-offset-4 col-md-4">
				<table class="table table-hover" id="show-foods">
				    <tr><th>Name</th><th>Amount (mg or ml)</th></tr>
				</table>
			    </div>
			</div>
		    </div>
		</div>
		<div class="panel panel-info">
		    <div class="panel-heading">
			<h3>Nutrient Requirement</h3>
			<h6>Reference: Nutrient Requirements and Recommended Dietary Allowences for Indians</h6>
			<h6>A Report of the Expert Group of the ICMR 2009</h6>
			<h6>National Institute of Nutrition, ICMR</h6>
		    </div>
		    <div class="body">
			<table class="table table-hover" id="show-requirement">
			    <tr><th>Member</th><th>Energy</th><th>Protein</th><th>Iron</th><th>Vitamin A</th><th>Thiamine</th><th>Riboflavin</th><th>Vitamin C</th></tr>
			</table>
		    </div>
		</div>
		<div class="panel panel-info">
		    <div class="panel-heading">
			<h3>Nutritional value of consumed foods</h3>
			<h6>Reference: Nutritive Value of Indian Foods, ICMR, 1989</h6>
		    </div>
		    <div class="body">
			<table class="table table-hover" id="show-value">
			    <tr><th>Food</th><th>Energy</th><th>Protein</th><th>Iron</th><th>Vitamin A</th><th>Thiamine</th><th>Riboflavin</th><th>Vitamin C</th></tr>
			</table>
		    </div>
		</div>
		<div class="panel panel-info">
		    <div class="panel-heading">
			<h3>Comparison of nutrient intake</h3>
		    </div>
		    <div class="body">
			<table class="table table-hover" id="show-compare">
			    <tr><th>Nutrient</th><th>Requirement</th><th>Intake</th><th>Deficeiency / Excess</th></tr>
			</table>
		    </div>
		</div>
		<div class="panel panel-info">
		    <div class="panel-heading">
			<h3>Summary</h3>
		    </div>
		    <div class="body row">
			<br>
			<div class="col-md-offset-3 col-md-6">
			    <ul class="list-group" id="show-summary">
				<li class="list-group-item">summary</li>
			    </ul>
			</div>
		    </div>
		</div>
		
		<canvas id="bar-diagram" height="400" width="600" class="center-block"></canvas>
		
		<hr>
		<div class="row">
		    <div class="col-sm-offset-4 col-sm-4">
			<a href="" class="btn btn-primary btn-lg btn-block">Do Another Survey</a>
		    </div>
		</div>
	    </div><!--Final-->
	    
	</div>
	
	<div class="modal fade" id="customize-food" tabindex="-1" role="dialog" aria-hidden="true">
	    <div class="modal-dialog modal-lg">
		<div class="modal-content">
		    <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4>Custom Food List:</h4>
		    </div>
		    <div class="modal-body">
			<table class="table">
			    <tr class="text-center">
				<th>Name</th>
				<th>Energy</th>
				<th>Protein</th>
				<th>Iron</th>
				<th>Vitamin A</th>
				<th>Thiamine</th>
				<th>Riboflavin</th>
				<th>Vitamin C</th>
				<th></th>
			    </tr>
			    <tr>
				<td><input type="text" class="form-control" id="custom-name" placeholder="Name"></td>
				<td><input type="text" class="form-control" id="custom-energy" placeholder="Energy"></td>
				<td><input type="text" class="form-control" id="custom-protein" placeholder="Protein"></td>
				<td><input type="text" class="form-control" id="custom-iron" placeholder="Iron"></td>
				<td><input type="text" class="form-control" id="custom-vitA" placeholder="Vit A"></td>
				<td><input type="text" class="form-control" id="custom-thiamine" placeholder="Thiamine"></td>
				<td><input type="text" class="form-control" id="custom-riboflavin" placeholder="Riboflavin"></td>
				<td><input type="text" class="form-control" id="custom-vitC" placeholder="Vit C"></td>
				<td><button class="btn btn-success" id="custom-save">Save</button></td>
			    </tr>
			</table>
			<hr>
			<div style="overflow:auto">
			    <table class="table table-hover" id="custom-existing">
			    </table>
			</div>
		    </div>
		    <div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		    </div>
		</div>
	    </div>
	</div>
	
	<div class="modal fade" id="block-wait" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
	    <div class="modal-dialog">
		<div class="modal-content">
		    <div class="modal-header">
			<h4>Please wait while retrieving data from server</h4>
		    </div>
		</div>
	    </div>
	</div>
	
	<div class="modal fade" id="connect-fail" tabindex="-1" role="dialog" aria-hidden="true">
	    <div class="modal-dialog">
		<div class="modal-content">
		    <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4>Failed to connect server</h4>
		    </div>
		</div>
	    </div>
	</div>
	
	<hr>
	<div class="panel panel-info">
	    <div class="panel-heading">
		<h3>Diet Survey</h3>
		<h4>Copyright &copy; 2014 Agnibho Mondal</h4>
		<h4><a href="http://www.agnibho.com" style="text-decoration:none">www.agnibho.com</a></h4>
	    </div>
	</div>
    </body>
</html>
