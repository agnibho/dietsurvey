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
      <div class="panel-title"><h3>Family Information</h3></div>
    </div>
    <div class="panel-body">
      <!--head
      <form>
        <div class="form-group">
          <label>Head of the Family:</label>
          <input class="form-control" type="text" placeholder="Enter full name"></div>
      </form>
      -->
      <template v-for="(m, i) in dataObj.family">
        <member :data-index="i" :data-member="m" v-on:removeMember="removeMember"></member>
      </template>
      <button v-on:click="addNew" type="button" class="btn btn-success center-block" title="Add member"><span class="glyphicon glyphicon-plus"></span></button>
    </div>
  </div>
</template>

<script>
import Member from "./member.vue"
export default {
  name:"Family",
  props:["dataObj"],
  methods:{
    addNew:function(){
      this.dataObj.family.push({name:"", age:"", month:"", sex:"", work:"sedentary", state:"npnl"});
    },
    removeMember:function(i){
      this.dataObj.family.splice(i, 1);
    }
  },
  created:function(){
    this.dataObj.family.push({name:"", age:"", month:"", sex:"", work:"sedentary", state:"npnl"});
  },
  components:{"member":Member}
}
</script>
