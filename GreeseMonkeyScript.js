// ==UserScript==
// @name         Youtube video screen shot
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  try to take over the world!
// @author       You
// @match        https://www.youtube.com/watch*
// @require     https://code.jquery.com/jquery-3.4.1.min.js
// @grant        none
// ==/UserScript==




function uuidv4() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
}

var sessionId = uuidv4();
var counter = 0;


function screenShot(){
    console.log("Taking screen shot...");
    addText( counter, $("#textId").val());
    counter = counter + 1;
    var canvas = document.getElementById('canvas');
    var video = document.getElementsByClassName("html5-main-video")[0];
    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;
    canvas.getContext('2d').drawImage(video, 0, 0, canvas.width, canvas.height);
    console.log(canvas.width, canvas.height);
    canvas.toBlob((blob) => {
        var reader = new FileReader();
        reader.readAsDataURL(blob);
        reader.onloadend = function() {
            var base64data = reader.result;
            base64data = base64data.replace('data:image/png;base64,','');
            callback(counter, base64data);
        }
    });

}

function callback(counter, base64data){
    console.log(base64data);
    $.ajax({
        type: "POST",
        url: 'http://localhost:8080/YouTubeScreenShot/session/'+sessionId+'/image',
        data: {
            'base64Image':base64data,
            'index':counter
        },
        success: function(response) {
            console.log(response);
        }
    });
}

function addText(counter, text){
    console.log(text);
    if($.trim(text) == ""){
        return;
    }
    $.ajax({
        type: "POST",
        url: 'http://localhost:8080/YouTubeScreenShot/session/'+sessionId+'/text',
        data: {
            'text':text,
            'index':counter
        },
        success: function(response) {
            console.log(response);
        }
    });
}

function showResult(){

}

(function() {
    'use strict';
    console.log("Start ..");
    // Your code here...
    setTimeout(function(){
        console.log("Adding button..");
        $(".style-scope.ytd-video-owner-renderer").parent().html("<textarea cols='80' rows='12' id='textId'></textarea>"
            + "<button id='screenShotBtnId'>Screen Shot</button>"
            + "<canvas id='canvas' style='overflow:auto; display: none;'></canvas>"
            + "<a target='_blank' href='http://localhost:8080/YouTubeScreenShot/session/"+sessionId+"'>Show Result</a>");

        $("#screenShotBtnId").click(screenShot);
        $("#showResult").click(showResult);
    }, 1000);
})();


