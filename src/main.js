/**********************************************************************
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
 **********************************************************************/

//Icon
import "./assets/logo.png"

//CSS
import "bootstrap/dist/css/bootstrap.css";
import "./custom.css";

//JS libs
import "jquery";
import "bootstrap";
import "bootstrap-validator";

//Data file
import data from "./data.json";

//Vue Components
import Vue from "vue";
import Input from "./input.vue";
import Output from "./output.vue";

//Start app
new Vue({
  el: "#app",
  components: {
    "app-input": Input,
    "app-output": Output
  },
  data: {dataObj: {family: [], food: []}, dataRef: false},
  created:function(){
    try{
      var loc=JSON.parse(localStorage.getItem(STORAGE));
      if(loc.version>data.version){
        this.dataRef=loc;
      }
      else{
        this.dataRef=data;
        localStorage.setItem(STORAGE, JSON.stringify(data));
      }
    }
    catch(e){
      if(!this.dataRef){
        this.dataRef=data;
        localStorage.setItem(STORAGE, JSON.stringify(data));
      }
    }
  }
});
//Routine jobs
import "./routine.js";

//Source Map
//#sourceMappingURL=dist/bundle.js.map
