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
      <div class="row"><button v-on:click="remove" type="button" class="close pull-left" style="margin-left:10px" title="Remove item"><span aria-hidden="true">&times;</span></button></div>
      <form class="form-horizontal" data-toggle="validator">
        <div class="form-group">
          <label class="col-sm-4 control-label">Food Item:</label>
          <div class="col-sm-8">
            <select v-model="dataItem.name" class="form-control">
              <option value="">---select a food item---</option>
              <option v-for="i in dataRef.food">{{i.name}}</option>
            </select>
          </div>
        </div>
        <div class="form-group">
          <label class="col-sm-4 control-label">Amount:</label>
          <div class="col-sm-8">
            <input v-model="dataItem.amount" class="form-control" type="number" min=0 step="0.1" placeholder="Enter amount in gm or ml"><div class="help-block with-errors"></div>
          </div>
        </div>
    </div>
  </div>
</template>

<script>
export default {
  name:"Item",
  props:["dataIndex", "dataItem", "dataRef"],
  methods:{
    remove:function(){
      this.$emit("removeItem", this.dataIndex);
    }
  }
}
</script>
