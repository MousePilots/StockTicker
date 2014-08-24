/*
 * Copyright 2014 Jurjen van Geenen. All rights reserved.
 */

var stockName2RowSelector={};

var trendSymbol={
    DOWN    :   "\u2193",
    FLAT    :   "=",
    UP      :   "\u2191"
};

function trID(r){
    return "tr" + r;
}

function tdID(r,c){
    return "td" + r + "_" + c;
}

function extraHeight(){
    var trh = $('#trh')[0];
    return (2 * trh.offsetHeight) + "px";
}

function normalHeight(){
    var trh = $('#trh')[0];
    return trh.offsetHeight + "px";
}

function updateRow(stockInfo,animate){
    var tds = $(stockName2RowSelector[stockInfo.stock])
            .removeClass('DOWN FLAT UP')
            .addClass(stockInfo.trend)
            .children()
    ;

    tds[0].innerText=stockInfo.stock;
    tds[1].innerText=stockInfo.price;

    if(animate){
        tds[1].innerText+=trendSymbol[stockInfo.trend];
        $(tds).animate(
            {height : extraHeight()},
            function(){
                $(tds).animate( 
                    {height : normalHeight()}, 
                    function(){
                        tds[1].innerText = tds[1].innerText.split(trendSymbol[stockInfo.trend])[0];
                    } 
                );
            }
        );


    }
}

function createTable(stockInfos){
    var t=$('#t').hide();
    for(var r=0 ; r<stockInfos.length; r++){
        var trid = trID(r);
        stockName2RowSelector[stockInfos[r].stock] = '#' + trid;
        var tr= $('<tr></tr>').attr('id',trid).appendTo(t);
        for(var c=0; c<3; c++){
            $('<td></td>').addClass('column' + c).attr('id',tdID(r,c)).appendTo(tr);
        }
        updateRow(stockInfos[r],false);
    }
    t.fadeIn(2000);
}

/**
 * fetches and displays all current stock info
 * @returns {undefined}
 */
function init(){
    $('#t').hide();
    $.ajax({
        type : "GET",
        cache: false,
        dataType: "json",
        url: "webresources/aex/stockInfos",
        success : function(stockInfos){
            createTable(stockInfos);
            listen();
       },
        error : function(){
            alert("an error occured: hit CTRL-F5 to restart");
        }
    });
}

/**
 * listens to the AEX for updates
 * @returns {undefined}
 */
function listen(){
    $.ajax({
        type : "GET",
        cache: false,
        dataType: "json",
        url: "webresources/aex/listen",
        success : function(stockInfo){
            updateRow(stockInfo,true);
            listen();
       },
        error : function(){
            alert("an error occured: closing connection. Hit CTRL-F5 to restart");
        }
    });
}
