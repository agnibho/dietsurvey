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
  <div class="row">
    <div class="col-sm-6">
      <family :data-obj="dataObj"></family>
    </div>
    <div class="col-sm-6">
      <food :data-obj="dataObj" :data-ref="dataRef"></food>
    </div>
  </div>
</template>

<script>
import Family from "./family.vue";
import Food from "./food.vue";
export default {
  name:"Input",
  props:["dataObj", "dataRef"],
  components: {"family": Family, "food": Food}
}
</script>
