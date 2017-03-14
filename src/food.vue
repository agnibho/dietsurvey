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
  <div class="panel panel-success">
    <div class="panel-heading">
      <div class="panel-title"><h3>Food Consumption</h3></div>
    </div>
    <div class="panel-body">
      <template v-for="(f, i) in dataObj.food">
        <item :data-index="i" :data-item="f" :data-ref="dataRef" v-on:removeItem="removeItem"></item>
      </template>
      <button v-on:click="addNew" type="button" class="btn btn-success center-block" title="Add item"><span class="glyphicon glyphicon-plus"></span></button>
    </div>
  </div>
</template>

<script>
import Item from "./item.vue"
export default {
  name:"Food",
  props:["dataObj", "dataRef"],
  methods:{
    addNew:function(){
      this.dataObj.food.push({name:"", amount:""});
    },
    removeItem:function(i){
      this.dataObj.food.splice(i, 1);
    }
  },
  created:function(){
    this.dataObj.food.push({name:"", amount:""});
  },
  components:{"item": Item}
}
</script>
