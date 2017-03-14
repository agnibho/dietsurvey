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
  <div class="panel panel-default">
    <div class="panel-body">
      <div class="row"><button v-on:click="remove" type="button" class="close pull-left" style="margin-left:10px" title="Remove member"><span aria-hidden="true">&times;</span></button></div>
      <form class="form-horizontal" data-toggle="validator">
        <div class="form-group">
          <label class="col-sm-4 control-label">Name:</label>
          <div class="col-sm-8">
            <input v-model="dataMember.name" class="form-control" type="text" placeholder="Enter full name"><div class="help-block with-errors"></div>
          </div>
        </div>
        <div class="form-group">
          <label  class="col-sm-4 control-label">Age:</label>
          <div class="col-sm-8">
            <input v-model.number="dataMember.age" class="form-control" type="number" min=0 max=99 placeholder="Enter age in years"><div class="help-block with-errors"></div>
          </div>
        </div>
        <div v-show="dataMember.age===0" class="form-group">
          <label  class="col-sm-4 control-label">Age in Months:</label>
          <div class="col-sm-8">
            <input v-model.number="dataMember.month" class="form-control" type="number" min=0 max=11 placeholder="Enter age in months"><div class="help-block with-errors"></div>
          </div>
        </div>
        <div class="form-group">
          <label class="control-label col-sm-4">Sex:</label>
          <div class="col-sm-8">
            <label>
              <input v-model="dataMember.sex" type="radio" name="sex" value="m">
              Male
            </label>
            <label>
              <input v-model="dataMember.sex" type="radio" name="sex" value="f">
              Female
            </label>
          </div>
          <div v-show="dataMember.age>18 && dataMember.age<99" class="form-group">
            <label class="control-label col-sm-4">Work Type:</label>
            <div class="col-sm-8">
              <select v-model="dataMember.work" class="form-control">
                <option value="sedentary">Sedentary</option>
                <option value="moderate">Moderate</option>
                <option value="heavy">Heavy</option>
              </select>
            </div>
          </div>
          <div v-show="dataMember.age>18 && dataMember.age<50 && dataMember.sex=='f'" class="form-group">
            <label class="control-label col-sm-4">Pregnancy Status:</label>
            <div class="col-sm-8">
              <select v-model="dataMember.state" class="form-control">
                <option value="npnl">Non-Pregnant, Non-Lactating</option>
                <option value="preg">Pregnant</option>
                <option value="lact-6">Lactating, below 6 months</option>
                <option value="lact+6">Lactating, above 6 months</option>
              </select>
            </div>
          </div>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name:"Member",
  props:["dataIndex", "dataMember"],
  methods:{
    remove:function(){
      this.$emit("removeMember", this.dataIndex);
    }
  }
}
</script>
