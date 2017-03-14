<!--
  **********************************************************************
  * Title: DietSurvey
  * Description: Nutritional Assessment App
  * Author: Agnibho Mondal
  * Website: http://code.agnibho.com
  **********************************************************************
  Copyright (c) 2016 Agnibho Mondal
  All rights reserved
  **********************************************************************
  This file is part of DietSurvey.

  DietSurvey is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  DietSurvey is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with DietSurvey. If not, see <http://www.gnu.org/licenses/>.
  **********************************************************************
-->
<template>
  <div v-if="calcVal" class="panel panel-default">
    <div class="panel-body">
      <!-- Requirements -->
      <div class="panel panel-info">
        <div class="panel-heading">
          <div class="panel-title"><h3>Nutritional Requirements</h3></div>
        </div>
        <div class="panel-body">
          <table class="table">
            <thead>
              <tr><th>Member</th><th>Energy</th><th>Protein</th><th>Iron</th><th>Vitamin A</th><th>Thiamine</th><th>Riboflavin</th><th>Vitamin C</th></tr>
            </thead>
              <tbody>
                <tr v-for="i in calcVal.req.list">
                  <td>{{i.name}}</td><td>{{i.energy|dec2}}</td><td>{{i.protein|dec2}}</td><td>{{i.iron|dec2}}</td><td>{{i.vitA|dec2}}</td><td>{{i.thiamine|dec2}}</td><td>{{i.riboflavin|dec2}}</td><td>{{i.vitC|dec2}}</td>
                </tr>
              </tbody>
              <thead>
                <tr><th>Total</th><th>{{calcVal.req.total.energy|dec2}}</th><th>{{calcVal.req.total.protein|dec2}}</th><th>{{calcVal.req.total.iron|dec2}}</th><th>{{calcVal.req.total.vitA|dec2}}</th><th>{{calcVal.req.total.thiamine|dec2}}</th><th>{{calcVal.req.total.riboflavin|dec2}}</th><th>{{calcVal.req.total.vitC|dec2}}</th></tr>
              </thead>
          </table>
        </div>
      </div>
      <!-- Consumption -->
      <div class="panel panel-info">
        <div class="panel-heading">
          <div class="panel-title"><h3>Nutritional Value of Consumed Foods</h3></div>
        </div>
        <div class="panel-body">
          <table class="table">
            <thead>
              <tr><th>Food</th><th>Energy</th><th>Protein</th><th>Iron</th><th>Vitamin A</th><th>Thiamine</th><th>Riboflavin</th><th>Vitamin C</th></tr>
            </thead>
              <tbody>
                <tr v-for="i in calcVal.intk.list">
                  <td>{{i.name}}</td><td>{{i.energy|dec2}}</td><td>{{i.protein|dec2}}</td><td>{{i.iron|dec2}}</td><td>{{i.vitA|dec2}}</td><td>{{i.thiamine|dec2}}</td><td>{{i.riboflavin|dec2}}</td><td>{{i.vitC|dec2}}</td>
                </tr>
              </tbody>
              <thead>
                <tr><th>Total</th><th>{{calcVal.intk.total.energy|dec2}}</th><th>{{calcVal.intk.total.protein|dec2}}</th><th>{{calcVal.intk.total.iron|dec2}}</th><th>{{calcVal.intk.total.vitA|dec2}}</th><th>{{calcVal.intk.total.thiamine|dec2}}</th><th>{{calcVal.intk.total.riboflavin|dec2}}</th><th>{{calcVal.intk.total.vitC|dec2}}</th></tr>
              </thead>
          </table>
        </div>
      </div>
      <!-- Comparison -->
      <div class="panel panel-info">
        <div class="panel-heading">
          <div class="panel-title"><h3>Requirement vs Intake Comparison</h3></div>
        </div>
        <div class="panel-body">
          <table class="table">
            <thead><tr><th>Nutrient</th><th>Total Requirement</th><th>Total Intake</th><th>Excess /Deficiency</th></tr></thead>
            <tbody>
              <tr><td>Energy</td><td>{{calcVal.req.total.energy|dec2}}</td><td>{{calcVal.intk.total.energy|dec2}}</td><td>{{calcVal.comp.energy[0]|cmp(2)}}</td></tr>
              <tr><td>Protein</td><td>{{calcVal.req.total.protein|dec2}}</td><td>{{calcVal.intk.total.protein|dec2}}</td><td>{{calcVal.comp.protein[0]|cmp(2)}}</td></tr>
              <tr><td>Iron</td><td>{{calcVal.req.total.iron|dec2}}</td><td>{{calcVal.intk.total.iron|dec2}}</td><td>{{calcVal.comp.iron[0]|cmp(2)}}</td></tr>
              <tr><td>Vitamin A</td><td>{{calcVal.req.total.vitA|dec2}}</td><td>{{calcVal.intk.total.vitA|dec2}}</td><td>{{calcVal.comp.vitA[0]|cmp(2)}}</td></tr>
              <tr><td>Thiamine</td><td>{{calcVal.req.total.thiamine|dec2}}</td><td>{{calcVal.intk.total.thiamine|dec2}}</td><td>{{calcVal.comp.thiamine[0]|cmp(2)}}</td></tr>
              <tr><td>Riboflavin</td><td>{{calcVal.req.total.riboflavin|dec2}}</td><td>{{calcVal.intk.total.riboflavin|dec2}}</td><td>{{calcVal.comp.riboflavin[0]|cmp(2)}}</td></tr>
              <tr><td>Vitamin C</td><td>{{calcVal.req.total.vitC|dec2}}</td><td>{{calcVal.intk.total.vitC|dec2}}</td><td>{{calcVal.comp.vitC[0]|cmp(2)}}</td></tr>
            </tbody>
        </div>
      </div>
      <!-- Summary -->
      <div class="panel panel-info">
        <div class="panel-heading">
          <div class="panel-title"><h3>Summary</h3></div>
        </div>
        <div class="panel-body">
          <ul class="list-group col-md-offset-4 col-md-4">
            <li class="list-group-item" v-bind:class="colorCls(calcVal.comp.energy[0])">Energy {{calcVal.comp.energy[0]|cmp(1)}} is {{calcVal.comp.energy[1]|cmp}}%</li>
            <li class="list-group-item" v-bind:class="colorCls(calcVal.comp.protein[0])">Protein {{calcVal.comp.protein[0]|cmp(1)}} is {{calcVal.comp.protein[1]|cmp}}%</li>
            <li class="list-group-item" v-bind:class="colorCls(calcVal.comp.iron[0])">Iron {{calcVal.comp.iron[0]|cmp(1)}} is {{calcVal.comp.iron[1]|cmp}}%</li>
            <li class="list-group-item" v-bind:class="colorCls(calcVal.comp.vitA[0])">Vitamin A {{calcVal.comp.vitA[0]|cmp(1)}} is {{calcVal.comp.vitA[1]|cmp}}%</li>
            <li class="list-group-item" v-bind:class="colorCls(calcVal.comp.thiamine[0])">Thiamine {{calcVal.comp.thiamine[0]|cmp(1)}} is {{calcVal.comp.thiamine[1]|cmp}}%</li>
            <li class="list-group-item" v-bind:class="colorCls(calcVal.comp.riboflavin[0])">Riboflavin {{calcVal.comp.riboflavin[0]|cmp(1)}} is {{calcVal.comp.riboflavin[1]|cmp}}%</li>
            <li class="list-group-item" v-bind:class="colorCls(calcVal.comp.vitC[0])">Vitamin C {{calcVal.comp.vitC[0]|cmp(1)}} is {{calcVal.comp.vitC[1]|cmp}}%</li>
          </ul>
        </div>
      </div>
      <hr>
    </div>
  </div>
