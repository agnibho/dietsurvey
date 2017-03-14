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

$(document).ready(function(){

  //Remove loader
  $(".loader").remove();
  $(".container").fadeIn();

  //Insert version code
  $(".version").text(VERSION);

  //Update copyright
  $(".copyright").each(function(){
    if(new Date().getFullYear()>$(this).data("start")){
      $(this).text($(this).data("start")+"-"+new Date().getFullYear());
    }
    else{
      $(this).text(new Date().getFullYear());
    }
  });

  //Emit input on form reset
  $("input[type='reset']").on("click", function(e){
    this.form.reset();
    $(this.form).find("input, select, textarea").each(function(){
      this.dispatchEvent(new Event("input"));
    });
  });

  //Change focus after number input
  $(".jump-focus").on("input", function(){
    if($(this).val().length == $(this).prop("maxlength")){
      var all=$("input").toArray();
      var i=all.indexOf(this)+1;
      $(all[i]).focus().select();
    }
  });

  //Defocus after input finished
  $(".stop-focus").on("input", function(){
    if($(this).val().length == $(this).prop("maxlength")){
      $(this).blur();
      var target=$($(this).data("ref"));
      console.log($(target).offset());
      $("html, body").animate({
        scrollTop: $(target).offset().top
      }, 1000);
    }
  });

  //Notifications
  $(window).resize(function(){
    $("#notify").width($(".container").width()-20);
  });
  $(window).scroll(function(){
    $("#notify").width($(".container").width()-20);
  });
  $.get("https://code.agnibho.com/dietsurvey/info.json", function(data){
    var vCurr=VERSION.split(".").map(Number);
    var vLtst=data.latest.split(".").map(Number);
    function isBiggerThan(v1, v2){
      while(v1.length<v2.length){
        v1.push(0);
      }
      while(v2.length<v1.length){
        v2.push(0);
      }
      for(var i=0; i<v1.length; i++){
        if(v1[i]>v2[i]){
          return true;
        }
      }
      return false;
    }
    if(isBiggerThan(data.latest, VERSION)){
      $("#notify").slideDown();
      $("#notify").width($(".container").width()-20);
      $("#notify-text").text("A new version of DietSurvey is available.");
      if(document.URL.indexOf("http://")==-1 && document.URL.indexOf("https://")==-1){
        if(/(android)/i.test(navigator.userAgent)){
          $("#notify-link").attr("href", data.apk);
          $("#notify-link").text("Download");
        }
        else{
          $("#notify-link").attr("href", data.url);
          $("#notify-link").text("Load");
        }
      }
      else{
        $("#notify-link").attr("href", data.url);
        $("#notify-link").text("Load");
      }
    }
    try{
      if(data.data.latest>JSON.parse(localStorage.getItem("dietsurvey_data")).version){
        $.get(data.data.src, function(d){
          localStorage.setItem("dietsurvey_data", JSON.stringify(d));
        });
      }
    }
    catch(e){}
  });
});
