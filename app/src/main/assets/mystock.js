var globalstock,globaldata;
$(function(){

   console.log("In here");

});


$(document).ready(function() {

    var stockData=Android.getStockData();
    var symbol=Android.getSymbol();
    console.log("stock data"+stockData);
    console.log("Calling stock");
//   $.fn.getstockdetails();
   $.fn.highstocktab(JSON.parse(stockData),symbol);


});



//Calls index.php which then calls interactive chart api of markit on demand (to avoid cross domain issue)
$.fn.getstockdetails = function(){
        $.ajax({
                type: "GET",
                url: "http://192.168.1.102/s1/index.php",
                dataType: 'json', // what type of data do we expect back from the serve
                success: function(data) {
                    console.log(data);
                    if(data.hasOwnProperty('Message')){
                        $('#errormsg').show();
                    }
                    else
                    {
                        console.log("Data srecie");
                        $.fn.highstocktab(data,"AAPL"); //Object + Symbol
                    }
                    
                },
                error: function(data) {
                    console.log(data);
                }
            });
}






$.fn.highstocktab = function(obj,Symbol) {


console.log("Object data "+typeof(obj));
       if (obj.Elements[0])
            Y_axis = obj.Elements[0].DataSeries.close.values;
        hist = [];
        for (i = 0; obj.Dates && i < obj.Dates.length; i++) {
            //console.log("in forloop");
            var date = new Date(obj.Dates[i]);
            hist.push([Date.UTC(date.getFullYear(), date.getMonth(), date.getDate()), Y_axis[i]]);
        }
        if (hist.length > 0) {
            $('#history').highcharts('StockChart', {
                rangeSelector: {
                    selected: 0
                    , inputEnabled: false
                    , buttons: [{
                        type: 'week'
                        , count: 1
                        , text: '1w'
                    }, {
                        type: 'month'
                        , count: 1
                        , text: '1m'
                    }, {
                        type: 'month'
                        , count: 3
                        , text: '3m'
                    }, {
                        type: 'month'
                        , count: 6
                        , text: '6m'
                    }, {
                        type: 'ytd'
                        , text: 'YTD'
                    }, {
                        type: 'year'
                        , count: 1
                        , text: '1y'
                    }, {
                        type: 'all'
                        , text: 'All'
                    }]
                },
                charts:{
                    width:undefined,
                    reflow: true
                }
                , yAxis: [{
                    title: {
                        text: 'Stock Value'
                    },min:0}]

                , title: {
                    text: Symbol + ' Stock Value'
                }
                , series: [{
                    name: Symbol.toUpperCase()
                    , data: hist
                    , type: 'area'
                    , threshold: null
                    , tooltip: {
                        valueDecimals: 2
                        , valuePrefix: "$"
                    }
                    , fillColor: {
                        linearGradient: {
                              x1: 0
                            , y1: 0
                            , x2: 0
                            , y2: 1
                        }
                        , stops: [
                        [0, Highcharts.getOptions().colors[0]]

                            , [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                        ]
                    }
                }]
            });
        }
        console.log("Da")

    

};