</template>

<script>
export default {
  name:"Output",
  props:["dataObj", "dataRef"],
  filters:{
    dec2: function(v){
      return Math.round(v*100)/100;
    },
    cmp: function(v, f){
      if(f==1){
        if(v<0) return "deficit";
        else return "excess";
      }if(f==2){
        if(v<0) return "Deficit by "+Math.abs(Math.round(v*100)/100);
        else return "Excess by "+Math.abs(Math.round(v*100)/100);
      }
      else{
        return Math.abs(Math.round(v*100)/100);
      }
    }
  },
  methods:{
    colorCls: function(v){
      if (v<0) return {"list-group-item-danger": true};
      else return {"list-group-item-success": true};
    }
  },
  computed:{
    calcVal: function(){
      var rda=this.dataRef.rda;
      var val=this.dataRef.food;
      var root={};
      // Requirement Calculation
      root.req={};
      root.req.list=[];
      root.req.total={};
      for(var i=0; i<this.dataObj.family.length; i++){
        var mem=this.dataObj.family[i];
        var buff={};
        // Retrieve individual rda
        // Female
        if(mem.sex=="f"){
          if(mem.age>=18){
            if(mem.state=="preg"){
              buff=rda.filter(function(d){
                return d.subject=="pregnant";
              })[0];
            }
            else if(mem.state=="lact-6"){
              buff=rda.filter(function(d){
                return d.subject=="lactating_below6";
              })[0];
            }
            else if(mem.state=="lact+6"){
              buff=rda.filter(function(d){
                return d.subject=="lactating_above6";
              })[0];
            }
            else if(mem.state=="npnl"){
              if(mem.work=="sedentary"){
                buff=rda.filter(function(d){
                  return d.subject=="female_sedentary";
                })[0];
              }
              else if(mem.work=="moderate"){
                buff=rda.filter(function(d){
                  return d.subject=="female_moderate";
                })[0];
              }
              else if(mem.work=="heavy"){
                buff=rda.filter(function(d){
                  return d.subject=="female_heavy";
                })[0];
              }
            }
            else{
              return false;
            }
          }
          else if(mem.age<18 && mem.age>0){
            buff=rda.filter(function(d){
              return d.subject=="female_"+mem.age+"y";
            })[0];
          }
          else if(mem.month>=9){
            buff=rda.filter(function(d){
              return d.subject=="infant_9m-12m";
            })[0];
          }
          else if(mem.month>=6){
            buff=rda.filter(function(d){
              return d.subject=="infant_6m-9m";
            })[0];
          }
          else if(mem.month>=0){
            if(mem.month===""){
              return false;
            }
            buff=rda.filter(function(d){
              return d.subject=="infant_0m-6m";
            })[0];
          }
          else{
            return false;
          }
        }
        // Male
        else if(mem.sex=="m"){
          if(mem.age>=18){
            if(mem.work=="sedentary"){
              buff=rda.filter(function(d){
                return d.subject=="male_sedentary";
              })[0];
            }
            else if(mem.work=="moderate"){
              buff=rda.filter(function(d){
                return d.subject=="male_moderate";
              })[0];
            }
            else if(mem.work=="heavy"){
              buff=rda.filter(function(d){
                return d.subject=="male_heavy";
              })[0];
            }
            else{
              return false;
            }
          }
          else if(mem.age<18 && mem.age>0){
            buff=rda.filter(function(d){
              return d.subject=="male_"+mem.age+"y";
            })[0];
          }
          else if(mem.month>=9){
            buff=rda.filter(function(d){
              return d.subject=="infant_9m-12m";
            })[0];
          }
          else if(mem.month>=6){
            buff=rda.filter(function(d){
              return d.subject=="infant_6m-9m";
            })[0];
          }
          else if(mem.month>=0){
            if(mem.month===""){
              return false;
            }
            buff=rda.filter(function(d){
              return d.subject=="infant_0m-6m";
            })[0];
          }
          else{
            return false;
          }
        }
        else{
          return false;
        }
        buff.name=mem.name;
        root.req.list.push(buff);
      }
      root.req.total.energy=0;
      root.req.total.protein=0;
      root.req.total.iron=0;
      root.req.total.vitA=0;
      root.req.total.thiamine=0;
      root.req.total.riboflavin=0;
      root.req.total.vitC=0;
      for(var i=0; i<root.req.list.length; i++){
        root.req.total.energy = +root.req.total.energy + +root.req.list[i].energy;
        root.req.total.protein = +root.req.total.protein + +root.req.list[i].protein;
        root.req.total.iron = +root.req.total.iron + +root.req.list[i].iron;
        root.req.total.vitA = +root.req.total.vitA + +root.req.list[i].vitA;
        root.req.total.thiamine = +root.req.total.thiamine + +root.req.list[i].thiamine;
        root.req.total.riboflavin = +root.req.total.riboflavin + +root.req.list[i].riboflavin;
        root.req.total.vitC = +root.req.total.vitC + +root.req.list[i].vitC;
      }
      // Intake Calculation
      root.intk={};
      root.intk.list=[];
      root.intk.total={};
      for(var i=0; i<this.dataObj.food.length; i++){
        var food=this.dataObj.food[i];
        var buff={};
        if(food.name!=="" && food.amount!=="" && food.amount>=0){
          buff.name=food.name;
          buff.energy=val.filter(function(d){
            return d.name==buff.name;
          })[0].energy*food.amount/100;
          buff.protein=val.filter(function(d){
            return d.name==buff.name;
          })[0].protein*food.amount/100;
          buff.iron=val.filter(function(d){
            return d.name==buff.name;
          })[0].iron*food.amount/100;
          buff.vitA=val.filter(function(d){
            return d.name==buff.name;
          })[0].vitA*food.amount/100;
          buff.thiamine=val.filter(function(d){
            return d.name==buff.name;
          })[0].thiamine*food.amount/100;
          buff.riboflavin=val.filter(function(d){
            return d.name==buff.name;
          })[0].riboflavin*food.amount/100;
          buff.vitC=val.filter(function(d){
            return d.name==buff.name;
          })[0].vitC*food.amount/100;
          root.intk.list.push(buff);
        }
        else{
          return false;
        }

      }
      root.intk.total.energy=0;
      root.intk.total.protein=0;
      root.intk.total.iron=0;
      root.intk.total.vitA=0;
      root.intk.total.thiamine=0;
      root.intk.total.riboflavin=0;
      root.intk.total.vitC=0;
      for(var i=0; i<root.intk.list.length; i++){
        root.intk.total.energy = +root.intk.total.energy + +root.intk.list[i].energy;
        root.intk.total.protein = +root.intk.total.protein + +root.intk.list[i].protein;
        root.intk.total.iron = +root.intk.total.iron + +root.intk.list[i].iron;
        root.intk.total.vitA = +root.intk.total.vitA + +root.intk.list[i].vitA;
        root.intk.total.thiamine = +root.intk.total.thiamine + +root.intk.list[i].thiamine;
        root.intk.total.riboflavin = +root.intk.total.riboflavin + +root.intk.list[i].riboflavin;
        root.intk.total.vitC = +root.intk.total.vitC + +root.intk.list[i].vitC;
      }
      // Comparison
      root.comp={};
      root.comp.energy = [ +root.intk.total.energy - +root.req.total.energy , (+root.intk.total.energy - +root.req.total.energy)*100/(+root.req.total.energy)];
      root.comp.protein = [ +root.intk.total.protein - +root.req.total.protein , (+root.intk.total.protein - +root.req.total.protein)*100/(+root.req.total.protein)];
      root.comp.iron = [ +root.intk.total.iron - +root.req.total.iron , (+root.intk.total.iron - +root.req.total.iron)*100/(+root.req.total.iron)];
      root.comp.vitA = [ +root.intk.total.vitA - +root.req.total.vitA , (+root.intk.total.vitA - +root.req.total.vitA)*100/(+root.req.total.vitA)];
      root.comp.thiamine = [ +root.intk.total.thiamine - +root.req.total.thiamine , (+root.intk.total.thiamine - +root.req.total.thiamine)*100/(+root.req.total.thiamine)];
      root.comp.riboflavin = [ +root.intk.total.riboflavin - +root.req.total.riboflavin , (+root.intk.total.riboflavin - +root.req.total.riboflavin)*100/(+root.req.total.riboflavin)];
      root.comp.vitC = [ +root.intk.total.vitC - +root.req.total.vitC , (+root.intk.total.vitC - +root.req.total.vitC)*100/(+root.req.total.vitC)];
      return root;
    }
  }
}
</script>
